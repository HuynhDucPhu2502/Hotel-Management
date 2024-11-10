package iuh.fit.models;

import iuh.fit.models.enums.DialogType;
import iuh.fit.utils.ErrorMessages;

import java.time.LocalDateTime;

public class RoomDialog {
    private Room room;
    private ReservationForm reservationForm;
    private String dialog;
    private DialogType dialogType;
    private LocalDateTime timestamp;

    public RoomDialog(Room room, ReservationForm reservationForm,
                      String dialog, DialogType dialogType,
                      LocalDateTime timestamp) {
        setDialog(dialog);
        setDialogType(dialogType);
        setRoom(room);
        setReservationForm(reservationForm);
        setTimestamp(timestamp);
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

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public void setDialogType(DialogType dialogType) {
        this.dialogType = dialogType;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Room getRoom() {
        return room;
    }

    public ReservationForm getReservationForm() {
        return reservationForm;
    }

    public String getDialog() {
        return dialog;
    }

    public DialogType getDialogType() {
        return dialogType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "RoomDialog{" +
                "room=" + room +
                ", reservationForm=" + reservationForm +
                ", dialog='" + dialog + '\'' +
                ", dialogType=" + dialogType +
                '}';
    }
}
