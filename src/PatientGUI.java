import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class PatientGUI extends JFrame 
{
    private PatientManager manager = new PatientManager();

    private JTextField idField;
    private JTextField nameField;
    private JTextField ageField;
    private JComboBox<String> genderBox;
    private JTextArea historyArea;

    private JButton addButton;
    private JButton updateButton;
    private JButton removeButton;
    private JButton clearButton;

    private JTable patientTable;
    private DefaultTableModel tableModel;

    public PatientGUI() 
    {

        setTitle("Hospital Management System - Patient Records");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        loadSamplePatients();

        setVisible(true);
    }

    private void initializeComponents() 
    {

        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idField = new JTextField();
        nameField = new JTextField();
        ageField = new JTextField();
        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        historyArea = new JTextArea(2, 20);

        addButton = new JButton("Add Patient");
        updateButton = new JButton("Update Patient");
        removeButton = new JButton("Remove Patient");
        clearButton = new JButton("Clear");

        formPanel.add(new JLabel("Patient ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Age:"));
        formPanel.add(ageField);

        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderBox);

        formPanel.add(new JLabel("Medical History:"));
        formPanel.add(new JScrollPane(historyArea));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(clearButton);

        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);

        add(formPanel, BorderLayout.NORTH);

        String[] columns = {"Patient ID", "Name", "Age", "Gender", "Medical History"};

        tableModel = new DefaultTableModel(columns, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        patientTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(patientTable);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                addPatient();
            }
        });

        updateButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                updatePatient();
            }
        });

        removeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                removePatient();
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

        patientTable.getSelectionModel().addListSelectionListener(e -> 
        {
            int row = patientTable.getSelectedRow();
            if (row >= 0) {
                idField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                ageField.setText(tableModel.getValueAt(row, 2).toString());
                genderBox.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                historyArea.setText(tableModel.getValueAt(row, 4).toString());
            }
        });
    }

    private void loadSamplePatients() 
    {

        if (manager.getPatients().isEmpty()) 
        {
            manager.addPatient(new Patient("P001", "John Tan", 34, "Male", "No known allergies"));
            manager.addPatient(new Patient("P002", "Ali Ahmad", 45, "Male", "Hypertension"));
            manager.addPatient(new Patient("P003", "Siti Aminah", 29, "Female", "Asthma"));
        }

        for (Patient p : manager.getPatients()) 
        {
            tableModel.addRow(new Object[]
            {
                p.getPatientID(),
                p.getName(),
                p.getAge(),
                p.getGender(),
                p.getMedicalHistory()
            });
        }
    }

    private void addPatient() 
    {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String gender = genderBox.getSelectedItem().toString();
        String history = historyArea.getText().trim();

        if (id.isEmpty() || name.isEmpty() || ageText.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this,"Patient ID, Name, and Age are required.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0 || age > 150) 
            {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.");
                return;
            }
        } catch (NumberFormatException ex) 
        {
            JOptionPane.showMessageDialog(this, "Age must be a number.");
            return;
        }

        Patient patient = new Patient(id, name, age, gender, history);

        if (manager.addPatient(patient)) 
        {
            tableModel.addRow(new Object[]{id, name, age, gender, history});

            JOptionPane.showMessageDialog(this, "Patient added successfully!");
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Duplicate Patient ID!\nA patient with this ID already exists.");
        }
    }

    private void updatePatient() 
    {
        String id = idField.getText().trim();

        if (id.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this,
                    "Select a patient from the table or enter a Patient ID to update.");
            return;
        }

        String name = nameField.getText().trim();
        String ageText = ageField.getText().trim();
        String gender = genderBox.getSelectedItem().toString();
        String history = historyArea.getText().trim();

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) 
        {
            JOptionPane.showMessageDialog(this, "Age must be a number.");
            return;
        }

        boolean success = manager.updatePatient(id, name, age, gender, history);

        if (success) {

            int row = patientTable.getSelectedRow();
            if (row >= 0) {
                tableModel.setValueAt(name, row, 1);
                tableModel.setValueAt(age, row, 2);
                tableModel.setValueAt(gender, row, 3);
                tableModel.setValueAt(history, row, 4);
            }

            JOptionPane.showMessageDialog(this, "Patient updated successfully!");
            clearForm();

        } else {
            JOptionPane.showMessageDialog(this,"Patient ID not found.\nUse Add Patient for new records.");
        }
    }

    private void removePatient() 
    {
        String id = idField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Select a patient from the table or enter a Patient ID to remove.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,"Remove patient " + id + "? This cannot be undone.","Confirm Remove",JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) 
        {
            return;
        }

        boolean success = manager.removePatient(id);

        if (success) {

            int row = patientTable.getSelectedRow();
            if (row >= 0) {
                tableModel.removeRow(row);
            }

            JOptionPane.showMessageDialog(this, "Patient removed successfully!");
            clearForm();

        } else {
            JOptionPane.showMessageDialog(this, "Patient ID not found.");
        }
    }

    private void clearForm() 
    {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderBox.setSelectedIndex(0);
        historyArea.setText("");
        patientTable.clearSelection();
    }

    public PatientManager getManager() 
    {
        return manager;
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                new PatientGUI();
            }
        });
    }
}
