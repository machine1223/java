package com.pw.common.json;
/**
 * used for return a common process result to client side
 * @author andy.yin@everbridge.com
 * Aug 17, 2012 7:21:08 PM
 */
public class JsonResult<T> {
    private boolean success;
    private String resultCode;
    private String message;
    private T data;


    public JsonResult() {

    }
    public JsonResult(boolean success) {
        this(success, "");
    }
    public JsonResult(T data, boolean success) {
        this.success = success;
        this.data = data;
    }
    public JsonResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
