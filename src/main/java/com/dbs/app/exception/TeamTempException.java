package com.dbs.app.exception;

public class TeamTempException extends  RuntimeException {

    public TeamTempException() {
        super();
    }

    public TeamTempException(String message) {
        super(message);
    }

    public TeamTempException(String message, Throwable cause) {
        super(message, cause);
    }

    public TeamTempException(Throwable cause) {
        super(cause);
    }
}
