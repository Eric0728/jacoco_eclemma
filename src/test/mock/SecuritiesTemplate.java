

package com.xxxx.xxxx.digital.securities.services.stock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.xxxx.digital.securities.exception.stock.StockRestClientException;
import com.xxxx.xxxx.digital.securities.resp.stock.Response;

@Component
@Service
public class SecuritiesTemplate {
	
	/**
	 * Constants for CONTENT_ENCODING.
	 */
	private static final String CONTENT_ENCODING = "UTF-8";
	/**
	 * restTemplate.
	 */
	private final RestTemplate restTemplate;

	/**
	 * the jacksonObjectMapper
	 */
	private final ObjectMapper jacksonObjectMapper;

	/**
	 * The name of the error logger, used when logging debug information.
	 */
	private static final String DEBUGGER = SecuritiesTemplate.class.getName();

	/**
	 * Constants for ERROR_RECORDER.
	 */
	private static final String ERROR_RECORDER = SecuritiesTemplate.class.getName();
	/**
	 * debug logger.
	 */
	private static final Logger DEBUG_LOG = LoggerFactory.getLogger(SecuritiesTemplate.DEBUGGER);

	/**
	 * error logger.
	 */
	private static final Logger ERROR_LOG = LoggerFactory.getLogger(SecuritiesTemplate.ERROR_RECORDER);

	@Autowired
	public SecuritiesTemplate(RestTemplate restTemplate, ObjectMapper jacksonObjectMapper) {
		super();
		this.restTemplate = restTemplate;
		this.jacksonObjectMapper = jacksonObjectMapper;
	}

	public <T extends Response> T postForObject(HttpMethod method, String url, Object request, Class<T> responseType,	Object... urlVariables){
		
		DEBUG_LOG.debug("Template *******start: ");

		T result = null;
        try {
			
			String requestBody = this.jacksonObjectMapper.writeValueAsString(request);
			
			DEBUG_LOG.debug("Template *******body:"+requestBody);
			
			
			HttpHeaders headers = new HttpHeaders();
			
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> httpRequest = new HttpEntity<String>(requestBody, headers);

//			ResponseEntity<T> responseEntity = 
//					restTemplate.exchange(url, method, httpRequest, responseType, urlVariables);
//			result = responseEntity.getBody();
			
			ResponseEntity<String> responseEntity = 
			        restTemplate.exchange(url, method, httpRequest, String.class,  urlVariables);
			result = this.jacksonObjectMapper.readValue(responseEntity.getBody(), responseType);
		
		} catch (RestClientException rce) {
			
			DEBUG_LOG.debug("Template postForObject *******RestClientException ");
			
			this.handleRestClientException(method.toString(), url, request, responseType, rce, urlVariables);
 			
 		} catch (Exception ex) {
 			
 			DEBUG_LOG.debug("Template postForObject *******Exception ");
 			
 			ERROR_LOG.error("Exception throw at postForObject", ex);
 			//TODO check
 			throw new RuntimeException(ex);
 		}

		return result;

	}
	
	/**
	 * handle RestClientException
	 * @param method
	 * @param url
	 * @param request
	 * @param responseType
	 * @param rce
	 * @param uriVariables
	 * @throws StockRestClientException
	 */
	private < T extends Response > void handleRestClientException(String method, String url, Object request, Class<T> responseType,
			RestClientException rce, Object... uriVariables)  {
		String faultInfo = "";
		
		String msg = rce.getMessage();
		
		if ( rce instanceof HttpStatusCodeException ) {
			
			HttpStatusCodeException hrce = (HttpStatusCodeException) rce;
			
			faultInfo = String.valueOf(hrce.getStatusCode().value());
			
			msg = new StringBuilder(msg).append(" with http body:").append(hrce.getResponseBodyAsString()).toString();
		}
		
		StockRestClientException ex = new StockRestClientException(msg, faultInfo, rce);
		
		if ( SecuritiesTemplate.ERROR_LOG.isErrorEnabled() ) {
			
			StringBuilder logMsg = genBasicLogHeader( method, url, uriVariables );
			
			logMsg.append(":\n Throws :\n");
			
			SecuritiesTemplate.ERROR_LOG.error(logMsg.toString(), ex);
		}
		throw ex;

	}
	
	/**
	 * Generate basic log header
	 * 
	 * @param method
	 * @param url
	 * @param uriVariables
	 * @return get basic log header
	 */
	private StringBuilder genBasicLogHeader( String method, String url, Object... uriVariables ) {
		StringBuilder msg = new StringBuilder( "SecuritiesTemplate " );
		msg.append( method ).append( "  " ).append( url ).append( "(" );
		for ( Object uvo : uriVariables ) {
			msg.append(uvo).append(",");
		}
		msg.append( ")" );
		return msg;
	}

	
	

}
