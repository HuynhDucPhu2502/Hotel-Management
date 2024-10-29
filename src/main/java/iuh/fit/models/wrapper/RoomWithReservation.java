package iuh.fit.models.wrapper;

import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;

public class RoomWithReservation {
    private Room room;
    private ReservationForm reservationForm;

    public RoomWithReservation(Room room, ReservationForm reservationForm) {
        this.room = room;
        this.reservationForm = reservationForm;
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
        return "RoomWithReservation{" +
                "room=" + room +
                ", reservationForm=" + reservationForm +
                '}';
    }
}
