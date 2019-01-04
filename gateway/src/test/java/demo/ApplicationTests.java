package demo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(ApplicationTests.class);
	
	@LocalServerPort
	private int port;

	private TestRestTemplate template;

	@Before
	public void setup() {
		template = new TestRestTemplate();
				
	    BasicAuthorizationInterceptor bai = new BasicAuthorizationInterceptor("admin", "admin");
	    template.getRestTemplate().getInterceptors().add(bai);
	}
	
	
	@Test
	public void homePageLoads() {
		ResponseEntity<String> response = template.getForEntity("http://localhost:"
				+ port + "/resource", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		log.info("<------------------------ body: {}", response.getBody());
		
		
	}

}
