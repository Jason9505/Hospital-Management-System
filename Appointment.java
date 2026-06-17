
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
}