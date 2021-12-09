package view;
import controllers.LoginController;
import controllers.PropertyController;
import models.Landlord;
import models.Property;

import javax.swing.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.table.*;

public class LandlordView {
	final private PropertyController propertyController = PropertyController.getOnlyInstance();
	final private LoginController loginController = LoginController.getOnlyInstance();
	JFrame baseFrame = null;
	Landlord landlord = null;

	public LandlordView() {
		main();
	}

	public void main() {
		landlord = null;
		landlord = (Landlord) loginController.getCurrentUser()
				.filter(user -> user instanceof Landlord)
				.orElseThrow(() -> new IllegalStateException("Current user must be landlord."));
		
		propertyController.updateDatabase();

		baseFrame = new JFrame("Online Application");
		baseFrame.setVisible(true);
		baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        baseFrame.setSize(500,500);

		final JPanel panel = new JPanel();

		final JButton registerPropertyButton = new JButton("Register a Property");
		final JButton payFeesButton = new JButton("Pay Fees");
		final JButton changeListingStatusButton = new JButton("Change Listing Status");

		panel.add(registerPropertyButton);
		panel.add(payFeesButton);
		panel.add(changeListingStatusButton);

		baseFrame.add(panel);

		registerPropertyButton.addActionListener(e -> {
			baseFrame.dispose();
			registerProperty();
		});

		payFeesButton.addActionListener(e -> {
			baseFrame.dispose();
			payFee(baseFrame);
		});

		changeListingStatusButton.addActionListener(e -> {
			baseFrame.dispose();
			changeListingStatus();
		});
	}

	public void registerProperty(){
		baseFrame = new JFrame("Register Property");
		baseFrame.setVisible(true);
		baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        baseFrame.setSize(500,500);

		final JPanel p1 = new JPanel();
		final JLabel j1 = new JLabel("Address:");
		final JLabel j2 = new JLabel("House Type:");
		final JLabel j3 = new JLabel("Number Of Bedrooms:");
		final JLabel j4 = new JLabel("Number Of Bathrooms:");
		final JLabel j5 = new JLabel("Furnished:");
		final JLabel j6 = new JLabel("City Quadrant:");
		final JTextField addressText = new JTextField(20);
		final JTextField numberOfBedroomsText = new JTextField(20);
		final JTextField numberOfBathroomsText = new JTextField(20);
		final JComboBox<Property.Type> houseTypeComboBox = new JComboBox<>(Property.Type.values());
		final JComboBox<Property.CityQuadrant> cityQuadrantComboBox = new JComboBox<>(Property.CityQuadrant.values());
		final JCheckBox isFurnishedCheckbox = new JCheckBox();

		JButton j7 = new JButton("Submit");
		JButton j8 = new JButton("Go Back");

		p1.add(j1);
		p1.add(addressText);
		p1.add(j2);
		p1.add(houseTypeComboBox);
		p1.add(j3);
		p1.add(numberOfBedroomsText);
		p1.add(j4);
		p1.add(numberOfBathroomsText);
		p1.add(j5);
		p1.add(isFurnishedCheckbox);
		p1.add(j6);
		p1.add(cityQuadrantComboBox);
		p1.add(j7);
		p1.add(j8);

		baseFrame.add(p1);

		j7.addActionListener(e -> {
			final String address = addressText.getText();
			final Property.Type houseType = (Property.Type) houseTypeComboBox.getSelectedItem();
			final String numberOfBedrooms = numberOfBedroomsText.getText();
			final String numberOfBathrooms = numberOfBathroomsText.getText();
			final boolean isFurnished = isFurnishedCheckbox.isSelected();
			final Property.CityQuadrant cityQuadrant = (Property.CityQuadrant) cityQuadrantComboBox.getSelectedItem();

			try{
				final int bedrooms = Integer.parseInt(numberOfBedrooms);
				final int bathrooms = Integer.parseInt(numberOfBathrooms);

				propertyController.uploadProperty(new Property(address, houseType, bedrooms,
						bathrooms, isFurnished, cityQuadrant,
						loginController.getCurrentUser().get().getEmail(), false,
						Property.Status.UNPUBLISHED, null, null));

			} catch (NumberFormatException f){
				f.printStackTrace();
			}

			baseFrame.dispose();
			main();
		});

		j8.addActionListener(e -> {
			baseFrame.dispose();
			main();
		});
	}

	public void payFee(JFrame oldFrame){
		baseFrame = new JFrame("Pay Fee");
		baseFrame.setVisible(true);
		baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        baseFrame.setSize(900,500);

		final Collection<Property> properties = propertyController.getPaymentProperties(landlord);
		final double paymentFee = propertyController.getPropertyPaymentFee();
		final JPanel p1 = new JPanel();
		
		final DefaultTableModel tableModel = Property.getTable(properties);

		final JLabel requiredFeeLabel = new JLabel("Required fee is $" + paymentFee);
		p1.add(requiredFeeLabel);

		final JTable table = new JTable(tableModel);
		
		final JScrollPane js = new JScrollPane(table);
		
		p1.add(table);
		p1.add(js);
		
		final JLabel j1 = new JLabel("HouseID:");
		final JComboBox<Integer> propertyIdComboBox = new JComboBox<>();

		for (final Property property : properties) {
			propertyIdComboBox.addItem(property.getID());
		}

		final JCheckBox payingFeeCheck = new JCheckBox(String.format("Paying $%.2f in fees", paymentFee));

		p1.add(j1);
		p1.add(propertyIdComboBox);
		p1.add(payingFeeCheck);

		final JButton submit = new JButton("Submit");
		final JButton goBack = new JButton("Go Back");

		p1.add(submit);
		p1.add(goBack);

		baseFrame.add(p1);

		submit.addActionListener(e -> {
			final Integer propertyId = (Integer) propertyIdComboBox.getSelectedItem();
			if (!Objects.isNull(propertyId) && payingFeeCheck.isSelected()) {
				final Property propertyToPublish = properties.stream().filter(p -> p.getID() == propertyId).findFirst().orElse(null);
				if (!Objects.isNull(propertyToPublish)) {
					propertyController.publishProperty(propertyToPublish);
				}
			}
			baseFrame.dispose();
			oldFrame.setVisible(true);
		});

		goBack.addActionListener((e) -> {
			baseFrame.dispose();
			main();
		});

	}

	public void changeListingStatus(){
		baseFrame = new JFrame("Change Listing State");
		baseFrame.setVisible(true);
		baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        baseFrame.setSize(500,500);

		Collection<Property> properties = propertyController.getAllProperties(landlord);
		JPanel p1 = new JPanel();
		
		DefaultTableModel tableModel = Property.getTable(properties);

		JTable table = new JTable(tableModel);
		
		JScrollPane js = new JScrollPane(table);
		
		p1.add(table);
		p1.add(js);
		
		JLabel j1 = new JLabel("HouseID:");
		JTextField houseIDText = new JTextField(20);
		JLabel j2 = new JLabel("New Listing State:");
		JTextField listingText = new JTextField(20);

		p1.add(j1);
		p1.add(houseIDText);
		p1.add(j2);
		p1.add(listingText);

		JButton submit = new JButton("Submit");
		
		JButton goBack = new JButton("Go Back");

		p1.add(submit);
		p1.add(goBack);

		baseFrame.add(p1);
		
		submit.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					Property.Status listing = Property.Status.ACTIVE;
					
					if(listingText.getText().toUpperCase().equals(Property.Status.CANCELLED.toString())){
						listing = Property.Status.CANCELLED;
					}
					
					else if(listingText.getText().toUpperCase().equals(Property.Status.SUSPENDED.toString())){
						listing = Property.Status.SUSPENDED;
					}
					
					else if(listingText.getText().toUpperCase().equals(Property.Status.RENTED.toString())){
						listing = Property.Status.RENTED;
					}
					
					else{
						changeListingStatus();
					}
					
					try{
						int houseID = Integer.parseInt(houseIDText.getText());
						
						
						propertyController.changePropertyState(houseID, listing, landlord);
						
					} catch(NumberFormatException f){
						f.printStackTrace();
					}
					
					baseFrame.dispose();
					main();
				}
			}
		);

		goBack.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					baseFrame.dispose();
					main();
				}
			}
		);
	}



}