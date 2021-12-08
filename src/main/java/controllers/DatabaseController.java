package controllers;

import models.*;
import util.DatabaseConnection;
import java.sql.*;
import java.util.*;
import java.util.Calendar;
import java.util.Collection;
import javax.swing.table.*;

public class DatabaseController {
    private final LoginController loginController = LoginController.getOnlyInstance();
    private final Connection connection = DatabaseConnection.getConnection();
    private static final DatabaseController databaseController = new DatabaseController();
	
	public void updateDatabase(){
		PreparedStatement mystmt = null;
		ResultSet result = null;
		int feePeriod = 0;
		
		try{
			String query = "SELECT * FROM manager_configuration";
			
			mystmt = this.connection.prepareStatement(query);
			
			result = mystmt.executeQuery();
			
			while(result.next()){
				feePeriod = result.getInt("period_days");
			}
			
		} catch(SQLException e){
			System.out.println("Could not execute query.");
		}
		
		try{
			String query = "SELECT * FROM Property WHERE property_status = ? AND is_fee_paid = ?";
			
			mystmt = this.connection.prepareStatement(query);
			
			mystmt.setString(1, Property.Status.ACTIVE.toString());
			mystmt.setBoolean(2, true);
			
			result = mystmt.executeQuery();
			
			while(result.next()){
				Timestamp date = result.getTimestamp("payment_date");
				Calendar cal = Calendar.getInstance();
				int houseID = result.getInt("id");
				
				cal.setTime(date);
				cal.add(Calendar.DATE, feePeriod);
				
				Timestamp currentDate = new Timestamp(System.currentTimeMillis());
				
				if(currentDate.compareTo(new Timestamp(cal.getTime().getTime())) == 0 || currentDate.compareTo(new Timestamp(cal.getTime().getTime())) > 0){
					try{
						String query2 = "UPDATE Property SET property_status = ?, is_fee_paid = ? WHERE id = ?";
						
						mystmt = this.connection.prepareStatement(query2);
			
						mystmt.setString(1, Property.Status.UNPUBLISHED.toString());
						mystmt.setBoolean(2, false);
						mystmt.setInt(3, houseID);
			
						int row = mystmt.executeUpdate();
						
					} catch(SQLException f){
						f.printStackTrace();
					}
				}
			}
			
		} catch(SQLException e){
			System.out.println("Could not execute query.");
		}
	}
	
	public int numberOfHousesListed(int period){
		int number = 0;
		PreparedStatement mystmt = null;
		ResultSet result = null;
		
		try{
			String query = "SELECT * FROM Property WHERE payment_date IS NOT NULL";
			
			mystmt = this.connection.prepareStatement(query);
			
			result = mystmt.executeQuery();
			
			while(result.next()){
				Timestamp currentDate = new Timestamp(System.currentTimeMillis());
				Timestamp date = result.getTimestamp("payment_date");
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);
				
				cal.add(Calendar.DATE, period * (-1));
				
				if(date.after(new Timestamp(cal.getTime().getTime()))){
					number++;
				}
				
			}
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return number;
	}
	
	public int numberOfHousesRented(int period){
		int number = 0;
		PreparedStatement mystmt = null;
		ResultSet result = null;
		
		try{
			String query = "SELECT * FROM Property WHERE property_status = ? AND payment_date IS NOT NULL";
			
			mystmt = this.connection.prepareStatement(query);
			mystmt.setString(1, Property.Status.RENTED.toString());
			
			result = mystmt.executeQuery();
			
			while(result.next()){
				Timestamp currentDate = new Timestamp(System.currentTimeMillis());
				Timestamp date = result.getTimestamp("payment_date");
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);
				
				cal.add(Calendar.DATE, period * (-1));
				
				if(date.after(new Timestamp(cal.getTime().getTime()))){
					number++;
				}
				
			}
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return number;
	}
	
	public int numberOfActiveListings(int period){
		int number = 0;
		PreparedStatement mystmt = null;
		ResultSet result = null;
		
		try{
			String query = "SELECT * FROM Property WHERE property_status = ?";
			
			mystmt = this.connection.prepareStatement(query);
			mystmt.setString(1, Property.Status.ACTIVE.toString());
			
			result = mystmt.executeQuery();
			
			while(result.next()){
				number++;
			}
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return number;
	}
	
	public DefaultTableModel getHousesRentedPeriod(int period){
		PreparedStatement mystmt = null;
		ResultSet result = null;
		ArrayList<ArrayList<String>> rented = new ArrayList<ArrayList<String>>();
		
		try{
			String query = "SELECT * FROM Property WHERE property_status = ? AND payment_date IS NOT NULL";
			
			mystmt = this.connection.prepareStatement(query);
			mystmt.setString(1, Property.Status.RENTED.toString());
			
			result = mystmt.executeQuery();
			
			while(result.next()){
				Timestamp currentDate = new Timestamp(System.currentTimeMillis());
				Timestamp date = result.getTimestamp("payment_date");
				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);
				
				cal.add(Calendar.DATE, period * (-1));
				
				if(date.after(new Timestamp(cal.getTime().getTime()))){
					ArrayList<String> temp = new ArrayList<>();
					temp.add(result.getString("landlord"));
					temp.add(result.getString("id"));
					temp.add(result.getString("address"));
				
					rented.add(temp);
				}
				
			}
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return createRentedTable(rented);
	}
	
	public DefaultTableModel createRentedTable(ArrayList<ArrayList<String>> rented){
		Object[] head = new String[]{"Landlord Email", "House ID", "Address"};
		
		DefaultTableModel tableModel = new DefaultTableModel(head, 0);
		tableModel.addRow(head);
		
		for(ArrayList<String> temp: rented){
			String email = temp.get(0);
			String id = temp.get(1);
			String address = temp.get(2);
			
			Object[] data = new String[]{email, id, address};
			tableModel.addRow(data);
		}
		return tableModel;
	}
	
	public DefaultTableModel getUsersOfRole(User.UserRole role) {
        PreparedStatement mystmt = null;
		ResultSet result = null;
		ArrayList<ArrayList<String>> users = new ArrayList<ArrayList<String>>();
		
		try{
			String query = "SELECT * FROM user WHERE role = ?";
			
			mystmt = this.connection.prepareStatement(query);
			mystmt.setString(1, role.toString());
			
			result = mystmt.executeQuery();
			
			while(result.next()){
				ArrayList<String> temp = new ArrayList<>();
				temp.add(String.valueOf(result.getInt("id")));
				temp.add(result.getString("email"));
				
				users.add(temp);
			}
			
		} catch(SQLException e){
			System.out.println("Could not execute query.");
		}
		
		return createUserTable(users);
    }
	
	public DefaultTableModel createUserTable(ArrayList<ArrayList<String>> users){
		Object[] head = new String[]{"User ID", "Email"};
		
		DefaultTableModel tableModel = new DefaultTableModel(head, 0);
		tableModel.addRow(head);
		
		for(ArrayList<String> temp: users){
			String id = temp.get(0);
			String email = temp.get(1);
			
			Object[] data = new String[]{id, email};
			tableModel.addRow(data);
		}
		return tableModel;
	}

    public Optional<Property> changePropertyState(final int propertyId, final Property.Status newStatus) {
        try {
            if (!checkPropertyExists(propertyId)) {
                throw new IllegalArgumentException("Property does not exist");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return Optional.empty();
        }

        final String query = "UPDATE property SET property_status = ? WHERE id = ? AND property_status = ? LIMIT 1";
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newStatus.toString());
            ps.setInt(2, propertyId);
            ps.setString(3, Property.Status.ACTIVE.toString());

            final int updatedRows = ps.executeUpdate();
            System.out.println(updatedRows + " row(s) changed from changing property state");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return Optional.empty();
        }

        return getProperty(propertyId);
    }

    private boolean checkPropertyExists(final int propertyId) throws SQLException {
        final String query = "SELECT EXISTS(SELECT * FROM property WHERE id = ?)";
        final PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, propertyId);
        final ResultSet rs = ps.executeQuery();
		if(rs.next()){
			return rs.getBoolean(1);
		}
        else{
			return false;
		}
    }
	
	public Optional<Property> getProperty(final int id) {
        final String query = "SELECT * FROM property WHERE id = ? LIMIT 1";
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, id);
			
            final ResultSet rs = ps.executeQuery();
			if(rs.next()){
				return Property.createFromResultSet(rs);
			}
			else{
				return null;
			}
            
        } catch (final SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Collection<Property> getProperties() {
        final String query = "SELECT * FROM property WHERE property_status = '" + Property.Status.ACTIVE.toString() + "'";
        return internalGetProperties(query);
    }
	
	public Collection<Property> getAllProperties(){
		final String query = "SELECT * FROM property";
        return internalGetProperties(query);
	}

    protected Collection<Property> internalGetProperties(final String query) {
        final Collection<Property> properties = new ArrayList<>();
        try (final Statement stm = connection.createStatement()) {
            final ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                Property.createFromResultSet(rs).ifPresent(properties::add);
            }
        } catch (final SQLException throwables) {
            throwables.printStackTrace();
        }
        return properties;
    }

    public static DatabaseController getOnlyInstance() {
        return databaseController;
    }
	
	public void setFeeAmount(double feeAmount){
		double fee = feeAmount;
		int feePeriod = 0;
		PreparedStatement mystmt = null;
		ResultSet result = null;
		
		try{
			String query = "SELECT * FROM manager_configuration";
			
			mystmt = this.connection.prepareStatement(query);
			
			
			result = mystmt.executeQuery();
			
			if(result.next()){
				feePeriod = result.getInt("period_days");
			}
			
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		try{
			String query = "TRUNCATE manager_configuration";
			
			mystmt = this.connection.prepareStatement(query);
			
			int row = mystmt.executeUpdate();
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		try{
			String query = "INSERT INTO manager_configuration(fees, period_days) VALUES (?,?)";
			
			mystmt = this.connection.prepareStatement(query);
			
			mystmt.setDouble(1, fee);
			mystmt.setInt(2, feePeriod);
			
			int row = mystmt.executeUpdate();
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	public void setFeePeriod(int feePeriod){
		int period = feePeriod;
		double feeAmount = 0;
		PreparedStatement mystmt = null;
		ResultSet result = null;
		
		try{
			String query = "SELECT * FROM manager_configuration";
			
			mystmt = this.connection.prepareStatement(query);
			
			
			result = mystmt.executeQuery();
			
			if(result.next()){
				feeAmount = result.getDouble("fees");
			}
			
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		try{
			String query = "TRUNCATE manager_configuration";
			
			mystmt = this.connection.prepareStatement(query);
			
			int row = mystmt.executeUpdate();
			
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		try{
			String query = "INSERT INTO manager_configuration(fees, period_days) VALUES (?,?)";
			
			mystmt = this.connection.prepareStatement(query);
			
			mystmt.setDouble(1, feeAmount);
			mystmt.setInt(2, period);
			
			int row = mystmt.executeUpdate();
			
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
}
