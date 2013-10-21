package com.mogu.webmail.service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MoguAuth extends Authenticator
{
	private String	user, password;
	
	public void setUserinfo(String getuser, String getpassword)
	{
		user = getuser;
		password = getpassword;
	}
	
	protected PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(user, password);
	}
}
