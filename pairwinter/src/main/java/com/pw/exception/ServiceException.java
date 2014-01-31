package com.pw.exception;

/**
 * ServiceException.<br/>
 * 
 * @author Patrick Liu
 * @since 2012-7-10
 */
public class ServiceException extends BaseException {
	
	private static final long	serialVersionUID	= 7353780947153693469L;
	
	public ServiceException() {
		super();
	}
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String message, Throwable throwable) {
		super(SystemExceptionCode.SERVICE_ERROR.getCode(), message, throwable);
	}
	
	public ServiceException(int code, String message) {
		super(code, message);
	}
	
	public ServiceException(int code, String message, Throwable throwable) {
		super(code, message, throwable);
	}
	
}
