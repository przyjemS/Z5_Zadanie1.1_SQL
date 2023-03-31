import java.util.List;

public class Doctor  {
    private int doctorId;
    private String doctorName;
    private String doctorLogin;
    private String doctorPassword;
    private List<Appointment> doctorVisitList;

    public Doctor(int doctorId, String doctorName, String doctorLogin, String doctorPassword, List<Appointment> doctorVisitList) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorLogin = doctorLogin;
        this.doctorPassword = doctorPassword;
        this.doctorVisitList = doctorVisitList;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorLogin() {
        return doctorLogin;
    }

    public void setDoctorLogin(String doctorLogin) {
        this.doctorLogin = doctorLogin;
    }

    public String getDoctorPassword() {
        return doctorPassword;
    }

    public void setDoctorPassword(String doctorPassword) {
        this.doctorPassword = doctorPassword;
    }

    public List<Appointment> getDoctorVisitList() {
        return doctorVisitList;
    }

    public void setDoctorVisitList(List<Appointment> doctorVisitList) {
        this.doctorVisitList = doctorVisitList;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", doctorLogin='" + doctorLogin + '\'' +
                ", doctorPassword='" + doctorPassword + '\'' +      //dla testow haslo jest w toStringu
                '}';
    }
}
