package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Objects;

public class ReservationForm {
    private String reservationID;
    private LocalDateTime reservationDate;
    private LocalDateTime approxCheckInDate;
    private LocalDateTime approxCheckOutDate;
    private Employee employee;
    private Room room;
    private Customer customer;

    public ReservationForm(String reservationID, LocalDateTime reservationDate, LocalDateTime approxCheckInDate, LocalDateTime approxCheckOutDate, Employee employee, Room room, Customer customer) {
        this.setReservationID(reservationID);
        this.setReservationDate(reservationDate);
        this.setApproxCheckInDate(approxCheckInDate);
        this.setApproxCheckOutDate(approxCheckOutDate);
        this.setEmployee(employee);
        this.setRoom(room);
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
        if(reservationID.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_ID_ISNULL);
        if(!RegexChecker.isValidIDFormat(GlobalConstants.RESERVATIONID_PREFIX, reservationID))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_ID_FORMAT);
        this.reservationID = reservationID;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        if(reservationDate == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_RESERVATION_DATE_ISNULL);
        if(!reservationDate.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_RESERVATION_DATE);
        this.reservationDate = reservationDate;
    }

    public LocalDateTime getApproxCheckInDate() {
        return approxCheckInDate;
    }

    public void setApproxCheckInDate(LocalDateTime approxCheckInDate) {
        if(approxCheckInDate == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKIN_DATE_ISNULL);
        if(!approxCheckInDate.isAfter(this.reservationDate))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKIN_DATE);
        this.approxCheckInDate = approxCheckInDate;
    }

    public LocalDateTime getApproxCheckOutDate() {
        return approxCheckOutDate;
    }

    public void setApproxCheckOutDate(LocalDateTime approxCheckOutDate) {
        if(approxCheckOutDate == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKOUT_DATE_ISNULL);
        if(!approxCheckOutDate.isAfter(approxCheckInDate))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKOUT_DATE);
        this.approxCheckOutDate = approxCheckOutDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        if(employee == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_EMPLOYEE_ISNULL);
        this.employee = employee;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        if(room == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_ROOM_ISNULL);
        this.room = room;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        if(customer == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_CUSTOMER_ISNULL);
        this.customer = customer;
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
        return Objects.hashCode(reservationID);
    }
}
