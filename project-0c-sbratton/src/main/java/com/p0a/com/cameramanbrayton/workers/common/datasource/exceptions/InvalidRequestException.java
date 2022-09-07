package main.java.com.p0a.com.cameramanbrayton.workers.common.datasource.exceptions;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException() {
        super("Invalid request data provided");
    }

    public InvalidRequestException(String message) {
        super(message);
    }
}
