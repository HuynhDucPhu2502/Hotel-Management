package iuh.fit.models.enums;

/**
 * @author Le Tran Gia Huy
 * @created 07/11/2024 - 1:05 PM
 * @project HotelManagement
 * @package iuh.fit.models.enums
 */
public enum ObjectStatus {
    ACTIVATE("Tồn tại"), DEACTIVATE("Không tồn tại");

    private final String displayName;

    ObjectStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
