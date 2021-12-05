package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

public class Property extends DatabaseModel {
    private static final String tableName = "property";
    private String address;
    private Type propertyType;
    private int numberOfBedrooms;
    private int numberOfBathrooms;
    private boolean isFurnished;
    private CityQuadrant cityQuadrant;
    private Landlord landlord;
    private int landlordId;
    private boolean isFeePaid;
    private Status propertyStatus;
    private LocalDateTime datePublished;
    private LocalDateTime paymentDate;

    public Property(final String address, final Type propertyType, final int numberOfBedrooms,
                    final int numberOfBathrooms, final boolean isFurnished,
                    final CityQuadrant cityQuadrant, final int landlordId,
                    final boolean isFeePaid, final Status propertyStatus,
                    final LocalDateTime datePublished, final LocalDateTime paymentDate) {
        this.address = address;
        this.propertyType = propertyType;
        this.numberOfBedrooms = numberOfBedrooms;
        this.numberOfBathrooms = numberOfBathrooms;
        this.isFurnished = isFurnished;
        this.cityQuadrant = cityQuadrant;
        this.landlordId = landlordId;
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
        final int landlordId = rs.getInt("landlord");
        final Status status = Status.valueOf(rs.getString("property_status"));
        final LocalDateTime datePublished = rs.getObject("date_published", LocalDateTime.class);
        final LocalDateTime paymentDate = rs.getObject("payment_date", LocalDateTime.class);

        final Property property = new Property(address, propertyType, numberOfBedrooms, numberOfBathrooms, isFurnished, quadrant, landlordId, isFeePaid, status, datePublished, paymentDate);
        property.setId(id);
        return Optional.of(property);
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(final LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getLandlordId() {
        return landlordId;
    }

    public void setLandlordId(final int landlordId) {
        this.landlordId = landlordId;
    }

    public boolean isFurnished() {
        return isFurnished;
    }

    public void setFurnished(final boolean furnished) {
        isFurnished = furnished;
    }

    public LocalDateTime getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(final LocalDateTime datePublished) {
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
        SUSPENDED
    }

    public enum Type {
        APARTMENT,
        ATTACHED,
        DETACHED,
        TOWNHOUSE,
    }
}
