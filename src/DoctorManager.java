import java.io.*;
import java.util.ArrayList;

public class DoctorManager 
{

    private static final String FILE_NAME = "doctors.txt";

    private ArrayList<Doctor> doctors;

    public DoctorManager() 
    {
        doctors = new ArrayList<>();
        loadFromFile();
    }

    public boolean addDoctor(Doctor doctor) 
    {
        for (Doctor d : doctors) 
        {
            if (d.getDoctorID().equals(doctor.getDoctorID())) 
            {
                return false;
            }
        }

        doctors.add(doctor);
        saveToFile();
        return true;
    }

    public boolean updateDoctor(String doctorID, String name, String specialization) 
    {
        Doctor doctor = findDoctorByID(doctorID);

        if (doctor == null) 
        {
            return false;
        }

        doctor.setName(name);
        doctor.setSpecialization(specialization);

        saveToFile();
        return true;
    }

    public boolean removeDoctor(String doctorID) 
    {
        Doctor doctor = findDoctorByID(doctorID);

        if (doctor == null) 
        {
            return false;
        }

        doctors.remove(doctor);
        saveToFile();
        return true;
    }

    public Doctor findDoctorByID(String doctorID) 
    {
        for (Doctor d : doctors) 
        {
            if (d.getDoctorID().equals(doctorID)) 
            {
                return d;
            }
        }
        return null;
    }

    public ArrayList<Doctor> getDoctors() 
    {
        return doctors;
    }

    public int getTotalDoctors() 
    {
        return doctors.size();
    }


    private void saveToFile() 
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) 
        {

            for (Doctor d : doctors) 
            {
                writer.println(d.toFileLine());
            }

        } catch (IOException e) 
        {
            System.err.println("Error saving doctors.txt: " + e.getMessage());
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

            while ((line = reader.readLine()) != null) 
            {
                Doctor d = Doctor.fromFileLine(line);

                if (d != null) 
                {
                    doctors.add(d);
                }
            }

        } catch (IOException e) 
        {
            System.err.println("Error loading doctors.txt: " + e.getMessage());
        }
    }
}
