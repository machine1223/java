package com.pw.exception;

/**
 * SysException.<br/>
 * 
 * @author bin (bin@hypersun.com)
 * @since May 8, 2012
 */
public enum SystemExceptionCode {
	
	SYSTEM_ERROR(-1, "System error."),
	DATA_ACCESS_ERROR(-2, "Data access error."),
	GENERATE_PRIMARY_KEY_ERROR(-3, "Generate primary key error."),
	GERNATE_CRITERIA_FOR_QUERY_ERROR(-4, "Generate criterid for query error."),
	CROSS_MODULE_INVOKE_ERROR(-5, "Please use business module implement."),
	SERVICE_ERROR(-6, "Error occurred when accessiong services");
	
	private int		code;
	private String	message;
	
	private SystemExceptionCode(int code, String message){
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}