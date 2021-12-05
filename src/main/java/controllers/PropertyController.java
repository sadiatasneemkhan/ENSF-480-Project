package controllers;

import models.Landlord;
import models.Property;
import models.PropertyForm;
import models.User;
import util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class PropertyController {
    private final LoginController loginController = LoginController.getOnlyInstance();
    private final Connection connection = DatabaseConnection.getConnection();
    private static final PropertyController propertyController = new PropertyController();

    public Property uploadProperty(final Property property) {
        final User user = loginController.getCurrentUser().filter(u -> u.getRole().equals(User.UserRole.LANDLORD))
                .orElseThrow(() -> new RuntimeException("Current user must be a landlord."));

        final String query = "INSERT INTO property(address, property_type, number_of_bedrooms, number_of_bathrooms, is_furnished, city_quadrant, landlord, is_fee_paid, property_status, date_published) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            final PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, property.getAddress());
            ps.setString(2, property.getPropertyType().toString());
            ps.setInt(3, property.getNumberOfBedrooms());
            ps.setInt(4, property.getNumberOfBathrooms());
            ps.setBoolean(5, property.isFurnished());
            ps.setString(6, property.getCityQuadrant().toString());
            ps.setInt(7, user.getId());
            ps.setBoolean(8, false);
            ps.setString(9, Property.Status.UNPUBLISHED.toString());
            ps.setDate(10, null);

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

    public Property publishProperty(final Property property) {
        final String query = "UPDATE property SET is_fee_paid = true, property_status = ?, date_published = NOW() LIMIT 1";
        try {
            final PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, Property.Status.ACTIVE.toString());

            final ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                final Property updatedProperty = Property.createFromResultSet(rs).get();
                updatedProperty.setLandlord(property.getLandlord());



                return updatedProperty;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public Collection<Property> getProperties() {
        final String query = "SELECT * FROM property WHERE property_status = 'ACTIVE'";
        return internalGetProperties(query);
    }

    public Collection<Property> getProperties(final PropertyForm propertyForm) {
        final String query = getPropertyFilterQuery(propertyForm);
        return internalGetProperties(query);
    }

    public Landlord getLandlord(final Property property) {
        if (property.getLandlord() == null) {
            try {
                final String query = "SELECT * FROM user WHERE id = ? LIMIT 1";
                final PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, property.getLandlordId());

                final ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    final Landlord landlord = (Landlord) User.createFromResultSet(rs).filter(user -> user instanceof Landlord)
                            .orElseThrow(() -> new RuntimeException("Could not find landlord at ID " + property.getLandlordId()));
                    property.setLandlord(landlord);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return property.getLandlord();
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
