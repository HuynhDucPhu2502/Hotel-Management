package iuh.fit.models;

import iuh.fit.models.enums.ShiftDaysSchedule;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Lớp Shift đại diện cho một ca làm việc, bao gồm mã ca, thời gian bắt đầu, thời gian kết thúc, số giờ làm việc và lịch làm việc theo ngày.
 * Lớp này cũng cung cấp các phương thức để kiểm tra và tính toán số giờ làm việc dựa trên thời gian bắt đầu và kết thúc.
 */
public class Shift {
    private String shiftID;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDateTime updatedDate;
    private ShiftDaysSchedule shiftDaysSchedule;
    private int numberOfHour = 0;

    /**
     * Constructor đầy đủ của lớp Shift.
     * Khởi tạo đối tượng với các thông tin về ID ca làm, thời gian bắt đầu, kết thúc, và ngày cập nhật.
     * Tự động tính số giờ làm việc sau khi khởi tạo.
     *
     * @param shiftID      Mã ca làm (String).
     * @param startTime    Thời gian bắt đầu ca (LocalTime).
     * @param endTime      Thời gian kết thúc ca (LocalTime).
     * @param updatedDate  Ngày cập nhật thông tin ca (LocalDateTime).
     * @param shiftDaysSchedule Lịch làm việc theo ca (ShiftDaysSchedule).
     */


    public Shift(String shiftID, LocalTime startTime, LocalTime endTime, LocalDateTime updatedDate, ShiftDaysSchedule shiftDaysSchedule) {
        setStartTime(startTime);
        setEndTime(endTime);
        setShiftID(shiftID);
        setUpdatedDate(updatedDate);
        setShiftDaysSchedule(shiftDaysSchedule);
        calcNumberOfHour(); // Tính toán số giờ làm việc
    }

    /**
     * Constructor không có tham số.
     * Dùng khi không cần thiết lập các thông tin ngay lập tức.
     */
    public Shift() {
    }

    /**
     * So sánh hai đối tượng Shift dựa trên mã ca làm (shiftID).
     *
     * @param o Đối tượng để so sánh.
     * @return true nếu hai đối tượng có cùng shiftID, false nếu không.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shift shift)) return false;
        return Objects.equals(shiftID, shift.shiftID);
    }

    /**
     * Tạo hash code dựa trên mã ca làm (shiftID).
     *
     * @return Giá trị hash code của shiftID.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(shiftID);
    }

    /**
     * Lấy mã ca làm (shiftID).
     *
     * @return Mã ca làm (String).
     */
    public String getShiftID() {
        return shiftID;
    }

    /**
     * Thiết lập mã ca làm (shiftID).
     * Mã ca làm phải hợp lệ dựa trên thời gian bắt đầu (startTime).
     *
     * @param shiftID Mã ca làm (String).
     * @throws IllegalArgumentException nếu startTime là null hoặc mã ca không hợp lệ.
     */
    public void setShiftID(String shiftID) {
        if (this.endTime == null)
            throw new IllegalArgumentException(ErrorMessages.SHIFT_NULL_ENDTIME);

        if (!RegexChecker.isValidShiftID(shiftID, this.endTime))
            throw new IllegalArgumentException(ErrorMessages.SHIFT_INVALID_SHIFTID);
        this.shiftID = shiftID;
    }

    /**
     * Lấy thời gian bắt đầu ca (startTime).
     *
     * @return Thời gian bắt đầu ca (LocalTime).
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Thiết lập thời gian bắt đầu ca làm.
     * Thời gian bắt đầu phải nằm trong khoảng hợp lệ, không được trước 5h sáng và không được sau thời gian kết thúc.
     *
     * @param startTime Thời gian bắt đầu ca (LocalTime).
     * @throws IllegalArgumentException nếu thời gian bắt đầu không hợp lệ.
     */
    public void setStartTime(LocalTime startTime) {
        if (startTime.isBefore(GlobalConstants.SHIFT_MIN_TIME))
            throw new IllegalArgumentException(ErrorMessages.SHIFT_INVALID_STARTTIME);

        if (endTime != null && startTime.isAfter(endTime))
            throw new IllegalArgumentException(ErrorMessages.SHIFT_INVALID_STARTTIME);

        this.startTime = startTime;
    }

    /**
     * Lấy thời gian kết thúc ca (endTime).
     *
     * @return Thời gian kết thúc ca (LocalTime).
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Thiết lập thời gian kết thúc ca làm.
     * Thời gian kết thúc phải nằm trong khoảng hợp lệ, không được sau 23h đêm và không được trước thời gian bắt đầu.
     *
     * @param endTime Thời gian kết thúc ca (LocalTime).
     * @throws IllegalArgumentException nếu thời gian kết thúc không hợp lệ.
     */
    public void setEndTime(LocalTime endTime) {
        if (endTime.isAfter(GlobalConstants.SHIFT_MAX_TIME))
            throw new IllegalArgumentException(ErrorMessages.SHIFT_INVALID_ENDTIME);

        if (startTime != null && endTime.isBefore(startTime))
            throw new IllegalArgumentException(ErrorMessages.SHIFT_INVALID_ENDTIME);

        this.endTime = endTime;
    }

    /**
     * Lấy ngày cập nhật thông tin ca (updatedDate).
     *
     * @return Ngày cập nhật (LocalDateTime).
     */
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    /**
     * Thiết lập ngày cập nhật thông tin ca.
     * Ngày cập nhật không được lớn hơn ngày hiện tại.
     *
     * @param updatedDate Ngày cập nhật (LocalDateTime).
     * @throws IllegalArgumentException nếu ngày cập nhật lớn hơn ngày hiện tại.
     */
    public void setUpdatedDate(LocalDateTime updatedDate) {
        if (updatedDate.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException(ErrorMessages.SHIFT_INVALID_MODIFIEDDATE);
        this.updatedDate = updatedDate;
    }

    /**
     * Tính toán số giờ làm việc dựa trên thời gian bắt đầu và kết thúc.
     * Nếu số giờ làm việc ít hơn số giờ tối thiểu (6 giờ), sẽ ném ngoại lệ.
     *
     * @throws IllegalArgumentException nếu startTime hoặc endTime không hợp lệ, hoặc nếu số giờ nhỏ hơn số giờ tối thiểu.
     */
    public void calcNumberOfHour() {
        if (startTime == null)
            throw new IllegalArgumentException(ErrorMessages.SHIFT_NULL_STARTTIME);

        if (endTime == null)
            throw new IllegalArgumentException(ErrorMessages.SHIFT_NULL_ENDTIME);

        int hours = (int) ChronoUnit.HOURS.between(startTime, endTime);

        if (hours < GlobalConstants.SHIFT_MIN_WORK_HOURS)
            throw new IllegalArgumentException(ErrorMessages.SHIFT_INVALID_WORKHOURS);

        this.numberOfHour = hours;
    }

    /**
     * Lấy lịch làm việc theo ca (shiftDaysSchedule).
     *
     * @return Lịch làm việc (ShiftDaysSchedule).
     */
    public ShiftDaysSchedule getShiftDaysSchedule() {
        return shiftDaysSchedule;
    }

    /**
     * Thiết lập lịch làm việc theo ca (shiftDaysSchedule).
     *
     * @param shiftDaysSchedule Lịch làm việc (ShiftDaysSchedule).
     */
    public void setShiftDaysSchedule(ShiftDaysSchedule shiftDaysSchedule) {
        this.shiftDaysSchedule = shiftDaysSchedule;
    }

    /**
     * Lấy số giờ làm việc.
     * Tự động tính toán nếu chưa được tính.
     *
     * @return Số giờ làm việc (int).
     */
    public int getNumberOfHour() {
        calcNumberOfHour();  // Tính toán số giờ mỗi khi gọi hàm
        return numberOfHour;
    }

    /**
     * Trả về chuỗi biểu diễn đối tượng Shift, bao gồm các thông tin chi tiết như
     * shiftID, startTime, endTime, updatedDate, shiftDaysSchedule và numberOfHour.
     *
     * @return Chuỗi biểu diễn của đối tượng Shift với các giá trị của các thuộc tính được phân cách bởi dấu ','.
     */
    @Override
    public String toString() {
        return "Shift{" +
                "shiftID='" + shiftID + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", updatedDate=" + updatedDate +
                ", shiftDaysSchedule=" + shiftDaysSchedule +
                ", numberOfHour=" + numberOfHour +
                '}';
    }
}
