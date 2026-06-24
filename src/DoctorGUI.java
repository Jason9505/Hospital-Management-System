import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class DoctorGUI extends JFrame 
{
    private DoctorManager doctorManager = new DoctorManager();

    private AppointmentManager appointmentManager;

    private JTextField idField;
    private JTextField nameField;
    private JTextField specializationField;

    private JButton addButton;
    private JButton viewScheduleButton;
    private JButton removeButton;
    private JButton clearButton;

    private JTable doctorTable;
    private DefaultTableModel tableModel;

    public DoctorGUI() 
    {
        this(new AppointmentManager(new PatientManager(), new DoctorManager()));
    }

    public DoctorGUI(AppointmentManager sharedAppointmentManager) 
    {
        this.appointmentManager = sharedAppointmentManager;

        setTitle("Hospital Management System - Doctor Management");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        loadSampleDoctors();

        setVisible(true);
    }

    private void initializeComponents() 
    {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idField = new JTextField();
        nameField = new JTextField();
        specializationField = new JTextField();

        addButton = new JButton("Add Doctor");
        viewScheduleButton = new JButton("View Schedule");
        removeButton = new JButton("Remove Doctor");
        clearButton = new JButton("Clear");

        formPanel.add(new JLabel("Doctor ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Specialization:"));
        formPanel.add(specializationField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(viewScheduleButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);

        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.NORTH);

        String[] columns = {"Doctor ID", "Name", "Specialization"};

        tableModel = new DefaultTableModel(columns, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        doctorTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(doctorTable);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                addDoctor();
            }
        });

        viewScheduleButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                viewSchedule();
            }
        });

        removeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                removeDoctor();
            }
        });

        clearButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                clearForm();
            }
        });

        doctorTable.getSelectionModel().addListSelectionListener(e -> 
        {
            int row = doctorTable.getSelectedRow();
            if (row >= 0) {
                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                specializationField.setText(tableModel.getValueAt(row, 2).toString());
            }
        });
    }

    private void loadSampleDoctors() 
    {

        if (doctorManager.getDoctors().isEmpty()) 
        {
            doctorManager.addDoctor(new Doctor("D001", "Dr Lim", "Cardiology"));
            doctorManager.addDoctor(new Doctor("D002", "Dr Wong", "Pediatrics"));
            doctorManager.addDoctor(new Doctor("D003", "Dr Lee", "Neurology"));
        }

        for (Doctor d : doctorManager.getDoctors()) 
        {
            tableModel.addRow(new Object[]{
                d.getDoctorID(),
                d.getName(),
                d.getSpecialization()
            });
        }
    }

    private void addDoctor() 
    {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String specialization = specializationField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || specialization.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this,
                    "Doctor ID, Name, and Specialization are required.");
            return;
        }

        Doctor doctor = new Doctor(id, name, specialization);

        if (doctorManager.addDoctor(doctor)) {

            tableModel.addRow(new Object[]{id, name, specialization});

            JOptionPane.showMessageDialog(this, "Doctor added successfully!");
            clearForm();

        } else {
            JOptionPane.showMessageDialog(this,
                    "Duplicate Doctor ID!\nA doctor with this ID already exists.");
        }
    }

    private void viewSchedule() 
    {
        String id = idField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Select a doctor from the table first.");
            return;
        }

        Doctor doctor = doctorManager.findDoctorByID(id);

        if (doctor == null) {
            JOptionPane.showMessageDialog(this, "Doctor not found.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Schedule for ").append(doctor.getName())
          .append(" (").append(doctor.getSpecialization()).append(")\n\n");

        boolean hasAppointments = false;

        for (Appointment a : appointmentManager.getAppointments()) 
        {
            if (a.getDoctor().getDoctorID().equals(id)) 
            {
                sb.append(a.getDate()).append("  ")
                  .append(a.getTime()).append("  -  ")
                  .append(a.getPatient().getName()).append("\n");
                hasAppointments = true;
            }
        }

        if (!hasAppointments) 
        {
            sb.append("No appointments scheduled.");
        }

        JOptionPane.showMessageDialog(this, sb.toString(),"Doctor Schedule", JOptionPane.INFORMATION_MESSAGE);
    }

    private void removeDoctor() 
    {
        String id = idField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Select a doctor from the table or enter a Doctor ID to remove.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,"Remove doctor " + id + "? This cannot be undone.", "Confirm Remove",JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) 
        {
            return;
        }

        boolean success = doctorManager.removeDoctor(id);

        if (success) 
        {
            int row = doctorTable.getSelectedRow();
            if (row >= 0) 
            {
                tableModel.removeRow(row);
            }

            JOptionPane.showMessageDialog(this, "Doctor removed successfully!");
            clearForm();

        } else 
        {
            JOptionPane.showMessageDialog(this, "Doctor ID not found.");
        }
    }

    private void clearForm() 
    {
        idField.setText("");
        nameField.setText("");
        specializationField.setText("");
        doctorTable.clearSelection();
    }

    public DoctorManager getManager() 
    {
        return doctorManager;
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new DoctorGUI();
            }
        });
    }
}
