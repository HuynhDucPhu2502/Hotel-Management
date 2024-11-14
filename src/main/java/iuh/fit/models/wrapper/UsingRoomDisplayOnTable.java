package iuh.fit.models.wrapper;

import java.time.LocalDateTime;
import java.util.Objects;

public class UsingRoomDisplayOnTable {
    private String roomCategoryID;
    private String nameRoomCategory;
    private String roomID;
    private String cusName;
    private String empName;
    private LocalDateTime createDate;
    private double deposit;
    private double serviceCharge;
    private double roomCharge;
    private double netDue;

    public UsingRoomDisplayOnTable(String roomCategoryID, String nameRoomCategory, String roomID, String cusName, String empName, LocalDateTime createDate, double deposit, double serviceCharge, double roomCharge, double netDue) {
        this.setRoomCategoryID(roomCategoryID);
        this.setNameRoomCategory(nameRoomCategory);
        this.setRoomID(roomID);
        this.setCusName(cusName);
        this.setEmpName(empName);
        this.setCreateDate(createDate);
        this.setDeposit(deposit);
        this.setServiceCharge(serviceCharge);
        this.setRoomCharge(roomCharge);
        this.setNetDue(netDue);
    }

    public UsingRoomDisplayOnTable(String roomID) {
        this.roomID = roomID;
    }

    public UsingRoomDisplayOnTable() {
    }

    public String getRoomCategoryID() {
        return roomCategoryID;
    }

    public void setRoomCategoryID(String roomCategoryID) {
        this.roomCategoryID = roomCategoryID;
    }

    public String getNameRoomCategory() {
        return nameRoomCategory;
    }

    public void setNameRoomCategory(String nameRoomCategory) {
        this.nameRoomCategory = nameRoomCategory;
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

    public double getNetDue() {
        return netDue;
    }

    public void setNetDue(double netDue) {
        this.netDue = netDue;
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
                ", netDue=" + netDue +
                '}';
    }
}
