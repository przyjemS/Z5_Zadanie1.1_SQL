import Exceptions.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        startApp();
    }

    public static void startApp() {
        while (true) {
            System.out.println("Choose 1 to register in or 2 to login");
            String option = getString();

            switch (option) {
                case "1" -> register();
                case "2" -> logIn();
                case "exit" -> {
                    return;
                }
                default -> throw new InvalidStartAppOptionException();
            }
        }
    }

    public static void logIn() {
        while (true) {
            System.out.println("Choose 1 to login as doctor or 2 to login as patient");
            String roleOption = getString();

            switch (roleOption) {
                case "1" -> logInAsDoctor();
                case "2" -> logInAsPatient();
                case "exit" -> {
                    return;
                }
                default -> throw new InvalidLogInOptionException();
            }
        }
    }

    public static void register() {
        while (true) {
            System.out.println("Choose 1 to register as doctor or 2 to register as patient");
            String roleOption = getString();

            switch (roleOption) {
                case "1" -> registerAsDoctor();
                case "2" -> registerAsPatient();
                case "exit" -> {
                    return;
                }

                default -> throw new InvalidRegisterOptionException();
            }
        }
    }

    public static void registerAsDoctor() {
        String doctorName;
        String doctorLogin;
        String doctorPassword;
        boolean tmp = true;
        while (tmp) {
            System.out.println("Podaj Imie");
            doctorName = getString();
            System.out.println("Ustaw login");
            doctorLogin = getString();
            System.out.println("Ustaw haslo");
            doctorPassword = getString();
            addDoctorToDataBase(doctorName, doctorLogin, doctorPassword);
            if (!isDoctorInDataBase(doctorLogin, doctorPassword)) {
                tmp = false;
                logInAsDoctor();
            } else {
                throw new InvalidDoctorException();
            }
        }
    }

    public static void registerAsPatient() {
        String patientName;
        String patientLogin;
        String patientPassword;
        boolean tmp = true;
        while (tmp) {
            System.out.println("Podaj Imie");
            patientName = getString();
            System.out.println("Podaj login");
            patientLogin = getString();
            System.out.println("Ustaw haslo");
            patientPassword = getString();
            addPatientToDataBase(patientName, patientLogin, patientPassword);

            if (!isPatientInDataBase(patientLogin, patientPassword)) {
                tmp = false;
                logInAsPatient();
            } else {
                throw new InvalidPatientException();
            }
        }
    }

    public static void logInAsDoctor() {
        String doctorLogin;
        String doctorPassword;
        boolean tmp = true;
        while (tmp) {
            System.out.println("Ustaw login");
            doctorLogin = getString();
            System.out.println("Ustaw haslo");
            doctorPassword = getString();
            if (isDoctorInDataBase(doctorLogin, doctorPassword)) {
                tmp = false;
                menuForDoctor(doctorLogin);
            }
        }
    }

    public static void logInAsPatient() {
        String patientLogin;
        String patientPassword;
        boolean tmp = true;
        while (tmp) {
            System.out.println("Podaj login");
            patientLogin = getString();
            System.out.println("Ustaw haslo");
            patientPassword = getString();
            if (isPatientInDataBase(patientLogin, patientPassword)) {
                tmp = false;
                menuForPatient();
            }
        }
    }

    public static void menuForDoctor(String doctorLogin) {
        boolean tmp = true;
        while (tmp) {
            System.out.println("Wybierz co chcesz zrobic:");
            System.out.println("1. Wyswietl wszystkie wizyty");
            System.out.println("2. wyloguj");
            String menuOption = getString();

            switch (menuOption) {
                case "1" -> {
                    System.out.println("Wszystkie wizyty:");
                    List<Appointment> doctorAppointments = getDoctorAppointmentsFromDataBase(getDoctorFromDataBase(doctorLogin));
                    doctorAppointments.forEach(System.out::println);
                }
                case "2" -> {
                    System.out.println("Wylogowano");
                    tmp = false;
                }
            }
        }
    }

    public static void menuForPatient() {
        String patientLogin = null;
        LocalDate date = null;
        boolean tmp = true;
        while (tmp) {
            System.out.println("Wybierz co chcesz zrobic:");
            System.out.println("1. umowic wizyte");
            System.out.println("2. wyswietl wszystkie wizyty");
            System.out.println("3. wyswietl wizyty po okreslonej dacie");
            System.out.println("4. wyloguj");
            String menuOption = getString();

            switch (menuOption) {
                case "1" -> {
                    System.out.println("Podaj dane do umowienia wizyty:");
                    System.out.println("Twoj login:");
                    patientLogin = getString();
                    System.out.println("Login doktora:");
                    String doctorLogin = getString();
                    System.out.println("Data wizyty (YYYY-MM-DD):");
                    date = LocalDate.parse(getString());
                    addAppointmentToDataBase(patientLogin, doctorLogin, date);
                }
                case "2" -> {
                    System.out.println("Wizyty pacjenta:");
                    List<Appointment> patientAppointments = getPatientAppointmentsFromDataBase(getPatientFromDataBase(patientLogin));
                    patientAppointments.forEach(System.out::println);
                }
                case "3" -> {
                    System.out.println("Wizyty pacjenta po: " + date);
                    List<Appointment> patientAppointmentsAfterDate = getPatientAppointmentsAfterDate(patientLogin, date);
                    patientAppointmentsAfterDate.forEach(System.out::println);
                }
                case "4" -> {
                    System.out.println("Wylogowano");
                    tmp = false;
                }
            }

        }
    }

    public static String getString() {
        return new Scanner(System.in).next();
    }

    public static int getInt() {
        return new Scanner(System.in).nextInt();
    }

    /**
     * CZESC BAZO DANOWA
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/z5_zad1?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Zarozumialec1!");
    }

    public static void addDoctorToDataBase(String doctorName, String doctorLogin, String doctorPassword) {
        String sqlAsk = "INSERT INTO doctor (doctorId, doctorName, doctorLogin, doctorPassword) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlAsk);
             //Statement umozliwia wykonanie zapytania ktore nie wymagaja parametrow
             // czyli odwrotnie do PreparedStatement
             //createStatement zwraca nowy obiekt Statement zwiazany z polaczenie do bazy danych
             Statement statement = connection.createStatement()) {
            //executeQuery wykonuje zapytanie SQL w bazie danych, zwraca jako resultSet czyli u mnie wykonuje zapytanie o ostatnie Id doktora w tabeli
            preparedStatement.setInt(1, getLastDoctorIdFromDataBase());
            preparedStatement.setString(2, doctorName);
            preparedStatement.setString(3, doctorLogin);
            preparedStatement.setString(4, doctorPassword);
            //executeUpdate wstawia aktualizacje do SQL
            // i zwraca liczbe wierszy modyfikowanych przez zapytanie
            // natomiast execute jest bardzo ogolnikowa do kazdego zapytania
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getLastDoctorIdFromDataBase() {
        String getLastDoctorId = "SELECT MAX(doctorId) FROM doctor";
        try (// zapytanie jest na tyle proste(nie ma parametrow ktore beda wczytywane z zewnatrz)
             // ze wystarczy samo statement (nie trzeba PreparedStatement)
             Statement statement = getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(getLastDoctorId);
            int nextDoctorId = resultSet.next() ? resultSet.getInt(1) + 1 : 1;
            return nextDoctorId;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addPatientToDataBase(String name, String login, String password) {
        String sqlAsk = "INSERT INTO patient (patientId, patientName, patientLogin, patientPassword) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlAsk);
             Statement statement = connection.createStatement()) {
            preparedStatement.setInt(1, getLastPatientIdFromDataBase());
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, login);
            preparedStatement.setString(4, password);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getLastPatientIdFromDataBase() {
        String getLastPatientId = "SELECT MAX(patientId) FROM patient";
        try (Statement statement = getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(getLastPatientId);
            int nextDoctorId = resultSet.next() ? resultSet.getInt(1) + 1 : 1;
            return nextDoctorId;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Doctor getDoctorFromDataBase(String login) {
        try {
            String sqlAsk = "SELECT * FROM doctor WHERE login = ?";
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sqlAsk);
            while (resultSet.next()) {
                String doctorNameInDataBase = resultSet.getString("doctorName");
                String doctorPasswordInDataBase = resultSet.getString("doctorPassword");
                return new Doctor(getLastDoctorIdFromDataBase(), doctorNameInDataBase, login, doctorPasswordInDataBase, List.of());
            }
            throw new InvalidDoctorException();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isDoctorInDataBase(String login, String password) {
        try {
            String sqlAsk = "SELECT * FROM doctor WHERE doctorLogin = ? AND doctorPassword = ?";
//            Statement statement = getConnection().createStatement();
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlAsk);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String doctorLoginInDataBase = resultSet.getString("doctorLogin");
                String doctorPasswordInDataBase = resultSet.getString("doctorPassword");

                if (!password.equals(doctorPasswordInDataBase)) {
                    throw new InvalidPasswordException();
                } else if (!login.equalsIgnoreCase(doctorLoginInDataBase)) {
                    throw new InvalidLoginException();
//                } else if (!password.equals(doctorPasswordInDataBase) && !login.equalsIgnoreCase(doctorLoginInDataBase)) {
//                    throw new InvalidDoctorException();
                } else {
                    return true;
                }
            }
            throw new InvalidDoctorException();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isPatientInDataBase(String login, String password) {
        try {
            String sqlAsk = "SELECT * FROM patient WHERE patientLogin = ? AND patientPassword = ?";
//            Statement statement = getConnection().createStatement();
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlAsk);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String patientLoginInDataBase = resultSet.getString("patientLogin");
                String patientPasswordInDataBase = resultSet.getString("patientPassword");

                if (!password.equals(patientPasswordInDataBase)) {
                    throw new InvalidPasswordException();
                } else if (!login.equalsIgnoreCase(patientLoginInDataBase)) {
                    throw new InvalidLoginException();
//                } else if (!password.equals(patientPasswordInDataBase) && !login.equalsIgnoreCase(patientLoginInDataBase)) {
//                    throw new InvalidDoctorException();
                } else {
                    return true;
                }
            }
            throw new InvalidPatientException();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Patient getPatientFromDataBase(String login) {
        try {
            String sqlAsk = "SELECT * FROM patient WHERE patientLogin = ?";
            PreparedStatement preparedStatement = getConnection().prepareStatement(sqlAsk);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String patientNameInDataBase = resultSet.getString("patientName");
                String patientPasswordInDataBase = resultSet.getString("patientPassword");
                return new Patient(resultSet.getInt("patientId"), patientNameInDataBase, login, patientPasswordInDataBase, List.of());
            }
            throw new InvalidPatientException();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addAppointmentToDataBase(String patientLogin, String doctorLogin, LocalDate appointmentDate) {
        String sqlAsk = "INSERT INTO appointment (appointmentId, patientId, doctorId, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlAsk);
             Statement statement = getConnection().createStatement()) {
            if (isDoctorAvailable(doctorLogin, appointmentDate)) {
                preparedStatement.setInt(1, getLastAppointmentIdFromDataBase());
                preparedStatement.setString(2, getPatientFromDataBase(patientLogin).getPatientLogin());
                preparedStatement.setString(3, getDoctorFromDataBase(doctorLogin).getDoctorLogin());
                preparedStatement.setDate(4, Date.valueOf(appointmentDate));
                preparedStatement.executeUpdate();
            } else {
                throw new DoctorIsNotAvailable();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isDoctorAvailable(String doctorLogin, LocalDate appointmentDate) {
        Doctor doctor = getDoctorFromDataBase(doctorLogin);
        List<Appointment> doctorAppointments = getDoctorAppointmentsFromDataBase(doctor);
        for (Appointment appointment : doctorAppointments) {
            if (appointment.getDoctor().getDoctorLogin().equalsIgnoreCase(doctorLogin) && appointment.getAppointmentDate().equals(appointmentDate)) {
                return false;
            }
        }
        return true;
    }

    public static int getLastAppointmentIdFromDataBase() {
        String getLastAppointmentId = "SELECT MAX(patientId) FROM patient";
        try (Statement statement = getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(getLastAppointmentId);
            int nextAppointmentId = resultSet.next() ? resultSet.getInt(1) + 1 : 1;
            return nextAppointmentId;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Appointment> getDoctorAppointmentsFromDataBase(Doctor doctor) {
        String sqlAsk = "SELECT * FROM appointment WHERE doctorId = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlAsk)) {
            preparedStatement.setInt(1, doctor.getDoctorId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Appointment> doctorAppointments = new ArrayList<>();
                while (resultSet.next()) {
                    int appointmentId = resultSet.getInt("appointmentId");
                    int patientId = resultSet.getInt("patientId");
                    LocalDate appointmentDate = resultSet.getDate("date").toLocalDate();
                    doctorAppointments.add(new Appointment(appointmentId, doctor, getPatientByIdFromDataBase(patientId), appointmentDate));
                }
                return doctorAppointments;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Appointment> getPatientAppointmentsFromDataBase(Patient patient) {
        String sqlAsk = "SELECT * FROM appointment WHERE patientId = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlAsk)) {
            preparedStatement.setInt(1, patient.getPatientId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Appointment> patientAppointments = new ArrayList<>();
                while (resultSet.next()) {
                    int appointmentId = resultSet.getInt("appointmentId");
                    int doctorId = resultSet.getInt("patientId");
                    LocalDate appointmentDate = resultSet.getDate("date").toLocalDate();
                    patientAppointments.add(new Appointment(appointmentId, getDoctorByIdFromDataBase(doctorId), patient, appointmentDate));
                }
                return patientAppointments;
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Appointment> getPatientAppointmentsAfterDate(String patientLogin, LocalDate afterDate) {
        List<Appointment> allAppointments = getPatientAppointmentsFromDataBase(getPatientFromDataBase(patientLogin));
        List<Appointment> appointmentsAfterDate = new ArrayList<>();
        for (Appointment appointment : allAppointments) {
            if (appointment.getAppointmentDate().isAfter(afterDate)) {
                appointmentsAfterDate.add(appointment);
            }
        }
        return appointmentsAfterDate;
    }

    public static Patient getPatientByIdFromDataBase(int patientId) {
        String sqlAsk = "SELECT * FROM patient WHERE patientId = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlAsk)) {
            preparedStatement.setInt(1, patientId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String patientName = resultSet.getString("patientName");
                    String patientLogin = resultSet.getString("patientLogin");
                    String patientPassword = resultSet.getString("patientPassword");
                    return new Patient(patientId, patientName, patientLogin, patientPassword, List.of());
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static Doctor getDoctorByIdFromDataBase(int doctorId) {
        String sqlAsk = "SELECT * FROM patient WHERE patientId = ?";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sqlAsk)) {
            preparedStatement.setInt(1, doctorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String doctorName = resultSet.getString("doctorName");
                    String doctorLogin = resultSet.getString("doctorLogin");
                    String doctorPassword = resultSet.getString("doctorPassword");
                    return new Doctor(doctorId, doctorName, doctorLogin, doctorPassword, List.of());
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}