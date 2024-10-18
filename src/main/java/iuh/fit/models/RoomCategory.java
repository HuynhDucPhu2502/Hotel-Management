package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;

public class RoomCategory {
    // Mã định danh của loại phòng, không được rỗng và phải theo định dạng hợp lệ
    private String roomCategoryID;

    // Tên của loại phòng, không được rỗng và không vượt quá 30 ký tự
    private String roomCategoryName;

    // Số lượng giường trong loại phòng, phải có ít nhất 1 giường
    private int numberOfBed;
    /**
     * Constructor đầy đủ cho RoomCategory
     * @param roomCategoryID Mã loại phòng
     * @param roomCategoryName Tên loại phòng
     * @param numberOfBed Số lượng giường trong loại phòng
     */
    public RoomCategory(String roomCategoryID, String roomCategoryName, int numberOfBed) {
        this.setRoomCategoryID(roomCategoryID);
        this.setRoomCategoryName(roomCategoryName);
        this.setNumberOfBed(numberOfBed);
    }

    /**
     * Constructor với chỉ mã loại phòng
     * @param roomCategoryID Mã loại phòng
     */
    public RoomCategory(String roomCategoryID) {
        this.setRoomCategoryID(roomCategoryID);
    }

    /**
     * Constructor mặc định
     */
    public RoomCategory() {
    }

    /**
     * Lấy mã loại phòng
     * @return roomCategoryid Mã loại phòng
     */
    public String getRoomCategoryID() {
        return roomCategoryID;
    }

    /**
     * Thiết lập mã loại phòng và kiểm tra tính hợp lệ
     * @param roomCategoryID Mã loại phòng
     * @throws IllegalArgumentException nếu mã rỗng hoặc sai định dạng
     */
    public void setRoomCategoryID(String roomCategoryID) {
        if(roomCategoryID.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_ID_ISNULL);
        else if(!RegexChecker.isValidIDFormat(GlobalConstants.ROOMCATEGORY_PREFIX, roomCategoryID))
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_ID_FORMAT);
        this.roomCategoryID = roomCategoryID;
    }

    /**
     * Lấy tên loại phòng
     * @return roomCategoryName Tên loại phòng
     */
    public String getRoomCategoryName() {
        return roomCategoryName;
    }

    /**
     * Thiết lập tên loại phòng và kiểm tra tính hợp lệ
     * @param roomCategoryName Tên loại phòng
     * @throws IllegalArgumentException nếu tên rỗng hoặc vượt quá giới hạn ký tự
     */
    public void setRoomCategoryName(String roomCategoryName) {
        if(roomCategoryName.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_NAME_ISNULL);
        else if(roomCategoryName.length() > 30)
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_NAME_OUTBOUND_OFLENGTH);
        this.roomCategoryName = roomCategoryName;
    }

    /**
     * Lấy số lượng giường trong loại phòng
     * @return numberOfBed Số lượng giường
     */
    public int getNumberOfBed() {
        return numberOfBed;
    }

    /**
     * Thiết lập số lượng giường và kiểm tra tính hợp lệ
     * @param numberOfBed Số lượng giường
     * @throws IllegalArgumentException nếu số lượng giường nhỏ hơn 1
     */
    public void setNumberOfBed(int numberOfBed) {
        if(numberOfBed < 1)
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_NUMOFBED_NAN);
        this.numberOfBed = numberOfBed;
    }


    /**
     * So sánh hai đối tượng RoomCategory dựa trên mã loại phòng
     * @param o Đối tượng cần so sánh
     * @return true nếu hai đối tượng có cùng roomCategoryid, ngược lại là false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomCategory that = (RoomCategory) o;
        return Objects.equals(roomCategoryID, that.roomCategoryID);
    }

    /**
     * Tạo mã băm cho đối tượng RoomCategory dựa trên roomCategoryid
     * @return Mã băm của roomCategoryid
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(roomCategoryID);
    }

    /**
     * Trả về chuỗi mô tả của đối tượng RoomCategory
     * @return Chuỗi chứa thông tin của RoomCategory (roomCategoryid, roomCategoryName, numberOfBed)
     */
    @Override
    public String toString() {
        return "RoomCategory{" +
                "roomCategoryid='" + roomCategoryID + '\'' +
                ", roomCategoryName='" + roomCategoryName + '\'' +
                ", numberOfBed=" + numberOfBed +
                '}';
    }
}
