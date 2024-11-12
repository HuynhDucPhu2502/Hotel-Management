package iuh.fit.models.wrapper;

import java.time.LocalDateTime;
import java.util.Objects;

public class UsingRoomDetailDisplayOnTable {
    private String roomID;
    private int timesUsing;
    private float percentUsing;
    private double netDue;
    private float percentNetDue;

    public UsingRoomDetailDisplayOnTable(String roomID, int timesUsing, float percentUsing, double netDue, float percentNetDue) {
        this.setRoomID(roomID);
        this.setTimesUsing(timesUsing);
        this.setPercentUsing(percentUsing);
        this.setNetDue(netDue);
        this.setPercentNetDue(percentNetDue);
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
                ", percentUsing=" + percentUsing +
                ", netDue=" + netDue +
                ", percentNetDue=" + percentNetDue +
                '}';
    }
}
