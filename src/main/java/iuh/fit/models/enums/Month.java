package iuh.fit.models.enums;

public enum Month {
    JANUARY("Tháng 1"),
    FEBRUARY("Tháng 2"),
    MARCH("Tháng 3"),
    APRIL("Tháng 4"),
    MAY("Tháng 5"),
    JUNE("Tháng 6"),
    JULY("Tháng 7"),
    AUGUST("Tháng 8"),
    SEPTEMBER("Tháng 9"),
    OCTOBER("Tháng 10"),
    NOVEMBER("Tháng 11"),
    DECEMBER("Tháng 12");

    private final String name;

    Month(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

