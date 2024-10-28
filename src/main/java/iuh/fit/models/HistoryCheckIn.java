package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Objects;

public class HistoryCheckIn {
    private String historyCheckInID;
    private LocalDateTime checkInDate;
    private ReservationForm reservationForm;
    private Employee employee;


    public HistoryCheckIn(
            String historyCheckInID, LocalDateTime checkInDate,
            ReservationForm reservationForm, Employee employee
    ) {
        this.setHistoryCheckInID(historyCheckInID);
        this.setCheckInDate(checkInDate);
        this.setReservationForm(reservationForm);
    }

    public HistoryCheckIn(String historyCheckInID) {
        this.setHistoryCheckInID(historyCheckInID);
    }

    public HistoryCheckIn() {
    }

    public void setEmployee(Employee employee) {
        if (employee == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_EMPLOYEE);
        this.employee = employee;
    }

    public void setHistoryCheckInID(String historyCheckInID) {
        if(historyCheckInID.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_INVALID_ID_ISNULL);
        if(!RegexChecker.isValidIDFormat(GlobalConstants.HISTORY_CHECKIN_ID_PREFIX, historyCheckInID))
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_INVALID_ID_FORMAT);
        this.historyCheckInID = historyCheckInID;
    }

    public void setCheckInDate(LocalDateTime checkInDate) {
        if(checkInDate == null){
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_INVALID_CHECKIN_DATE_ISNULL);
        }
        if(!checkInDate.isAfter(reservationForm.getCheckInDate())){
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_INVALID_CHECKIN_DATE);
        }
        this.checkInDate = checkInDate;
    }

    public void setReservationForm(ReservationForm reservationForm) {
        if(reservationForm == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_RESERVATIONFORM);
        this.reservationForm = reservationForm;
    }

    public String getHistoryCheckInID() {
        return historyCheckInID;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    public ReservationForm getReservationForm() {
        return reservationForm;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryCheckIn that = (HistoryCheckIn) o;
        return Objects.equals(historyCheckInID, that.historyCheckInID);
    }

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
