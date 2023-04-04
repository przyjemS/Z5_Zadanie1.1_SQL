package Exceptions;

public class InvalidLogInOptionException extends RuntimeException {
    public String InvalidLogInOptionException(String message) {
        return "INVALID OPTION WHILE LOGGING IN";
    }
}
