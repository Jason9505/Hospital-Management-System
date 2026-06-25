import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame 
{
    private User loggedInUser;

    // ADDED: shared manager instances so all sub-GUIs share the same data layer
    private PatientManager patientManager;
    private DoctorManager doctorManager;
    private AppointmentManager appointmentManager;

    private JButton appointmentButton;
    private JButton patientButton;
    private JButton doctorButton;
    private JButton reportButton;
    private JButton logoutButton;
    private JButton exitButton;

    public MainMenu(User user) 
    {
        this.loggedInUser = user;

        // ADDED: create managers once and share across all sub-windows
        patientManager = new PatientManager();
        doctorManager = new DoctorManager();
        appointmentManager = new AppointmentManager(patientManager, doctorManager);

        setTitle("Hospital Management System");
        setSize(420, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();

        setVisible(true);
    }

    private void initializeComponents() 
    {
        setLayout(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));

        JLabel title = new JLabel("Hospital Management System", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        titlePanel.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel(loggedInUser.getDashboardTitle(), SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.ITALIC, 12));
        subtitle.setForeground(Color.DARK_GRAY);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        add(titlePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 15, 12));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 60, 15, 60));

        appointmentButton = new JButton("Appointment Booking");
        patientButton = new JButton("Patient Records");
        doctorButton = new JButton("Doctor Management");
        reportButton = new JButton("Reports");
        logoutButton = new JButton("Logout");
        exitButton = new JButton("Exit");

        String role = loggedInUser.getRole();

        if (role.equals("Admin")) 
        {
            buttonPanel.add(appointmentButton);
            buttonPanel.add(patientButton);
            buttonPanel.add(doctorButton);
            buttonPanel.add(reportButton);
        } 
        else if (role.equals("Doctor")) 
        {
            buttonPanel.add(appointmentButton);
            buttonPanel.add(reportButton);
        } 
        else if (role.equals("Receptionist")) 
        {
            buttonPanel.add(patientButton);
            buttonPanel.add(appointmentButton);
        }

        buttonPanel.add(logoutButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);

        // CHANGED: pass loggedInUser and shared managers to every sub-GUI
        appointmentButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                new AppointmentGUI(loggedInUser, patientManager, doctorManager, appointmentManager);
            }
        });

        patientButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                new PatientGUI(loggedInUser, patientManager);
            }
        });

        doctorButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                new DoctorGUI(loggedInUser, doctorManager, appointmentManager);
            }
        });

        reportButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                showReport();
            }
        });

        logoutButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                dispose();
                new LoginGUI();
            }
        });

        exitButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                System.exit(0);
            }
        });
    }

    // CHANGED: showReport now uses the shared managers instead of creating new ones
    private void showReport() 
    {
        StringBuilder report = new StringBuilder();
        report.append("=== SYSTEM REPORT ===\n\n");
        report.append("Total Patients: ").append(patientManager.getTotalPatients()).append("\n");
        report.append("Total Doctors: ").append(doctorManager.getTotalDoctors()).append("\n");
        report.append("Total Appointments: ").append(appointmentManager.getAppointments().size()).append("\n\n");

        report.append("=== DOCTOR SCHEDULES ===\n");
        for (Doctor d : doctorManager.getDoctors()) 
        {
            report.append("\n").append(d.getName()).append(" (").append(d.getSpecialization()).append("):\n");
            boolean hasAppt = false;
            for (Appointment a : appointmentManager.getAppointments()) 
            {
                if (a.getDoctor().getDoctorID().equals(d.getDoctorID())) 
                {
                    report.append("  - ").append(a.getDate()).append(" at ").append(a.getTime())
                           .append(" with ").append(a.getPatient().getName()).append("\n");
                    hasAppt = true;
                }
            }
            if (!hasAppt) 
            {
                report.append("  No appointments scheduled.\n");
            }
        }

        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "System Reports", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new LoginGUI();
            }
        });
    }
}
