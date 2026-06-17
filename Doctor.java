
public class Doctor {

    private String doctorID;
    private String name;
    private String specialization;

    public Doctor(String doctorID,
                  String name,
                  String specialization) {

        this.doctorID = doctorID;
        this.name = name;
        this.specialization = specialization;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}