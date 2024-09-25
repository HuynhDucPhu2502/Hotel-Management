package iuh.fit.models.enums;

/**
 * Enum ShiftDaysSchedule đại diện cho các lịch làm việc theo ca, bao gồm:
 * - MON_WED_FRI: Thứ 2, Thứ 4, Thứ 6 (2, 4, 6)
 * - TUE_THU_SAT: Thứ 3, Thứ 5, Thứ 7 (3, 5, 7)
 * - SUNDAY: Chủ Nhật (CN)
 */
public enum ShiftDaysSchedule {
    MON_WED_FRI("2 4 6"),
    TUE_THU_SAT("3 5 7"),
    SUNDAY("CN");

    // Tên hiển thị của lịch làm việc
    private final String displayName;

    /**
     * Hàm khởi tạo để đặt tên hiển thị cho lịch làm việc.
     *
     * @param displayName Tên hiển thị của lịch làm việc
     */
    ShiftDaysSchedule(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Phương thức trả về tên hiển thị của lịch làm việc.
     *
     * @return Tên hiển thị của lịch làm việc (ví dụ: "2 4 6", "3 5 7", "CN").
     */
    @Override
    public String toString() {
        return this.displayName;
    }
}
