package iuh.fit.models.wrapper;

import java.time.LocalDateTime;
import java.util.Objects;

public class UsingRoomDetailDisplayOnTable {
    private String roomID;
    private int timesUsing;
    private double netDue;

    public UsingRoomDetailDisplayOnTable(String roomID, int timesUsing, double netDue) {
        this.roomID = roomID;
        this.timesUsing = timesUsing;
        this.netDue = netDue;
    }

    public UsingRoomDetailDisplayOnTable(String roomID) {
        this.roomID = roomID;
    }

    public UsingRoomDetailDisplayOnTable() {
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsingRoomDetailDisplayOnTable that)) return false;
        return Objects.equals(getRoomID(), that.getRoomID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoomID());
    }

    @Override
    public String toString() {
        return "UsingRoomDetailDisplayOnTable{" +
                "roomID='" + roomID + '\'' +
                ", timesUsing=" + timesUsing +
                ", netDue=" + netDue +
                '}';
    }
}
