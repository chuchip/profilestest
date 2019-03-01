package com.profesorp.profiletest;

public class WriteImpl implements IWrite{
	private String profile;
	
	public WriteImpl(String profile)
	{
		this.profile=profile;
	}
	
	public String getProfile()
	{
		return profile;
	}
	@Override
	public void writeLog(String log) {
		System.out.println("Profile: "+profile+" -> "+ log);
		
	}
}
