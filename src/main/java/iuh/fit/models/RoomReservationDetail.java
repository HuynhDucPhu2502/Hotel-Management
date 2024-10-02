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

    public RoomReservationDetail(String roomReservationDetailID, LocalDateTime dateChanged, Room room, ReservationForm reservationForm) {
        this.setRoomReservationDetailID(roomReservationDetailID);
        this.setDateChanged(dateChanged);
        this.setRoom(room);
        this.setReservationForm(reservationForm);
    }

    public RoomReservationDetail() {
    }

    public RoomReservationDetail(String roomReservationDetailID) {
        this.setRoomReservationDetailID(roomReservationDetailID);
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

    public String getRoomReservationDetailID() {
        return roomReservationDetailID;
    }

    public void setRoomReservationDetailID(String roomReservationDetailID) {
        if (RegexChecker.isValidIDFormat(GlobalConstants.ROOM_RESERVATION_DETAIL_PREFIX, roomReservationDetailID))
            throw new IllegalArgumentException(ErrorMessages.ROOM_RESERVATION_DETAIL_INVALID_ID);
        this.roomReservationDetailID = roomReservationDetailID;
    }

    public LocalDateTime getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(LocalDateTime dateChanged) {
        if (dateChanged.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException(ErrorMessages.ROOM_RESERVATION_DETAIL_INVALID_DATECHANGED);
        this.dateChanged = dateChanged;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ReservationForm getReservationForm() {
        return reservationForm;
    }

    public void setReservationForm(ReservationForm reservationForm) {
        this.reservationForm = reservationForm;
    }

    @Override
    public String toString() {
        return "RoomReservationDetail{" +
                "roomReservationDetailID='" + roomReservationDetailID + '\'' +
                ", dateChanged=" + dateChanged +
                ", room=" + room +
                ", reservationForm=" + reservationForm +
                '}';
    }
}
