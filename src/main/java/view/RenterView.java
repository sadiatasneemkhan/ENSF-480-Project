package view;

import controllers.LoginController;
import controllers.PropertyController;
import models.Property;
import models.PropertyForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.util.stream.Collectors;

public class RenterView {
    private static String BLANK = "";
    final private PropertyController propertyController = PropertyController.getOnlyInstance();
    final private LoginController loginController = LoginController.getOnlyInstance();
    private JFrame baseFrame;

    public RenterView() {
        baseFrame = new JFrame("Renter view");
        baseFrame.setVisible(true);
        baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        baseFrame.setSize(500,500);

        final JPanel renterOptions = new JPanel();

        final JButton searchListingsButtons = new JButton("Search for properties");
        searchListingsButtons.addActionListener(e -> {
            baseFrame.setVisible(false);
            searchPropertyListing();
        });

        renterOptions.add(searchListingsButtons);

        if (loginController.isUserLoggedIn()) {
            final JButton seeNotifications = new JButton("See notifications");
            renterOptions.add(seeNotifications);
        }
        baseFrame.add(renterOptions);
    }

    protected void searchPropertyListing() {
        final JFrame renterViewFrame = new JFrame("Property Search Form");
        renterViewFrame.setVisible(true);
        renterViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renterViewFrame.setSize(500,500);

        final JPanel panel = new JPanel();
        final JLabel infoLabel = new JLabel("Enter your search criteria, leave blank if necessary");
        final JLabel newLine = new JLabel("<html><br><br><p></p></html>");

        final JLabel houseTypeLabel = new JLabel("House Type:");
        final JComboBox<String> houseTypeComboBox = new JComboBox<>(getEnumStringValuesForCheckbox(Property.Type.values()));
        final JLabel numBedroomLabel = new JLabel("Number Of Bedrooms:");
        final JTextField numberOfBedroomsText = new JTextField(20);
        final JLabel numBathroomLabel = new JLabel("Number Of Bathrooms:");
        final JTextField numberOfBathroomsText = new JTextField(20);
        final JLabel cityQuadrantLabel = new JLabel("City quadrant:");
        final JComboBox<String> cityQuadrantComboBox = new JComboBox<>(getEnumStringValuesForCheckbox(Property.CityQuadrant.values()));

        if (loginController.isUserLoggedIn()) {
            final JCheckBox receiveNotificationsForSearch = new JCheckBox("Receive notifications for this search?");
            panel.add(receiveNotificationsForSearch);
        }

        final JButton submitButton = new JButton("Submit");

        panel.add(infoLabel);
        panel.add(newLine);
        panel.add(houseTypeLabel);
        panel.add(houseTypeComboBox);
        panel.add(numBedroomLabel);
        panel.add(numberOfBedroomsText);
        panel.add(numBathroomLabel);
        panel.add(numberOfBathroomsText);
        panel.add(cityQuadrantLabel);
        panel.add(cityQuadrantComboBox);

        panel.add(submitButton);
        renterViewFrame.add(panel);

        submitButton.addActionListener(e -> {
            final PropertyForm propertyForm = new PropertyForm();

            if (!Objects.equals(houseTypeComboBox.getSelectedItem(), BLANK)) {
                propertyForm.setPropertyType(Property.Type.valueOf((String) houseTypeComboBox.getSelectedItem()));
            }

            try {
                propertyForm.setNumberOfBedrooms(Integer.parseInt(numberOfBedroomsText.getText()));
            } catch (final NumberFormatException | NullPointerException ex) {
                System.out.println("DEBUG: numBedroom text was invalid or null");
            }

            try {
                propertyForm.setNumberOfBathrooms(Integer.parseInt(numberOfBathroomsText.getText()));
            } catch (final NumberFormatException | NullPointerException ex) {
                System.out.println("DEBUG: numBathroom text was invalid or null");
            }

            if (!Objects.equals(cityQuadrantComboBox.getSelectedItem(), BLANK)) {
                propertyForm.setCityQuadrant(Property.CityQuadrant.valueOf((String) cityQuadrantComboBox.getSelectedItem()));
            }

            renterViewFrame.dispose();
            propertyListingView(propertyForm);
        });
    }

    protected void propertyListingView(final PropertyForm propertyForm) {
        final JFrame renterViewFrame = new JFrame("All Properties");
        renterViewFrame.setVisible(true);
        renterViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        renterViewFrame.setSize(500,500);


        final Collection<Property> properties = propertyController.getProperties(propertyForm);
        final JPanel panel = new JPanel();

        final DefaultTableModel tableModel = Property.getTable(properties);

        final JTable propertyTable = new JTable(tableModel);

        final JScrollPane js = new JScrollPane(propertyTable);

        final JButton goBackButton = new JButton("Go back");

        goBackButton.addActionListener(e -> {
            renterViewFrame.dispose();
            baseFrame.setVisible(true);
        });

        panel.add(propertyTable);
        panel.add(js);
        panel.add(goBackButton);

        renterViewFrame.add(panel);
    }


    private static String[] getEnumStringValuesForCheckbox(final Enum<?>[] enumValues) {
        final List<String> stringValues = new ArrayList<>();
        stringValues.add(BLANK);
        stringValues.addAll(Arrays.stream(enumValues).map(Enum::toString).collect(Collectors.toList()));
        return stringValues.toArray(new String[0]);
    }
}
