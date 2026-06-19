import java.io.*;
import java.util.ArrayList;

public class AppointmentManager 
{
    private static final String FILE_NAME = "appointments.txt";

    private ArrayList<Appointment> appointments;

    private PatientManager patientManager;
    private DoctorManager doctorManager;

    public AppointmentManager(PatientManager patientManager, DoctorManager doctorManager) 
    {
        this.patientManager = patientManager;
        this.doctorManager = doctorManager;
        appointments = new ArrayList<>();
        loadFromFile();
    }

    public boolean addAppointment(Appointment appointment) 
    {
        for (Appointment a : appointments) 
        {
            if (a.getDoctor().getDoctorID()
                    .equals(appointment.getDoctor().getDoctorID())
                    &&
                a.getDate().equals(appointment.getDate())
                    &&
                a.getTime().equals(appointment.getTime())) 
            {
                return false;
            }
        }

        appointments.add(appointment);
        saveToFile();
        return true;
    }

    public ArrayList<Appointment> getAppointments() 
    {
        return appointments;
    }


    private void saveToFile() 
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {

            for (Appointment a : appointments) 
            {
                writer.println(a.toFileLine());
            }

        } catch (IOException e) 
        {
            System.err.println("Error saving appointments.txt: " + e.getMessage());
        }
    }

    private void loadFromFile() 
    {
        File file = new File(FILE_NAME);

        if (!file.exists()) 
        {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
        {

            String line;

            while ((line = reader.readLine()) != null) { Appointment a = Appointment.fromFileLine(line, patientManager, doctorManager);

                if (a != null) 
                {
                    appointments.add(a);
                }
            }

        } catch (IOException e) 
        {
            System.err.println("Error loading appointments.txt: " + e.getMessage());
        }
    }
}
