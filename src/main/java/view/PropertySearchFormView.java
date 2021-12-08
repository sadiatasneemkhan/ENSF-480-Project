package view;

import controllers.LoginController;
import models.Property;
import models.PropertyForm;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PropertySearchFormView {
    private static String BLANK = "";
    private final LoginController loginController = LoginController.getOnlyInstance();

    public PropertySearchFormView() {
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
            new RenterView(propertyForm);
        });
    }

    private static String[] getEnumStringValuesForCheckbox(final Enum<?>[] enumValues) {
        final List<String> stringValues = new ArrayList<>();
        stringValues.add(BLANK);
        stringValues.addAll(Arrays.stream(enumValues).map(Enum::toString).collect(Collectors.toList()));
        return stringValues.toArray(new String[0]);
    }
}
