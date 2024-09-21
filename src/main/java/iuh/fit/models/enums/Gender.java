package iuh.fit.models.enums;


/**
 * Enum Gender đại diện cho giới tính của người dùng, bao gồm:
 * - MALE: Nam
 * - FEMALE: Nữ
 */
public enum Gender {
    MALE("Nam"), FEMALE("Nữ");

    // Tên hiển thị của giới tính
    private final String displayName;

    /**
     * Hàm khởi tạo để đặt tên hiển thị cho giới tính.
     *
     * @param displayName Tên hiển thị của giới tính
     */
    Gender(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Phương thức trả về tên hiển thị của giới tính.
     *
     * @return Tên hiển thị của giới tính (ví dụ: "Nam", "Nữ").
     */
    @Override
    public String toString() {
        return this.displayName;
    }
}