package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Objects;

public class HistoryCheckOut {

    private String historyCheckOutID;
    private LocalDateTime checkOutDate;
    private ReservationForm reservationForm;
    private Employee employee;

    public HistoryCheckOut(String historyCheckOutID, LocalDateTime checkOutDate,
                           ReservationForm reservationForm, Employee employee) {
        this.setHistoryCheckOutID(historyCheckOutID);
        this.setEmployee(employee);
        this.setReservationForm(reservationForm);
        this.setCheckOutDate(checkOutDate);
    }

    public HistoryCheckOut() {
    }

    public void setHistoryCheckOutID(String historyCheckOutID) {
        if (historyCheckOutID == null || historyCheckOutID.trim().isBlank()) {
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKOUT_INVALID_ID_ISNULL);
        }
        if (!RegexChecker.isValidIDFormat(GlobalConstants.HISTORY_CHECKOUT_ID_PREFIX, historyCheckOutID)) {
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKOUT_INVALID_ID_FORMAT);
        }
        this.historyCheckOutID = historyCheckOutID;
    }

    public void setCheckOutDate(LocalDateTime checkOutDate) {
        if (checkOutDate == null) {
            throw new IllegalArgumentException(ErrorMessages.HISTORY_CHECKOUT_INVALID_CHECKOUT_DATE_ISNULL);
        }
        this.checkOutDate = checkOutDate;
    }

    public void setReservationForm(ReservationForm reservationForm) {
        if (reservationForm == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_RESERVATIONFORM);
        this.reservationForm = reservationForm;
    }

    public void setEmployee(Employee employee) {
        if (employee == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_EMPLOYEE);
        this.employee = employee;
    }

    public ReservationForm getReservationForm() {
        return reservationForm;
    }

    public LocalDateTime getCheckOutDate() {
        return checkOutDate;
    }

    public String getHistoryCheckOutID() {
        return historyCheckOutID;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryCheckOut that = (HistoryCheckOut) o;
        return Objects.equals(historyCheckOutID, that.historyCheckOutID);
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(historyCheckOutID);
    }

    @Override
    public String toString() {
        return "HistoryCheckOut{" +
                "historyCheckOutID='" + historyCheckOutID + '\'' +
                ", checkOutDate=" + checkOutDate +
                ", reservationForm=" + reservationForm +
                ", employee=" + employee +
                '}';
    }
}
