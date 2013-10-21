package com.mogu.webmail.service;

import java.util.Date;

public class Mail
{
	/**
	 * 主题
	 */
	private String				subject;
	/**
	 * 发件人
	 */
	private String				from;
	private Date				date;
	private int					msgNum;
	/**
	 * 邮件类型
	 */
	private String				typt;
	private String				content;
	private int					signed;
	private int					encrypted;
	private String				verifyResult;
	/**
	 * 收件人
	 */
	private String				receipt;
	public static final String	TYPE_NORMAL			= "普通邮件";
	public static final String	TYPE_ENCRYPT		= "加密邮件";
	public static final String	TYPE_SIGNED			= "签名邮件";
	public static final String	TYPE_SIGNED_ENCRYPT	= "签名加密邮件";
	
	public String getSubject()
	{
		return subject;
	}
	
	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	
	public String getFrom()
	{
		return from;
	}
	
	public void setFrom(String from)
	{
		this.from = from;
	}
	
	public Date getDate()
	{
		return date;
	}
	
	public void setDate(Date date)
	{
		this.date = date;
	}
	
	public String getTypt()
	{
		return typt;
	}
	
	public void setTypt(String typt)
	{
		this.typt = typt;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public String getReceipt()
	{
		return receipt;
	}
	
	public void setReceipt(String receipt)
	{
		this.receipt = receipt;
	}
	
	public int getSigned()
	{
		return signed;
	}
	
	public void setSigned(int signed)
	{
		this.signed = signed;
	}
	
	public int getEncrypted()
	{
		return encrypted;
	}
	
	public void setEncrypted(int encrypted)
	{
		this.encrypted = encrypted;
	}
	
	public String getVerifyResult()
	{
		return verifyResult;
	}
	
	public void setVerifyResult(String verifyResult)
	{
		this.verifyResult = verifyResult;
	}
	
	public int getMsgNum()
	{
		return msgNum;
	}
	
	public void setMsgNum(int msgNum)
	{
		this.msgNum = msgNum;
	}
	
}
