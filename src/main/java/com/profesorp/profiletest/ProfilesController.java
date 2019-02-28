package com.profesorp.profiletest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfilesController {
	@Autowired 
	IRead read;
	
	@Value("${app.ID}")
	int id;
	
	@Autowired
	IWrite write;
	
	@GetMapping("/hello")
	public String get(@RequestParam(value="name",required=false) String name)
	{
		return "Hello "+(name==null?"SIN NOMBRE":name)+
				"<br><br> The name of the profile number:"+ id+" in the database H2 is: "+read.readRegistry(id)+
				"<br> The profile used in the application is : "+write.getProfile();
				
	}
}
