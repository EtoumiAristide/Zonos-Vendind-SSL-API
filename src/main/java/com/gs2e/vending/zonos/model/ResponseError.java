package com.gs2e.vending.zonos.model;

public class ResponseError {
    private String exception;
    private Throwable cause;
    private int code;
    private String message;

    public ResponseError() {
    }

    public ResponseError(String exception, Throwable cause, int code, String message) {
        this.exception = exception;
        this.cause = cause;
        this.code = code;
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
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

    @Override
    public String toString() {
        return "ResponseError{" +
                "exception='" + exception + '\'' +
                ", cause=" + cause +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
