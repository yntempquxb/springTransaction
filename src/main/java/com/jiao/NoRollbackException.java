package com.jiao;

public class NoRollbackException extends Exception {
    public NoRollbackException() {
    }

    public NoRollbackException(String message) {
        super(message);
    }

    public NoRollbackException(Throwable e) {
        super(e);
    }
}
