package view;
import controllers.PropertyController;
import controllers.UserController;
import models.Landlord;
import models.Property;
import models.Renter;
import models.User;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.DefaultTableModel;

class ManagerView {
	final private UserController userController = UserController.getOnlyInstance();
	final private PropertyController propertyController = PropertyController.getOnlyInstance();

	JFrame managerViewFrame = null;

	public ManagerView(){
		main();
	}

	public void main(){
		managerViewFrame = new JFrame("Online Application");
		managerViewFrame.setVisible(true);
		managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerViewFrame.setSize(500,500);

		JPanel panel = new JPanel();
		JButton createSummaryButton = new JButton("Create Summary");
		JButton getRenters = new JButton("Get Renters");
		JButton getLandlords = new JButton("Get Landlords");
		JButton getProperties = new JButton("Get Properties");
		JButton changeListingStatus = new JButton("Change Listing Status");
		JButton changeFeeAmount = new JButton("Change Fee Amount");
		JButton changeFeePeriod = new JButton("Change Fee Period");

		panel.add(createSummaryButton);
		panel.add(getRenters);
		panel.add(getLandlords);
		panel.add(getProperties);
		panel.add(changeListingStatus);
		panel.add(changeFeeAmount);
		panel.add(changeFeePeriod);

		managerViewFrame.add(panel);

		createSummaryButton.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					createSummary();
				}
			}
		);

		getRenters.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					getRenters();
				}
			}
		);

		getLandlords.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					getLandlords();
				}
			}
		);

		getProperties.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					getProperties();
				}
			}
		);

		changeListingStatus.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					changeListingStatus();
				}
			}
		);

		changeFeeAmount.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					changeFeeAmount();
				}
			}
		);

		changeFeePeriod.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					changeFeePeriod();
				}
			}
		);

	}

	public void createSummary(){

	}

	public void getRenters(){
		managerViewFrame = new JFrame("All Renters");
		managerViewFrame.setVisible(true);
		managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerViewFrame.setSize(500,500);

        final Collection<Renter> users = (Collection<Renter>) userController.getUsersOfRole(User.UserRole.RENTER);
        JPanel p1 = new JPanel();

		// TODO: POPULATE THIS TABLE WITH RENTER INFO
        // DefaultTableModel tableModel = new DefaultTableModel(head, 0);
		// JTable table = new JTable(tableModel);


		// p1.add(table);
		// p1.add(js);

		JButton j3 = new JButton("Go Back");
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));

		p1.add(j3);

		managerViewFrame.add(p1);

		j3.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					main();
				}
			}
		);
	}

	public void getLandlords(){
		managerViewFrame = new JFrame("All Landlords");
		managerViewFrame.setVisible(true);
		managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerViewFrame.setSize(500,500);

        Collection<Landlord> landlords = (Collection<Landlord>) userController.getUsersOfRole(User.UserRole.LANDLORD);
		JPanel p1 = new JPanel();

		// TODO: populate grid with landlords

		JButton j3 = new JButton("Go Back");
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));

		p1.add(j3);

		managerViewFrame.add(p1);

		j3.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					main();
				}
			}
		);

	}

	public void getProperties(){
		managerViewFrame = new JFrame("All Landlords");
		managerViewFrame.setVisible(true);
		managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerViewFrame.setSize(500,500);


		Collection<Property> array = propertyController.getProperties();
		JPanel p1 = new JPanel();

		Object[] head = new String[]{"House ID", "Address", "House Type", "Number Of Bedrooms", "Number Of Bathrooms", "Furnish Status", "City Quadrant" , "Landlord Name", "Landlord Email",
			"Fee Status", "Property Status", "Payment Date"};

		// TODO: populate properties table

		JButton j3 = new JButton("Go Back");
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));

		p1.add(j3);

		managerViewFrame.add(p1);

		j3.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					main();
				}
			}
		);
	}

	public void changeListingStatus(){
		managerViewFrame = new JFrame("Change Listing Status");
		managerViewFrame.setVisible(true);
		managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerViewFrame.setSize(500,500);

		Collection<Property> properties = propertyController.getProperties();
		JPanel p1 = new JPanel();

		JLabel j1 = new JLabel("HouseID");
		JTextField houseIDText = new JTextField(20);

		// TODO: make this selectable from the Property.status enum, https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
		JLabel j2 = new JLabel("New Listing State");
		JTextField stateText = new JTextField(20);

		p1.add(j1);
		p1.add(houseIDText);
		p1.add(j2);
		p1.add(stateText);

		JButton j3 = new JButton("Submit");
		JButton j4 = new JButton("Go Back");

		p1.add(j3);
		p1.add(j4);

		managerViewFrame.add(p1);

		j3.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					final int houseID = Integer.parseInt(houseIDText.getText());

					// convert this to enum value
					String state = stateText.getText();

					// TODO: set up the status it's being updated to
					propertyController.changePropertyState(houseID, Property.Status.RENTED);

					managerViewFrame.dispose();
					main();
				}
			}
		);

		j4.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					main();
				}
			}
		);
	}

	public void changeFeeAmount(){
		managerViewFrame = new JFrame("Fee Amount");
		managerViewFrame.setVisible(true);
		managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerViewFrame.setSize(500,500);

		JPanel p1 = new JPanel();
		JLabel j1 = new JLabel("New Fee Amount:");
		JTextField feeText = new JTextField(20);

		JButton j2 = new JButton("Submit");
		JButton j3 = new JButton("Go Back");

		p1.add(j1);
		p1.add(feeText);
		p1.add(j2);
		p1.add(j3);

		managerViewFrame.add(p1);

		j2.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					try{
						double fee = Double.parseDouble(feeText.getText());
					} catch (NumberFormatException f){
						f.printStackTrace();
					}

					managerViewFrame.dispose();
					main();
				}
			}
		);

		j3.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					main();
				}
			}
		);

	}

	public void changeFeePeriod(){
		managerViewFrame = new JFrame("Fee Period");
		managerViewFrame.setVisible(true);
		managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerViewFrame.setSize(500,500);

		JPanel p1 = new JPanel();
		JLabel j1 = new JLabel("New Fee Period:");
		JTextField periodText = new JTextField(20);

		JButton j2 = new JButton("Submit");
		JButton j3 = new JButton("Go Back");

		p1.add(j1);
		p1.add(periodText);
		p1.add(j2);
		p1.add(j3);

		managerViewFrame.add(p1);

		j2.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					try{
						int period = Integer.parseInt(periodText.getText());
					} catch (NumberFormatException f){
						f.printStackTrace();
					}

					managerViewFrame.dispose();
					main();
				}
			}
		);

		j3.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					main();
				}
			}
		);
	}
}