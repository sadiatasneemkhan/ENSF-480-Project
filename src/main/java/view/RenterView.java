package view;

import controllers.LoginController;
import controllers.PropertyController;
import models.Property;
import models.PropertyForm;
import models.Renter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.*;
import java.util.stream.Collectors;

public class RenterView {
    private static final String BLANK = "";
    final private LoginController loginController = LoginController.getOnlyInstance();
    private final PropertyController propertyController = PropertyController.getOnlyInstance();

    final private static List<String> columnsToRemove = List.of("Fee Status", "Property Status", "Date Published", "Payment Date");
    private final JFrame baseFrame;

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

            seeNotifications.addActionListener(e -> {
                baseFrame.setVisible(false);
                notificationView();
            });
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
        final JComboBox<String> houseTypeComboBox = new JComboBox<>(enumStringValues(Property.Type.values()));
        final JLabel numBedroomLabel = new JLabel("Number Of Bedrooms:");
        final JTextField numberOfBedroomsText = new JTextField(20);
        final JLabel numBathroomLabel = new JLabel("Number Of Bathrooms:");
        final JTextField numberOfBathroomsText = new JTextField(20);
        final JLabel cityQuadrantLabel = new JLabel("City quadrant:");
        final JComboBox<String> cityQuadrantComboBox = new JComboBox<>(enumStringValues(Property.CityQuadrant.values()));

        final JCheckBox receiveNotificationsForSearch = loginController.isUserLoggedIn()
                ? new JCheckBox("Receive notifications for this search?") : null;

        if (loginController.isUserLoggedIn()) {
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

            System.out.println("houseTypeComboBox = " + houseTypeComboBox.getSelectedItem());
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

            if (loginController.isUserLoggedIn() && receiveNotificationsForSearch != null && receiveNotificationsForSearch.isSelected()) {
                System.out.println("DEBUG: saving property form for user " + loginController.getCurrentUser());
                propertyController.savePropertyForm(propertyForm, (Renter) loginController.getCurrentUser().get());
            }

            renterViewFrame.dispose();
            propertyListingView(propertyForm);
        });
    }

    protected void propertyListingView(final PropertyForm propertyForm) {
        final JFrame propertyViewFrame = new JFrame("All Properties");
        propertyViewFrame.setVisible(true);
        propertyViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        propertyViewFrame.setSize(610,500);

        final Collection<Property> properties = propertyController.getProperties(propertyForm);
        final JPanel panel = new JPanel();

        addFilteredTable(properties, propertyViewFrame, panel);

        final JButton emailButton = new JButton("Email landlord");
        panel.add(emailButton);

        emailButton.addActionListener(e -> {

            System.out.println("DEBUG: email button");
            propertyViewFrame.dispose();
            emailView();
        });

        propertyViewFrame.add(panel);
    }

    protected void emailView() {
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

        final JTextField email = new JTextField(20);
        final JLabel emailLabel = new JLabel("Please enter your email address:");

        final JButton submitButton = new JButton("Submit");

        panel.add(infoLabel);
        panel.add(newLine);
        panel.add(emailLabel);
        panel.add(email);
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
            confirmationView(id, message);
        });

        final JButton goBackButton = new JButton("Go Back");
        panel.add(goBackButton);

        goBackButton.addActionListener(e -> {
            emailViewFrame.dispose();
            baseFrame.setVisible(true);
        });

        emailViewFrame.add(panel);
    }

    protected void confirmationView(final JTextField id, final JTextField message) {
        final Integer ID = Integer.parseInt(id.getText());
        final String msg = message.getText();

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
            baseFrame.setVisible(true);
        });

        confirmationViewFrame.add(panel);
    }

    protected void notificationView() {
        final JFrame notificationFrame = new JFrame("Notifications");
        notificationFrame.setVisible(true);
        notificationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        notificationFrame.setSize(400, 130);

        final Collection<Property> properties = loginController.getNotificationsForRenter((Renter) loginController.getCurrentUser().get());

        final JPanel panel = new JPanel();

        addFilteredTable(properties, notificationFrame, panel);
        notificationFrame.add(panel);
    }

    private void addFilteredTable(final Collection<Property> properties, final JFrame frame, final JPanel panel) {
        final DefaultTableModel tableModel = Property.getTable(properties);

        final JTable propertyTable = new JTable(tableModel);

        for (final String col : columnsToRemove){

            final TableColumn colFee = propertyTable.getColumn(col);
            propertyTable.removeColumn(colFee);
        }

        final JScrollPane js = new JScrollPane(propertyTable);

        final JButton goBackButton = new JButton("Go back");

        goBackButton.addActionListener(e -> {
            frame.dispose();
            baseFrame.setVisible(true);
        });

        panel.add(propertyTable);
        panel.add(js);
        panel.add(goBackButton);
    }

    private static String[] enumStringValues(final Enum<?>[] enumValues) {
        final List<String> stringValues = new ArrayList<>();
        stringValues.add(BLANK);
        stringValues.addAll(Arrays.stream(enumValues).map(Enum::toString).collect(Collectors.toList()));
        return stringValues.toArray(new String[0]);
    }
}
