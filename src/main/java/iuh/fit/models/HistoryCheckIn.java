package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Lớp đại diện cho lịch sử nhận phòng của khách hàng.
 */
public class HistoryCheckIn {
    // Mã lịch sử nhận phòng
    private String historyCheckInID;
    // Ngày giờ nhận phòng
    private LocalDateTime checkInDate;
    // Phiếu đặt phòng liên quan
    private ReservationForm reservationForm;

    /**
     * Khởi tạo đối tượng HistoryCheckIn với các tham số.
     *
     * @param historyCheckInID Mã lịch sử nhận phòng
     * @param checkInDate Ngày giờ nhận phòng
     * @param reservationForm Phiếu đặt phòng liên quan
     */
    public HistoryCheckIn(String historyCheckInID, LocalDateTime checkInDate, ReservationForm reservationForm) {
        this.setHistoryCheckInID(historyCheckInID);
        this.setCheckInDate(checkInDate);
        this.setReservationForm(reservationForm);
    }

    /**
     * Khởi tạo đối tượng HistoryCheckIn với mã lịch sử nhận phòng.
     *
     * @param historyCheckInID Mã lịch sử nhận phòng
     */
    public HistoryCheckIn(String historyCheckInID) {
        this.setHistoryCheckInID(historyCheckInID);
    }

    /**
     * Khởi tạo đối tượng HistoryCheckIn rỗng.
     */
    public HistoryCheckIn() {
    }

    /**
     * Lấy mã lịch sử nhận phòng.
     *
     * @return Mã lịch sử nhận phòng.
     */
    public String getHistoryCheckInID() {
        return historyCheckInID;
    }

    /**
     * Thiết lập mã lịch sử nhận phòng.
     *
     * @param historyCheckInID Mã lịch sử nhận phòng
     * @throws IllegalArgumentException nếu mã lịch sử nhận phòng rỗng hoặc không hợp lệ.
     */
    public void setHistoryCheckInID(String historyCheckInID) {
        if(historyCheckInID.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_IVALID_ID_ISNULL);
        if(!RegexChecker.isValidIDFormat(GlobalConstants.HISTORY_CHECKIN_ID_PREFIX, historyCheckInID))
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_IVALID_ID_FORMAT);
        this.historyCheckInID = historyCheckInID;
    }

    /**
     * Lấy ngày giờ nhận phòng.
     *
     * @return Ngày giờ nhận phòng.
     */
    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    /**
     * Thiết lập ngày giờ nhận phòng.
     *
     * @param checkInDate Ngày giờ nhận phòng
     * @throws IllegalArgumentException nếu ngày giờ nhận phòng rỗng hoặc không hợp lệ.
     */
    public void setCheckInDate(LocalDateTime checkInDate) {
        if(checkInDate == null){
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_IVALID_CHECKIN_DATE_ISNULL);
        }
        if(!checkInDate.isAfter(reservationForm.getCheckInDate())){
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_IVALID_CHECKIN_DATE);
        }
        this.checkInDate = checkInDate;
    }

    /**
     * Lấy phiếu đặt phòng liên quan.
     *
     * @return Phiếu đặt phòng liên quan.
     */
    public ReservationForm getReservationForm() {
        return reservationForm;
    }

    /**
     * Thiết lập phiếu đặt phòng liên quan.
     *
     * @param reservationForm Phiếu đặt phòng liên quan
     * @throws IllegalArgumentException nếu phiếu đặt phòng rỗng.
     */
    public void setReservationForm(ReservationForm reservationForm) {
        if(reservationForm == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_RESERVATIONFORM);
        this.reservationForm = reservationForm;
    }

    /**
     * Kiểm tra xem hai đối tượng HistoryCheckIn có giống nhau không.
     *
     * @param o Đối tượng cần so sánh
     * @return true nếu hai đối tượng giống nhau, false nếu không.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryCheckIn that = (HistoryCheckIn) o;
        return Objects.equals(historyCheckInID, that.historyCheckInID);
    }

    /**
     * Trả về mã băm của đối tượng HistoryCheckIn.
     *
     * @return Mã băm của đối tượng.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(historyCheckInID);
    }

    @Override
    public String toString() {
        return "HistoryCheckIn{" +
                "historyCheckInID='" + historyCheckInID + '\'' +
                ", checkInDate=" + checkInDate +
                ", reservationForm=" + reservationForm +
                '}';
    }
}
