package com.profesorp.profiletest.impl.def;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.profesorp.profiletest.IRead;
import com.profesorp.profiletest.IWrite;
import com.profesorp.profiletest.entities.ProfileEntity;
import com.profesorp.profiletest.repository.ProfileRepository;

@Component
@Profile("default")
public class ReadDefImpl implements IRead{

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
			System.out.println("Customer "+id+" NOT found");
			return null;
		}
		ProfileEntity registro=registroOpc.get(); 
		out.writeLog("Name  customer "+id+" is: "+registro.getName());
		return registro.getName();
	}
}
