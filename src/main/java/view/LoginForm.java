package view;
import javax.swing.*;

import controllers.LoginController;
import models.User;

public class LoginForm {
	JFrame userRoleFrame = new JFrame("User Role: ");
	User.UserRole roleToBeRegistered = null;
	private final LoginController loginController = LoginController.getOnlyInstance();

	public LoginForm() {
		userRoleFrame.setVisible(true);
		userRoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userRoleFrame.setSize(500,500);

		final JPanel p1 = new JPanel();

		for (User.UserRole role : User.UserRole.values()) {
			final JButton button = new JButton(role.toString().substring(0,1).toUpperCase() + role.toString().substring(1).toLowerCase());

			button.addActionListener(e -> {
				System.out.println("DEBUG: role to be registered is " + role.toString());
				setRoleToBeRegistered(role);
				userRoleFrame.dispose();
				loginView();
			});

			p1.add(button);
		}

		final JButton continueWithoutLoginButton = new JButton("Continue without loggin in (unregistered renter)");
		continueWithoutLoginButton.addActionListener(e -> {
			System.out.println("DEBUG: continuing without logging in");
			userRoleFrame.dispose();
			new PropertySearchFormView();
		});

		p1.add(continueWithoutLoginButton);

		userRoleFrame.add(p1);
	}
	
	public void setRoleToBeRegistered(final User.UserRole roleToBeRegistered) {
		this.roleToBeRegistered = roleToBeRegistered;
	}
	
	public void loginView(){
		userRoleFrame = new JFrame("Login");
		userRoleFrame.setVisible(true);
		userRoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userRoleFrame.setSize(500,500);

        final JButton goBack = new JButton("Go back");

		final JPanel basePanel = new JPanel();
		final JLabel emailLabel = new JLabel("Email:");
		final JLabel passwordLabel = new JLabel("Password:");
		final JTextField emailText = new JTextField(20);
		final JTextField passwordText = new JTextField(20);
		
		final JButton submitButton = new JButton("Submit");
		final JButton createNewAccountButton = new JButton("Create New Account");

		basePanel.add(goBack);
		basePanel.add(emailLabel);
		basePanel.add(emailText);
		basePanel.add(passwordLabel);
		basePanel.add(passwordText);
		basePanel.add(submitButton);
		basePanel.add(createNewAccountButton);
		
		userRoleFrame.add(basePanel);

		submitButton.addActionListener(e -> {
			final String email = emailText.getText();
			final String password = passwordText.getText();

			loginController.login(email, password);
			userRoleFrame.dispose();
			nextStep();
		});

		createNewAccountButton.addActionListener(e -> {
			final String email = emailText.getText();
			final String password = passwordText.getText();

			loginController.register(email, password, roleToBeRegistered);
			userRoleFrame.dispose();
			nextStep();
		});
		
		goBack.addActionListener(e -> {
			userRoleFrame.dispose();
			new LoginForm();
		});
	}

	private void nextStep() {
		loginController.getCurrentUser().ifPresent(user -> {
			switch (user.getRole()) {
				case RENTER:
					new PropertySearchFormView();
					break;
				case LANDLORD:
					new LandlordView();
					break;
				case MANAGER:
					new ManagerView();
					break;
			}
		});
	}
}

