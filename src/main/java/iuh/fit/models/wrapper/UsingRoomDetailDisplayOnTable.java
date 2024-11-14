package iuh.fit.models.wrapper;

import java.time.LocalDateTime;
import java.util.Objects;

public class UsingRoomDetailDisplayOnTable {
    private String roomCategoryID;
    private String nameRoomCategory;
    private int timesUsing;
    private float percentUsing;
    private double netDue;
    private float percentNetDue;

    public UsingRoomDetailDisplayOnTable(String roomCategoryID, String nameRoomCategory, int timesUsing, float percentUsing, double netDue, float percentNetDue) {
        this.setRoomCategoryID(roomCategoryID);
        this.setNameRoomCategory(nameRoomCategory);
        this.setTimesUsing(timesUsing);
        this.setPercentUsing(percentUsing);
        this.setNetDue(netDue);
        this.setPercentNetDue(percentNetDue);
    }

    public UsingRoomDetailDisplayOnTable(String roomCategoryID) {
        this.roomCategoryID = roomCategoryID;
    }

    public UsingRoomDetailDisplayOnTable() {
    }

    public String getRoomCategoryID() {
        return roomCategoryID;
    }

    public void setRoomCategoryID(String roomCategoryID) {
        this.roomCategoryID = roomCategoryID;
    }

    public String getNameRoomCategory() {
        return nameRoomCategory;
    }

    public void setNameRoomCategory(String nameRoomCategory) {
        this.nameRoomCategory = nameRoomCategory;
    }

    public double getNetDue() {
        return netDue;
    }

    public void setNetDue(double netDue) {
        this.netDue = netDue;
    }

    public int getTimesUsing() {
        return timesUsing;
    }

    public void setTimesUsing(int timesUsing) {
        this.timesUsing = timesUsing;
    }

    public float getPercentUsing() {
        return percentUsing;
    }

    public void setPercentUsing(float percentUsing) {
        this.percentUsing = Math.round(percentUsing * 100) / 100.0f;
    }

    public float getPercentNetDue() {
        return percentNetDue;
    }

    public void setPercentNetDue(float percentNetDue) {
        this.percentNetDue = Math.round(percentNetDue * 100) / 100.0f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsingRoomDetailDisplayOnTable that)) return false;
        return Objects.equals(getRoomCategoryID(), that.getRoomCategoryID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoomCategoryID());
    }

    @Override
    public String toString() {
        return "UsingRoomDetailDisplayOnTable{" +
                "roomCategoryID='" + roomCategoryID + '\'' +
                ", nameRoomCategory='" + nameRoomCategory + '\'' +
                ", timesUsing=" + timesUsing +
                ", percentUsing=" + percentUsing +
                ", netDue=" + netDue +
                ", percentNetDue=" + percentNetDue +
                '}';
    }
}
