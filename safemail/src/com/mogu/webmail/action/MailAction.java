package com.mogu.webmail.action;

import com.mogu.webmail.service.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class MailAction extends BaseAction
{
	
	private User				user;
	private Mail				mail;
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private String				errorMessage;
	private Mail[]				mails;
	
	public String toRegister() throws Exception
	{
		return SUCCESS;
	}
	
	public String register() throws Exception
	{
		try
		{
			UserService userService = new UserServiceImpl();
			userService.register(user);
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String toLogin() throws Exception
	{
		//getPubKeyFromIE(getRequest(), getResponse());
		return SUCCESS;
	}
	
	public String login() throws Exception
	{
		try
		{
			UserService userService = new UserServiceImpl();
			if(!userService.isRegistered(user))
				throw new Exception("该用户还未注册，请先注册！");
			getSession().setAttribute("user", user);
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String main() throws Exception
	{
		try
		{
			MailService mailService = new MailServiceImpl();
			this.user = (User) getSession().getAttribute("user");
			mails = mailService.listMails(user);
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String sendMail() throws Exception
	{
		try
		{
			MailService mailService = new MailServiceImpl();
			user = (User) getSession().getAttribute("user");
			mailService.sendMail(user, mail);
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String viewMail() throws Exception
	{
		try
		{
			MailService mailService = new MailServiceImpl();
			user = (User) getSession().getAttribute("user");
			mail = mailService.viewMail(user, mail.getMsgNum());
		}
		catch (Exception e)
		{
			errorMessage = e.getMessage();
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
	public String logout() throws Exception
	{
		getSession().invalidate();
		return SUCCESS;
	}
	
	private PublicKey getPubKeyFromIE(HttpServletRequest request,

	HttpServletResponse response) throws ServletException, IOException
	{
		System.out.println("...security receive done..." + request.getScheme());
		String cipherSuite = "";
		PublicKey pk = null;
		try
		{
			cipherSuite = (String) request.getAttribute("javax.servlet.request.cipher_suite");
			System.out.println("cipherSuite=====" + cipherSuite);
			if(cipherSuite != null)
			{
				X509Certificate[] certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
				if(certs != null)
				{
					if(certs.length > 0)
					{
						X509Certificate t = certs[0];
						pk = t.getPublicKey();
						System.out.println(t.getSubjectDN());
					}
				}
				else
				{
					if("https".equals(request.getScheme()))
					{
					}
					else
					{
					}
				}
			}
			return pk;
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
	}
	
	public User getUser()
	{
		return user;
	}
	
	public void setUser(User user)
	{
		this.user = user;
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}
	
	public Mail[] getMails()
	{
		return mails;
	}
	
	public void setMails(Mail[] mails)
	{
		this.mails = mails;
	}
	
	public Mail getMail()
	{
		return mail;
	}
	
	public void setMail(Mail mail)
	{
		this.mail = mail;
	}
}
