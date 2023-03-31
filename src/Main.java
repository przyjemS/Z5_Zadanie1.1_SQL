import Exceptions.*;

import java.sql.*;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        List<Patient> patientList;
        List<Doctor> doctorList;

    }

    public void startApp(List<Doctor> doctorList, List<Patient> patientList) {
        while (true) {
            String option = getString();
            System.out.println("Choose 1 to log in or 2 to register");

            switch (option) {
                case "1" -> {
                    logIn(doctorList, patientList);
                }
                case "2" -> {
                    register(doctorList, patientList);
                }
                case "exit" -> {
                    return;
                }
                default -> throw new InvalidStartAppOptionException();
            }
        }
    }

    public void logIn(List<Doctor> doctorList, List<Patient> patientList) {
        // trzeba wybrac czy logujesz sie jako doctor czy jako pacjent zeby potem mu wyswietlalo inne rzeczy
        while (true) {
            System.out.println("Choose 1 to login as doctor or 2 to login as patient");
            String roleOption = getString();

            switch (roleOption) {
                case "1" -> {
                    logInAsDoctor(doctorList);
                }
                case "2" -> {
                    logInAsPatient(patientList);
                }
                case "exit" -> {
                    return;
                }
                default -> throw new InvalidRegisterOptionException();
            }
        }
    }

    public void register(List<Doctor> doctorList, List<Patient> patientList) {
        while (true) {
            System.out.println("Choose 1 to register as doctor or 2 to register as patient");
            String roleOption = getString();

            switch (roleOption) {
                case "1" -> {
                    registerAsDoctor(doctorList);
                }
                case "2" -> {
                    registerAsPatient(patientList);
                }
                case "exit" -> {
                    return;
                }

                default -> throw new InvalidRegisterOptionException();
            }
        }
    }

    //TODO na koniec register raczej login trzeba dac
    public void registerAsDoctor(List<Doctor> doctorList) {
        int doctorId = 0;
        String doctorName;
        String doctorLogin;
        String doctorPassword;
        boolean tmp = true;
        while (tmp) {
            System.out.println("Podaj imie:");
            doctorName = getString();
            System.out.println("Podaj login");
            doctorLogin = getString();
            System.out.println("Ustaw haslo");
            doctorPassword = getString();
            doctorList.add(new Doctor(doctorId++, doctorName, doctorLogin, doctorPassword, List.of()));
            tmp = false;
//            System.out.println("Jezeli chcesz zarejestrowac kolejnego doktora wybierz 1, w przeciwnym wypadku 0 - wyjscie");
//            int option = getInt();
//            if (option == 1) {
//                tmp = true;
//            } else {
//                tmp = false;
//            }
        }
        //TODO przejscie dalej (jakas metoda co daje opcje do wybrania np wyswietl albo cos)
    }

    public void registerAsPatient(List<Patient> patientList) {
        int patientId = 0;
        //id bedzie trzeba spraawdzac ostatnie jakie jest w bazie danych
        String patientName;
        String patientLogin;
        String patientPassword;
        boolean tmp = true;
        while (tmp) {
            System.out.println("Podaj imie:");
            patientName = getString();
            System.out.println("Podaj login");
            patientLogin = getString();
            System.out.println("Ustaw haslo");
            patientPassword = getString();
            patientList.add(new Patient(patientId++, patientName, patientLogin, patientPassword, List.of()));
            tmp = false;
//            System.out.println("Jezeli chcesz zarejestrowac kolejnego pacjenta wybierz 1, w przeciwnym wypadku 0 - wyjscie");
//            int option = getInt();
//            if (option == 1) {
//                tmp = true;
//            } else {
//                tmp = false;
//            }
        }
        //TODO przejscie dalej (jakas metoda co daje opcje do wybrania np wyswietl albo cos)
//        menuForDoctor();
        logInAsPatient(patientList);
    }

    // metoda nie ma miec parametru metody?
    public void logInAsDoctor(List<Doctor> doctorList) {
        String doctorLogin;
        String doctorPassword;
        boolean tmp = true;
        while (tmp) {
            System.out.println("Podaj login:");
            doctorLogin = getString();
            //pojdzie search po bazie danych ma wypisac wszystkich uzytkownikoiw z tym samym loginem i sprawdzic nastepnie
            // czy haslo sie zgadza z podanychj
            System.out.println("Podaj haslo");
            doctorPassword = getString();

            for (Doctor doctor : doctorList) {
                if (doctor.getDoctorLogin().equalsIgnoreCase(doctorLogin) && doctor.getDoctorPassword().equals(doctorPassword)) {
                    menuForDoctor();
                } else if (doctor.getDoctorLogin().equalsIgnoreCase(doctorLogin) && !doctor.getDoctorPassword().equals(doctorPassword)) {
                    throw new InvalidPasswordException();
                } else if (!doctor.getDoctorLogin().equalsIgnoreCase(doctorLogin) && doctor.getDoctorPassword().equals(doctorPassword)) {
                    throw new InvalidLoginException();
                } else {
                    throw new InvalidDoctorException();
                }
            }
            tmp = false;
        }
        menuForDoctor();
    }


    public void logInAsPatient(List<Patient> patientList) {

    }

    public void menuForDoctor() {

    }

    public void menuForPatient() {

    }


    public List<Appointment> showPatientAppointments(List<Patient> patientList, int searchedPatientID) {
        return patientList.stream().filter(patient -> patient.getPatientId() == searchedPatientID)
                .findFirst()
                .map(Patient::getPatientVisitList)
                .orElseThrow(InvalidPatientException::new);
    }

    public List<Appointment> showPatientAppointmentsAfterDate(List<Patient> patientList, int searchedPatientID, LocalDate date) {
        return patientList.stream()
                .filter(patient -> patient.getPatientId() == searchedPatientID)
                .findFirst()
                .map(patient -> patient.getPatientVisitList().stream()
                        .filter(appointment -> appointment.getAppointmentDate().equals(date))
                        .toList())
                .orElseThrow(InvalidPatientException::new);
    }

    public List<Appointment> showDoctorAppointments(List<Doctor> doctorList, int searchedDoctorID) {
        return doctorList.stream().filter(doctor -> doctor.getDoctorId() == searchedDoctorID)
                .findFirst()
                .map(Doctor::getDoctorVisitList)
                .orElseThrow(InvalidDoctorException::new);
    }

    public static String getString() {
        return new Scanner(System.in).next();
    }

    public static int getInt() {
        return new Scanner(System.in).nextInt();
    }

    /**
     CZESC BAZO DANOWA
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/z5_zad1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Zarozumialec1");
    }

    //TODO void czy Doctor
    public static void addDoctorToDataBase(Doctor doctorToBeAdded) {
        String sqlAsk = "INSERT INTO doctor (doctorId, doctorName, doctorLogin, doctorPassword) VALUES (?, ?, ?, ?)";
        String getLastDoctorId = "SELECT MAX(doctorId) FROM doctor";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlAsk);
             //Statement umozliwia wykonanie zapytania ktore nie wymagaja parametrow
             // czyli odwrotnie do PreparedStatement
             //createStatement zwraca nowy obiekt Statement zwiazany z polaczenie do bazy danych
             Statement statement = connection.createStatement()) {
            //executeQuery wykonuje zapytanie SQL w bazie danych, zwraca jako resultSet czyli u mnie wykonuje zapytanie o ostatnie Id doktora w tabeli
            ResultSet resultSet = statement.executeQuery(getLastDoctorId);
            int nextId = resultSet.next() ? resultSet.getInt(1) + 1 : 1;
            preparedStatement.setInt(1, nextId);
            preparedStatement.setString(2, doctorToBeAdded.getDoctorName());
            preparedStatement.setString(3, doctorToBeAdded.getDoctorLogin());
            preparedStatement.setString(4, doctorToBeAdded.getDoctorPassword());
            //executeUpdate wstawia aktualizacje do SQL
            // i zwraca liczbe wierszy modyfikowanych przez zapytanie
            // natomiast execute jest bardzo ogolnikowa do kazdego zapytania
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addPatientToDataBase(Patient patientToBeAdded) {
        String sqlAsk = "INSERT INTO patient (patientId, patientName, patientLogin, patientPassword) VALUES (?, ?, ?, ?)";
        String getLastPatientId = "SELECT MAX(patientId) FROM patient";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlAsk);
             //Statement umozliwia wykonanie zapytania ktore nie wymagaja parametrow
             // czyli odwrotnie do PreparedStatement
             //createStatement zwraca nowy obiekt Statement zwiazany z polaczenie do bazy danych
             Statement statement = connection.createStatement()) {
            //executeQuery wykonuje zapytanie SQL w bazie danych, zwraca jako resultSet czyli u mnie wykonuje zapytanie o ostatnie Id doktora w tabeli
            ResultSet resultSet = statement.executeQuery(getLastPatientId);
            int nextId = resultSet.next() ? resultSet.getInt(1) + 1 : 1;
            preparedStatement.setInt(1, nextId);
            preparedStatement.setString(2, patientToBeAdded.getPatientName());
            preparedStatement.setString(3, patientToBeAdded.getPatientLogin());
            preparedStatement.setString(4, patientToBeAdded.getPatientPassword());
            //executeUpdate wstawia aktualizacje do SQL
            // i zwraca liczbe wierszy modyfikowanych przez zapytanie
            // natomiast execute jest bardzo ogolnikowa do kazdego zapytania
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static Doctor getDoctorFromDataBase(String login, String password) {
        String sqlAsk = "SELECT * FROM doctor WHERE login = ? AND password = ?";
        try (Connection connection = getConnection();
             // PreparedStatement to obiekt co pozwala na robienie zapytan do SQL ktore bedzie wykonane
             // w bazie danych
             PreparedStatement preparedStatement = connection.prepareStatement(sqlAsk)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            // ResultSet to obiekt ktory przechowywuje wyniki zapytania SQL
            // i umozliwia iterowanie po nich
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int doctorId = resultSet.getInt("doctorId");
                    String doctorName = resultSet.getString("doctorName");
                    return new Doctor(doctorId, doctorName, login, password, List.of());
                } else {
                    throw new InvalidDoctorException();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Patient getPatientFromDataBase(String login, String password) {
        String sqlAsk = "SELECT * FROM doctor WHERE login = ? AND password = ?";
        try (Connection connection = getConnection();
             // PreparedStatement to obiekt co pozwala na robienie zapytan do SQL ktore bedzie wykonane
             // w bazie danych
             PreparedStatement preparedStatement = connection.prepareStatement(sqlAsk)) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            // ResultSet to obiekt ktory przechowywuje wyniki zapytania SQL
            // i umozliwia iterowanie po nich
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int patientId = resultSet.getInt("patientId");
                    String patientName = resultSet.getString("patientName");
                    return new Patient(patientId, login, patientName, password, List.of());
                } else {
                    throw new InvalidPatientException();
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
/**
 stworz aplikacje ktora bedzie miala mozliwosci:
 1. rejestracji uzytkownika
 2. logowanie uzytkownika, uzytkownik(pacjent) zalogowany ma dane opcje do wyboru:
 a) umowienie wizyty
 b) DONE wyswietlenie wszystkich umowionych swoich wizyt
 c) DONE wyswietlenie wszystkich swoich wizyt po konkretnej dacie
 d) wyloguj
 3. zalogowany uzytkownik (lekarz) ma opcje:
 a) DONE wyswietlenie swoich wszystkich wizyt
 b) wyloguj / wylaczenie programu
 zaimplementuj kazdy z punktow tak aby uzytkownik mial wply na dzialanie aplikacji wszystkie dane zapisujemy w bazie danych mySQL
 */

