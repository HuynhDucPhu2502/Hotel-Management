package iuh.fit.utils;

import iuh.fit.models.enums.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConvertHelper {
    // ==================================================================================================================
    // Database to Application Converter
    // ==================================================================================================================

    public static LocalTime localTimeConverter(Time input) {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        String formattedInput = formatter.format(input);

        if (!formattedInput.matches("((([0-1][0-9]|2[0-2]):[0-5][0-9])|(23:00))"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_LOCALTIME);

        return LocalTime.parse(formattedInput);
    }

    public static LocalDate localDateConverter(Date input) {
        return input.toLocalDate();
    }

    public static LocalDateTime localDateTimeConverter(Timestamp input) {
        return input.toLocalDateTime();
    }

    public static ObjectStatus objectStatusConverter(String input) {
        if (!input.matches("(ACTIVATE|DEACTIVATE)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_OBJECT_STATUS);

        return input.equalsIgnoreCase("ACTIVATE")
                ? ObjectStatus.ACTIVATE : ObjectStatus.DEACTIVATE;
    }

    public static Gender genderConverter(String input) {
        if (!input.matches("(FEMALE|MALE)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_GENDER);

        return input.equalsIgnoreCase("FEMALE")
                ? Gender.FEMALE : Gender.MALE;
    }

    public static Position positionConverter(String input) {
        if (!input.matches("(MANAGER|RECEPTIONIST)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_POSITION);

        return input.equalsIgnoreCase("MANAGER")
                ? Position.MANAGER : Position.RECEPTIONIST;
    }

    public static PriceUnit priceUnitConverter(String input) {
        if (!input.matches("(DAY|HOUR)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_PRICE_UNIT);

        return input.equalsIgnoreCase("DAY")
                ? PriceUnit.DAY : PriceUnit.HOUR;
    }

    public static ShiftDaysSchedule shiftDaysScheduleConverter(String input) {
        return switch (input.toUpperCase()) {
            case "MON_WEB_FRI" -> ShiftDaysSchedule.MON_WED_FRI;
            case "TUE_THU_SAT" -> ShiftDaysSchedule.TUE_THU_SAT;
            case "SUNDAY" -> ShiftDaysSchedule.SUNDAY;
            default -> throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_SHIFT_DAYS_SCHEDULE);
        };
    }

    public static AccountStatus accountStatusConverter(String input) {
        return switch (input.toUpperCase()) {
            case "ACTIVE" -> AccountStatus.ACTIVE;
            case "INACTIVE" -> AccountStatus.INACTIVE;
            case "LOCKED" -> AccountStatus.LOCKED;
            default -> throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_ACCOUNT_STATUS);
        };
    }

    public static RoomStatus roomStatusConverter(String input) {
        return switch (input.toUpperCase()) {
            case "AVAILABLE" -> RoomStatus.AVAILABLE;
            case "ON_USE" -> RoomStatus.ON_USE;
            case "UNAVAILABLE" -> RoomStatus.UNAVAILABLE;
            case "OVERDUE" -> RoomStatus.OVERDUE;
            default -> throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_ROOM_STATUS);
        };
    }

    public static DialogType dialogTypeConverter(String input) {
        return switch (input.toUpperCase()) {
            case "TRANSFER" -> DialogType.TRANSFER;
            case "RESERVATION" -> DialogType.RESERVATION;
            case "CHECKIN" -> DialogType.CHECKIN;
            case "CHECKOUT" -> DialogType.CHECKOUT;
            case "SERVICE" -> DialogType.SERVICE;
            default -> throw new IllegalArgumentException(ErrorMessages.ROOM_DIALOG_TYPE_INVALID);
        };
    }

    // ==================================================================================================================
    // Application to Database Converter
    // ==================================================================================================================

    public static Time localTimeToSQLConverter(LocalTime input) {
        return Time.valueOf(input);
    }

    public static Date localDateToSQLConverter(LocalDate input) {
        return Date.valueOf(input);
    }

    public static Timestamp localDateTimeToSQLConverter(LocalDateTime input) {
        return Timestamp.valueOf(input);
    }

    public static String objectStatusToSQLConverter(ObjectStatus input) {
        if (!input.toString().matches("(Tồn tại|Không tồn tại)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_OBJECT_STATUS);

        return input.toString().equalsIgnoreCase("Tồn tại")
                ? "ACTIVATE" : "DEACTIVATE";
    }

    public static String genderToSQLConverter(Gender input) {
        if (!input.toString().matches("(Nữ|Nam)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_GENDER);

        return input.toString().equalsIgnoreCase("Nữ")
                ? "FEMALE" : "MALE";
    }

    public static String positionConverterToSQL(Position input) {
        if (!input.toString().matches("(Lễ Tân|Quản Lý)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_POSITION);

        return input.toString().equalsIgnoreCase("Quản Lý")
                ? "MANAGER" : "RECEPTIONIST";
    }

    public static String priceUnitToSQLConverter(PriceUnit input) {
        if (!input.toString().matches("(Ngày|Giờ)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_PRICE_UNIT);

        return input.toString().equalsIgnoreCase("Ngày")
                ? "DAY" : "HOUR";
    }

    public static String shiftDaysScheduleToSQLConverter(ShiftDaysSchedule input) {
        return switch (input) {
            case MON_WED_FRI -> "MON_WEB_FRI";
            case TUE_THU_SAT -> "TUE_THU_SAT";
            case SUNDAY -> "SUNDAY";
            default -> throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_SHIFT_DAYS_SCHEDULE);
        };
    }

    public static String currentDaysScheduleToSQLConverter(LocalDateTime dateTime) {
        return switch (dateTime.getDayOfWeek().toString()) {
            case "MONDAY", "WEDNESDAY", "FRIDAY" -> "MON_WEB_FRI";
            case "TUESDAY", "THURSDAY", "SATURDAY" -> "TUE_THU_SAT";
            case "SUNDAY" -> "SUNDAY";
            default -> throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_SHIFT_DAYS_SCHEDULE);
        };
    }

    public static String roomStatusToSQLConverter(RoomStatus input) {
        return switch (input.toString()) {
            case "Phòng trống" -> "AVAILABLE";
            case "Phòng đang sử dụng" -> "ON_USE";
            case "Phòng không được sử dụng" -> "UNAVAILABLE";
            case "Phòng quá hạn sử dụng" -> "OVERDUE";
            default -> throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_ROOM_STATUS);
        };
    }

    public static String dialogTypeToSQLConverter(DialogType input) {
        return switch (input.toString()) {
            case "Chuyển phòng" -> "TRANSFER";
            case "Đặt phòng" -> "RESERVATION";
            case "Check-in" -> "CHECKIN";
            case "Check-out" -> "CHECKOUT";
            case "Thêm dịch vụ" -> "SERVICE";
            default -> throw new IllegalArgumentException(ErrorMessages.ROOM_DIALOG_TYPE_INVALID);
        };
    }

    // ==================================================================================================================
    // Others
    // ==================================================================================================================
    public static double doubleConverter(String numbStr, String errorMessage) {
        try {
            return Double.parseDouble(numbStr);
        } catch (Exception exception) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
