public class DoctorUser extends User 
{
    public DoctorUser(String username, String password) 
    {
        super(username, password, "Doctor");
    }

    @Override
    public String getDashboardTitle() 
    {
        return "Doctor Dashboard - View Appointments & Patients";
    }
}
