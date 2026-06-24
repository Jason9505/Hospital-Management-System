public class Doctor 
{
    private String doctorID;
    private String name;
    private String specialization;

    public Doctor(String doctorID,String name,String specialization) {

        this.doctorID = doctorID;
        this.name = name;
        this.specialization = specialization;
    }

    public String getDoctorID() 
    {
        return doctorID;
    }

    public String getName() 
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getSpecialization() 
    {
        return specialization;
    }

    public void setSpecialization(String specialization) 
    {
        this.specialization = specialization;
    }

    @Override
    public String toString() 
    {
        return name;
    }


    public String toFileLine() 
    {
        String safeName = name == null ? "" : name.replace("|", "/");
        String safeSpec = specialization == null ? "" : specialization.replace("|", "/");
        return doctorID + "|" + safeName + "|" + safeSpec;
    }

    public static Doctor fromFileLine(String line) 
    {

        if (line == null || line.trim().isEmpty()) 
        {
            return null;
        }

        String[] parts = line.split("\\|", -1);

        if (parts.length < 3) 
        {
            return null;
        }

        return new Doctor(parts[0], parts[1], parts[2]);
    }
}