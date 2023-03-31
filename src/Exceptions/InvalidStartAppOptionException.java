package Exceptions;

public class InvalidStartAppOptionException extends RuntimeException {
    public String InvalidStartingAppException(String message) {
        return "INVALID OPTION WHILE STARTING APP";
    }
}
