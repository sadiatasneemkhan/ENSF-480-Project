package view;
import controllers.LoginController;
import controllers.PropertyController;
import models.Landlord;
import models.Property;

import javax.swing.*;
import java.awt.event.*;
import java.util.Collection;
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
			payFee();
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

		// TODO: convert these to use combo box (https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html)
		// with Property.Type and Property.Quadrant enums
		final JTextField houseTypeText = new JTextField(20);
		final JTextField cityQuadrantText = new JTextField(20);
		// TODO: convert this to a 'isFurnishedCheckBox'
		final JTextField furnishText = new JTextField(20);

		JButton j7 = new JButton("Submit");
		JButton j8 = new JButton("Go Back");

		p1.add(j1);
		p1.add(addressText);
		p1.add(j2);
		p1.add(houseTypeText);
		p1.add(j3);
		p1.add(numberOfBedroomsText);
		p1.add(j4);
		p1.add(numberOfBathroomsText);
		p1.add(j5);
		p1.add(furnishText);
		p1.add(j6);
		p1.add(cityQuadrantText);
		p1.add(j7);
		p1.add(j8);

		baseFrame.add(p1);

		j7.addActionListener(e -> {
			final String address = addressText.getText();
			final String houseType = houseTypeText.getText();
			final String numberOfBedrooms = numberOfBedroomsText.getText();
			final String numberOfBathrooms = numberOfBathroomsText.getText();
			final String furnish = furnishText.getText();
			final String cityQuadrant = cityQuadrantText.getText();

			try{
				final int bedrooms = Integer.parseInt(numberOfBedrooms);
				final int bathrooms = Integer.parseInt(numberOfBathrooms);

				// TODO: use the correct parsed values after setting the inputs above
				propertyController.uploadProperty(new Property(address, Property.Type.APARTMENT,
						bedrooms, bathrooms, furnish == null,
						Property.CityQuadrant.NE, loginController.getCurrentUser().get().getEmail(),
						false, Property.Status.UNPUBLISHED, null, null));

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

	public void payFee(){
		baseFrame = new JFrame("Pay Fee");
		baseFrame.setVisible(true);
		baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        baseFrame.setSize(500,500);

		Collection<Property> properties = propertyController.getPaymentProperties(landlord);
		JPanel p1 = new JPanel();
		
		DefaultTableModel tableModel = Property.getTable(properties);

		JTable table = new JTable(tableModel);
		
		JScrollPane js = new JScrollPane(table);
		
		p1.add(table);
		p1.add(js);
		
		JLabel j1 = new JLabel("HouseID:");
		JTextField houseIDText = new JTextField(20);
		JLabel j2 = new JLabel("Fee Amount:");
		JTextField amountText = new JTextField(20);

		p1.add(j1);
		p1.add(houseIDText);
		p1.add(j2);
		p1.add(amountText);

		JButton submit = new JButton("Submit");
		
		JButton goBack = new JButton("Go Back");

		p1.add(submit);
		p1.add(goBack);

		baseFrame.add(p1);
		
		submit.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					
					try{
						int houseID = Integer.parseInt(houseIDText.getText());
						double amount = Double.parseDouble(amountText.getText());
						
						propertyController.payProperty(houseID, amount, landlord);
						
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