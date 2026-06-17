public class Patient {

    private String patientID;
    private String name;

    public Patient(String patientID, String name) {
        this.patientID = patientID;
        this.name = name;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}