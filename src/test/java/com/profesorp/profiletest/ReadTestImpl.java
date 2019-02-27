package com.profesorp.profiletest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.profesorp.profiletest.IRead;
import com.profesorp.profiletest.entities.ProfileEntity;
import com.profesorp.profiletest.repository.ProfileRepository;

@Component
@Profile("test")
public class ReadTestImpl implements IRead{

	@Autowired
	ProfileRepository customerRepository;
	
	

	public String  readRegistry(int id)
	{
		System.out.println("entry in ReadImpl of test profile" );
		Optional<ProfileEntity> registroOpc=customerRepository.findById(id);
		if (!registroOpc.isPresent())
		{
			System.out.println("(test) Customer "+id+" NOT found");
			return null;
		}
		ProfileEntity registro=registroOpc.get(); 
		System.out.println("(test) Name  customer "+id+" is: "+registro.getName());
		return registro.getName();
	}
}
