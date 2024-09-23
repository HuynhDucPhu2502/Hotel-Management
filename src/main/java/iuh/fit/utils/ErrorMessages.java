package iuh.fit.utils;

public class ErrorMessages {
    //Global ErrorMessages
    public static final String INVALID_PHONENUMBER = "Số điện thoại phải có 10 ký số";
    public static final String INVALID_ADDRESS = "Địa chỉ không được rỗng";
    public static final String INVALID_EMAIL = "Email phải từ 4 đến 30 ký tự và không cứ ký tự đặc biệt";
    public static final String INVALID_CCCD = "CCCD phải theo mẫu: XXXYZZTTTTTT\n" +
            "XXXX: Mã thành phố từ 001 đến 096\n" +
            "Y: mã kỷ sinh\n" +
            "Thế kỷ 20 (từ năm 1900 đến hết năm 1999): Nam 0, nữ 1;\n" +
            "Thế kỷ 21 (từ năm 2000 đến hết năm 2099): Nam 2, nữ 3;\n" +
            "ZZ: 2 số cuối năm sinh\n" +
            "TTTTTT: 6 ký số\n";

    // Employee
    public static final String EMP_INVALID_ID = "Mã nhân viên phải theo định dạng EMP-XXXXXX, với X là ký số";
    public static final String EMP_INVALID_FULLNAME = "Tên nhân viên phải từ 2 đến 50 ký tự";
    public static final String EMP_INVALID_PHONENUMBER = "Số điện thoại phải có 10 ký số";
    public static final String EMP_INVALID_ADDRESS = "Email phải từ 4 đến 30 ký tự và không cứ ký tự đặc biệt";
    public static final String EMP_INVALID_DOB = "Ngày sinh không hợp lý, tuổi ít nhât từ 18 trở lên";

    // Account
    public static final String ACC_INVALID_ID = "Mã tài khoản phải theo định dạng ACC-XXXXXX, với X là ký số";
    public static final String ACC_INVALID_USERNAME = "Tên đăng nhập phải có ít nhất 5 ký tự và không vượt quá 20 ký tự";
    public static final String ACC_INVALID_PASSWORD = "Mật khẩu phải từ 8 đến 30 ký tự\nCó ít nhất một chữ cái, một chữ số và một kí tự đặc biệt như !@#$%^&*()";

    // Pricing
    public static final String PRICING_INVALID_PRICE = "Số tiền không được rỗng, phải lớn hơn 0";
    public static final String PRICING_INVALID_ID = "Mã giá phải theo định dạng P-XXXXXX, với X là ký số";

    // Room category
    public static final String ROOM_CATEGORY_INVALID_ID_ISNULL = "Mã loại phòng không được rỗng!!!";
    public static final String ROOM_CATEGORY_INVALID_ID_FORMAT = "Mã loại phòng phải theo định dạng RC-XXXXXX, với XXXXXX là dãy số!!!";
    public static final String ROOM_CATEGORY_INVALID_NAME_ISNULL = "Tên loại phòng không được rỗng!!!";
    public static final String ROOM_CATEGORY_INVALID_NAME_OUTBOUND_OFLENGTH = "Tên loại phòng không được quá 30 kí tự!!!";
    public static final String ROOM_CATEGORY_INVALID_NUMOFBED_NAN = "Số giường phải là chữ số và lớn hơn 0!!!";

    // Shift
    public static final String SHIFT_INVALID_STARTTIME = "Thời gian bắt đầu ca phải sau 5h sáng và trước thời gian kết thúc ca.";
    public static final String SHIFT_INVALID_ENDTIME = "Thời gian kết thúc ca phải trước 23h đêm và sau thời gian bắt đầu ca.";
    public static final String SHIFT_INVALID_MODIFIEDDATE = "Thời gian cập nhật ca không được lớn hơn ngày hiện tại.";
    public static final String SHIFT_INVALID_SHIFTID = "Mã ca theo mẫu SHIFT-XX-YYYY. Với X là AM/PM và Y là ký số.";
    public static final String SHIFT_NULL_STARTTIME = "Thời gian bắt đầu ca đang trống";
    public static final String SHIFT_NULL_ENDTIME = "Thời gian kết thúc ca đang trống";
    public static final String SHIFT_INVALID_WORKHOURS = "Thời gian trên mỗi ca làm ít nhất 6 tiếng";

    public static final String CUS_INVALID_ID = "Mã khách hàng phải theo định dạng CUS-XXXXXX, với X là ký số";
    public static final String CUS_INVALID_PHONENUMBER = "Số điện thoại phải có 10 ký số";
    public static final String CUS_INVALID_EMAIL = "Email phải từ 4 đến 30 ký tự và không cứ ký tự đặc biệt";
}
