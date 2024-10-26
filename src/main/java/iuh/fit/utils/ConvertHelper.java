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

    public static Date dateToSQLConverter(LocalDate input) {
        return Date.valueOf(input);
    }

    public static Timestamp dateTimeToSQLConverter(LocalDateTime input) {
        return Timestamp.valueOf(input);
    }

    public static Time timeConvertertoSQL(LocalTime input) {
        return Time.valueOf(input);
    }

    public static String genderConverterToSQL(Gender input) {
        if (!input.toString().matches("(Nữ|Nam)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_GENDER);

        return input.toString().equalsIgnoreCase("Nữ")
                ? "FEMALE" : "MALE";
    }

    public static String shiftDaysScheduleConverterToSQL(ShiftDaysSchedule input) {
        return switch (input.toString().toUpperCase()) {
            case "2 4 6" -> "MON_WED_FRI";
            case "3 5 7" -> "TUE_THU_SAT";
            case "CN" -> "SUNDAY";
            default -> throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_SHIFT_DAYS_SCHEDULE);
        };
    }

    public static String pricingConverterToSQL(PriceUnit input) {
        if (!input.toString().matches("(Ngày|Giờ)"))
            throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_PRICE_UNIT);

        return input.toString().equalsIgnoreCase("Ngày")
                ? "DAY" : "HOUR";
    }

    public static String roomStatusConverterToSQL(RoomStatus input) {
        return switch (input.toString()) {
            case "Phòng trống" -> "AVAILABLE";
            case "Phòng đang sử dụng" -> "ON_USE";
            case "Phòng không được sử dụng" -> "UNAVAILABLE";
            case "Phòng quá hạn sử dụng" -> "OVERDUE";
            default -> throw new IllegalArgumentException(ErrorMessages.CONVERT_HELPER_INVALID_ROOM_STATUS);
        };
    }

    public static double doubleConverter(String numbStr, String errorMessage) {
        try {
            return Double.parseDouble(numbStr);
        } catch (Exception exception) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
