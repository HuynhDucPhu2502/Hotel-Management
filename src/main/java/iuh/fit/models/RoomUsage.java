package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;

/**
 * Lớp RoomUsage đại diện cho việc sử dụng một phòng, bao gồm các khoản phí cho phòng và dịch vụ bổ sung.
 */
public class RoomUsage {

    // ID duy nhất cho việc sử dụng phòng
    private String roomUsageID;

    // Tổng phí dịch vụ phát sinh trong thời gian sử dụng phòng
    private double totalServiceCharge;

    // Phí cho chính phòng
    private double roomCharge;

    /**
     * Kiểm tra xem hai đối tượng RoomUsage có bằng nhau dựa trên roomUsageID hay không.
     *
     * @param o Đối tượng cần so sánh với instance RoomUsage này.
     * @return true nếu roomUsageID bằng nhau; false nếu không.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Kiểm tra tham chiếu
        if (!(o instanceof RoomUsage roomUsage)) return false; // Kiểm tra loại đối tượng
        return Objects.equals(roomUsageID, roomUsage.roomUsageID); // So sánh roomUsageID
    }

    /**
     * Tạo mã băm cho instance RoomUsage dựa trên roomUsageID.
     *
     * @return Mã băm cho instance RoomUsage này.
     */
    @Override
    public int hashCode() {
        return Objects.hash(roomUsageID); // Tạo mã băm dựa trên roomUsageID
    }

    /**
     * Khởi tạo một instance RoomUsage với ID phòng sử dụng, tổng phí dịch vụ và phí phòng.
     *
     * @param roomUsageID ID duy nhất cho việc sử dụng phòng.
     * @param totalServiceCharge Tổng phí cho các dịch vụ đã sử dụng.
     * @param roomCharge Phí cho chính phòng.
     */
    public RoomUsage(String roomUsageID, double totalServiceCharge, double roomCharge) {
        setRoomUsageID(roomUsageID); // Thiết lập roomUsageID
        setTotalServiceCharge(totalServiceCharge); // Thiết lập tổng phí dịch vụ
        setRoomCharge(roomCharge); // Thiết lập phí phòng
    }

    /**
     * Khởi tạo một instance RoomUsage mặc định.
     */
    public RoomUsage() {
        // Constructor mặc định
    }

    /**
     * Khởi tạo một instance RoomUsage với chỉ ID phòng sử dụng.
     *
     * @param roomUsageID ID duy nhất cho việc sử dụng phòng.
     */
    public RoomUsage(String roomUsageID) {
        this.roomUsageID = roomUsageID; // Thiết lập roomUsageID
    }

    /**
     * Lấy ID duy nhất của việc sử dụng phòng.
     *
     * @return ID của việc sử dụng phòng.
     */
    public String getRoomUsageID() {
        return roomUsageID; // Trả về roomUsageID
    }

    /**
     * Thiết lập ID duy nhất cho việc sử dụng phòng.
     *
     * @param roomUsageID ID phòng sử dụng mới.
     * @throws IllegalArgumentException nếu ID trống hoặc không đúng định dạng yêu cầu.
     */
    public void setRoomUsageID(String roomUsageID) {
        // Kiểm tra nếu roomUsageID trống
        if(roomUsageID.trim().isBlank())
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_INVALID_ID_ISNULL);
            // Kiểm tra định dạng ID
        else if(!RegexChecker.isValidIDFormat(GlobalConstants.ROOMUSAGE_PREFIX, roomUsageID))
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_INVALID_ID_FORMAT);
        this.roomUsageID = roomUsageID; // Thiết lập roomUsageID
    }

    /**
     * Lấy tổng phí dịch vụ.
     *
     * @return Tổng phí dịch vụ.
     */
    public double getTotalServiceCharge() {
        return totalServiceCharge; // Trả về tổng phí dịch vụ
    }

    /**
     * Thiết lập tổng phí dịch vụ.
     *
     * @param totalServiceCharge Tổng phí dịch vụ mới.
     * @throws IllegalArgumentException nếu phí nhỏ hơn hoặc bằng 0.
     */
    public void setTotalServiceCharge(double totalServiceCharge) {
        // Kiểm tra nếu tổng phí dịch vụ không hợp lệ
        if(totalServiceCharge <= 0)
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_INVALID_TOTAL_SERVICE_CHARGE);
        this.totalServiceCharge = totalServiceCharge; // Thiết lập tổng phí dịch vụ
    }

    /**
     * Lấy phí phòng.
     *
     * @return Phí phòng.
     */
    public double getRoomCharge() {
        return roomCharge; // Trả về phí phòng
    }

    /**
     * Thiết lập phí phòng.
     *
     * @param roomCharge Phí phòng mới.
     * @throws IllegalArgumentException nếu phí nhỏ hơn hoặc bằng 0.
     */
    public void setRoomCharge(double roomCharge) {
        // Kiểm tra nếu phí phòng không hợp lệ
        if(roomCharge <= 0)
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_INVALID_ROOM_CHARGE);
        this.roomCharge = roomCharge; // Thiết lập phí phòng
    }

    /**
     * Tính toán tổng phí dịch vụ (chưa được cài đặt).
     *
     * @return 0 (chưa có cài đặt).
     */
    private double calcTotalService(){
        return 0; // Chưa có cài đặt
    }

    /**
     * Tính toán thời gian sử dụng (chưa được cài đặt).
     *
     * @return 0 (chưa có cài đặt).
     */
    private double calcTimeUsed(){
        return 0; // Chưa có cài đặt
    }

    /**
     * Trả về chuỗi đại diện cho instance RoomUsage này.
     *
     * @return Chuỗi đại diện cho instance RoomUsage.
     */
    @Override
    public String toString() {
        return "RoomUsage{" +
                "roomUsageID='" + roomUsageID + '\'' +
                ", totalServiceCharge=" + totalServiceCharge +
                ", roomCharge=" + roomCharge +
                '}'; // Trả về chuỗi thông tin về RoomUsage
    }
}
