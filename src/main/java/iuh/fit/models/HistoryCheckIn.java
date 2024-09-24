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

    public HistoryCheckIn(String historyCheckInID, LocalDateTime checkInDate, ReservationForm reservationForm) {
        this.setHistoryCheckInID(historyCheckInID);
        this.setCheckInDate(checkInDate);
        this.setReservationForm(reservationForm);
    }

    public HistoryCheckIn(String historyCheckInID) {
        this.setHistoryCheckInID(historyCheckInID);
    }

    public HistoryCheckIn() {
    }

    public String getHistoryCheckInID() {
        return historyCheckInID;
    }

    public void setHistoryCheckInID(String historyCheckInID) {
        if(historyCheckInID.isEmpty())
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_IVALID_ID_ISNULL);
        if(!RegexChecker.isValidIDFormat(GlobalConstants.HISTORY_CHEKCIN_ID_PREFIX, historyCheckInID))
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_IVALID_ID_FORMAT);
        this.historyCheckInID = historyCheckInID;
    }

    public LocalDateTime getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDateTime checkInDate) {
        if(checkInDate == null)
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_IVALID_CHECKIN_DATE_ISNULL);
        if(!checkInDate.isAfter(reservationForm.getApproxCheckInDate()))
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_IVALID_CHECKIN_DATE);
        this.checkInDate = checkInDate;
    }

    public ReservationForm getReservationForm() {
        return reservationForm;
    }

    public void setReservationForm(ReservationForm reservationForm) {
        if(reservationForm == null)
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKIN_IVALID_RESERVATION);
        this.reservationForm = reservationForm;
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
}
