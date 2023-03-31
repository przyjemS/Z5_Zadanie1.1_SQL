package Exceptions;

public class InvalidRegisterOptionException extends RuntimeException {
    public String InvalidRegisterOptionException (String message) {
        return "INVALID OPTION WHILE REGISTERING";
    }
}
