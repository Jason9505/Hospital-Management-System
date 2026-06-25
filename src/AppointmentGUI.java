import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AppointmentGUI extends JFrame 
{
    private User loggedInUser; // ADDED: track who is viewing this screen
    private PatientManager patientManager; // CHANGED: now accepts shared manager
    private DoctorManager doctorManager; // CHANGED: now accepts shared manager
    private AppointmentManager manager; // CHANGED: now accepts shared manager

    private JComboBox<Patient> patientBox;
    private JComboBox<Doctor> doctorBox;
    private JTextField dateField;
    private JComboBox<String> timeBox;
    private JButton bookButton;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    // CHANGED: constructor now accepts User context and all three shared managers
    public AppointmentGUI(User user, PatientManager sharedPM, DoctorManager sharedDM, AppointmentManager sharedAM) 
    {
        this.loggedInUser = user;
        this.patientManager = sharedPM;
        this.doctorManager = sharedDM;
        this.manager = sharedAM;

        setTitle("Hospital Management System - Appointment Booking");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        loadPatients();
        loadDoctors();
        loadTimes();
        applyRoleRestrictions(); // ADDED: restrict UI for Doctor role
        loadExistingAppointments();

        setVisible(true);
    }

    // ADDED: for Doctor users, pre-select their doctor profile, disable changes, and filter table
    private void applyRoleRestrictions() 
    {
        if (loggedInUser.getRole().equals("Doctor")) 
        {
            DoctorUser docUser = (DoctorUser) loggedInUser;
            // pre-select the logged-in doctor in the doctor combo box
            for (int i = 0; i < doctorBox.getItemCount(); i++) 
            {
                Doctor d = doctorBox.getItemAt(i);
                if (d.getDoctorID().equals(docUser.getDoctorID())) 
                {
                    doctorBox.setSelectedIndex(i);
                    break;
                }
            }
            // disable doctor selection and booking for view-only access
            doctorBox.setEnabled(false);
            bookButton.setEnabled(false);
        }
    }

    private void initializeComponents() 
    {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 10));

        patientBox = new JComboBox<>();
        doctorBox = new JComboBox<>();
        dateField = new JTextField();
        timeBox = new JComboBox<>();
        bookButton = new JButton("Book Appointment");

        formPanel.add(new JLabel("Patient:"));
        formPanel.add(patientBox);

        formPanel.add(new JLabel("Doctor:"));
        formPanel.add(doctorBox);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        formPanel.add(dateField);

        formPanel.add(new JLabel("Time:"));
        formPanel.add(timeBox);

        formPanel.add(new JLabel(""));
        formPanel.add(bookButton);

        add(formPanel, BorderLayout.NORTH);

        String[] columns = 
        {
            "Appointment ID",
            "Patient",
            "Doctor",
            "Date",
            "Time"
        };

        tableModel = new DefaultTableModel(columns, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        appointmentTable = new JTable(tableModel);

        JScrollPane scrollPane =
                new JScrollPane(appointmentTable);

        add(scrollPane, BorderLayout.CENTER);

        bookButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                bookAppointment();
            }

        });
    }

    private void loadPatients() 
    {
        // CHANGED: only seed sample data if the shared manager's list is empty
        if (patientManager.getPatients().isEmpty()) 
        {
            patientManager.addPatient(new Patient("P001", "John Tan", 34, "Male", "No known allergies"));
            patientManager.addPatient(new Patient("P002", "Ali Ahmad", 45, "Male", "Hypertension"));
            patientManager.addPatient(new Patient("P003", "Siti Aminah", 29, "Female", "Asthma"));
        }

        for (Patient p : patientManager.getPatients()) 
        {
            patientBox.addItem(p);
        }
    }

    private void loadDoctors() 
    {
        // CHANGED: only seed if the shared manager's list is empty
        if (doctorManager.getDoctors().isEmpty()) {
            doctorManager.addDoctor(new Doctor("D001", "Dr Lim", "Cardiology"));
            doctorManager.addDoctor(new Doctor("D002", "Dr Wong", "Pediatrics"));
            doctorManager.addDoctor(new Doctor("D003", "Dr Lee", "Neurology"));
        }

        for (Doctor d : doctorManager.getDoctors()) 
        {
            doctorBox.addItem(d);
        }
    }

    private void loadTimes() 
    {
        timeBox.addItem("09:00");
        timeBox.addItem("10:00");
        timeBox.addItem("11:00");
        timeBox.addItem("14:00");
        timeBox.addItem("15:00");
    }

    // CHANGED: filter table to only show the logged-in doctor's appointments if role is Doctor
    private void loadExistingAppointments() 
    {
        String doctorFilter = null;
        if (loggedInUser.getRole().equals("Doctor")) 
        {
            doctorFilter = ((DoctorUser) loggedInUser).getDoctorID();
        }

        for (Appointment a : manager.getAppointments()) 
        {
            if (doctorFilter != null && !a.getDoctor().getDoctorID().equals(doctorFilter)) 
            {
                continue; // skip appointments not belonging to this doctor
            }
            tableModel.addRow(new Object[]{
                a.getAppointmentID(),
                a.getPatient().getName(),
                a.getDoctor().getName(),
                a.getDate(),
                a.getTime()
            });
        }
    }

    private void bookAppointment() 
    {
        Patient patient = (Patient) patientBox.getSelectedItem();

        Doctor doctor = (Doctor) doctorBox.getSelectedItem();

        String time = timeBox.getSelectedItem().toString();
        
        String date = dateField.getText().trim();

        if (date.isEmpty()) {

            JOptionPane.showMessageDialog(this,"Please enter a date.");
            return;
        }
        
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDate enteredDate = LocalDate.parse(date, formatter);

            if (enteredDate.isBefore(LocalDate.now())) 
            {
                JOptionPane.showMessageDialog(this,"Appointment date cannot be in the past.");
                return;
            }
            
            if (enteredDate.isAfter(LocalDate.now().plusYears(1))) 
            {
                JOptionPane.showMessageDialog(this,"Appointment date cannot be more than 1 year in advance.");
                return;
            }

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(this,"Invalid date!\n" + "Please enter date as YYYY-MM-DD.\n" + "Example: 2026-06-30");
            return;
        }

        String appointmentID = "A" + (manager.getAppointments().size() + 1);

        Appointment appointment = new Appointment(appointmentID,patient,doctor,date,time);

        boolean success = manager.addAppointment(appointment);

        if (success) 
        {
            tableModel.addRow(new Object[]{
                appointmentID,
                patient.getName(),
                doctor.getName(),
                date,
                time
            });

            JOptionPane.showMessageDialog(this,"Appointment Booked Successfully!");
            dateField.setText("");

        } else 
        {
            JOptionPane.showMessageDialog(this,"Duplicate Time Slot!\n" + "Doctor already has an appointment " + "at this date and time.");
        }
    }
}
