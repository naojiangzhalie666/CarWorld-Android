package com.liansheng.carworld.bean;

public class Message {

    /**
     * message : 此账号未注册
     * code : 400
     * stackTrace : null
     */

    private String message;
    private int code;
    private Object stackTrace;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(Object stackTrace) {
        this.stackTrace = stackTrace;
    }
}
