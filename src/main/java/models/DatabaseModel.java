package models;

public abstract class DatabaseModel {
    private static String tableName;
    private Integer id;

    public static String getTableName() {
        return tableName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

}
