package iuh.fit.models;

import iuh.fit.utils.CostCalculator;
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

    public ReservationForm(String reservationID, LocalDateTime reservationDate, LocalDateTime checkInDate, LocalDateTime checkOutDate, Employee employee, Room room, Customer customer) {
        this.setReservationID(reservationID);
        this.setReservationDate(reservationDate);
        this.setEmployee(employee);
        this.setRoom(room);
        this.setCheckInDate(checkInDate);
        this.setCheckOutDate(checkOutDate);
        this.setCustomer(customer);
    }

    public ReservationForm(String reservationID) {
        this.setReservationID(reservationID);
    }

    public ReservationForm() {
    }

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        if (reservationID.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_ID_ISNULL);  // Kiểm tra mã đặt phòng không được trống
        if (!RegexChecker.isValidIDFormat(GlobalConstants.RESERVATIONID_PREFIX, reservationID))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_ID_FORMAT);  // Kiểm tra mã đặt phòng đúng định dạng
        this.reservationID = reservationID;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        if (reservationDate == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_RESERVATION_DATE_ISNULL);  // Kiểm tra ngày đặt không được null
        if (!reservationDate.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_RESERVATION_DATE);  // Kiểm tra ngày đặt phải trước thời gian hiện tại
        this.reservationDate = reservationDate;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDateTime checkInDate) {
        if (checkInDate == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKIN_DATE_ISNULL);  // Kiểm tra ngày nhận phòng không được null
        if (!checkInDate.isAfter(this.reservationDate))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKIN_DATE);  // Kiểm tra ngày nhận phòng phải sau ngày đặt
        this.checkInDate = checkInDate;
        if (checkOutDate != null && room != null)
            this.roomBookingDeposit = CostCalculator.calculateBookingDeposit(this.room, this.checkInDate, this.checkOutDate);
    }

    public LocalDateTime getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDateTime checkOutDate) {
        if (checkOutDate == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKOUT_DATE_ISNULL);  // Kiểm tra ngày trả phòng không được null
        if (!checkOutDate.isAfter(checkInDate))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKOUT_DATE);  // Kiểm tra ngày trả phòng phải sau ngày nhận phòng
        this.checkOutDate = checkOutDate;
        if (checkInDate != null && room != null)
            this.roomBookingDeposit = CostCalculator.calculateBookingDeposit(this.room, this.checkInDate, this.checkOutDate);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        if (employee == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_EMPLOYEE);
        this.employee = employee;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        if (room == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_ROOM);
        this.room = room;
        if (checkInDate != null && checkOutDate != null)
            this.roomBookingDeposit = CostCalculator.calculateBookingDeposit(this.room, this.checkInDate, this.checkOutDate);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if (customer == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_CUSTOMER);
        this.customer = customer;
    }

    public double getRoomBookingDeposit() {
        return roomBookingDeposit;
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

    @Override
    public String toString() {
        return "ReservationForm{" +
                "reservationID='" + reservationID + '\'' +
                ", reservationDate=" + reservationDate +
                ", approxCheckInDate=" + checkInDate +
                ", approxCheckOutDate=" + checkOutDate +
                ", employee=" + employee +
                ", room=" + room +
                ", customer=" + customer +
                '}';
    }
}
