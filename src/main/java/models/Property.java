package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Collection;
import javax.swing.table.*;
import java.sql.Timestamp;

public class Property extends DatabaseModel {
	private int ID;
    private String address;
    private Type propertyType;
    private int numberOfBedrooms;
    private int numberOfBathrooms;
    private boolean isFurnished;
    private CityQuadrant cityQuadrant;
    private Landlord landlord;
    private String landlordEmail;
    private boolean isFeePaid;
    private Status propertyStatus;
    private Timestamp datePublished;
    private Timestamp paymentDate;

    public Property(final String address, final Type propertyType, final int numberOfBedrooms,
                    final int numberOfBathrooms, final boolean isFurnished,
                    final CityQuadrant cityQuadrant, final String landlordEmail,
                    final boolean isFeePaid, final Status propertyStatus,
                    final Timestamp datePublished, final Timestamp paymentDate) {
        this.address = address;
        this.propertyType = propertyType;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.isFurnished = isFurnished;
        this.cityQuadrant = cityQuadrant;
        this.landlordEmail = landlordEmail;
        this.isFeePaid = isFeePaid;
        this.propertyStatus = propertyStatus;
        this.datePublished = datePublished;
        this.paymentDate = paymentDate;
    }

    public static Optional<Property> createFromResultSet(final ResultSet rs) throws SQLException {
        final Integer id = rs.getInt("id");
        final String address = rs.getString("address");
        final Type propertyType = Type.valueOf(rs.getString("property_type"));
        final int numberOfBedrooms = rs.getInt("number_of_bedrooms");
        final int numberOfBathrooms = rs.getInt("number_of_bathrooms");
        final boolean isFurnished = rs.getBoolean("is_furnished");
        final CityQuadrant quadrant = CityQuadrant.valueOf(rs.getString("city_quadrant"));
        final boolean isFeePaid = rs.getBoolean("is_fee_paid");
        final String landlordEmail = rs.getString("landlord");
        final Status status = Status.valueOf(rs.getString("property_status"));
        final Timestamp datePublished = rs.getTimestamp("date_published");
        final Timestamp paymentDate = rs.getTimestamp("payment_date");

        final Property property = new Property(address, propertyType, numberOfBedrooms, numberOfBathrooms, isFurnished, quadrant, landlordEmail, isFeePaid, status, datePublished, paymentDate);
		property.setID(id);
        return Optional.of(property);
    }

    public Timestamp getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(final Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getLandlordEmail() {
        return landlordEmail;
    }

    public void setLandlordEnail(final String landlordEmail) {
        this.landlordEmail = landlordEmail;
    }
	
	public int getID() {
        return ID;
    }

    public void setID(final int ID) {
        this.ID = ID;
    }

    public boolean isFurnished() {
        return isFurnished;
    }

    public void setFurnished(final boolean furnished) {
        isFurnished = furnished;
    }

    public Timestamp getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(final Timestamp datePublished) {
        this.datePublished = datePublished;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Type getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(final Type propertyType) {
        this.propertyType = propertyType;
    }

    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(final int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public int getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public void setNumberOfBathrooms(final int numberOfBathrooms) {
        this.numberOfBathrooms = numberOfBathrooms;
    }

    public boolean getIsFurnished() {
        return isFurnished;
    }

    public void setIsFurnished(final boolean isFurnished) {
        this.isFurnished = isFurnished;
    }

    public CityQuadrant getCityQuadrant() {
        return cityQuadrant;
    }

    public void setCityQuadrant(final CityQuadrant cityQuadrant) {
        this.cityQuadrant = cityQuadrant;
    }

    public Landlord getLandlord() {
        return landlord;
    }

    public void setLandlord(final Landlord landlord) {
        this.landlord = landlord;
    }

    public boolean isFeePaid() {
        return isFeePaid;
    }

    public void setFeePaid(final boolean feePaid) {
        this.isFeePaid = feePaid;
    }

    public Status getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(final Status propertyStatus) {
        this.propertyStatus = propertyStatus;
    }
	public static DefaultTableModel getTable(Collection<Property> properties){
		
		
		Object[] head = new String[]{"House ID", "Address", "House Type", "Bedrooms", "Bathrooms", "Is Furnished", "City Quadrant", 
			"Fee Status", "Property Status", "Date Published", "Payment Date"};
		
		DefaultTableModel tableModel = new DefaultTableModel(head, 0);
		tableModel.addRow(head);
		
		for (final Property property : properties) {
			String furnish = "";
			String feeStatus = "";
			String payDate = "";
			String pubDate = "";
			
			if(property.getIsFurnished() == true){
				furnish = "True";
			}
			else{
				furnish = "False";
			}
			
			if(property.isFeePaid() == true){
				feeStatus = "True";
			}
			else{
				feeStatus = "False";
			}
			
			if(property.getDatePublished() == null){
				payDate = "Null";
			}
			else{
				payDate = property.getDatePublished().toString();
			}
			
			if(property.getPaymentDate() == null){
				pubDate = "Null";
			}
			else{
				pubDate = property.getPaymentDate().toString();
			}
			
			Object[] data = {Integer.toString(property.getID()), property.getAddress(), property.getPropertyType().toString(), Integer.toString(property.getNumberOfBedrooms()), 
				Integer.toString(property.getNumberOfBathrooms()), furnish, property.getCityQuadrant().toString(), feeStatus, property.getPropertyStatus().toString(), 
					payDate, pubDate};
					
			tableModel.addRow(data);
		}
		
		return tableModel;
	}

    public enum CityQuadrant {
        NW,
        NE,
        SW,
        SE
    }

    public enum Status {
        UNPUBLISHED,
        ACTIVE,
        RENTED,
        CANCELLED,
        SUSPENDED, 
		EXPIRED
    }

    public enum Type {
        APARTMENT,
        ATTACHED,
        DETACHED,
        TOWNHOUSE,
    }
}
