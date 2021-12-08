package controllers;

import models.*;
import util.DatabaseConnection;

import java.sql.*;
import java.util.*;
import java.util.Calendar;

public class PropertyController {
    private final LoginController loginController = LoginController.getOnlyInstance();
    private final Connection connection = DatabaseConnection.getConnection();
    private static final PropertyController propertyController = new PropertyController();
	
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
			
						mystmt.setString(1, Property.Status.EXPIRED.toString());
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

    public Property uploadProperty(final Property property) {
        final User user = loginController.getCurrentUser().filter(u -> u.getRole().equals(User.UserRole.LANDLORD))
                .orElseThrow(() -> new RuntimeException("Current user must be a landlord."));

        final String query = "INSERT INTO property(address, property_type, number_of_bedrooms, number_of_bathrooms, is_furnished, city_quadrant, landlord, is_fee_paid, property_status, date_published, payment_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            final PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, property.getAddress());
            ps.setString(2, property.getPropertyType().toString());
            ps.setInt(3, property.getNumberOfBedrooms());
            ps.setInt(4, property.getNumberOfBathrooms());
            ps.setBoolean(5, property.isFurnished());
            ps.setString(6, property.getCityQuadrant().toString());
            ps.setString(7, user.getEmail());
            ps.setBoolean(8, false);
            ps.setString(9, Property.Status.UNPUBLISHED.toString());
            ps.setDate(10, null);
            ps.setDate(11, null);

            ps.executeUpdate();
            final ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                property.setId(rs.getInt(1));
                System.out.println("Property " + property.getId() + " successfully uploaded");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return property;
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
	
	public void payProperty(final int propertyID, double amount, Landlord landlord){
		PreparedStatement mystmt = null;
		ResultSet result = null;
		
		try{
			String query = "SELECT fees FROM manager_configuration";
			
			mystmt = this.connection.prepareStatement(query);
			
			result = mystmt.executeQuery();
			
			while(result.next()){
				if(amount == result.getDouble("fees")){
					continue;
				}
				else{
					return;
				}
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		try{
			String query = "SELECT * FROM property WHERE id = ? AND landlord = ?";
			
			mystmt = this.connection.prepareStatement(query);
			
			mystmt.setInt(1, propertyID);
			mystmt.setString(2, landlord.getEmail());
			
			result = mystmt.executeQuery();
			while(result.next()){
				
				publishProperty(Property.createFromResultSet(result).get());
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		
	}

    public void publishProperty(final Property property) {
        final String query = "UPDATE property SET is_fee_paid = true, property_status = ?, date_published = NOW(), payment_date = NOW() WHERE id = ?";
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, Property.Status.ACTIVE.toString());
            ps.setInt(2, property.getID());

            int row = ps.executeUpdate();
			
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Optional<Property> changePropertyState(final int propertyId, final Property.Status newStatus, Landlord landlord) {
        try {
            if (!checkPropertyExists(propertyId)) {
                throw new IllegalArgumentException("Property does not exist");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return Optional.empty();
        }

        final String query = "UPDATE property SET property_status = ? WHERE id = ? AND landlord = ? AND property_status = ? LIMIT 1";
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, newStatus.toString());
            ps.setInt(2, propertyId);
            ps.setString(3, landlord.getEmail());
            ps.setString(4, Property.Status.ACTIVE.toString());

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

    public Collection<Property> getPaymentProperties(final Landlord landlord) {
        final String query = "SELECT * FROM property WHERE landlord = ? AND property_status != ?";
        final List<Property> results = new ArrayList<>();
        try {
            final PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, landlord.getEmail());
            ps.setString(2, Property.Status.ACTIVE.toString());

            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Property.createFromResultSet(rs).ifPresent(results::add);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return results;
    }


    public Collection<Property> getProperties() {
        final String query = "SELECT * FROM property WHERE property_status = '" + Property.Status.ACTIVE.toString() + "'";
        return internalGetProperties(query);
    }

    public Optional<Property> fetchProperty(final int id) {
        final String query = "SELECT * FROM property WHERE id = ? LIMIT 1";

        try (final PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Property.createFromResultSet(rs);
            } else {
                throw new RuntimeException("Property at id " + id + " could not be fetched");
            }
        } catch (final SQLException | RuntimeException throwables) {
            throwables.printStackTrace();
            return Optional.empty();
        }
    }

    public Collection<Property> getProperties(final PropertyForm propertyForm) {
        final String query = getPropertyFilterQuery(propertyForm);
        return internalGetProperties(query);
    }


    public Landlord getLandlord(final Property property) {
        if (property.getLandlord() == null) {
            try {
                final String query = "SELECT * FROM user WHERE email = ? LIMIT 1";
                final PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, property.getLandlordEmail());

                final ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    final Landlord landlord = (Landlord) User.createFromResultSet(rs).filter(user -> user instanceof Landlord)
                            .orElseThrow(() -> new RuntimeException("Could not find landlord at Email " + property.getLandlordEmail()));
                    property.setLandlord(landlord);
                }
            } catch (final SQLException | RuntimeException throwables) {
                throwables.printStackTrace();
            }
        }
        return property.getLandlord();
    }
	
	public Collection<Property> getAllProperties(Landlord landlord){
		final Collection<Property> properties = new ArrayList<>();
		PreparedStatement mystmt = null;
		ResultSet result = null;
		
		try{
			String query = "SELECT * FROM property WHERE landlord = ?";
			mystmt = connection.prepareStatement(query);
			mystmt.setString(1, landlord.getEmail());
			
			result = mystmt.executeQuery();
			
			while(result.next()){
				Property.createFromResultSet(result).ifPresent(properties::add);
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		
		return properties;
		
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

    private String getPropertyFilterQuery(final PropertyForm propertyForm) {
        final StringBuilder sb = new StringBuilder("Select * FROM " + Property.getTableName() + " WHERE property_status = 'ACTIVE'");
        final Map<String, Object> columnMethodMap = new LinkedHashMap<>();
        columnMethodMap.put("property_type", propertyForm.getPropertyType());
        columnMethodMap.put("city_quadrant", propertyForm.getCityQuadrant());
        columnMethodMap.put("number_of_bathrooms", propertyForm.getNumberOfBathrooms());
        columnMethodMap.put("number_of_bedrooms", propertyForm.getNumberOfBedrooms());


        for (final Map.Entry<String, Object> entry : columnMethodMap.entrySet()) {
            if (entry.getValue() != null) {
                sb.append(" AND " + entry.getKey() + " = " + entry.getValue().toString());
            }
        }

        return sb.toString();
    }

    public static PropertyController getOnlyInstance() {
        return propertyController;
    }
	
}
