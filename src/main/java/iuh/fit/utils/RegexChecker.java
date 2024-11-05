package iuh.fit.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class RegexChecker {

    /**
     * Phương thức kiểm tra xem chuỗi đầu vào có tuân theo định dạng mã cụ thể hay không.
     *
     * Định dạng hợp lệ sẽ có tiền tố (prefix) và theo sau là dấu '-' cùng 6 ký số.
     * Các tiền tố hợp lệ bao gồm:
     * - SHIFTASSIGN: Ví dụ "SHIFTASSIGN-123456"
     * - EMP: Ví dụ "EMP-123456"
     * - ACC: Ví dụ "ACC-123456"
     * - RF: Ví dụ "RF-123456"
     * - HCI: Ví dụ "HCI-123456"
     * - RC: Ví dụ "RC-123456"
     * - CUS: Ví dụ "CUS-123456"
     * - RU: Ví dụ "RU-123456"
     * - RUS: Ví dụ "RUS-123456"
     * - HCO: Ví dụ "HCO-123456"
     * - SC: Ví dụ "SC-123456"
     * - S: Ví dụ "S-123456"
     *
     * @param prefix Chuỗi tiền tố cần kiểm tra (ví dụ: "EMP", "ACC", "RF", v.v.)
     * @param input Chuỗi đầu vào cần kiểm tra.
     * @return true nếu chuỗi đầu vào tuân theo định dạng "PREFIX-XXXXXX" với 6 ký số sau dấu '-', ngược lại trả về false.
     */
    public static boolean isValidIDFormat(String prefix, String input) {
        // Regex cho định dạng PREFIX-XXXXXX, với X là ký số
        String regex = prefix + "-\\d{6}";
        return input.matches(regex);
    }


    public static boolean isValidName(String input, int minLength, int maxLength) {
        if (!input.matches("[\\p{L} ]+")) {
            return false;
        }

        if (input.length() < minLength || input.length() > maxLength) {
            return false;
        }

        return true;
    }

    /**
     * Phương thức kiểm tra xem chuỗi đầu vào có phải là số điện thoại hợp lệ hay không.
     *
     * Một số điện thoại hợp lệ có độ dài từ 8 đến 11 chữ số.
     *
     * @param input Chuỗi đầu vào cần kiểm tra.
     * @return true nếu chuỗi đầu vào là một số điện thoại hợp lệ (từ 8 đến 11 chữ số), ngược lại trả về false.
     *
     */
    public static boolean isValidPhoneNumber(String input) {
        return input.matches("^0\\d{9}$");
    }

    /**
     * Phương thức kiểm tra xem chuỗi đầu vào có độ dài từ 4 đến 30 ký tự
     * và không chứa các ký tự đặc biệt như !, #, $, %, ^, &, *, (, ), +, =, {}, [], :, ;, ', ", <, >, ?
     *
     * @param input Chuỗi đầu vào cần kiểm tra.
     * @return true nếu chuỗi hợp lệ, ngược lại trả về false.
     */
    public static boolean isValidEmail(String input) {
        // Regex kiểm tra chuỗi với điều kiện đặt ra
        String regex = "^[a-zA-Z0-9_-]{4,30}@(gmail\\.com|yahoo\\.com)$";


        return input.matches(regex);
    }

    /**
     * Phương thức kiểm tra xem CCCD có tuân theo định dạng và quy tắc cụ thể hay không.
     *
     * Định dạng:
     * - Mã thành phố (001-096)
     * - Mã kỷ sinh (0,1,2,3)
     * - 2 số cuối năm sinh
     * - 6 ký số
     *
     * @param idCardNumber Chuỗi số CCCD cần kiểm tra.
     * @return true nếu CCCD hợp lệ, ngược lại trả về false.
     */
    public static boolean isValidIDCardNumber(String idCardNumber) {
        // Regex cơ bản cho định dạng CCCD
        String regex = "^\\d{3}[0-3]\\d{8}$";

        // Kiểm tra định dạng tổng quát
        if (!idCardNumber.matches(regex)) {
            return false;
        }

        // Kiểm tra mã thành phố (001 - 096)
        int cityCode = Integer.parseInt(idCardNumber.substring(0, 3));
        if (cityCode < 1 || cityCode > 96)
            return false;


        // Mã CCCD hợp lệ
        return true;
    }


    /**
     * Phương thức kiểm tra xem ngày sinh có cách ngày hiện tại ít nhất 18 năm không.
     *
     * @param birthDate Ngày sinh cần kiểm tra (dạng LocalDate).
     * @return true nếu ngày sinh cách hiện tại ít nhất 18 năm, ngược lại trả về false.
     */
    public static boolean isValidDOB(LocalDate birthDate) {
        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Tính toán khoảng cách năm giữa ngày sinh và ngày hiện tại
        long yearsBetween = ChronoUnit.YEARS.between(birthDate, currentDate);

        // Kiểm tra xem khoảng cách có ít nhất là 18 năm không
        return yearsBetween >= 18;
    }

    public static boolean isValidUsername(String input, int minLength, int maxLength) {
        // Kiểm tra xem chuỗi có chứa khoảng trắng hoặc ký tự đặc biệt không
        // Regex [a-zA-Z0-9]+ chỉ cho phép các ký tự chữ cái và số
        if (!input.matches("[a-zA-Z0-9]+")) {
            return false; // Nếu chuỗi chứa khoảng trắng hoặc ký tự đặc biệt, trả về false
        }

        // Kiểm tra độ dài chuỗi có nằm trong khoảng từ minLength đến maxLength không
        return input.length() >= minLength && input.length() <= maxLength;
    }

    public static boolean isValidHashPassword(String hashPassword) {
        String regex = "^[a-fA-F0-9]{64}$";
        return hashPassword.matches(regex);
    }

    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-zA-Z\\d!@#$%^&*()]{8,32}$";
        return password.matches(regex);
    }

    public static boolean isValidShiftID(String input, LocalTime endTime) {
        // Xác định AM hoặc PM dựa trên startTime
        String prefix = "SHIFT";
        String timePeriod = endTime.isBefore(LocalTime.NOON) ? "AM" : "PM";

        // Regex kiểm tra định dạng của shiftID
        String regex = "^" + prefix + "-" + timePeriod + "-\\d{4}$";

        return input.matches(regex);
    }

    public static boolean isValidTaxName(String input) {
        // Kiểm tra rỗng
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        // Kiểm tra ký tự đặc biệt
        return input.matches("[a-zA-Z0-9\\s]+");
    }

    public static boolean isValidTaxDateOfCreation(LocalDate input) {
        // Kiểm tra xem input có nhỏ hơn ngày hiện tại không
        if (input == null) {
            return false; // Kiểm tra null
        }
        return input.isBefore(LocalDate.now());
    }

    public static boolean isValidRoomID(String roomID) {
        return roomID.matches("^([TV])\\d{4}$");
    }
}
