import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AppointmentGUI extends JFrame 
{

    private PatientManager patientManager = new PatientManager();
    private DoctorManager doctorManager = new DoctorManager();
    private AppointmentManager manager = new AppointmentManager(patientManager, doctorManager);

    private JComboBox<Patient> patientBox;
    private JComboBox<Doctor> doctorBox;
    private JTextField dateField;
    private JComboBox<String> timeBox;
    private JButton bookButton;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public AppointmentGUI() 
    {
        setTitle("Hospital Management System - Appointment Booking");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        loadPatients();
        loadDoctors();
        loadTimes();
        loadExistingAppointments();

        setVisible(true);
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

    private void loadExistingAppointments() 
    {
        for (Appointment a : manager.getAppointments()) {
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

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() 
            {
                new AppointmentGUI();
            }

        });
    }
}
