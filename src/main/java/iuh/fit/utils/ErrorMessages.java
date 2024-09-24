package iuh.fit.utils;

public class ErrorMessages {
    //Global ErrorMessages
    public static final String INVALID_PHONENUMBER = "Số điện thoại phải có 10 chữ số và chữ số đầu tiên là số 0";
    public static final String INVALID_ADDRESS = "Địa chỉ không được rỗng";
    public static final String INVALID_EMAIL = "Email phải từ 4 đến 30 ký tự và không cứ ký tự đặc biệt";
    public static final String INVALID_CCCD = "CCCD phải gồm 12 chữ số";

    // Employee
    public static final String EMP_INVALID_ID = "Mã nhân viên phải theo định dạng EMP-XXXXXX, với X là ký số";
    public static final String EMP_INVALID_FULLNAME = "Tên nhân viên phải từ 2 đến 50 ký tự";
    public static final String EMP_INVALID_DOB = "Ngày sinh không hợp lý, tuổi ít nhât từ 18 trở lên";

    // Account
    public static final String ACC_INVALID_ID = "Mã tài khoản phải theo định dạng ACC-XXXXXX, với X là ký số";
    public static final String ACC_INVALID_USERNAME = "Tên đăng nhập phải có ít nhất 5 ký tự và không vượt quá 20 ký tự";
    public static final String ACC_INVALID_PASSWORD = "Mật khẩu phải từ 8 đến 30 ký tự\nCó ít nhất một chữ cái, một chữ số và một kí tự đặc biệt như !@#$%^&*()";

    // Pricing
    public static final String PRICING_INVALID_PRICE = "Số tiền không được rỗng, phải lớn hơn 0";
    public static final String PRICING_INVALID_ID = "Mã giá phải theo định dạng P-XXXXXX, với X là ký số";
    public static final String PRICING_INVALID_ROOMCATEGORY_ISNULL = "Loại phòng không được rỗng";
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

    // ShiftAssignment
    public static final String SHIFTASSIGNMENT_INVALID_ID = "Mã phân công ca làm việc phải theo định dạng SA-XXXXXX, với X là ký số";
    public static final String SHIFTASSIGNMENT_INVALID_DESCRIPTION = "Mô tả không được để trống";
    public static final String SHIFTASSIGNMENT_INVALID_EMPLOYEE_ISNULL = "Nhân viên không được để trống";
    public static final String SHIFTASSIGNMENT_INVALID_SHIFT_ISNULL = "Ca làm không được để trống";

    //Customer
    public static final String CUS_INVALID_ID = "Mã khách hàng phải theo định dạng CUS-XXXXXX, với X là ký số";
    public static final String CUS_INVALID_FULLNAME = "Tên Khách hàng phải từ 3 đến 30 ký tự";
    
    //Tax
    public static final String TAX_INVALID_TAXNAME = "Tên thuế không được trùng, không rỗng và không chưa khoảng trắng";
    public static final String TAX_INVALID_TAXRATE = "Hệ số thuế phải là số dương";
    public static final String TAX_INVALID_TAXDATEOFCREATION = "Ngày gian thêm thuế phải trước ngày hiện tại";

    // Room
    public static final String ROOM_INVALID_ID_ISNULL = "Mã phòng không được rỗng!!!";
    public static final String ROOM_INVALID_ID_FORMAT = "Mã phòng phải theo cấu trúc : XYZZ, gồm:\nX: \"T\" (phòng thường) hoặc \"V\" (phòng VIP), Y: 1 hoặc 2 (số giường), ZZ: số thứ tự phòng từ 01 đến 99.";
    public static final String ROOM_INVALID_ROOMSTATUS_ISNULL = "Trạng thái phòng không được rỗng!!!";
    public static final String ROOM_INVALID_ROOMSTATUS_TYPES = "Trạng thái phòng phải là một trong các giá trị AVAILABLE, ON_USE,  UNAVAILABLE!!!";
    public static final String ROOM_INVALID_DATEOFCREATION = "Ngày tạo phải trước ngày giờ hiện tại!!!";
    public static final String ROOM_INVALID_ROOMCATEGORY_ISNULL = "Loại phòng không được rỗng!!!";

    //ServiceCategory
    public static final String SERVICE_CATEGORY_INVALID_ID_ISNULL = "Mã loại dịch vụ không được rỗng!!!";
    public static final String SERVICE_CATEGORY_INVALID_ID_FORMAT = "Mã loại dịch vụ phải theo định dạng SC-XXXXXX, với XXXXXX là dãy số!!!";
    public static final String SERVICE_CATEGORY_INVALID_NAME_ISNULL = "Tên loại dịch vụ không được rỗng!!!";

    //HotelService
    public static final String HOTEL_SERVICE_INVALID_ID_ISNULL = "Mã dịch vụ không được rỗng!!!";
    public static final String HOTEL_SERVICE_INVALID_ID_FORMAT = "Mã dịch vụ phải theo định dạng HS-XXXXXX, với XXXXXX là dãy số!!!";
    public static final String HOTEL_SERVICE_INVALID_NAME_ISNULL = "Tên dịch vụ không được rỗng!!!";
    public static final String HOTEL_SERVICE_DESCRIPTION_ISNULL = "Mô tả dịch vụ không được rỗng!!!";
    public static final String HOTEL_SERVICE_INVALID_PRICE = "Giá dịch vụ không được rỗng, phải lớn hơn 0!!!";
    public static final String HOTEL_SERVICE_INVALID_SERVICECATEGORY_ISNULL = "Loại dịch vụ không được rỗng!!!";

    //RoomUsageService
    public static final String ROOM_USAGE_SERVICE_INVALID_ID_ISNULL = "Mã sử dụng dịch vụ phòng không được rỗng!!!";
    public static final String ROOM_USAGE_SERVICE_INVALID_ID_FORMAT = "Mã sử dụng dịch vụ phòng phải theo định dạng RUS-XXXXXX, với XXXXXX là dãy số!!!";
    public static final String ROOM_USAGE_SERVICE_INVALID_QUANTITY = "Số lượng dịch vụ phải lớn hơn 0!!!";
    public static final String ROOM_USAGE_SERVICE_INVALID_HOTELSERVICE_ISNULL = "Dịch vụ không được rỗng!!!";

    // ReservationForm
    public static final String RESERVATION_FORM_INVALID_ID_ISNULL = "Mã phiếu đặt phòng không được rỗng!!!";
    public static final String RESERVATION_FORM_INVALID_ID_FORMAT = "Mã phiếu đặt phòng phải theo định dạng RF-XXXXXX!!!";
    public static final String RESERVATION_FORM_INVALID_RESERVATION_DATE_ISNULL = "Ngày đặt phòng không được rỗng!!!";
    public static final String RESERVATION_FORM_INVALID_RESERVATION_DATE = "Ngày đặt phòng phải trước thời điểm hiện tại!!!";
    public static final String RESERVATION_FORM_INVALID_APPROX_CHECKIN_DATE_ISNULL = "Ngày nhận phòng dự kiến không được rỗng!!!";
    public static final String RESERVATION_FORM_INVALID_APPROX_CHECKIN_DATE = "Ngày nhận phòng dự kiến phải lớn hơn ngày đặt phòng!!!";
    public static final String RESERVATION_FORM_INVALID_APPROX_CHECKOUT_DATE_ISNULL = "Ngày trả phòng dự kiến không được rỗng!!!";
    public static final String RESERVATION_FORM_INVALID_APPROX_CHECKOUT_DATE = "Ngày trả phòng dự kiến phải lớn hơn ngày nhận phòng dự kiến!!!";
    public static final String RESERVATION_FORM_INVALID_EMPLOYEE_ISNULL = "Thông tin nhân viên không được rỗng!!!";
    public static final String RESERVATION_FORM_INVALID_CUSTOMER_ISNULL = "Thông tin nhân viên không được rỗng!!!";
    public static final String RESERVATION_FORM_INVALID_ROOM_ISNULL = "Thông phòng không được rỗng!!!";

    // HistoryCheckIn
    public static final String HISTORY_CHECKIN_IVALID_ID_ISNULL = "Mã phiếu nhận phòng không được rỗng!!!";
    public static final String HISTORY_CHECKIN_IVALID_ID_FORMAT = "Mã ngày nhận phòng phải theo định dạng HCI-XXXXXX";
    public static final String HISTORY_CHECKIN_IVALID_CHECKIN_DATE_ISNULL = "Ngày nhận phòng không được rỗng!!!";
    public static final String HISTORY_CHECKIN_IVALID_CHECKIN_DATE = "Ngày nhận phòng phải sau ngày nhận phòng dự kiến";
    public static final String HISTORY_CHECKIN_IVALID_RESERVATION = "Thông tin phiếu đặt phòng không được rỗng!!!";
    //RoomUsage
    public static final String ROOM_USAGE_INVALID_ID_ISNULL = "Mã sử dụng phòng không được rỗng";
    public static final String ROOM_USAGE_INVALID_ID_FORMAT = "Mã sử dụng phòng phải theo định dạng RU-XXXXXX, với XXXXXX là dãy số";
    public static final String ROOM_USAGE_INVALID_TOTAL_SERVICE_CHARGE = "Tổng giá tiền dịch vụ phải lớn hơn 0";
    public static final String ROOM_USAGE_INVALID_ROOM_CHARGE = "Tiền phòng phải lớn hơn 0";
}
