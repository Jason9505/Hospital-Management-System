public class Receptionist extends User 
{
    public Receptionist(String username, String password) 
    {
        super(username, password, "Receptionist");
    }

    @Override
    public String getDashboardTitle() 
    {
        return "Receptionist Dashboard - Manage Patients & Appointments";
    }
}
