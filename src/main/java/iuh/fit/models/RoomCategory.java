package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;

public class RoomCategory {
    // Mã định danh của loại phòng, không được rỗng và phải theo định dạng hợp lệ
    private String roomCategoryid;

    // Tên của loại phòng, không được rỗng và không vượt quá 30 ký tự
    private String roomCategoryName;

    // Số lượng giường trong loại phòng, phải có ít nhất 1 giường
    private int numberOfBed;
    /**
     * Constructor đầy đủ cho RoomCategory
     * @param roomCategoryid Mã loại phòng
     * @param roomCategoryName Tên loại phòng
     * @param numberOfBed Số lượng giường trong loại phòng
     */
    public RoomCategory(String roomCategoryid, String roomCategoryName, int numberOfBed) {
        this.setRoomCategoryid(roomCategoryid);
        this.setRoomCategoryName(roomCategoryName);
        this.setNumberOfBed(numberOfBed);
    }

    /**
     * Constructor với chỉ mã loại phòng
     * @param roomCategoryid Mã loại phòng
     */
    public RoomCategory(String roomCategoryid) {
        this.setRoomCategoryid(roomCategoryid);
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
    public String getRoomCategoryid() {
        return roomCategoryid;
    }

    /**
     * Thiết lập mã loại phòng và kiểm tra tính hợp lệ
     * @param roomCategoryid Mã loại phòng
     * @throws IllegalArgumentException nếu mã rỗng hoặc sai định dạng
     */
    public void setRoomCategoryid(String roomCategoryid) {
        if(roomCategoryid.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_ID_ISNULL);
        else if(!RegexChecker.isValidIDFormat(GlobalConstants.ROOMCATEGORY_PREFIX, roomCategoryid))
            throw new IllegalArgumentException(ErrorMessages.ROOM_CATEGORY_INVALID_ID_FORMAT);
        this.roomCategoryid = roomCategoryid;
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
        return Objects.equals(roomCategoryid, that.roomCategoryid);
    }

    /**
     * Tạo mã băm cho đối tượng RoomCategory dựa trên roomCategoryid
     * @return Mã băm của roomCategoryid
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(roomCategoryid);
    }

    /**
     * Trả về chuỗi mô tả của đối tượng RoomCategory
     * @return Chuỗi chứa thông tin của RoomCategory (roomCategoryid, roomCategoryName, numberOfBed)
     */
    @Override
    public String toString() {
        return "RoomCategory{" +
                "roomCategoryid='" + roomCategoryid + '\'' +
                ", roomCategoryName='" + roomCategoryName + '\'' +
                ", numberOfBed=" + numberOfBed +
                '}';
    }
}
