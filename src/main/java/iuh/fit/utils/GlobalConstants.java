package iuh.fit.utils;

import java.time.LocalTime;

public class GlobalConstants {
    public static final LocalTime SHIFT_MAX_TIME = LocalTime.of(23, 0);
    public static final LocalTime SHIFT_MIN_TIME = LocalTime.of(5, 0);
    public static final int SHIFT_MIN_WORK_HOURS = 6;

    public static final String ROOMCATEGORY_PREFIX = "RC";
    public static final String PRICING_PREFIX = "P";
    public static final String EMPLOYEE_PREFIX = "EMP";
    public static final String ACCOUNT_PREFIX = "ACC";
    public static final String CUSTOMER_PREFIX = "CUS";

    public static final String SERVICECATEGORY_PREFIX = "SC";
    public static final String HOTELSERVICE_PREFIX = "HS";
    public static final String ROOMUSAGESERVICE_PREFIX = "RUS";

    public static final String RESERVATIONID_PREFIX = "RF";
    public static final String HISTORY_CHEKCIN_ID_PREFIX = "HCI";

    public static final String ROOMUSAGE_PREFIX = "RU";
}
