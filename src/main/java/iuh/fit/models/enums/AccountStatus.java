package iuh.fit.models.enums;

/**
 * Enum AccountStatus đại diện cho trạng thái của tài khoản, bao gồm:
 * - ACTIVE: Tài khoản đang kích hoạt
 * - INACTIVE: Tài khoản không kích hoạt
 * - LOCKED: Tài khoản bị khóa
 */
public enum AccountStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    LOCKED("Locked");

    // Tên hiển thị cho trạng thái tài khoản
    private final String displayName;

    /**
     * Hàm khởi tạo để đặt tên hiển thị cho trạng thái tài khoản.
     *
     * @param displayName Tên hiển thị của trạng thái tài khoản (ví dụ: "Kích hoạt", "Không kích hoạt", "Bị khóa").
     */
    AccountStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Phương thức trả về tên hiển thị của trạng thái tài khoản.
     *
     * @return Tên hiển thị của trạng thái tài khoản.
     */
    @Override
    public String toString() {
        return this.displayName;
    }
}

