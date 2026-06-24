import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGUI extends JFrame 
{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;

    private UserManager userManager;

    public LoginGUI() 
    {
        userManager = new UserManager();

        setTitle("Hospital Management System - Login");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeComponents();

        setVisible(true);
    }

    private void initializeComponents() 
    {
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Hospital Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                performLogin();
            }
        });

        cancelButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                System.exit(0);
            }
        });

        passwordField.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                performLogin();
            }
        });
    }

    private void performLogin() 
    {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) 
        {
            JOptionPane.showMessageDialog(
                this,
                "Please enter both username and password.",
                "Login Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        User user = userManager.authenticate(username, password);

        if (user != null) 
        {
            JOptionPane.showMessageDialog(
                this,
                "Welcome, " + user.getUsername() + "!\n" + user.getDashboardTitle(),
                "Login Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
            new MainMenu(user);
        } 
        else 
        {
            JOptionPane.showMessageDialog(
                this,
                "Invalid username or password.\nPlease try again.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
            );

            usernameField.setText("");
            passwordField.setText("");
            usernameField.requestFocus();
        }
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
