package iuh.fit.models.wrapper;

import java.time.LocalDateTime;
import java.util.Objects;

public class InvoiceDisplayOnTable {
    private String invoiceID;
    private String cusName;
    private String roomID;
    private String empName;
    private LocalDateTime createDate;
    private double deposit;
    private double serviceCharge;
    private double roomCharge;
    private double netDue;

    public InvoiceDisplayOnTable(String invoiceID, String cusName, String roomID, String empName, LocalDateTime createDate, double deposit, double serviceCharge, double roomCharge, double tax, double netDue) {
        this.invoiceID = invoiceID;
        this.cusName = cusName;
        this.roomID = roomID;
        this.empName = empName;
        this.createDate = createDate;
        this.deposit = deposit;
        this.serviceCharge = serviceCharge;
        this.roomCharge = roomCharge;
        this.netDue = netDue;
    }

    public InvoiceDisplayOnTable(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public InvoiceDisplayOnTable() {
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public double getRoomCharge() {
        return roomCharge;
    }

    public void setRoomCharge(double roomCharge) {
        this.roomCharge = roomCharge;
    }


    public double getNetDue() {
        return netDue;
    }

    public void setNetDue(double netDue) {
        this.netDue = netDue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceDisplayOnTable that = (InvoiceDisplayOnTable) o;
        return Objects.equals(invoiceID, that.invoiceID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(invoiceID);
    }

    @Override
    public String toString() {
        return "InvoiceDisplayOnTable{" +
                "invoiceID='" + invoiceID + '\'' +
                ", cusName='" + cusName + '\'' +
                ", roomID='" + roomID + '\'' +
                ", empName='" + empName + '\'' +
                ", createDate=" + createDate +
                ", deposit=" + deposit +
                ", serviceCharge=" + serviceCharge +
                ", roomCharge=" + roomCharge +
                ", netDue=" + netDue +
                '}';
    }
}
