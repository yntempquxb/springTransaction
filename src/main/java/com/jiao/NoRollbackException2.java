package com.jiao;

public class NoRollbackException2 extends RuntimeException {
    public NoRollbackException2() {
    }

    public NoRollbackException2(String message) {
        super(message);
    }

    public NoRollbackException2(Throwable e) {
        super(e);
    }
}
