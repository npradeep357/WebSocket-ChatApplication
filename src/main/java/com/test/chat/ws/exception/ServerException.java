package com.test.chat.ws.exception;

public class ServerException extends Exception {

    private static final long serialVersionUID = -6066463636236736954L;

    public ServerException() {
        super();
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable cause) {
        super(cause);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
