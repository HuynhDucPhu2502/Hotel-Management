package iuh.fit.utils;

public class ErrorMessages {
    // Employee
    public static final String EMP_INVALID_ID = "Mã nhân viên phải theo định dạng EMP-XXXXXX, với X là ký số";
    public static final String EMP_INVALID_FULLNAME = "Tên nhân viên phải từ 2 đến 50 ký tự";
    public static final String EMP_INVALID_PHONENUMBER = "Số điện thoại từ 8 đến 11 ký số";
    public static final String EMP_INVALID_ADDRESS = "Email phải từ 4 đến 30 ký tự và không cứ ký tự đặc biệt";
    public static final String EMP_INVALID_DOB = "Ngày sinh không hợp lý, tuổi ít nhât từ 18 trở lên";

    // Account
    public static final String ACC_INVALID_ID = "Mã tài khoản phải theo định dạng ACC-XXXXXX, với X là ký số";
    public static final String ACC_INVALID_USERNAME = "Tên đăng nhập phải có ít nhất 5 ký tự và không vượt quá 20 ký tự";
    public static final String ACC_INVALID_PASSWORD = "Mật khẩu phải từ 8 đến 30 ký tự\nCó ít nhất một chữ cái, một chữ số và một kí tự đặc biệt như !@#$%^&*()";

    // Pricing
    public static final String PRICING_INVALID_PRICE = "Số tiền không được rỗng, phải lớn hơn 0";
    public static final String PRICING_INVALID_ID = "Mã giá phải theo định dạng P-XXXXXX, với X là ký số";

}
