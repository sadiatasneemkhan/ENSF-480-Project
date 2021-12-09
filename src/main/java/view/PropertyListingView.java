package view;

import controllers.PropertyController;
import models.Property;
import models.PropertyForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.util.stream.Collectors;

public class PropertyListingView {

    final private PropertyController propertyController = PropertyController.getOnlyInstance();

    
    PropertyListingView(final PropertyForm propertyForm,JFrame baseFrame) {

        final JFrame propertyViewFrame = new JFrame("All Properties");
        propertyViewFrame.setVisible(true);
        propertyViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        propertyViewFrame.setSize(910,500);

        final Collection<Property> properties = propertyController.getProperties(propertyForm);
        final JPanel panel = new JPanel();

        final DefaultTableModel tableModel = Property.getTable(properties);

        final JTable propertyTable = new JTable(tableModel);

        final JScrollPane js = new JScrollPane(propertyTable);

        final JButton goBackButton = new JButton("Go back");

        goBackButton.addActionListener(e -> {
            propertyViewFrame.dispose();
            baseFrame.setVisible(true);
        });

        panel.add(propertyTable);
        panel.add(js);
        panel.add(goBackButton);

        final JButton emailButton = new JButton("Email landlord");
        panel.add(emailButton);

        emailButton.addActionListener(e -> {
			
            System.out.println("DEBUG: email button");
			propertyViewFrame.dispose();
            new EmailView(propertyViewFrame);
		});

        propertyViewFrame.add(panel);
    }
}
