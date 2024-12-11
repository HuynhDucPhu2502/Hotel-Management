package iuh.fit.models.misc;

import iuh.fit.models.Invoice;

/**
 * @author Le Tran Gia Huy
 * @created 10/12/2024 - 9:29 PM
 * @project HotelManagement
 * @package iuh.fit.models.misc
 */
public class ShiftDetailForInvoice {
    private int FIID;
    private ShiftDetail shiftDetail;
    private Invoice invoice;

    public ShiftDetailForInvoice() {
    }

    public ShiftDetailForInvoice(int FIID, Invoice invoice, ShiftDetail shiftDetail) {
        this.FIID = FIID;
        this.invoice = invoice;
        this.shiftDetail = shiftDetail;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftDetailForInvoice that)) return false;

        return FIID == that.FIID;
    }

    @Override
    public int hashCode() {
        return FIID;
    }

    public int getFIID() {
        return FIID;
    }

    public void setFIID(int FIID) {
        this.FIID = FIID;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public ShiftDetail getShiftDetail() {
        return shiftDetail;
    }

    public void setShiftDetail(ShiftDetail shiftDetail) {
        this.shiftDetail = shiftDetail;
    }

    @Override
    public String toString() {
        return "ShiftDetailForInvoiceDAO{" +
                "FIID=" + FIID +
                ", shiftDetail=" + shiftDetail +
                ", invoice=" + invoice +
                '}';
    }
}
