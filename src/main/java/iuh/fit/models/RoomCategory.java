package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;
import java.util.SimpleTimeZone;

public class RoomCategory {
    private String roomCategoryid;
    private String roomCategoryName;
    private int numberOfBed;

    public RoomCategory(String roomCategoryid, String roomCategoryName, int numberOfBed) {
        this.setRoomCategoryid(roomCategoryid);
        this.setRoomCategoryName(roomCategoryName);
        this.setNumberOfBed(numberOfBed);
    }

    public RoomCategory(String roomCategoryid) {
        this.setRoomCategoryid(roomCategoryid);
    }

    public RoomCategory() {
    }

    public String getRoomCategoryid() {
        return roomCategoryid;
    }

    public void setRoomCategoryid(String roomCategoryid) {
        if(roomCategoryid.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_ID_ISNULL);
        else if(RegexChecker.isValidRoomCategoryidFormat(roomCategoryid))
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_ID_FORMAT);
        this.roomCategoryid = roomCategoryid;
    }

    public String getRoomCategoryName() {
        return roomCategoryName;
    }

    public void setRoomCategoryName(String roomCategoryName) {
        if(roomCategoryName.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_NAME_ISNULL);
        else if(roomCategoryName.length() > 30)
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_NAME_OUTBOUND_OFLENGTH);
        this.roomCategoryName = roomCategoryName;
    }

    public int getNumberOfBed() {
        return numberOfBed;
    }

    public void setNumberOfBed(int numberOfBed) {
        if(numberOfBed < 1)
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_NUMOFBED_NAN);
        this.numberOfBed = numberOfBed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomCategory that = (RoomCategory) o;
        return Objects.equals(roomCategoryid, that.roomCategoryid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roomCategoryid);
    }

    @Override
    public String toString() {
        return "RoomCategory{" +
                "roomCategoryid='" + roomCategoryid + '\'' +
                ", roomCategoryName='" + roomCategoryName + '\'' +
                ", numberOfBed=" + numberOfBed +
                '}';
    }
}
