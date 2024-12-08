package iuh.fit.models;

import iuh.fit.utils.Calculator;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Objects;

public class ReservationForm {

    private String reservationID;
    private LocalDateTime reservationDate;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Employee employee;
    private Room room;
    private Customer customer;
    private double roomBookingDeposit;

    public ReservationForm(
            String reservationID, LocalDateTime reservationDate, LocalDateTime checkInDate,
            LocalDateTime checkOutDate, Employee employee, Room room,
            Customer customer
    ) {
        this.setReservationID(reservationID);
        this.setReservationDate(reservationDate);
        this.setCheckInDate(checkInDate);
        this.setCheckOutDate(checkOutDate);
        this.setEmployee(employee);
        this.setRoom(room);
        this.setCustomer(customer);
        updateBookingDeposit();
    }

    public ReservationForm(LocalDateTime reservationDate, LocalDateTime checkInDate, LocalDateTime checkOutDate,
                           Employee employee, Room room, Customer customer) {
        this.setReservationDate(reservationDate);
        this.setCheckInDate(checkInDate);
        this.setCheckOutDate(checkOutDate);
        this.setEmployee(employee);
        this.setRoom(room);
        this.setCustomer(customer);
        updateBookingDeposit();
    }

    public ReservationForm() {}

    public void setEmployee(Employee employee) {
        if (employee == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_EMPLOYEE);
        this.employee = employee;
    }

    public void setReservationID(String reservationID) {
        if (reservationID.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_ID_ISNULL);  // Kiểm tra mã đặt phòng không được trống
        if (!RegexChecker.isValidIDFormat(GlobalConstants.RESERVATIONID_PREFIX, reservationID))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_ID_FORMAT);  // Kiểm tra mã đặt phòng đúng định dạng
        this.reservationID = reservationID;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        if (reservationDate == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_RESERVATION_DATE_ISNULL);  // Kiểm tra ngày đặt không được null
        this.reservationDate = reservationDate;
    }

    public void setCustomer(Customer customer) {
        if (customer == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_CUSTOMER);
        this.customer = customer;
    }

    public void setRoom(Room room) {
        if (room == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_ROOM);
        this.room = room;
    }


    public void setCheckInDate(LocalDateTime checkInDate) {
        this.checkInDate = checkInDate;
        validateDateRange();
    }

    public void setCheckOutDate(LocalDateTime checkOutDate) {
        this.checkOutDate = checkOutDate;
        validateDateRange();
    }

    private void validateDateRange() {
        if (reservationDate == null) {
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_RESERVATION_DATE_ISNULL);
        }

        if (checkInDate != null && checkInDate.isBefore(reservationDate)) {
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_CHECKIN_DATE);
        }

        if (checkOutDate != null && checkInDate != null && checkOutDate.isBefore(checkInDate)) {
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_CHECKOUT_DATE);
        }
    }




    private void updateBookingDeposit() {
        if (room != null && checkInDate != null && checkOutDate != null) {
            this.roomBookingDeposit = Calculator.calculateBookingDeposit(room, checkInDate, checkOutDate);
        }
    }

    public void setRoomBookingDeposit(double roomBookingDeposit) {
        if (roomBookingDeposit < 0)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_ROOM_BOOKING_DEPOSIT_AMOUNT);
        this.roomBookingDeposit = roomBookingDeposit;
    }

    public String getReservationID() {
        return reservationID;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    public LocalDateTime getCheckOutDate() {
        return checkOutDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Room getRoom() {
        return room;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getRoomBookingDeposit() {
        return roomBookingDeposit;
    }

    @Override
    public String toString() {
        return "ReservationForm{" +
                "reservationID='" + reservationID + '\'' +
                ", reservationDate=" + reservationDate +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", employee=" + employee +
                ", room=" + room +
                ", customer=" + customer +
                ", roomBookingDeposit=" + roomBookingDeposit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationForm that = (ReservationForm) o;
        return Objects.equals(reservationID, that.reservationID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationID);
    }
}
