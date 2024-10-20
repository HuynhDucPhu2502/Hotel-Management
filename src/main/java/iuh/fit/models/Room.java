package iuh.fit.models;

import iuh.fit.models.enums.RoomStatus;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class Room {
    private String roomID;

    private RoomStatus roomStatus;

    private LocalDateTime dateOfCreation;

    private RoomCategory roomCategory;

    public Room(String roomID, RoomStatus roomStatus, LocalDateTime dateOfCreation, RoomCategory roomCategory) {
        this.setRoomID(roomID);
        this.setRoomStatus(roomStatus);
        this.setDateOfCreation(dateOfCreation);
        this.setRoomCategory(roomCategory);
    }

    public Room(String roomID) {
        this.setRoomID(roomID);
    }

    public Room() {
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        if(roomID.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.ROOM_INVALID_ID_ISNULL);
        if(!RegexChecker.isValidRoomID(roomID))
            throw new IllegalArgumentException(ErrorMessages.ROOM_INVALID_ID_FORMAT);
        this.roomID = roomID;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(RoomStatus roomStatus) {
        if(roomStatus == null)
            throw new IllegalArgumentException(ErrorMessages.ROOM_INVALID_ROOMSTATUS_ISNULL);
        if(!Arrays.stream(RoomStatus.values()).toList().contains(roomStatus))
            throw new IllegalArgumentException(ErrorMessages.ROOM_INVALID_ROOMSTATUS_TYPES);
        this.roomStatus = roomStatus;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        if(dateOfCreation.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException(ErrorMessages.ROOM_INVALID_DATEOFCREATION);
        this.dateOfCreation = dateOfCreation;
    }

    public RoomCategory getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(RoomCategory roomCategory) {
        if(roomCategory == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_ROOMCATEGORY);
        this.roomCategory = roomCategory;
    }

    public String getRoomFloorNumber() {
        return String.valueOf(roomID.charAt(2));
    }

    public String getRoomNumer() {
        return roomID.substring(2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomID, room.roomID);
    }

    /**
     * Tạo mã băm cho đối tượng Room dựa trên roomID
     * @return Mã băm của roomID
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(roomID);
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomID='" + roomID + '\'' +
                ", roomStatus=" + roomStatus +
                ", dateOfCreation=" + dateOfCreation +
                ", roomCategory=" + roomCategory +
                '}';
    }
}
