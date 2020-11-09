package homeScreen.Model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Customer {

    private int customerID;
    private String customerName;
    private Address address;
    private Boolean active;
    private LocalDateTime createdDate;
    private String createdBy;
    private Timestamp lastUpdated;
    private String lastUpdateBy;

    public Customer(String customerName, Address address, Boolean active,
                    LocalDateTime createdDate, String createdBy, Timestamp lastUpdated, String lastUpdateBy) {

        this.customerName = customerName;
        this.address = address;
        this.active = active;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdateBy = lastUpdateBy;
    }


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCustomerID() {
        return customerID;
    }
}
