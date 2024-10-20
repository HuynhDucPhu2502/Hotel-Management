package iuh.fit.models.enums;

public enum RoomStatus {
    AVAILABLE("Phòng trống"),
    ON_USE("Phòng đang sử dụng"),
    UNAVAILABLE("Phòng không được sử dụng"),
    OVERDUE("Phòng quá hạn sử dụng");

    private final String displayName;

    RoomStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
