package com.gscdn.managedapi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@RestClientTest(RestTest.class)
public class RestTest {
	RestTemplate resTemplate = new RestTemplate();
	
	@Test
	public void test() {
		System.out.println("test");
	}
}
