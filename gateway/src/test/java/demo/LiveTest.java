package demo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.config;



public class LiveTest {

	   	private final String ROOT_URI = "http://localhost:8080";
	    private final FormAuthConfig formConfig = new FormAuthConfig("/", "username", "password");

	    @Before
	    public void setup() {
	        RestAssured.config = config().redirect(RedirectConfig.redirectConfig()
	            .followRedirects(false));
	    }	    
	    
/*	    @Test
	    public void whenGetAllBooks_thenSuccess() {
	        final Response response = RestAssured.get(ROOT_URI + "/resource");
	        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	        Assert.assertNotNull(response.getBody());
	    }*/
	    
	    @Test
	    public void whenAccessProtectedResourceAfterLogin_thenSuccess() {
	        final Response response = RestAssured.given()
	            .auth()
	            .form("admin", "admin", formConfig)
	            .get(ROOT_URI + "/resource");
	        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	        Assert.assertNotNull(response.getBody());
	    }
	    
	
}
