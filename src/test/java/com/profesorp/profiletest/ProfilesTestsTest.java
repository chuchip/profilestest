package com.profesorp.profiletest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Profile("test")
@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class ProfilesTestsTest {
	
	@Value("${app.ID}")
	int id;
	
	@Autowired 
	IRead read;
	
	@Autowired
	Environment env;
	
	@Test
	public void inicio() {
		String name=read.readRegistry(id);
		assertEquals(name,"test");
		String principal=env.getProperty("app.principal","NULL");
		assertNotEquals(principal,"profe"); // No deberia estar la variable app.principal pues estamos en test
		String test=env.getProperty("app.test","NULL");
		assertEquals(test,"test");
	}

}
