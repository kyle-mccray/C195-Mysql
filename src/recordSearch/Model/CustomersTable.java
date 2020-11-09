package recordSearch.Model;

public class CustomersTable {
    private String customerName;
    private String address;
    private String customerPhone;
    private String customerId;

    public CustomersTable(String customerName, String address, String customerPhone, String customerId) {
        this.customerName = customerName;
        this.address = address;
        this.customerPhone = customerPhone;
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerId() {
        return customerId;
    }
}
