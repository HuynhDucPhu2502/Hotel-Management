package iuh.fit.utils;

import java.time.LocalDate;
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

    /**
     * Phương thức simpleNameChecker kiểm tra xem chuỗi đầu vào có tuân theo quy tắc về độ dài hay không.
     * <p>
     * Chuỗi đầu vào sẽ được loại bỏ tất cả các khoảng trắng và kiểm tra xem độ dài của chuỗi
     * sau khi loại bỏ có nằm trong khoảng độ dài tối thiểu và tối đa được chỉ định không.
     *
     * @param input Chuỗi đầu vào cần kiểm tra.
     * @param minLength Độ dài tối thiểu của chuỗi (sau khi loại bỏ khoảng trắng).
     * @param maxLength Độ dài tối đa của chuỗi (sau khi loại bỏ khoảng trắng).
     * @return true nếu độ dài của chuỗi sau khi loại bỏ khoảng trắng nằm trong khoảng từ minLength đến maxLength, ngược lại trả về false.
     */
    public static boolean isValidNameLength(String input, int minLength, int maxLength) {
        // Loại bỏ tất cả khoảng trắng trong chuỗi
        String trimmedInput = input.replaceAll("\\s+", "");

        // Kiểm tra độ dài chuỗi sau khi loại bỏ khoảng trắng có nằm trong khoảng minLength và maxLength không
        return trimmedInput.length() >= minLength && trimmedInput.length() <= maxLength;
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
        return input.matches("\\d{8,11}");
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
        String regex = "^[a-zA-Z0-9_-]{4,30}$";
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
     * @param cccd Chuỗi số CCCD cần kiểm tra.
     * @return true nếu CCCD hợp lệ, ngược lại trả về false.
     */
    public static boolean isValidCCCD(String cccd) {
        // Regex cơ bản cho định dạng CCCD
        String regex = "^\\d{3}[0-3]\\d{8}$";

        // Kiểm tra định dạng tổng quát
        if (!cccd.matches(regex)) {
            return false;
        }

        // Kiểm tra mã thành phố (001 - 096)
        int cityCode = Integer.parseInt(cccd.substring(0, 3));
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

    /**
     * Phương thức kiểm tra xem mật khẩu có đúng theo mẫu cho sẵn không.
     *
     * @param password Mật khẩu cần kiểm tra (dạng String).
     * @return true nếu mật khẩu có từ 8 đến 30 ký tự, có ít nhất 1 ký tự, 1 ký ố và 1 ký tự đặc biệt như !@#$%^&*().
     */
    public static boolean isValidPassword(String password){
        String regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[a-zA-Z\\d!@#$%^&*()]{8,30}$";

        return password.matches(regex);
    }


}
