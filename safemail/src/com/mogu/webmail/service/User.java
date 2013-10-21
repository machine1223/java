package com.mogu.webmail.service;

public class User
{
	private String	userEmail;
	private String	userPassword;
	private String	company;
	private String	organization;
	private String	city;
	private String	provence;
	private String	country;
	
	public String getUserEmail()
	{
		return userEmail;
	}
	
	public void setUserEmail(String userEmail)
	{
		this.userEmail = userEmail;
	}
	
	public String getUserPassword()
	{
		return userPassword;
	}
	
	public void setUserPassword(String userPassword)
	{
		this.userPassword = userPassword;
	}
	
	public String getCompany()
	{
		return company;
	}
	
	public void setCompany(String company)
	{
		this.company = company;
	}
	
	public String getOrganization()
	{
		return organization;
	}
	
	public void setOrganization(String organization)
	{
		this.organization = organization;
	}
	
	public String getCity()
	{
		return city;
	}
	
	public void setCity(String city)
	{
		this.city = city;
	}
	
	public String getProvence()
	{
		return provence;
	}
	
	public void setProvence(String provence)
	{
		this.provence = provence;
	}
	
	public String getCountry()
	{
		return country;
	}
	
	public void setCountry(String country)
	{
		this.country = country;
	}
	
}
