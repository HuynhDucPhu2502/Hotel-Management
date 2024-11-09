package iuh.fit.utils;

public class ErrorMessages {
    // Global
    public static final String INVALID_PHONENUMBER = "Số điện thoại phải có 10 chữ số và chữ số đầu tiên là số 0";
    public static final String INVALID_ADDRESS = "Địa chỉ không được rỗng";
    public static final String INVALID_EMAIL = "Email phải từ 4 đến 30 ký tự và không cứ ký tự đặc biệt. Kết thúc phải là @gmail.com hoặc @yahoo.com";
    public static final String INVALID_ID_CARD_NUMBER = "CCCD có cấu trúc XYZ. X là chuỗi 3 chữ số, Y là 1 chữ số thuộc khoảng từ 1 đến 3, và Z là chuỗi 8 chữ số.";

    public static final String NULL_ROOMCATEGORY = "Loại phòng không được trống";
    public static final String NULL_EMPLOYEE = "Nhân viên không được để trống";
    public static final String NULL_SHIFT = "Ca làm không được để trống";
    public static final String NULL_SERVICECATEGORY = "Loại dịch vụ không được để trống";
    public static final String NULL_HOTELSERVICE = "Dịch vụ không được để trống";
    public static final String NULL_CUSTOMER = "Khách hàng không được để trống";
    public static final String NULL_ROOM = "Phòng không được để trống";
    public static final String NULL_RESERVATIONFORM = "Phiếu đặt phòng không được để trống";
    public static final String NULL_INVOICE = "Hóa đơn không được để trống";

    // Employee
    public static final String EMP_INVALID_ID = "Mã nhân viên phải theo định dạng EMP-XXXXXX, với X là ký số";
    public static final String EMP_INVALID_FULLNAME = "Tên nhân viên phải từ 2 đến 50 ký tự";
    public static final String EMP_INVALID_DOB = "Ngày sinh không hợp lý, tuổi ít nhât từ 18 trở lên";

    // Account
    public static final String ACC_INVALID_ID = "Mã tài khoản phải theo định dạng ACC-XXXXXX, với X là ký số";
    public static final String ACC_INVALID_USERNAME = "Tên đăng nhập phải có ít nhất 5 ký tự và không vượt quá 30 ký tự";
    public static final String ACC_INVALID_PASSWORD = "Mật khẩu phải từ 8 đến 30 ký tự\nCó ít nhất một chữ cái, một chữ số và một kí tự đặc biệt như !@#$%^&*()";

    // Pricing
    public static final String PRICING_INVALID_PRICE = "Giá loại phòng không được rỗng, phải lớn hơn 0";
    public static final String PRICING_INVALID_FORMAT = "Giá loại phòng phải là một con số";
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
    public static final String SHIFT_NULL_WORKHOURS = "Số giờ làm đang trống";
    public static final String SHIFT_INVALID_WORKHOURS = "Thời gian trên mỗi ca làm ít nhất 6 tiếng";

    // Shift Assignment
    public static final String SHIFTASSIGNMENT_INVALID_ID = "Mã phân công ca làm việc phải theo định dạng SA-XXXXXX, với X là ký số";
    public static final String SHIFTASSIGNMENT_INVALID_DESCRIPTION = "Mô tả không được để trống";

    // Customer
    public static final String CUS_INVALID_ID = "Mã khách hàng phải theo định dạng CUS-XXXXXX, với X là ký số";
    public static final String CUS_INVALID_FULLNAME = "Tên Khách hàng phải từ 3 đến 30 ký tự";

    // Tax
    public static final String TAX_INVALID_TAXNAME = "Tên thuế không được trùng, không rỗng và không chưa khoảng trắng";
    public static final String TAX_INVALID_TAXRATE = "Hệ số thuế phải là số dương";
    public static final String TAX_INVALID_TAXDATEOFCREATION = "Ngày gian thêm thuế phải trước ngày hiện tại";

    // Room
    public static final String ROOM_INVALID_ID_ISNULL = "Mã phòng không được rỗng!!!";
    public static final String ROOM_INVALID_ID_FORMAT = "Mã phòng phải theo cấu trúc : XYZTT, gồm:\nX: \"T\" (phòng thường) hoặc \"V\" (phòng VIP), Y: 1 hoặc 2 (số giường), Z số tầng 1-5, TT: số thứ tự phòng từ 01 đến 99.";
    public static final String ROOM_INVALID_ROOMSTATUS_ISNULL = "Trạng thái phòng không được rỗng!!!";
    public static final String ROOM_INVALID_ROOMSTATUS_TYPES = "Trạng thái phòng phải là một trong các giá trị AVAILABLE, ON_USE,  UNAVAILABLE!!!";
    public static final String ROOM_INVALID_DATEOFCREATION = "Ngày tạo phải trước ngày giờ hiện tại!!!";

    // Service Category
    public static final String SERVICE_CATEGORY_INVALID_ID_ISNULL = "Mã loại dịch vụ không được rỗng";
    public static final String SERVICE_CATEGORY_INVALID_ID_FORMAT = "Mã loại dịch vụ phải theo định dạng SC-XXXXXX, với X là số";
    public static final String SERVICE_CATEGORY_INVALID_NAME = "Tên loại dịch vụ phải từ 3 đến 30 ký tự. Không ký tự đặc biệt và ký số";

    //Hotel Service
    public static final String HOTEL_SERVICE_INVALID_ID_ISNULL = "Mã dịch vụ không được rỗng";
    public static final String HOTEL_SERVICE_INVALID_ID_FORMAT = "Mã dịch vụ phải theo định dạng HS-XXXXXX, với X là dãy số";
    public static final String HOTEL_SERVICE_INVALID_NAME_ISNULL = "Tên dịch vụ không được rỗng";
    public static final String HOTEL_SERVICE_DESCRIPTION_ISNULL = "Mô tả dịch vụ không được rỗng";
    public static final String HOTEL_SERVICE_INVALID_FORMAT = "Giá dịch vụ phải là một con số";
    public static final String HOTEL_SERVICE_INVALID_PRICE = "Giá dịch vụ không được rỗng, phải lớn hơn 0";

    // Room Usage Service
    public static final String ROOM_USAGE_SERVICE_INVALID_ID_ISNULL = "Mã sử dụng dịch vụ phòng không được rỗng!!!";
    public static final String ROOM_USAGE_SERVICE_INVALID_ID_FORMAT = "Mã sử dụng dịch vụ phòng phải theo định dạng RUS-XXXXXX, với XXXXXX là dãy số!!!";
    public static final String ROOM_USAGE_SERVICE_INVALID_QUANTITY = "Số lượng dịch vụ phải lớn hơn 0!!!";
    public static final String ROOM_USAGE_SERVICE_INVALID_UNIT_PRICE = "Đơn giá dịch vụ phải là một con số lớn hơn 0";


    // ReservationForm
    public static final String RESERVATION_FORM_INVALID_ID_ISNULL = "Mã phiếu đặt phòng không được rỗng";
    public static final String RESERVATION_FORM_INVALID_ID_FORMAT = "Mã phiếu đặt phòng phải theo định dạng RF-XXXXXX";
    public static final String RESERVATION_FORM_INVALID_RESERVATION_DATE_ISNULL = "Ngày đặt phòng không được rỗng";
    public static final String RESERVATION_FORM_INVALID_CHECKIN_DATE_ISNULL = "Ngày nhận phòng dự kiến không được rỗng";
    public static final String RESERVATION_FORM_INVALID_CHECKOUT_DATE_ISNULL = "Ngày trả phòng dự kiến không được rỗng";
    public static final String RESERVATION_FORM_INVALID_ROOM_BOOKING_DEPOSIT_AMOUNT = "Tiền đặt cọc phải lớn hơn 0";
    public static final String RESERVATION_FORM_INVALID_CHECKIN_DATE = "Ngày nhận phòng không thể trước ngày đặt phòng.";
    public static final String RESERVATION_FORM_INVALID_CHECKOUT_DATE = "Ngày trả phòng nhỏ hơn ngày nhận phòng.";
    public static final String RESERVATION_FORM_STAY_LENGTH_INVALID = "Thời gian lưu trú không phù hợp";

    // HistoryCheckIn
    public static final String HISTORY_CHECKIN_INVALID_ID_ISNULL = "Mã phiếu nhận phòng không được rỗng";
    public static final String HISTORY_CHECKIN_INVALID_ID_FORMAT = "Mã ngày nhận phòng phải theo định dạng HCI-XXXXXX";
    public static final String HISTORY_CHECKIN_INVALID_CHECKIN_DATE_ISNULL = "Ngày nhận phòng không được rỗng";
    public static final String HISTORY_CHECKIN_INVALID_CHECKIN_DATE = "Ngày nhận phòng phải sau ngày nhận phòng dự kiến";

    // RoomReservationDetail
    public static final String ROOM_RESERVATION_DETAIL_INVALID_ID = "Mã chi tiết sử dụng phòng theo mẫu RRD-XXXXXX, với X là ký số";
    public static final String ROOM_RESERVATION_DETAIL_INVALID_DATECHANGED = "Thời gian thay đổi phòng phải nhỏ hơn hoặc bằng ngày thời gian hiện tại";

    // Invoice
    public static final String INVOICE_INVALID_ID_ISNUL = "Mã hóa đơn không dược rỗng!!!";
    public static final String INVOICE_INVALID_ID_FORMAT = "Mã hóa đơn phải theo đúng định dạng INV-XXXXXXXXXX-YYYY!!!";
    public static final String INVOICE_INVALID_INVOICE_DATE_ISNULL = "Ngày tạo hóa đơn không được rỗng!!!";
    public static final String INVOICE_INVALID_INVOICE_DATE = "Ngày tạo hóa đơn phải trước ngày hiện tại!!!";
    public static final String INVOICE_INVALID_TOTALDUE= "Tiền trước thuế không được rỗng và phải lớn hơn 0!!!";
    public static final String INVOICE_INVALID_NETDUE= "Tiền sau thuế không được rỗng và phải lớn hơn 0!!!";
    public static final String INVOICE_INVALID_TAX_ISNULL= "Thông tin thuế không được rỗng!!!";

    // HistoryCheckout
    public static final String HISTORY_CHECKOUT_INVALID_ID_ISNULL = "Mã phiếu trả phòng không được rỗng!!!";
    public static final String HISTORY_CHECKOUT_INVALID_ID_FORMAT = "Mã phiếu trả phòng phải theo định dạng HCO-XXXXXX!!!";
    public static final String HISTORY_CHECKOUT_INVALID_CHECKOUT_DATE_ISNULL = "Ngày trả phòng không được rỗng!!!";
    public static final String HISTORY_CHECKOUT_INVALID_CHECKOUT_DATE= "Ngày trả phòng phải trước ngày hiện tại!!!";

    // ConvertHelper
    public static final String CONVERT_HELPER_INVALID_LOCALTIME = "Thời gian phải nằm từ 5:00 đến 23:00";
    public static final String CONVERT_HELPER_INVALID_GENDER = "Giới tính không hợp lệ. Phải nằm trong FEMALE,  MALE";
    public static final String CONVERT_HELPER_INVALID_OBJECT_STATUS = "Trạng thái đối tượng không phù hợp. Phải nằm trong ACTIVATE,  DEACTIVATE";
    public static final String CONVERT_HELPER_INVALID_POSITION = "Chức vụ không hợp lệ. Phải nằm trong RECEPTIONIST, MANAGER";
    public static final String CONVERT_HELPER_INVALID_SHIFT_DAYS_SCHEDULE = "Ngày ca làm việc không hợp lệ. Phải nằm trong MON_WED_FRI, TUE_THU_SAT, SUNDAY";
    public static final String CONVERT_HELPER_INVALID_ACCOUNT_STATUS= "trạng thái tài khoản không hợp lệ. Phải nằm trong ACTIVE, INACTIVE, LOCKED";
    public static final String CONVERT_HELPER_INVALID_PRICE_UNIT = "Đơn vị giá không hợp lệ. Phải nằm trong DAY, HOUR";
    public static final String CONVERT_HELPER_INVALID_ROOM_STATUS = "Trạng thái phòng không hợp lệ. Phải nằm trong AVAILABLE, ON_USE, UNAVAILABLE";

    // Cost Calculator
    public static final String PRICING_NOT_FOUND = "Không tìm thấy bản ghi giá cả theo ngày hoặc theo giờ";
    public static final String ERROR_CHECK_OUT_BEFORE_CHECK_IN = "Thời gian check-out phải sau thời gian check-in";

    // Login
    public static final String LOGIN_INVALID_USERNAME = "Tài khoản không được bỏ trống";
    public static final String LOGIN_INVALID_PASSWORD = "Mật khẩu không được để trống";
    public static final String LOGIN_INVALID_ACCOUNT = "Tài khoản hoặc mật khẩu không hợp lệ";

    // Reservation Form
    public static final String ID_CARD_NUMBER_OVER_LIMIT = "CCCD không được quá 12 ký tự";
    public static final String CUS_NOT_FOUND = "Không tìm thấy khách hàng";
    public static final String RESERVATION_CHECK_DATE_OVERLAP = "Thời gian đặt phòng đã trùng, không thể tạo phiếu mới.";
    public static final String RESERVATION_ID_CARD_NUMBER_OVERLAP = "ID Card Number đã được sử dụng cho cho việc đặt phòng, không thể tạo phiếu mới.";

    // Customer Manager
    public static final String CUS_GENDER_NOT_SELECTED = "Vui lòng chọn giới tính";

}
