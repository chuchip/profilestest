package com.profesorp.profiletest;

import org.springframework.stereotype.Service;

@Service
public interface IRead {
	public String readRegistry(int id);
}
