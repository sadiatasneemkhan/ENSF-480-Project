package view;

import javax.swing.*;

public class EmailView {

    public EmailView(JFrame baseFrame) {
        
        final JFrame emailViewFrame = new JFrame("Contact Landlord");
        emailViewFrame.setVisible(true);
        emailViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        emailViewFrame.setSize(500,500);

        final JPanel panel = new JPanel();
        final JLabel infoLabel = new JLabel("Interested in a property or have any further questions?");
        final JLabel newLine = new JLabel("<html><br><br><p></p></html>");

        final JTextField id = new JTextField(20);
        final JLabel idLabel = new JLabel("Please enter the House ID of the property you are interested in:");

        final JTextField message = new JTextField(20);
        final JLabel messageLabel = new JLabel("Please enter your message:");

        final JButton submitButton = new JButton("Submit");

        panel.add(infoLabel);
        panel.add(newLine);
        panel.add(idLabel);
        panel.add(id);
        panel.add(newLine);
        panel.add(messageLabel);
        panel.add(message);
        panel.add(submitButton);

        submitButton.addActionListener(z -> {

            System.out.println("DEBUG: email submittion button");
			emailViewFrame.dispose();
            new ConfirmationView(emailViewFrame, baseFrame, id, message);
        });

        final JButton goBackButton = new JButton("Go Back");
        panel.add(goBackButton);

        goBackButton.addActionListener(e -> {
            emailViewFrame.dispose();
            baseFrame.setVisible(true);
        });

        emailViewFrame.add(panel);
    }


    
}
