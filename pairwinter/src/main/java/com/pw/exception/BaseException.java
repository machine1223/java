package com.pw.exception;

import java.io.Serializable;

/**
 * BaseException. throw the base exception in service.<br/>
 * 
 * @author bin (bin@hypersun.com)
 * @since Apr 12, 2012
 */
public class BaseException extends RuntimeException implements Serializable {
	public final static long	serialVersionUID	= 12345678;
	
	private boolean				debug				= true;
	private String				trace;
	private int					code				= SystemExceptionCode.SYSTEM_ERROR.getCode();

    private Object[] messageArgs;
	
	public BaseException() {
		super();
		init();
	}
	
	public BaseException(String message) {
		super(message);
		init();
	}
	
	public BaseException(int code, String message) {
		super(message);
		this.code = code;
		init();
	}
    public BaseException(int code, String message,  Object[] messageArgs) {
        super(message);
        this.code = code;
        this.messageArgs = messageArgs;
        init();
    }
    public BaseException(int code, String message, Throwable throwable, Object[] messageArgs) {
        super(message);
        this.code = code;
        this.messageArgs = messageArgs;
        init();
    }
	public BaseException(String message, Throwable throwable) {
		super(message, throwable);
		init();
	}
	
	public BaseException(int code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = code;
		init();
	}
	
	public BaseException(Throwable throwable) {
		super(throwable);
		init();
	}
	
	public int getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return (debug ? trace : "") + super.getMessage();
	}

    public Object[] getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(Object[] messageArgs) {
        this.messageArgs = messageArgs;
    }

    private void init() {
		StackTraceElement traces[] = getStackTrace();
		String className = traces[0].getClassName();
		int n = className.lastIndexOf('.');
		if(n > 0)
			className = className.substring(n + 1);
		trace = className + "." + traces[0].getMethodName() + "[line: " + traces[0].getLineNumber() + "]: ";
	}
}