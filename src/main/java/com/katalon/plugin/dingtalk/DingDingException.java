package com.katalon.plugin.dingtalk;

public class DingDingException extends Exception {

    private static final long serialVersionUID = 1171601532588814667L;

    public DingDingException(String message) {
        super(message, null);
    }

    public DingDingException(Throwable throwable) {
        super("", throwable);
    }

    public DingDingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
