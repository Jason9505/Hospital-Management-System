public class Appointment {

    private String appointmentID;
    private Patient patient;
    private Doctor doctor;
    private String date;
    private String time;

    public Appointment(String appointmentID,
                       Patient patient,
                       Doctor doctor,
                       String date,
                       String time) {

        this.appointmentID = appointmentID;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String toFileLine() {
        return appointmentID + "|" + patient.getPatientID() + "|"
                + doctor.getDoctorID() + "|" + date + "|" + time;
    }

    public static Appointment fromFileLine(String line,
                                            PatientManager patientManager,
                                            DoctorManager doctorManager) {

        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        String[] parts = line.split("\\|", -1);

        if (parts.length < 5) {
            return null;
        }

        String appointmentID = parts[0];
        String patientID = parts[1];
        String doctorID = parts[2];
        String date = parts[3];
        String time = parts[4];

        Patient patient = patientManager.findPatientByID(patientID);
        Doctor doctor = doctorManager.findDoctorByID(doctorID);

        if (patient == null || doctor == null) {
            return null;
        }

        return new Appointment(appointmentID, patient, doctor, date, time);
    }
}