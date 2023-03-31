import java.util.ArrayList;
import java.util.List;

public class Patient {
    private int patientId;
    private String patientName;
    private String patientLogin;
    private String patientPassword;
    private List<Appointment> patientVisitList = new ArrayList<>();


    public Patient(int patientId, String patientName, String patientLogin, String patientPassword, List<Appointment> patientVisitList) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientLogin = patientLogin;
        this.patientPassword = patientPassword;
        this.patientVisitList = patientVisitList;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientLogin() {
        return patientLogin;
    }

    public void setPatientLogin(String patientLogin) {
        this.patientLogin = patientLogin;
    }

    public String getPatientPassword() {
        return patientPassword;
    }

    public void setPatientPassword(String patientPassword) {
        this.patientPassword = patientPassword;
    }

    public List<Appointment> getPatientVisitList() {
        return patientVisitList;
    }

    public void setPatientVisitList(List<Appointment> patientVisitList) {
        this.patientVisitList = patientVisitList;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", patientLogin='" + patientLogin + '\'' +
                ", patientPassword='" + patientPassword + '\'' +    //dla testow haslo jest w toStringu
                '}';
    }
}

