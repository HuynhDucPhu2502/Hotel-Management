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

    /**
     * Phương thức isValidName kiểm tra xem chuỗi đầu vào có tuân theo quy tắc về độ dài hay không.
     * <p>
     * Chuỗi đầu vào sẽ được loại bỏ tất cả các khoảng trắng và kiểm tra xem độ dài của chuỗi
     * sau khi loại bỏ có nằm trong khoảng độ dài tối thiểu và tối đa được chỉ định không.
     *
     * @param input Chuỗi đầu vào cần kiểm tra.
     * @param minLength Độ dài tối thiểu của chuỗi (sau khi loại bỏ khoảng trắng).
     * @param maxLength Độ dài tối đa của chuỗi (sau khi loại bỏ khoảng trắng).
     * @return true nếu độ dài của chuỗi sau khi loại bỏ khoảng trắng nằm trong khoảng từ minLength đến maxLength, ngược lại trả về false.
     */
    public static boolean isValidName(String input, int minLength, int maxLength) {
        if (!input.matches("[\\p{L} ]+")) {
            return false; // Không hợp lệ nếu có ký tự số hoặc ký tự đặc biệt
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

    /**
     * Phương thức kiểm tra tính hợp lệ của tên người dùng (username).
     * Username phải có độ dài trong khoảng cho phép và không chứa khoảng trắng
     * hoặc ký tự đặc biệt (chỉ cho phép chữ cái và số).
     *
     * @param input Tên người dùng cần kiểm tra.
     * @param minLength Độ dài tối thiểu cho tên người dùng.
     * @param maxLength Độ dài tối đa cho tên người dùng.
     * @return true nếu tên người dùng hợp lệ (chỉ chứa chữ cái và số, và độ dài nằm trong khoảng cho phép),
     *         ngược lại trả về false.
     */
    public static boolean isValidUsername(String input, int minLength, int maxLength) {
        // Kiểm tra xem chuỗi có chứa khoảng trắng hoặc ký tự đặc biệt không
        // Regex [a-zA-Z0-9]+ chỉ cho phép các ký tự chữ cái và số
        if (!input.matches("[a-zA-Z0-9]+")) {
            return false; // Nếu chuỗi chứa khoảng trắng hoặc ký tự đặc biệt, trả về false
        }

        // Kiểm tra độ dài chuỗi có nằm trong khoảng từ minLength đến maxLength không
        return input.length() >= minLength && input.length() <= maxLength;
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

    /**
     * Phương thức kiểm tra mã ca (shiftID) có đúng định dạng hay không.
     *
     * Quy tắc mã ca:
     * - Bắt đầu với chuỗi "SHIFT".
     * - Sau đó là ký hiệu thời gian "AM" hoặc "PM" tùy thuộc vào thời gian bắt đầu (startTime).
     * - Kết thúc bằng chuỗi gồm 4 chữ số.
     * Ví dụ hợp lệ: SHIFT-AM-0001, SHIFT-PM-1234.
     *
     * @param input Mã ca (shiftID) cần kiểm tra.
     * @param endTime Thời gian bắt đầu để xác định mã thời gian là AM hoặc PM.
     * @return true nếu mã ca hợp lệ theo quy tắc trên, false nếu không.
     */
    public static boolean isValidShiftID(String input, LocalTime endTime) {
        // Xác định AM hoặc PM dựa trên startTime
        String prefix = "SHIFT";
        String timePeriod = endTime.isBefore(LocalTime.NOON) ? "AM" : "PM";

        // Regex kiểm tra định dạng của shiftID
        String regex = "^" + prefix + "-" + timePeriod + "-\\d{4}$";

        return input.matches(regex);
    }
  
    /**
     * Kiểm tra tính hợp lệ của tên thuế.
     * 
     * @param input Tên thuế cần kiểm tra
     * @return true nếu tên thuế hợp lệ, false nếu không hợp lệ
     *         (không rỗng, không chứa khoảng trắng và không có ký tự đặc biệt)
     */
    public static boolean isValidTaxName(String input) {
        // Kiểm tra rỗng
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        // Kiểm tra ký tự đặc biệt
        return input.matches("[a-zA-Z0-9\\s]+");
    }

    /**
     * Kiểm tra tính hợp lệ của ngày tạo thuế.
     *
     * @param input Ngày tạo thuế cần kiểm tra
     * @return true nếu ngày tạo thuế nhỏ hơn ngày hiện tại, false nếu không hợp lệ
     *         (hoặc nếu input là null)
     */
    public static boolean isValidTaxDateOfCreation(LocalDate input) {
        // Kiểm tra xem input có nhỏ hơn ngày hiện tại không
        if (input == null) {
            return false; // Kiểm tra null
        }
        return input.isBefore(LocalDate.now());
    }

    /**
     * Kiểm tra tính hợp lệ của mã phòng theo định dạng:
     * X: "T" hoặc "V", ZZZZ: Số tầng (4 chữ số), TT: Số thứ tự (01-99)
     *
     * @param roomID Mã phòng cần kiểm tra
     * @return true nếu roomID hợp lệ theo định dạng "^([TV])\\d{4}\\d{2}$", ngược lại là false
     */
    public static boolean isValidRoomID(String roomID) {
        return roomID.matches("^([TV])\\d{4}$");
    }
}
