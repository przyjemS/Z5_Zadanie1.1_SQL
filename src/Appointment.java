import java.sql.Date;
import java.time.LocalDate;

public class Appointment {
    private int appointmentId;
    private Doctor doctor;
    private Patient patient;
    private LocalDate appointmentDate;

    public Appointment(int appointmentId, Doctor doctor, Patient patient, LocalDate appointmentDate) {
        this.appointmentId = appointmentId;
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDate = appointmentDate;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", doctor=" + doctor +
                ", patient=" + patient +
                ", appointmentDate=" + appointmentDate +
                '}';
    }
}