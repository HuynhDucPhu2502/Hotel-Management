package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Lớp ReservationForm đại diện cho phiếu đặt phòng của khách sạn.
 * Nó chứa thông tin như mã đặt phòng, ngày đặt, ngày dự kiến nhận phòng và trả phòng,
 * cũng như thông tin về nhân viên, phòng, và khách hàng liên quan đến phiếu đặt phòng.
 */
public class ReservationForm {

    // Các trường dữ liệu lưu trữ thông tin của phiếu đặt phòng
    private String reservationID;  // Mã đặt phòng duy nhất
    private LocalDateTime reservationDate;  // Ngày đặt phòng
    private LocalDateTime approxCheckInDate;  // Ngày dự kiến nhận phòng
    private LocalDateTime approxCheckOutDate;  // Ngày dự kiến trả phòng
    private Employee employee;  // Nhân viên liên quan đến đặt phòng
    private Room room;  // Phòng đặt
    private Customer customer;  // Khách hàng đặt phòng

    /**
     * Constructor khởi tạo đầy đủ các trường của ReservationForm.
     *
     * @param reservationID       Mã đặt phòng duy nhất.
     * @param reservationDate     Ngày đặt phòng.
     * @param approxCheckInDate   Ngày dự kiến nhận phòng.
     * @param approxCheckOutDate  Ngày dự kiến trả phòng.
     * @param employee            Nhân viên chịu trách nhiệm cho đặt phòng này.
     * @param room                Phòng đặt.
     * @param customer            Khách hàng đặt phòng.
     */
    public ReservationForm(String reservationID, LocalDateTime reservationDate, LocalDateTime approxCheckInDate, LocalDateTime approxCheckOutDate, Employee employee, Room room, Customer customer) {
        this.setReservationID(reservationID);  // Thiết lập mã đặt phòng với kiểm tra hợp lệ
        this.setReservationDate(reservationDate);  // Thiết lập ngày đặt phòng với kiểm tra hợp lệ
        this.setApproxCheckInDate(approxCheckInDate);  // Thiết lập ngày dự kiến nhận phòng
        this.setApproxCheckOutDate(approxCheckOutDate);  // Thiết lập ngày dự kiến trả phòng
        this.setEmployee(employee);  // Thiết lập nhân viên
        this.setRoom(room);  // Thiết lập phòng
        this.setCustomer(customer);  // Thiết lập khách hàng
    }

    /**
     * Constructor chỉ khởi tạo mã đặt phòng.
     *
     * @param reservationID Mã đặt phòng duy nhất.
     */
    public ReservationForm(String reservationID) {
        this.setReservationID(reservationID);  // Thiết lập mã đặt phòng với kiểm tra hợp lệ
    }

    /**
     * Constructor không tham số.
     */
    public ReservationForm() {
    }

    /**
     * Lấy mã đặt phòng.
     *
     * @return Mã đặt phòng hiện tại.
     */
    public String getReservationID() {
        return reservationID;
    }

    /**
     * Thiết lập mã đặt phòng với kiểm tra hợp lệ.
     * Mã đặt phòng không được để trống và phải tuân thủ định dạng quy định.
     *
     * @param reservationID Mã đặt phòng mới.
     */
    public void setReservationID(String reservationID) {
        if (reservationID.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_ID_ISNULL);  // Kiểm tra mã đặt phòng không được trống
        if (!RegexChecker.isValidIDFormat(GlobalConstants.RESERVATIONID_PREFIX, reservationID))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_ID_FORMAT);  // Kiểm tra mã đặt phòng đúng định dạng
        this.reservationID = reservationID;
    }

    /**
     * Lấy ngày đặt phòng.
     *
     * @return Ngày đặt phòng hiện tại.
     */
    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    /**
     * Thiết lập ngày đặt phòng với kiểm tra hợp lệ.
     * Ngày đặt phòng không được để trống và phải trước thời gian hiện tại.
     *
     * @param reservationDate Ngày đặt phòng mới.
     */
    public void setReservationDate(LocalDateTime reservationDate) {
        if (reservationDate == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_RESERVATION_DATE_ISNULL);  // Kiểm tra ngày đặt không được null
        if (!reservationDate.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_RESERVATION_DATE);  // Kiểm tra ngày đặt phải trước thời gian hiện tại
        this.reservationDate = reservationDate;
    }

    /**
     * Lấy ngày dự kiến nhận phòng.
     *
     * @return Ngày dự kiến nhận phòng hiện tại.
     */
    public LocalDateTime getApproxCheckInDate() {
        return approxCheckInDate;
    }

    /**
     * Thiết lập ngày dự kiến nhận phòng với kiểm tra hợp lệ.
     * Ngày dự kiến nhận phòng không được để trống và phải sau ngày đặt phòng.
     *
     * @param approxCheckInDate Ngày dự kiến nhận phòng mới.
     */
    public void setApproxCheckInDate(LocalDateTime approxCheckInDate) {
        if (approxCheckInDate == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKIN_DATE_ISNULL);  // Kiểm tra ngày nhận phòng không được null
        if (!approxCheckInDate.isAfter(this.reservationDate))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKIN_DATE);  // Kiểm tra ngày nhận phòng phải sau ngày đặt
        this.approxCheckInDate = approxCheckInDate;
    }

    /**
     * Lấy ngày dự kiến trả phòng.
     *
     * @return Ngày dự kiến trả phòng hiện tại.
     */
    public LocalDateTime getApproxCheckOutDate() {
        return approxCheckOutDate;
    }

    /**
     * Thiết lập ngày dự kiến trả phòng với kiểm tra hợp lệ.
     * Ngày dự kiến trả phòng không được để trống và phải sau ngày dự kiến nhận phòng.
     *
     * @param approxCheckOutDate Ngày dự kiến trả phòng mới.
     */
    public void setApproxCheckOutDate(LocalDateTime approxCheckOutDate) {
        if (approxCheckOutDate == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKOUT_DATE_ISNULL);  // Kiểm tra ngày trả phòng không được null
        if (!approxCheckOutDate.isAfter(approxCheckInDate))
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_APPROX_CHECKOUT_DATE);  // Kiểm tra ngày trả phòng phải sau ngày nhận phòng
        this.approxCheckOutDate = approxCheckOutDate;
    }

    /**
     * Lấy nhân viên phụ trách đặt phòng.
     *
     * @return Nhân viên hiện tại.
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * Thiết lập nhân viên phụ trách đặt phòng với kiểm tra hợp lệ.
     * Nhân viên không được để trống.
     *
     * @param employee Nhân viên mới.
     */
    public void setEmployee(Employee employee) {
        if (employee == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_EMPLOYEE_ISNULL);  // Kiểm tra nhân viên không được null
        this.employee = employee;
    }

    /**
     * Lấy phòng đặt.
     *
     * @return Phòng hiện tại.
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Thiết lập phòng đặt với kiểm tra hợp lệ.
     * Phòng không được để trống.
     *
     * @param room Phòng mới.
     */
    public void setRoom(Room room) {
        if (room == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_ROOM_ISNULL);  // Kiểm tra phòng không được null
        this.room = room;
    }

    /**
     * Lấy khách hàng đặt phòng.
     *
     * @return Khách hàng hiện tại.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Thiết lập khách hàng đặt phòng với kiểm tra hợp lệ.
     * Khách hàng không được để trống.
     *
     * @param customer Khách hàng mới.
     */
    public void setCustomer(Customer customer) {
        if (customer == null)
            throw new IllegalArgumentException(ErrorMessages.RESERVATION_FORM_INVALID_CUSTOMER_ISNULL);  // Kiểm tra khách hàng không được null
        this.customer = customer;
    }

    /**
     * Phương thức equals để so sánh hai đối tượng ReservationForm.
     * So sánh dựa trên mã đặt phòng.
     *
     * @param o Đối tượng cần so sánh.
     * @return true nếu hai đối tượng có mã đặt phòng giống nhau, ngược lại false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationForm that = (ReservationForm) o;
        return Objects.equals(reservationID, that.reservationID);
    }

    /**
     * Phương thức hashCode dựa trên mã đặt phòng.
     *
     * @return Mã băm của đối tượng.
     */
    @Override
    public int hashCode() {
        return Objects.hash(reservationID);
    }

    @Override
    public String toString() {
        return "ReservationForm{" +
                "reservationID='" + reservationID + '\'' +
                ", reservationDate=" + reservationDate +
                ", approxCheckInDate=" + approxCheckInDate +
                ", approxCheckOutDate=" + approxCheckOutDate +
                ", employee=" + employee +
                ", room=" + room +
                ", customer=" + customer +
                '}';
    }
}
