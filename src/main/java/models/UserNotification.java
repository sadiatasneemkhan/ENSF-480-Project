package models;

public class UserNotification extends DatabaseModel {
    private static final String tableName = "user_notification";

    private Integer userId;
    private Property propertyId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(final Integer userId) {
        this.userId = userId;
    }

    public Property getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(final Property propertyId) {
        this.propertyId = propertyId;
    }
}
