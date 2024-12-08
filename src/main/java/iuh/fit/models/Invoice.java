package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;

import java.time.LocalDateTime;
import java.util.Objects;

public class Invoice {
    private String invoiceID;
    private LocalDateTime invoiceDate;
    private double roomCharge;
    private double servicesCharge;
    private double totalDue;
    private double netDue;
    private ReservationForm reservationForm;

    public Invoice(String invoiceID, LocalDateTime invoiceDate, double roomCharge, double servicesCharge,
            double totalDue, double netDue, ReservationForm reservationForm) {
        this.setInvoiceID(invoiceID);
        this.setInvoiceDate(invoiceDate);
        this.roomCharge = roomCharge;
        this.servicesCharge = servicesCharge;
        this.setTotalDue(totalDue);
        this.setNetDue(netDue);
        this.reservationForm = reservationForm;
    }

    public Invoice() {
    }

    public void setInvoiceID(String invoiceID) {
        if (invoiceID == null || invoiceID.trim().isBlank())
            throw new IllegalArgumentException(ErrorMessages.INVOICE_INVALID_ID_ISNUL);
        this.invoiceID = invoiceID;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        if (invoiceDate == null)
            throw new IllegalArgumentException(ErrorMessages.INVOICE_INVALID_INVOICE_DATE_ISNULL);
        this.invoiceDate = invoiceDate;
    }

    public void setTotalDue(double totalDue) {
        if (totalDue <= 0)
            throw new IllegalArgumentException(ErrorMessages.INVOICE_INVALID_TOTALDUE);
        this.totalDue = totalDue;
    }

    public void setNetDue(double netDue) {
        if (netDue <= 0)
            throw new IllegalArgumentException(ErrorMessages.INVOICE_INVALID_NETDUE);
        this.netDue = netDue;
    }

    public void setRoomCharge(double roomCharge) {
        this.roomCharge = roomCharge;
    }

    public void setServicesCharge(double servicesCharge) {
        this.servicesCharge = servicesCharge;
    }

    public void setReservationForm(ReservationForm reservationForm) {
        this.reservationForm = reservationForm;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public double getRoomCharge() {
        return roomCharge;
    }

    public double getServicesCharge() {
        return servicesCharge;
    }

    public double getTotalDue() {
        return totalDue;
    }

    public double getNetDue() {
        return netDue;
    }

    public ReservationForm getReservationForm() {
        return reservationForm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(invoiceID, invoice.invoiceID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceID);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceID='" + invoiceID + '\'' +
                ", invoiceDate=" + invoiceDate +
                ", roomCharge=" + roomCharge +
                ", servicesCharge=" + servicesCharge +
                ", netDue=" + netDue +
                ", totalDue=" + totalDue +
                ", reservationForm=" + reservationForm +
                '}';
    }
}
