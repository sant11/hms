package demo;

import static org.junit.Assert.assertEquals;

import java.security.Principal;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class ResourceTests {
	
	private ResourceApplication resource = new ResourceApplication();

	@Ignore
	@Test
	public void home() {
		assertEquals("Hello World", resource.home().getContent());
	}

	@Ignore
	@Test
	public void changes() {
		Principal user = new UsernamePasswordAuthenticationToken("admin", "");
		resource.update(new Message("Foo"), user);
		assertEquals(1, resource.changes().size());
	}

	@Ignore
	@Test
	public void changesOverflow() {
		for (int i=1; i<=11; i++) { resource.changes().add(new Change("foo", "bar")); } 
		Principal user = new UsernamePasswordAuthenticationToken("admin", "");
		resource.update(new Message("Foo"), user);
		assertEquals(10, resource.changes().size());
	}

}
