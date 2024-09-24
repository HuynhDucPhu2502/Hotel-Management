package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;

/**
 * @author Le Tran Gia Huy
 * @created 24/09/2024 - 1:50 PM
 * @project HotelManagement
 * @package iuh.fit.models
 */
public class RoomUsage {
    private String roomUsageID;
    private double totalServiceCharge;
    private double roomCharge;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomUsage roomUsage)) return false;
        return Objects.equals(roomUsageID, roomUsage.roomUsageID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomUsageID);
    }

    public RoomUsage(String roomUsageID, double totalServiceCharge, double roomCharge) {
        setRoomUsageID(roomUsageID);
        setTotalServiceCharge(totalServiceCharge);
        setRoomCharge(roomCharge);
    }

    public RoomUsage(String roomUsageID) {
        this.roomUsageID = roomUsageID;
    }

    public String getRoomUsageID() {
        return roomUsageID;
    }

    public void setRoomUsageID(String roomUsageID) {
        if(roomUsageID.trim().isBlank())
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_INVALID_ID_ISNULL);
        else if(!RegexChecker.isValidIDFormat(GlobalConstants.ROOMUSAGE_PREFIX, roomUsageID))
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_INVALID_ID_FORMAT);
        this.roomUsageID = roomUsageID;
    }

    public double getTotalServiceCharge() {
        return totalServiceCharge;
    }

    public void setTotalServiceCharge(double totalServiceCharge) {
        if(totalServiceCharge <= 0)
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_INVALID_TOTAL_SERVICE_CHARGE);
        this.totalServiceCharge = totalServiceCharge;
    }

    public double getRoomCharge() {
        return roomCharge;
    }

    public void setRoomCharge(double roomCharge) {
        if(roomCharge <= 0)
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_INVALID_ROOM_CHARGE);
        this.roomCharge = roomCharge;
    }

    private double calcTotalService(){
        return 0;
    }

    private double calcTimeUsed(){
        return 0;
    }

    @Override
    public String toString() {
        return "RoomUsage{" +
                "roomUsageID='" + roomUsageID + '\'' +
                ", totalServiceCharge=" + totalServiceCharge +
                ", roomCharge=" + roomCharge +
                '}';
    }
}
