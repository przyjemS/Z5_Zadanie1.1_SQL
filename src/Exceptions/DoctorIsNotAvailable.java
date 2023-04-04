package Exceptions;

public class DoctorIsNotAvailable extends RuntimeException {
    public String DoctorIsNotAvailable(String message) {
        return "DOCTOR ISNT AVAILABLE THIS DAY";
    }
}
