package demo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.test.context.junit4.SpringRunner;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.sant.hms.ui.UiApplication;

@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "security.user.password:foo")
@SpringBootTest(classes=UiApplication.class, webEnvironment=WebEnvironment.RANDOM_PORT)
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
		template = new TestRestTemplate();
		
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void userEndpointProtected() {
		template = new TestRestTemplate();
		
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/user", String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	public void resourceEndpointProtected() {
		template = new TestRestTemplate();
		
		ResponseEntity<String> response = template.getForEntity("http://localhost:" + port + "/resource", String.class);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
	}

	@Test
	public void loginSucceeds() {
		
		ResponseEntity<String> response = template.getForEntity("http://localhost:8080/resource", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		
		log.info("<------------------------ body: {}", response.getBody());
	}

}
