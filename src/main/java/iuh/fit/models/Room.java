package iuh.fit.models;

import iuh.fit.models.enums.RoomStatus;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class Room {
    // Mã phòng, không được rỗng và phải theo định dạng hợp lệ
    private String roomID;

    // Trạng thái của phòng (có thể là trống, đã đặt, đang sử dụng, v.v.)
    private RoomStatus roomStatus;

    // Thời gian tạo phòng, phải trước thời gian hiện tại
    private LocalDateTime dateOfCreation;

    // Danh mục phòng liên kết với phòng này
    private RoomCategory roomCategory;

    /**
     * Constructor đầy đủ cho Room
     * @param roomID Mã phòng
     * @param roomStatus Trạng thái của phòng
     * @param dateOfCreation Thời gian tạo phòng
     * @param roomCategory Danh mục phòng
     */
    public Room(String roomID, RoomStatus roomStatus, LocalDateTime dateOfCreation, RoomCategory roomCategory) {
        this.setRoomID(roomID);
        this.setRoomStatus(roomStatus);
        this.setDateOfCreation(dateOfCreation);
        this.setRoomCategory(roomCategory);
    }

    /**
     * Constructor với chỉ mã phòng
     * @param roomID Mã phòng
     */
    public Room(String roomID) {
        this.setRoomID(roomID);
    }

    /**
     * Constructor mặc định
     */
    public Room() {
    }

    /**
     * Lấy mã phòng
     * @return roomID Mã phòng
     */
    public String getRoomID() {
        return roomID;
    }

    /**
     * Thiết lập mã phòng và kiểm tra tính hợp lệ
     * @param roomID Mã phòng
     * @throws IllegalArgumentException nếu mã rỗng hoặc sai định dạng
     */
    public void setRoomID(String roomID) {
        if(roomID.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.ROOM_INVALID_ID_ISNULL);
        if(!RegexChecker.isValidRoomID(roomID))
            throw new IllegalArgumentException(ErrorMessages.ROOM_INVALID_ID_FORMAT);
        this.roomID = roomID;
    }

    /**
     * Lấy trạng thái của phòng
     * @return roomStatus Trạng thái của phòng
     */
    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    /**
     * Thiết lập trạng thái phòng và kiểm tra tính hợp lệ
     * @param roomStatus Trạng thái của phòng
     * @throws IllegalArgumentException nếu trạng thái rỗng hoặc không hợp lệ
     */
    public void setRoomStatus(RoomStatus roomStatus) {
        if(roomStatus == null)
            throw new IllegalArgumentException(ErrorMessages.ROOM_INVALID_ROOMSTATUS_ISNULL);
        if(!Arrays.stream(RoomStatus.values()).toList().contains(roomStatus))
            throw new IllegalArgumentException(ErrorMessages.ROOM_INVALID_ROOMSTATUS_TYPES);
        this.roomStatus = roomStatus;
    }

    /**
     * Lấy thời gian tạo phòng
     * @return dateOfCreation Thời gian tạo phòng
     */
    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * Thiết lập thời gian tạo phòng và kiểm tra tính hợp lệ
     * @param dateOfCreation Thời gian tạo phòng
     * @throws IllegalArgumentException nếu thời gian không hợp lệ (không trước thời gian hiện tại)
     */
    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        if(dateOfCreation.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException(ErrorMessages.ROOM_INVALID_DATEOFCREATION);
        this.dateOfCreation = dateOfCreation;
    }

    /**
     * Lấy danh mục phòng
     * @return roomCategory Danh mục phòng
     */
    public RoomCategory getRoomCategory() {
        return roomCategory;
    }

    /**
     * Thiết lập danh mục phòng và kiểm tra tính hợp lệ
     * @param roomCategory Danh mục phòng
     * @throws IllegalArgumentException nếu danh mục rỗng
     */
    public void setRoomCategory(RoomCategory roomCategory) {
        if(roomCategory == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_ROOMCATEGORY);
        this.roomCategory = roomCategory;
    }

    /**
     * So sánh hai đối tượng Room dựa trên mã phòng
     * @param o Đối tượng cần so sánh
     * @return true nếu hai đối tượng có cùng roomID, ngược lại là false
     */
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
