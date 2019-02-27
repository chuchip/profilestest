package com.profesorp.profiletest.impl.other;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.profesorp.profiletest.IRead;
import com.profesorp.profiletest.IWrite;
import com.profesorp.profiletest.entities.ProfileEntity;
import com.profesorp.profiletest.repository.ProfileRepository;

@Component
@Profile("other")
public class ReadOtherImpl implements IRead{

	@Autowired
	ProfileRepository customerRepository;

	@Autowired
	IWrite out;
	
	public String readRegistry(int id)
	{
		out.writeLog("entry in ReadImpl" );
		Optional<ProfileEntity> registroOpc=customerRepository.findById(id);
		if (!registroOpc.isPresent())
		{
			System.out.println("(other) Customer "+id+" NOT found");
			return null; 
		}
		ProfileEntity registro=registroOpc.get(); 
		out.writeLog("Name  customer "+id+" is: "+registro.getName());
		return registro.getName();
	}
}
