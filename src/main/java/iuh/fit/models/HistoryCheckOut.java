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

    // Hoá đơn liên quan đến lịch sử check-out
    private Invoice invoice;

    /**
     * Constructor đầy đủ cho lớp HistoryCheckOut.
     *
     * @param roomHistoryCheckOutID ID của lịch sử check-out (không được null hoặc rỗng).
     * @param historyCheckOutDate   Thời gian check-out (phải trước thời điểm hiện tại).
     * @param invoice               Hoá đơn liên quan (không được null).
     */
    public HistoryCheckOut(String roomHistoryCheckOutID, LocalDateTime historyCheckOutDate, Invoice invoice) {
        this.setRoomHistoryCheckOutID(roomHistoryCheckOutID);
        this.setHistoryCheckOutDate(historyCheckOutDate);
        this.setInvoice(invoice);
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

    /**
     * Lấy hoá đơn liên quan đến lịch sử check-out.
     *
     * @return Hoá đơn liên quan.
     */
    public Invoice getInvoice() {
        return invoice;
    }

    /**
     * Thiết lập hoá đơn liên quan đến lịch sử check-out. Hoá đơn không được null.
     *
     * @param invoice Hoá đơn liên quan.
     * @throws IllegalArgumentException nếu hoá đơn là null.
     */
    public void setInvoice(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKOUT_INVALID_INVOICE_ISNULL);
        }
        this.invoice = invoice;
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
}
