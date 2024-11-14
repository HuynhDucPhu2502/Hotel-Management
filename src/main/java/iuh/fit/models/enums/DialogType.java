package iuh.fit.models.enums;

public enum DialogType {
    TRANSFER("Chuyển phòng"),
    RESERVATION("Đặt phòng"),
    CHECKIN("Check-in"),
    CHECKOUT("Check-out"),
    SERVICE("Thêm dịch vụ");

    private final String displayName;

    DialogType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
