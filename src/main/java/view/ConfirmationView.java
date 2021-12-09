package view;

import javax.swing.*;

public class ConfirmationView {

    public ConfirmationView(JFrame baseFrame, JFrame oldBaseFrame, JTextField id, JTextField message){

        Integer ID = Integer.parseInt(id.getText());
        String msg = message.getText();

        final JFrame confirmationViewFrame = new JFrame("Confirmation");
        confirmationViewFrame.setVisible(true);
        confirmationViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        confirmationViewFrame.setSize(400,130);

        final JPanel panel = new JPanel();
        final JLabel msgSentLabel = new JLabel("Message has been sent successfully to the landlord of House " + ID + "!");
        final JLabel newLine = new JLabel("<html><br><br><p></p></html>");

        panel.add(newLine); 
        panel.add(msgSentLabel); 
        panel.add(newLine); 

        final JButton goBackButton = new JButton("Go Back");
        panel.add(goBackButton);

        goBackButton.addActionListener(e -> {
            confirmationViewFrame.dispose();
            oldBaseFrame.setVisible(true);
        });

        confirmationViewFrame.add(panel);

    }
    
}
