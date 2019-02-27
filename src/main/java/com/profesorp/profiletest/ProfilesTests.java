package com.profesorp.profiletest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;


@SpringBootApplication
@Configuration
public class ProfilesTests {

	@Autowired
	private Environment environment;
	
	public static void main(String[] args) {
		SpringApplication.run(ProfilesTests.class, args);
	}

	@Bean
	@Profile("test")
	IWrite getWriterTest()
	{
		return new WriteImpl("..test.. "+getProfile());		
	}
	@Bean
	@Profile("default")
	IWrite getWriterDefault()
	{
		return new WriteImpl("..default.. "+getProfile());		
	}
	@Bean
	@Profile("other")
	IWrite getWriterOthe()
	{
		return new WriteImpl("..other.. "+getProfile());		
	}
	String getProfile()
	{
		if (environment.getActiveProfiles()==null)
			return "default";
		String[] profiles=environment.getActiveProfiles();
		return profiles.length>0?profiles[0]:"default"; 
	}
}
