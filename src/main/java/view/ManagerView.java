package view;
import controllers.DatabaseController;
import controllers.UserController;
import models.Landlord;
import models.Property;
import models.Renter;
import models.User;

import javax.swing.*;
import java.awt.event.*;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.table.DefaultTableModel;

class ManagerView {
	final private UserController userController = UserController.getOnlyInstance();
	final private DatabaseController databaseController = DatabaseController.getOnlyInstance();

	JFrame managerViewFrame = null;

	public ManagerView(){
		main();
	}

	public void main(){
		databaseController.updateDatabase();
		managerViewFrame = new JFrame("Manager Options");
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
		managerViewFrame = new JFrame("Summary");
		managerViewFrame.setVisible(true);
		managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerViewFrame.setSize(500,500);
		
		JPanel p1 = new JPanel();
		
		JLabel j1 = new JLabel("Period:");
		JTextField periodText = new JTextField(20);
		
		p1.add(j1);
		p1.add(periodText);

		JButton submit = new JButton("Submit");
		
		JButton goBack = new JButton("Go Back");

		p1.add(submit);
		p1.add(goBack);

		managerViewFrame.add(p1);
		
		submit.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					
					managerViewFrame = new JFrame("Summary Results");
					managerViewFrame.setVisible(true);
					managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					managerViewFrame.setSize(500,500);
					try{
						
						int period = Integer.parseInt(periodText.getText());
						
						JPanel p2 = new JPanel();
						
						JLabel j2 = new JLabel("Number of Houses Listed in the last " + period + " days: " + String.valueOf(databaseController.numberOfHousesListed(period)));
						JLabel j3 = new JLabel("Number of Houses Rented in the last " + period + " days: " + String.valueOf(databaseController.numberOfHousesRented(period)));
						JLabel j4 = new JLabel("Total Number of Active Listings: "+ String.valueOf(databaseController.numberOfActiveListings(period)));
						JLabel j5 = new JLabel("Total Number of Houses Rented in the last " + period + " days: ");
						
						DefaultTableModel tableModel = databaseController.getHousesRentedPeriod(period);
						JTable table = new JTable(tableModel);
						JScrollPane js = new JScrollPane(table);
						
						j2.setAlignmentX(Component.CENTER_ALIGNMENT);
						j3.setAlignmentX(Component.CENTER_ALIGNMENT);
						j4.setAlignmentX(Component.CENTER_ALIGNMENT);
						j5.setAlignmentX(Component.CENTER_ALIGNMENT);
						
						p2.add(j2);
						p2.add(j3);
						p2.add(j4);
						p2.add(j5);
						p2.add(table);
						p2.add(js);
						
						JButton goBack2 = new JButton("Go Back");
						goBack2.setAlignmentX(Component.CENTER_ALIGNMENT);
						
						p2.add(goBack);
						
						p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
						managerViewFrame.add(p2);
						
						goBack2.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e){
									managerViewFrame.dispose();
									main();
								}
							}
						);
						
					} catch(NumberFormatException f){
						f.printStackTrace();
					}
					

				}
			}
		);

		goBack.addActionListener(
			new ActionListener() {

				public void actionPerformed(ActionEvent e){
					managerViewFrame.dispose();
					main();
				}
			}
		);
		
	}

	public void getRenters(){
		managerViewFrame = new JFrame("All Renters");
		managerViewFrame.setVisible(true);
		managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerViewFrame.setSize(500,500);

        JPanel p1 = new JPanel();

        DefaultTableModel tableModel = databaseController.getUsersOfRole(User.UserRole.RENTER);
		JTable table = new JTable(tableModel);
		JScrollPane js = new JScrollPane(table);

		p1.add(table);
		p1.add(js);

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

        JPanel p1 = new JPanel();

        DefaultTableModel tableModel = databaseController.getUsersOfRole(User.UserRole.LANDLORD);
		JTable table = new JTable(tableModel);
		JScrollPane js = new JScrollPane(table);

		p1.add(table);
		p1.add(js);
		
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
		managerViewFrame = new JFrame("All Properties");
		managerViewFrame.setVisible(true);
		managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerViewFrame.setSize(500,500);


		Collection<Property> properties = databaseController.getAllProperties();
		JPanel p1 = new JPanel();
		
		DefaultTableModel tableModel = Property.getTable(properties);
		
		JTable table = new JTable(tableModel);
		
		JScrollPane js = new JScrollPane(table);
		
		p1.add(table);
		p1.add(js);

		JButton j3 = new JButton("Go Back");

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
		managerViewFrame = new JFrame("Change Listing State");
		managerViewFrame.setVisible(true);
		managerViewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerViewFrame.setSize(500,500);

		Collection<Property> properties = databaseController.getAllProperties();
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
		
		managerViewFrame.add(p1);
		
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
						
						
						databaseController.changePropertyState(houseID, listing);
						
					} catch(NumberFormatException f){
						f.printStackTrace();
					}
					
					managerViewFrame.dispose();
					main();
				}
			}
		);

		goBack.addActionListener(
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
						databaseController.setFeeAmount(fee);
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
						databaseController.setFeePeriod(period);
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