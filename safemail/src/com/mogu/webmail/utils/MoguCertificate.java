package com.mogu.webmail.utils;

import java.util.Date;

public class MoguCertificate
{
	/**
	 * 用户名称（可以是名字或邮箱或其他）
	 */
	private String	name;
	/**
	 * 公司
	 */
	private String	company;
	/**
	 * 组织
	 */
	private String	organization;
	/**
	 * 所在城市
	 */
	private String	city;
	/**
	 * 所在省份
	 */
	private String	province;
	/**
	 * 所在国家
	 */
	private String	country;
	/**
	 * 证书密码
	 */
	private String	password;
	/**
	 * 重复密码
	 */
	private String	repassword;
	
	/**
	 * 证书生效日期
	 */
	private Date	start;
	/**
	 * 证书失效日期
	 */
	private Date	end;
	
	public Date getStart()
	{
		return start;
	}
	
	public void setStart(Date start)
	{
		this.start = start;
	}
	
	public Date getEnd()
	{
		return end;
	}
	
	public void setEnd(Date end)
	{
		this.end = end;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
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
	
	public String getProvince()
	{
		return province;
	}
	
	public void setProvince(String province)
	{
		this.province = province;
	}
	
	public String getCountry()
	{
		return country;
	}
	
	public void setCountry(String country)
	{
		this.country = country;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public String getRepassword()
	{
		return repassword;
	}
	
	public void setRepassword(String repassword)
	{
		this.repassword = repassword;
	}
}
