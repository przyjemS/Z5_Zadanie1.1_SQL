package Exceptions;

public class InvalidDoctorException extends RuntimeException {
    public String InvalidDoctorException (String message) {
        return "COULDNT FOUND DOCTOR";
    }
}
