package com.mogu.webmail.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport
{
	private static final long	serialVersionUID	= -4584042177312844883L;
	protected final Logger		logger				= Logger.getLogger(getClass());
	
	/**
	 * 获取Request对象
	 * 
	 * @return current request
	 */
	protected HttpServletRequest getRequest()
	{
		return ServletActionContext.getRequest();
	}
	
	/**
	 * 获取Response对象
	 * 
	 * @return current response
	 */
	protected HttpServletResponse getResponse()
	{
		return ServletActionContext.getResponse();
	}
	
	/**
	 * 获得一个Session对象，如果没有则创建一个新的Session
	 * 
	 * @return the session from the request (request.getSession()).
	 */
	protected HttpSession getSession()
	{
		return getRequest().getSession(true);
	}
}
