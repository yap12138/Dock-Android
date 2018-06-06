package com.yaphets.dock.model.validation;

public class Result {
    private int mCode;
    private String mMessage;

    public Result() {
        this(0, null);
    }

    public Result(int Code, String Message) {
        this.mCode = Code;
        this.mMessage = Message;
    }

    public void setCode(int mCode) {
        this.mCode = mCode;
    }

    public void setMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }
}
