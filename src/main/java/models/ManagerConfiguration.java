package models;

public class ManagerConfiguration extends DatabaseModel {
    private static final String tableName = "manager_configuration";
    private Integer periodDays;
    private Double fees;

    public Double getFees() {
        return fees;
    }

    public void setFees(final Double fees) {
        this.fees = fees;
    }

    public Integer getPeriodDays() {
        return periodDays;
    }

    public void setPeriodDays(final Integer periodDays) {
        this.periodDays = periodDays;
    }
}
