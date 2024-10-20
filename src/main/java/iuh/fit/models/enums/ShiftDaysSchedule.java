package iuh.fit.models.enums;

public enum ShiftDaysSchedule {
    MON_WED_FRI("2 4 6"),
    TUE_THU_SAT("3 5 7"),
    SUNDAY("CN");

    private final String displayName;

    ShiftDaysSchedule(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
