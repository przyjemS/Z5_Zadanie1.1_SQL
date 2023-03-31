package Exceptions;

public class InvalidPatientException extends RuntimeException {
    public String InvalidPatientException (String message) {
        return "COULDNT FOUND PATIENT";
    }
}
