package iuh.fit.models.enums;

/**
 * Enum Position đại diện cho các vị trí công việc trong công ty, bao gồm:
 * - RECEPTIONIST: Lễ tân
 * - MANAGER: Quản lý
 */
public enum Position {
    RECEPTIONIST("Lễ Tân"),
    MANAGER("Quản Lý");

    // Tên hiển thị của vị trí công việc
    private final String displayName;

    /**
     * Hàm khởi tạo để đặt tên hiển thị cho vị trí công việc.
     *
     * @param displayName Tên hiển thị của vị trí công việc (ví dụ: "Lễ Tân", "Quản lý").
     */
    Position(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Phương thức trả về tên hiển thị của vị trí công việc.
     *
     * @return Tên hiển thị của vị trí công việc.
     */
    @Override
    public String toString() {
        return displayName;
    }
}
