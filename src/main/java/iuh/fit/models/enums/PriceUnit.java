package iuh.fit.models.enums;

/**
 * Enum PriceUnit đại diện cho các đơn vị tính giá, bao gồm:
 * - DAY: okay
 * - HOUR: Giờ
 */
public enum PriceUnit {
    DAY("Ngày"), HOUR("Giờ");

    // Tên hiển thị của đơn vị giá
    private final String displayName;

    /**
     * Hàm khởi tạo để đặt tên hiển thị cho đơn vị giá.
     *
     * @param displayName Tên hiển thị của đơn vị giá
     */
    PriceUnit(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Phương thức trả về tên hiển thị của đơn vị giá.
     *
     * @return Tên hiển thị của đơn vị giá (ví dụ: "Ngày", "Giờ").
     */
    @Override
    public String toString() {
        return this.displayName;
    }
}
