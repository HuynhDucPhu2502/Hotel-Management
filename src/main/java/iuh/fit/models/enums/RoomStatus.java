package iuh.fit.models.enums;

/**
 * Enum RoomStatus đại diện cho trạng thái của phòng khách sạn, bao gồm:
 * - AVAILABLE: Phòng trống
 * - ON_USE: Phòng đang sử dụng
 * - UNAVAILABLE: Phòng không được sử dụng
 */
public enum RoomStatus {
    AVAILABLE("Phòng trống"),
    ON_USE("Phòng đang sử dụng"),
    UNAVAILABLE("Phòng không được sử dụng"),
    OVERDUE("Phòng quá hạn sử dụng");

    // Tên hiển thị cho trạng thái phòng
    private final String displayName;

    /**
     * Hàm khởi tạo để đặt mô tả trạng thái phòng.
     *
     * @param displayName Mô tả trạng thái của phòng khách sạn.
     */
    RoomStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Phương thức trả về mô tả của trạng thái phòng.
     *
     * @return Chuỗi mô tả trạng thái phòng (ví dụ: "Phòng trống", "Phòng đang sử dụng").
     */
    @Override
    public String toString() {
        return displayName;
    }
}
