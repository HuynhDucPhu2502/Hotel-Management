package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Lớp HistoryCheckOut đại diện cho lịch sử check-out của một phòng khách sạn.
 * Mỗi lịch sử check-out bao gồm ID của lịch sử check-out, thời gian check-out, và hoá đơn liên quan.
 */
public class HistoryCheckOut {

    // Mã định danh của lịch sử check-out
    private String roomHistoryCheckOutID;

    // Ngày và giờ khi check-out diễn ra
    private LocalDateTime historyCheckOutDate;

    // Thông tin phiếu đặt phòng
    private ReservationForm reservationForm;

    /**
     * Constructor đầy đủ cho lớp HistoryCheckOut.
     *
     * @param roomHistoryCheckOutID ID của lịch sử check-out (không được null hoặc rỗng).
     * @param historyCheckOutDate   Thời gian check-out (phải trước thời điểm hiện tại).
     * @param reservationForm   Thông tin phiếu đặt phòng.
     */
    public HistoryCheckOut(String roomHistoryCheckOutID, LocalDateTime historyCheckOutDate, ReservationForm reservationForm) {
        this.setRoomHistoryCheckOutID(roomHistoryCheckOutID);
        this.setHistoryCheckOutDate(historyCheckOutDate);
        this.setReservationForm(reservationForm);
    }

    /**
     * Constructor tối giản chỉ với ID của lịch sử check-out.
     *
     * @param roomHistoryCheckOutID ID của lịch sử check-out (không được null hoặc rỗng).
     */
    public HistoryCheckOut(String roomHistoryCheckOutID) {
        this.setRoomHistoryCheckOutID(roomHistoryCheckOutID);
    }

    /**
     * Constructor mặc định không có tham số.
     */
    public HistoryCheckOut() {
    }

    /**
     * So sánh hai đối tượng HistoryCheckOut dựa trên roomHistoryCheckOutID.
     *
     * @param o Đối tượng cần so sánh.
     * @return true nếu hai đối tượng có cùng roomHistoryCheckOutID, ngược lại là false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryCheckOut that = (HistoryCheckOut) o;
        return Objects.equals(roomHistoryCheckOutID, that.roomHistoryCheckOutID);
    }

    /**
     * Tính toán mã băm của đối tượng dựa trên roomHistoryCheckOutID.
     *
     * @return Mã băm của đối tượng.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(roomHistoryCheckOutID);
    }

    /**
     * Lấy ID của lịch sử check-out.
     *
     * @return ID của lịch sử check-out.
     */
    public String getRoomHistoryCheckOutID() {
        return roomHistoryCheckOutID;
    }

    /**
     * Thiết lập ID cho lịch sử check-out. ID phải không được rỗng và phải theo định dạng hợp lệ.
     *
     * @param roomHistoryCheckOutID ID của lịch sử check-out.
     * @throws IllegalArgumentException nếu ID là null, rỗng, hoặc không theo định dạng hợp lệ.
     */
    public void setRoomHistoryCheckOutID(String roomHistoryCheckOutID) {
        if (roomHistoryCheckOutID == null || roomHistoryCheckOutID.trim().isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKOUT_INVALID_ID_ISNULL);
        }
        if (!RegexChecker.isValidIDFormat(GlobalConstants.HISTORY_CHECKOUT_ID_PREFIX, roomHistoryCheckOutID)) {
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKOUT_INVALID_ID_FORMAT);
        }
        this.roomHistoryCheckOutID = roomHistoryCheckOutID;
    }

    /**
     * Lấy thời gian check-out.
     *
     * @return Thời gian check-out.
     */
    public LocalDateTime getHistoryCheckOutDate() {
        return historyCheckOutDate;
    }

    /**
     * Thiết lập thời gian check-out. Thời gian này phải không được null và phải trước thời điểm hiện tại.
     *
     * @param historyCheckOutDate Thời gian check-out.
     * @throws IllegalArgumentException nếu thời gian check-out là null hoặc sau thời điểm hiện tại.
     */
    public void setHistoryCheckOutDate(LocalDateTime historyCheckOutDate) {
        if (historyCheckOutDate == null) {
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKOUT_INVALID_CHECKOUT_DATE_ISNULL);
        }
        if (!historyCheckOutDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKOUT_INVALID_CHECKOUT_DATE);
        }
        this.historyCheckOutDate = historyCheckOutDate;
    }

    public ReservationForm getReservationForm() {
        return reservationForm;
    }

    public void setReservationForm(ReservationForm reservationForm) {
        if (reservationForm == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_RESERVATIONFORM);
        this.reservationForm = reservationForm;
    }
}
