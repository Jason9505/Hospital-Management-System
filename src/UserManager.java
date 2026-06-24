import java.io.*;
import java.util.ArrayList;

public class UserManager 
{
    private static final String FILE_NAME = "users.txt";

    private ArrayList<User> users;

    public UserManager() 
    {
        users = new ArrayList<>();
        loadFromFile();
    }

    public User authenticate(String username, String password) 
    {
        if (username == null || username.trim().isEmpty()) 
        {
            return null;
        }

        if (password == null || password.trim().isEmpty()) 
        {
            return null;
        }

        for (User u : users) 
        {
            if (u.getUsername().equals(username.trim())) 
            {
                if (u.validatePassword(password)) 
                {
                    return u;
                } 
                else 
                {
                    return null;
                }
            }
        }

        return null;
    }

    public ArrayList<User> getUsers() 
    {
        return users;
    }

    private void loadFromFile() 
    {
        File file = new File(FILE_NAME);

        if (!file.exists()) 
        {
            createDefaultUsers();
            saveToFile();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                User u = User.fromFileLine(line);
                if (u != null) 
                {
                    users.add(u);
                }
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error loading users.txt: " + e.getMessage());
        }
    }

    private void saveToFile() 
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) 
        {
            for (User u : users) 
            {
                writer.println(u.toFileLine());
            }
        } 
        catch (IOException e) 
        {
            System.err.println("Error saving users.txt: " + e.getMessage());
        }
    }

    private void createDefaultUsers() 
    {
        users.add(new Admin("admin", "admin123"));
        users.add(new DoctorUser("drlim", "pass123"));
        users.add(new Receptionist("reception", "pass123"));
    }
}
