package iuh.fit.models.misc;

import iuh.fit.models.Shift;

import java.time.LocalDateTime;

/**
 * @author Le Tran Gia Huy
 * @created 10/12/2024 - 9:22 PM
 * @project HotelManagement
 * @package iuh.fit.models.misc
 */
public class ShiftDetail {
    private int SDTID;
    private LocalDateTime createAt;
    private int numbOfPreOrderRoom;
    private int numbOfCheckOutRoom;
    private int numbOfCheckInRoom;

    public ShiftDetail() {
    }

    public ShiftDetail(LocalDateTime createAt, int numbOfCheckInRoom, int numbOfCheckOutRoom, int numbOfPreOrderRoom, int SDTID) {
        this.createAt = createAt;
        this.numbOfCheckInRoom = numbOfCheckInRoom;
        this.numbOfCheckOutRoom = numbOfCheckOutRoom;
        this.numbOfPreOrderRoom = numbOfPreOrderRoom;
        this.SDTID = SDTID;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftDetail that)) return false;

        return SDTID == that.SDTID;
    }

    @Override
    public int hashCode() {
        return SDTID;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public int getNumbOfCheckInRoom() {
        return numbOfCheckInRoom;
    }

    public void setNumbOfCheckInRoom(int numbOfCheckInRoom) {
        this.numbOfCheckInRoom = numbOfCheckInRoom;
    }

    public int getNumbOfCheckOutRoom() {
        return numbOfCheckOutRoom;
    }

    public void setNumbOfCheckOutRoom(int numbOfCheckOutRoom) {
        this.numbOfCheckOutRoom = numbOfCheckOutRoom;
    }

    public int getNumbOfPreOrderRoom() {
        return numbOfPreOrderRoom;
    }

    public void setNumbOfPreOrderRoom(int numbOfPreOrderRoom) {
        this.numbOfPreOrderRoom = numbOfPreOrderRoom;
    }

    public int getSDTID() {
        return SDTID;
    }

    public void setSDTID(int SDTID) {
        this.SDTID = SDTID;
    }

    @Override
    public String toString() {
        return "ShiftDetail{" +
                "createAt=" + createAt +
                ", SDTID=" + SDTID +
                ", numbOfPreOrderRoom=" + numbOfPreOrderRoom +
                ", numbOfCheckOutRoom=" + numbOfCheckOutRoom +
                ", numbOfCheckInRoom=" + numbOfCheckInRoom +
                '}';
    }
}
