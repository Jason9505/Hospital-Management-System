import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JFrame 
{

    public MainMenu() 
    {
        setTitle("Hospital Management System - Main Menu");
        setSize(420, 360);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();

        setVisible(true);
    }

    private void initializeComponents() 
    {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Hospital Management System", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 30, 60));

        JButton appointmentButton = new JButton("Appointment Booking");
        JButton patientButton = new JButton("Patient Records");
        JButton doctorButton = new JButton("Doctor Management");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(appointmentButton);
        buttonPanel.add(patientButton);
        buttonPanel.add(doctorButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);

        appointmentButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                new AppointmentGUI();
            }
        });

        patientButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                new PatientGUI();
            }
        });

        doctorButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                new DoctorGUI();
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

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new MainMenu();
            }
        });
    }
}
