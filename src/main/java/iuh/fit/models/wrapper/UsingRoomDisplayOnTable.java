package iuh.fit.models.wrapper;

import java.time.LocalDateTime;
import java.util.Objects;

public class UsingRoomDisplayOnTable {
    private String roomID;
    private String cusName;
    private String empName;
    private LocalDateTime createDate;
    private double deposit;
    private double serviceCharge;
    private double roomCharge;
    private double tax;
    private double netDue;

    public UsingRoomDisplayOnTable( String cusName, String roomID, String empName, LocalDateTime createDate, double deposit, double serviceCharge, double roomCharge, double tax, double netDue) {
        this.cusName = cusName;
        this.roomID = roomID;
        this.empName = empName;
        this.createDate = createDate;
        this.deposit = deposit;
        this.serviceCharge = serviceCharge;
        this.roomCharge = roomCharge;
        this.setTax(tax);
        this.netDue = netDue;
    }

    public UsingRoomDisplayOnTable(String roomID) {
        this.roomID = roomID;
    }

    public UsingRoomDisplayOnTable() {
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
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

    public double getTax() {
        return tax;
    }

    public double getNetDue() {
        return netDue;
    }

    public void setNetDue(double netDue) {
        this.netDue = netDue;
    }

    public void setTax(double tax) {
        this.tax = tax * (this.serviceCharge + this.roomCharge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsingRoomDisplayOnTable that)) return false;
        return Objects.equals(getRoomID(), that.getRoomID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoomID());
    }

    @Override
    public String toString() {
        return "UsingRoomDisplayOnTable{" +
                "roomID='" + roomID + '\'' +
                ", cusName='" + cusName + '\'' +
                ", empName='" + empName + '\'' +
                ", createDate=" + createDate +
                ", deposit=" + deposit +
                ", serviceCharge=" + serviceCharge +
                ", roomCharge=" + roomCharge +
                ", tax=" + tax +
                ", netDue=" + netDue +
                '}';
    }
}
