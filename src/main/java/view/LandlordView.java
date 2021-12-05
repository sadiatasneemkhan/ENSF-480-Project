package view;
import controllers.LoginController;
import controllers.PropertyController;
import models.Landlord;
import models.Property;

import javax.swing.*;
import java.awt.event.*;
import java.util.Collection;

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
						Property.CityQuadrant.NE, loginController.getCurrentUser().get().getId(),
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

		for (final Property property : properties) {
			p1.add(new JLabel(property.getAddress()));
		}

		// TODO: make this work directly with `Property` objects
		// a good idea would be to make a new class like "PropertyLine" which returns
		// a JFrame with all the property details populated, since there's super similar display in a lot of the other
		// views


//		Object[] head = new String[]{"House ID", "Address", "House Type", "Number Of Bedrooms", "Number Of Bathrooms", "Furnish Status", "City Quadrant" , "Landlord Name", "Landlord Email",
//			"Fee Status", "Property Status", "Payment Date"};

//		DefaultTableModel tableModel = new DefaultTableModel(head, 0);
//
//		JTable table = new JTable(tableModel);
//
//		tableModel.addRow(head);
//
//		for(ArrayList<String> temp: array){
//			String houseID = temp.get(0);
//			String address = temp.get(1);
//			String houseType = temp.get(2);
//			String numberOfBedrooms = temp.get(3);
//			String numberOfBathrooms = temp.get(4);
//			String furnishStatus = temp.get(5);
//			String cityQuadrant = temp.get(6);
//			String landlordName = temp.get(7);
//			String landlordEmail = temp.get(8);
//
//			String feeStatus = "";
//			if(temp.get(9).equals("0")){
//				feeStatus = "False";
//			}
//			else{
//				feeStatus = "True";
//			}
//			String propertyStatus = temp.get(10);
//
//			String paymentDate = "";
//			if(temp.get(11) == null){
//				paymentDate = "NULL";
//			}
//			else{
//				paymentDate = temp.get(11);
//			}
//
//			Object[] data = {houseID, address, houseType, numberOfBedrooms, numberOfBathrooms, furnishStatus, cityQuadrant, landlordName, landlordEmail, feeStatus, propertyStatus, paymentDate};
//			tableModel.addRow(data);
//		}
//
//		JScrollPane js = new JScrollPane(table);
//
//		p1.add(table);
//		p1.add(js);
//
//		JLabel j1 = new JLabel("HouseID");
//		JTextField houseIDText = new JTextField(20);
//		JLabel j2 = new JLabel("Payment Amount");
//		JTextField stateText = new JTextField(20);
//
//		p1.add(j1);
//		p1.add(houseIDText);
//		p1.add(j2);
//		p1.add(stateText);
//
//		JButton j3 = new JButton("Submit");
		JButton j4 = new JButton("Go Back");
//
//		p1.add(j3);
		p1.add(j4);
//
		baseFrame.add(p1);
//
//		j3.addActionListener(
//			new ActionListener() {
//
//				public void actionPerformed(ActionEvent e){
//					String houseID = houseIDText.getText();
//
//					try{
//						double fee = Double.parseDouble(stateText.getText());
//
//						propertyController.payFee(houseID, fee, landlord.getName());
//					} catch(NumberFormatException f){
//						f.printStackTrace();
//					}
//
//					baseFrame.dispose();
//					main();
//				}
//			}
//		);
//
		j4.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					baseFrame.dispose();
					main();
				}
			}
		);

	}

	public void changeListingStatus(){
		// TODO: you can use propertyController.changePropertyState

//		baseFrame = new JFrame("Change Listing Status");
//		baseFrame.setVisible(true);
//		baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        baseFrame.setSize(500,500);
//
//		ArrayList<ArrayList<String>> array = propertyController.getLandlordProperties(landlord.getName());
//		JPanel p1 = new JPanel();
//
//		Object[] head = new String[]{"House ID", "Address", "House Type", "Number Of Bedrooms", "Number Of Bathrooms", "Furnish Status", "City Quadrant" , "Landlord Name", "Landlord Email",
//			"Fee Status", "Property Status", "Payment Date"};
//
//		DefaultTableModel tableModel = new DefaultTableModel(head, 0);
//
//		JTable table = new JTable(tableModel);
//
//		tableModel.addRow(head);
//
//		for(ArrayList<String> temp: array){
//			String houseID = temp.get(0);
//			String address = temp.get(1);
//			String houseType = temp.get(2);
//			String numberOfBedrooms = temp.get(3);
//			String numberOfBathrooms = temp.get(4);
//			String furnishStatus = temp.get(5);
//			String cityQuadrant = temp.get(6);
//			String landlordName = temp.get(7);
//			String landlordEmail = temp.get(8);
//
//			String feeStatus = "";
//			if(temp.get(9).equals("0")){
//				feeStatus = "False";
//			}
//			else{
//				feeStatus = "True";
//			}
//			String propertyStatus = temp.get(10);
//
//			String paymentDate = "";
//			if(temp.get(11) == null){
//				paymentDate = "NULL";
//			}
//			else{
//				paymentDate = temp.get(11);
//			}
//
//			Object[] data = {houseID, address, houseType, numberOfBedrooms, numberOfBathrooms, furnishStatus, cityQuadrant, landlordName, landlordEmail, feeStatus, propertyStatus, paymentDate};
//			tableModel.addRow(data);
//		}
//
//		JScrollPane js = new JScrollPane(table);
//
//		p1.add(table);
//		p1.add(js);
//
//		JLabel j1 = new JLabel("HouseID");
//		JTextField houseIDText = new JTextField(20);
//		JLabel j2 = new JLabel("New Listing State");
//		JTextField stateText = new JTextField(20);
//
//		p1.add(j1);
//		p1.add(houseIDText);
//		p1.add(j2);
//		p1.add(stateText);
//
//		JButton j3 = new JButton("Submit");
//		JButton j4 = new JButton("Go Back");
//
//		p1.add(j3);
//		p1.add(j4);
//
//		baseFrame.add(p1);
//
//		j3.addActionListener(
//			new ActionListener() {
//
//				public void actionPerformed(ActionEvent e){
//					String houseID = houseIDText.getText();
//					String state = stateText.getText();
//
//					propertyController.changeListingState(houseID, state, landlord.getName());
//
//					baseFrame.dispose();
//					main();
//				}
//			}
//		);
//
//		j4.addActionListener(
//			new ActionListener() {
//
//				public void actionPerformed(ActionEvent e){
//					baseFrame.dispose();
//					main();
//				}
//			}
//		);
	}



}