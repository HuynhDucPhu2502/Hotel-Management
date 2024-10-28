package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Objects;

public class RoomReservationDetail {
    private String roomReservationDetailID;
    private LocalDateTime dateChanged;
    private Room room;
    private ReservationForm reservationForm;
    private Employee employee;

    public RoomReservationDetail(
            String roomReservationDetailID, LocalDateTime dateChanged,
            Room room, ReservationForm reservationForm,
            Employee employee) {
        this.setRoomReservationDetailID(roomReservationDetailID);
        this.setDateChanged(dateChanged);
        this.setRoom(room);
        this.setReservationForm(reservationForm);
        this.setEmployee(employee);
    }

    public RoomReservationDetail() {
    }

    public RoomReservationDetail(String roomReservationDetailID) {
        this.setRoomReservationDetailID(roomReservationDetailID);
    }

    public void setEmployee(Employee employee) {
        if (employee == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_EMPLOYEE);
        this.employee = employee;
    }

    public void setRoomReservationDetailID(String roomReservationDetailID) {
        if (!RegexChecker.isValidIDFormat(GlobalConstants.ROOM_RESERVATION_DETAIL_PREFIX, roomReservationDetailID)) {
            throw new IllegalArgumentException(ErrorMessages.ROOM_RESERVATION_DETAIL_INVALID_ID);
        }
        this.roomReservationDetailID = roomReservationDetailID;
    }

    public void setDateChanged(LocalDateTime dateChanged) {
        if (dateChanged == null || dateChanged.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(ErrorMessages.ROOM_RESERVATION_DETAIL_INVALID_DATECHANGED);
        }
        this.dateChanged = dateChanged;
    }

    public void setRoom(Room room) {
        if (room == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_ROOM);
        this.room = room;
    }

    public void setReservationForm(ReservationForm reservationForm) {
        if (reservationForm == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_RESERVATIONFORM);
        this.reservationForm = reservationForm;
    }

    public String getRoomReservationDetailID() {
        return roomReservationDetailID;
    }

    public LocalDateTime getDateChanged() {
        return dateChanged;
    }

    public Room getRoom() {
        return room;
    }

    public ReservationForm getReservationForm() {
        return reservationForm;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomReservationDetail that)) return false;
        return Objects.equals(roomReservationDetailID, that.roomReservationDetailID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roomReservationDetailID);
    }

    @Override
    public String toString() {
        return "RoomReservationDetail{" +
                "roomReservationDetailID='" + roomReservationDetailID + '\'' +
                ", dateChanged=" + dateChanged +
                ", room=" + room +
                ", reservationForm=" + reservationForm +
                ", employee=" + employee +
                '}';
    }
}
