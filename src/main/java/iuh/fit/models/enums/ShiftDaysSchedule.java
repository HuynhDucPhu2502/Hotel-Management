package iuh.fit.models.enums;

public enum ShiftDaysSchedule {
    MON_WED_FRI("Thứ 2, 4, 6"),
    TUE_THU_SAT("Thứ 3, 5, 7"),
    SUNDAY("Chủ nhật");

    private final String displayName;

    ShiftDaysSchedule(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
