public abstract class User 
{
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) 
    {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() 
    {
        return username;
    }

    public String getPassword() 
    {
        return password;
    }

    public String getRole() 
    {
        return role;
    }

    public abstract String getDashboardTitle();

    public boolean validatePassword(String inputPassword) 
    {
        return this.password.equals(inputPassword);
    }

    public String toFileLine() 
    {
        return username + "|" + password + "|" + role;
    }

    public static User fromFileLine(String line) 
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

        String username = parts[0];
        String password = parts[1];
        String role = parts[2];

        switch (role) 
        {
            case "Admin":
                return new Admin(username, password);
            case "Doctor":
                return new DoctorUser(username, password);
            case "Receptionist":
                return new Receptionist(username, password);
            default:
                return null;
        }
    }
}
