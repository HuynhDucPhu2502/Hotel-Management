package iuh.fit.models.wrapper;

import java.time.LocalDateTime;
import java.util.Objects;

public class ServiceDisplayOnTable {
    private String serviceId;
    private String serviceName;
    private String serviceCategory;
    private String employee;
    private LocalDateTime exportDate;
    private int quantity;
    private double unitPrice;
    private double totalMoney;

    public ServiceDisplayOnTable(String serviceId, String serviceName, String serviceCategory, String employee, LocalDateTime exportDate, int quantity, double price, double totalMoney) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceCategory = serviceCategory;
        this.employee = employee;
        this.exportDate = exportDate;
        this.quantity = quantity;
        this.unitPrice = price;
        this.totalMoney = totalMoney;
    }

    public ServiceDisplayOnTable(String serviceId) {
        this.serviceId = serviceId;
    }

    public ServiceDisplayOnTable() {
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public LocalDateTime getExportDate() {
        return exportDate;
    }

    public void setExportDate(LocalDateTime exportDate) {
        this.exportDate = exportDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceDisplayOnTable that = (ServiceDisplayOnTable) o;
        return Objects.equals(serviceId, that.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serviceId);
    }

    @Override
    public String toString() {
        return "ServiceDisplayOnTable{" +
                "serviceId='" + serviceId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", serviceCategory='" + serviceCategory + '\'' +
                ", employee='" + employee + '\'' +
                ", exportDate=" + exportDate +
                ", quantity=" + quantity +
                ", price=" + unitPrice +
                ", totalMoney=" + totalMoney +
                '}';
    }
}
