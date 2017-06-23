/**
 *
 */
package comxxxxdigital.securities.tddtest.stock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import comxxxxdigital.securities.config.stock.StockConfiguration;
import comxxxxdigital.securities.services.stock.StockQuoteServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StockQuoteServiceImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private StockConfiguration config;

    @Value("${dummy.stock.vcm.path}")
    private String root;

    private static final Logger LOG = LoggerFactory.getLogger(StockQuoteServiceImplTest.class.getName());

    private RestTemplate template = new RestTemplate(); 
    
    @Rule
    public WireMockRule mock = new WireMockRule(8899);
    /**
     * 
     * <p>
     * <b> Free Real-Time Basic Quote - VCM period </b>
     * </p>
     *
     * @throws Exception
     */
    @Test
    public void search_GetResponse_TrueVCM_Stock_5() throws Exception {

        LOG.info("+++++++++++++++++++++root=" + root);

        //Mock sapi service Start
        String dummyRequest  = FileUtils.readParamFromFile(this.root + "/request_message_for_stock_5.json");
        String dummyResponse = FileUtils.readParamFromFile(this.root + "/response_message_for_stock_5.txt");
        //Mock sapi URL
        config.setStockUrl("http://localhost:8899/security-trading/stock-hk/get-quotes-details");
        mock.stubFor(
            com.github.tomakehurst.wiremock.client.WireMock
            .post( urlEqualTo("/security-trading/stock-hk/get-quotes-details" ) )
            .withHeader( "Accept", equalTo("text/plain, application/json, application/*+json, */*") )
            .willReturn( aResponse()
                         .withStatus(200)
                         .withHeader("Content-Type", "application/json")
                         .withBody(dummyResponse) ) 
            );
        //Mock sapi service End
        
        //Invoke papi Start
        this.mockMvc.perform(
            post("/api/security-trading/stock-hk/get-quotes-details")
            .content(dummyRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo( print() )
            .andExpect( status().isOk() )
            .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8 ) )
            .andExpect( jsonPath("stock_quote.vcm_cas.vcm_flag").value("Y") )
            .andExpect( jsonPath("stock_quote.vcm_cas.vcm_status").value("Y") );
      //Invoke papi End

    }

    // @Test
    // public void whenSucessCallGoodTillApiCheckRightResult() throws Exception
    // {
    // To Do
    /*
     * mockMvc.perform(get("/api/securities/good-till-date-list"))
     * .andDo(print()) .andExpect(status().isOk());
     */

    /*
     * mockMvc.perform(get("/api/v1.1/securities/good-till-date-list"))
     * .andDo(print()) .andExpect(status().isOk())
     * .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
     * .andExpect(jsonPath("name").value("aaa"));
     */

    /*
     * mockMvc.perform(get("/api/v1.1/securities/good-till-date-list"))
     * .andDo(print()) .andExpect(status().isOk())
     * .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
     * .andExpect(jsonPath("name", is("aaa")));
     */

    /*
     * mockMvc.perform(post("/api/v1.1/securities/good-till-date-list"))
     * .andDo(print()) .andExpect(status().isOk())
     * .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
     * .andReturn().getResponse().getContentAsString();
     */

    /*
     * aassertEquals(expectResult,
     * mockMvc.perform(get("/api/v1.1/securities/good-till-date-list"))
     * .andDo(print()) .andExpect(status().isOk())
     * .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
     * .andReturn().getResponse().getContentAsString());ssertEquals(
     * expectResult,
     * mockMvc.perform(get("/api/v1.1/securities/good-till-date-list"))
     * .andDo(print()) .andExpect(status().isOk())
     * .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
     * .andReturn().getResponse().getContentAsString());
     */
    // }

}
