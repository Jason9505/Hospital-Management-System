public class Patient 
{
    private String patientID;
    private String name;
    private int age;
    private String gender;
    private String medicalHistory;

    public Patient(String patientID, String name, int age,String gender, String medicalHistory) 
    {
        this.patientID = patientID;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.medicalHistory = medicalHistory;
    }

    public Patient(String patientID, String name) 
    {
        this(patientID, name, 0, "", "");
    }

    public String getPatientID() 
    {
        return patientID;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public int getAge() 
    {
        return age;
    }

    public void setAge(int age) 
    {
        this.age = age;
    }

    public String getGender() 
    {
        return gender;
    }

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public String getMedicalHistory() 
    {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) 
    {
        this.medicalHistory = medicalHistory;
    }

    @Override
    public String toString() 
    {
        return name;
    }

    public String toFileLine() 
    {
        String safeHistory = medicalHistory == null
                ? "" : medicalHistory.replace("|", "/").replace("\n", " ");
        String safeName = name == null ? "" : name.replace("|", "/");
        String safeGender = gender == null ? "" : gender.replace("|", "/");

        return patientID + "|" + safeName + "|" + age + "|" + safeGender + "|" + safeHistory;
    }

    public static Patient fromFileLine(String line) 
    {
        if (line == null || line.trim().isEmpty()) 
        {
            return null;
        }

        String[] parts = line.split("\\|", -1);

        if (parts.length < 5) {
            return null;
        }

        try {
            String id = parts[0];
            String name = parts[1];
            int age = Integer.parseInt(parts[2].trim());
            String gender = parts[3];
            String history = parts[4];

            return new Patient(id, name, age, gender, history);

        } catch (NumberFormatException e) 
        {
            return null;
        }
    }
}
