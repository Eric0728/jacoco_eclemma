package com.hsbc.hase.digital.securities.exception.stock;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**

 */
@ControllerAdvice
public class GlobalExceptionMapper {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionMapper.class);

	/**
	 * 
	 */
	public GlobalExceptionMapper() {
		// TODO Auto-generated constructor stub
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleException(HttpServletRequest request, Exception exception) {
		log.error( String.format( "%s ", exception.getMessage() ), exception);

		return ErrorResponseBuilder.internalServerError().withRequest(request).withException(exception).build();
	}
}
