package iuh.fit.utils;

import java.time.LocalTime;

public class GlobalConstants {
    public static final String SHIFT_PREFIX = "SHIFT";
    public static final LocalTime SHIFT_MAX_TIME = LocalTime.of(23, 0);
    public static final LocalTime SHIFT_MIN_TIME = LocalTime.of(5, 0);
    public static final int SHIFT_MIN_WORK_HOURS = 6;

    public static final String ROOMCATEGORY_PREFIX = "RC";
    public static final String PRICING_PREFIX = "P";
    public static final String EMPLOYEE_PREFIX = "EMP";
    public static final String ACCOUNT_PREFIX = "ACC";
    public static final String CUSTOMER_PREFIX = "CUS";
    public static final String SHIFTASSIGNMENT_PREFIX = "SA";
    public static final String SERVICECATEGORY_PREFIX = "SC";
    public static final String HOTELSERVICE_PREFIX = "HS";
    public static final String ROOM_USAGE_SERVICE_PREFIX = "RUS";
    public static final String RESERVATIONID_PREFIX = "RF";
    public static final String HISTORY_CHECKIN_ID_PREFIX = "HCI";
    public static final String ROOM_RESERVATION_DETAIL_PREFIX = "RRD";
    public static final String HISTORY_CHECKOUT_ID_PREFIX = "HCO";
    public static final String INVOICE_ID_PREFIX = "INV";

    public static final String STAY_LENGTH_EMPTY = "Chưa Đặt Lịch";
    public static final String BOOKING_DEPOSIT_EMPTY = "0 VND";

    public static final String ROOM_BOOKING_ALL_BTN = "Tổng ";
    public static final String ROOM_BOOKING_AVAIL_BTN = "Trống ";
    public static final String ROOM_BOOKING_ON_USE_BTN = "Đang sử dụng ";
    public static final String ROOM_BOOKING_OVER_DUE_BTN = "Quá hạn  ";

    public static final String DEFAULT_AVATAR_BASE64 = "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAABRElEQVRIieXVSU/CQAAF4P7/EyfE4MkAmiBcBEwgokhAIwGUxi40gGA3QAmdtudnW1MMURjpcvLwkqYzed/MNJMy9kpBnGH+EfDxBmsxgr2cOM9ydIClCSBcBaSfA+llvZj9c1hi7QsMA1gav1X8I8952ItxcIDwld3l/m6kmxDAvtX7wKAYL0AGF3HvoBAcMNkCHeDKIQD+ig4MQ3xka9ymAtasEwLQBTpAuWyUmyyDsMXdwMvl3vI/AArWUvP38m4W61E7PEAUAbN6Ckb3u9zoZfBaS8KQuSgAzgGOIZYTHjStH0EsJTC9TnljgQFTFbES76C3M1AbJ1AaaW/Vk2oSym3ae+eOuXPcuVTA1CXnTDt4Z6ub0kOitU6xfCphNWxt7YwxVQHzx/zBhbS4nabzL2HmD7nIyzfI/RmYuMr9xA58As9AE1gjSRApAAAAAElFTkSuQmCC";
}
