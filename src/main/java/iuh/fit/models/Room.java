package iuh.fit.models;

import java.util.Objects;

public class Room {
    private String roomCategoryID;
    private String roomCategoryName;
    private int numberOfBed;

    public Room(String roomCategoryID, String roomCategoryName, int numberOfBed) {
        this.roomCategoryID = roomCategoryID;
        this.roomCategoryName = roomCategoryName;
        this.numberOfBed = numberOfBed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room room)) return false;
        return Objects.equals(roomCategoryID, room.roomCategoryID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(roomCategoryID);
    }
}
