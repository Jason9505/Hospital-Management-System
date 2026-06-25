public class DoctorUser extends User 
{
    private String doctorID; // ADDED: link to Doctor domain record (e.g. "D001")

    public DoctorUser(String username, String password) 
    {
        super(username, password, "Doctor");
        this.doctorID = ""; // ADDED: default empty if not specified
    }

    // ADDED: new constructor with doctorID to link DoctorUser to their Doctor profile
    public DoctorUser(String username, String password, String doctorID) 
    {
        super(username, password, "Doctor");
        this.doctorID = doctorID;
    }

    // ADDED: getter for doctorID so other classes can find the linked Doctor profile
    public String getDoctorID() 
    {
        return doctorID;
    }

    @Override
    public String getDashboardTitle() 
    {
        return "Doctor Dashboard - View Appointments & Patients";
    }

    // ADDED: override toFileLine to persist doctorID after role
    @Override
    public String toFileLine() 
    {
        return super.toFileLine() + "|" + doctorID;
    }
}
