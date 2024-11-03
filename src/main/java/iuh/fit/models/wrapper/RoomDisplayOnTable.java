package iuh.fit.models.wrapper;

import java.time.LocalDateTime;

public class RoomDisplayOnTable {
    private String roomID;
    private String roomCategory;
    private int numOfPeople;
    private LocalDateTime bookingDate;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private double totalMoney;

    public RoomDisplayOnTable(String roomID, String roomCategory, int numOfPeople, LocalDateTime bookingDate, LocalDateTime checkInDate, LocalDateTime checkOutDate, double totalMoney) {
        this.roomID = roomID;
        this.roomCategory = roomCategory;
        this.numOfPeople = numOfPeople;
        this.bookingDate = bookingDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalMoney = totalMoney;
    }

    public RoomDisplayOnTable(String roomID) {
        this.roomID = roomID;
    }

    public RoomDisplayOnTable() {
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(String roomCategory) {
        this.roomCategory = roomCategory;
    }

    public int getNumOfPeople() {
        return numOfPeople;
    }

    public void setNumOfPeople(int numOfPeople) {
        this.numOfPeople = numOfPeople;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDateTime checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDateTime getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDateTime checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public String toString() {
        return "RoomDisplayOnTable{" +
                "roomID='" + roomID + '\'' +
                ", roomCategory='" + roomCategory + '\'' +
                ", numOfPeople=" + numOfPeople +
                ", bookingDate=" + bookingDate +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", totalMoney=" + totalMoney +
                '}';
    }
}
