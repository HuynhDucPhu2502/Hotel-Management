package iuh.fit.models;

public class Room {
    private String roomCategoryID;
    private String roomCategoryName;
    private int numberOfBed;

    public Room(String roomCategoryID, String roomCategoryName, int numberOfBed) {
        this.roomCategoryID = roomCategoryID;
        this.roomCategoryName = roomCategoryName;
        this.numberOfBed = numberOfBed;
    }
}
