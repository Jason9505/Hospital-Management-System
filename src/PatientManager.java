import java.io.*;
import java.util.ArrayList;

public class PatientManager 
{

    private static final String FILE_NAME = "patients.txt";

    private ArrayList<Patient> patients;

    public PatientManager() 
    {
        patients = new ArrayList<>();
        loadFromFile();
    }

    public boolean addPatient(Patient patient) 
    {

        for (Patient p : patients) 
        {
            if (p.getPatientID().equals(patient.getPatientID())) 
            {
                return false;
            }
        }

        patients.add(patient);
        saveToFile();
        return true;
    }

    public boolean updatePatient(String patientID, String name, int age, String gender, String medicalHistory) 
    {
        Patient patient = findPatientByID(patientID);

        if (patient == null) 
        {
            return false;
        }

        patient.setName(name);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setMedicalHistory(medicalHistory);

        saveToFile();
        return true;
    }

    public boolean removePatient(String patientID) 
    {

        Patient patient = findPatientByID(patientID);

        if (patient == null) 
        {
            return false;
        }

        patients.remove(patient);
        saveToFile();
        return true;
    }

    public Patient findPatientByID(String patientID) 
    {
        for (Patient p : patients) 
        {
            if (p.getPatientID().equals(patientID)) 
            {
                return p;
            }
        }

        return null;
    }

    public ArrayList<Patient> getPatients() 
    {
        return patients;
    }

    public int getTotalPatients() 
    {
        return patients.size();
    }

    private void saveToFile() 
    {

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) 
        {

            for (Patient p : patients) 
            {
                writer.println(p.toFileLine());
            }

        }catch (IOException e) 
        {
            System.err.println("Error saving patients.txt: " + e.getMessage());
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

                Patient p = Patient.fromFileLine(line);

                if (p != null) 
                {
                    patients.add(p);
                }
            }

        } catch (IOException e) 
        {
            System.err.println("Error loading patients.txt: " + e.getMessage());
        }
    }
}
