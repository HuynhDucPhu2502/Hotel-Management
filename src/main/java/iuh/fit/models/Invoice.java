package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Lớp đại diện cho hóa đơn thanh toán trong hệ thống quản lý khách sạn.
 */
public class Invoice {
    // Mã hóa đơn
    private String invoiceID;
    // Ngày giờ lập hóa đơn
    private LocalDateTime invoiceDate;
    // Tổng tiền phòng
    private double roomCharge;
    // Tổng tiền dịch vụ;
    private double servicesCharge;
    // Tổng tiền phải trả
    private double totalDue;
    // Số tiền thực tế phải trả (sau thuế và các khoản khác)
    private double netDue;
    // Thông tin thuế
    private Tax tax;

    /**
     * Khởi tạo đối tượng `Invoice` với các tham số đầy đủ.
     *
     * @param invoiceID   Mã hóa đơn
     * @param invoiceDate Ngày giờ lập hóa đơn
     * @param tax         Thông tin thuế
     */
    public Invoice(String invoiceID, LocalDateTime invoiceDate, Tax tax) {
        this.setInvoiceID(invoiceID);
        this.setInvoiceDate(invoiceDate);
        this.setTotalDue(calTotalDue());
        this.setNetDue(calNetDue());
        this.setTax(tax);
    }

    /**
     * Khởi tạo đối tượng `Invoice` với mã hóa đơn.
     *
     * @param invoiceID Mã hóa đơn
     */
    public Invoice(String invoiceID) {
        this.setInvoiceID(invoiceID);
    }

    /**
     * Khởi tạo đối tượng `Invoice` rỗng.
     */
    public Invoice() {
    }

    /**
     * Lấy mã hóa đơn.
     *
     * @return Mã hóa đơn
     */
    public String getInvoiceID() {
        return invoiceID;
    }

    /**
     * Thiết lập mã hóa đơn.
     *
     * @param invoiceID Mã hóa đơn
     * @throws IllegalArgumentException nếu mã hóa đơn rỗng hoặc không hợp lệ
     */
    public void setInvoiceID(String invoiceID) {
        if (invoiceID == null || invoiceID.trim().isBlank())
            throw new IllegalArgumentException(ErrorMessages.INVOICE_INVALID_ID_ISNUL);
        if (!RegexChecker.isValidInvoiceID(invoiceID))
            throw new IllegalArgumentException(ErrorMessages.INVOICE_INVALID_ID_FORMAT);
        this.invoiceID = invoiceID;
    }

    /**
     * Lấy ngày giờ lập hóa đơn.
     *
     * @return Ngày giờ lập hóa đơn
     */
    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * Thiết lập ngày giờ lập hóa đơn.
     *
     * @param invoiceDate Ngày giờ lập hóa đơn
     * @throws IllegalArgumentException nếu ngày lập hóa đơn rỗng hoặc trong tương lai
     */
    public void setInvoiceDate(LocalDateTime invoiceDate) {
        if (invoiceDate == null)
            throw new IllegalArgumentException(ErrorMessages.INVOICE_INVALID_INVOICE_DATE_ISNULL);
        if (!invoiceDate.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException(ErrorMessages.INVOICE_INVALID_INVOICE_DATE);
        this.invoiceDate = invoiceDate;
    }

    /**
     * Lấy tổng tiền phải trả.
     *
     * @return Tổng tiền phải trả
     */
    public double getTotalDue() {
        return totalDue;
    }

    /**
     * Thiết lập tổng tiền phải trả.
     *
     * @param totalDue Tổng tiền
     * @throws IllegalArgumentException nếu tổng tiền <= 0
     */
    public void setTotalDue(double totalDue) {
        if (totalDue <= 0)
            throw new IllegalArgumentException(ErrorMessages.INVOICE_INVALID_TOTALDUE);
        this.totalDue = totalDue;
    }

    /**
     * Lấy số tiền thực tế phải trả.
     *
     * @return Số tiền thực tế phải trả
     */
    public double getNetDue() {
        return netDue;
    }

    /**
     * Thiết lập số tiền thực tế phải trả.
     *
     * @param netDue Số tiền thực tế
     * @throws IllegalArgumentException nếu số tiền thực tế <= 0
     */
    public void setNetDue(double netDue) {
        if (netDue <= 0)
            throw new IllegalArgumentException(ErrorMessages.INVOICE_INVALID_NETDUE);
        this.netDue = netDue;
    }


    /**
     * Lấy thông tin thuế.
     *
     * @return Thông tin thuế
     */
    public Tax getTax() {
        return tax;
    }

    /**
     * Thiết lập thông tin thuế.
     *
     * @param tax Thông tin thuế
     * @throws IllegalArgumentException nếu thông tin thuế rỗng
     */
    public void setTax(Tax tax) {
        if (tax == null)
            throw new IllegalArgumentException(ErrorMessages.INVOICE_INVALID_TAX_ISNULL);
        this.tax = tax;
    }

    /**
     * Tính tổng tiền phải trả (chưa bao gồm thuế và các khoản khác).
     *
     * @return Tổng tiền phải trả
     */
    private double calTotalDue() {
        // Cần hoàn thiện hàm này với logic tính toán
        return 0;
    }

    /**
     * Tính số tiền thực tế phải trả (bao gồm thuế và các khoản khác).
     *
     * @return Số tiền thực tế phải trả
     */
    private double calNetDue() {
        // Cần hoàn thiện hàm này với logic tính toán
        return 0;
    }

    /**
     * So sánh hai đối tượng `Invoice`.
     *
     * @param o Đối tượng cần so sánh
     * @return true nếu hai đối tượng giống nhau, false nếu không
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(invoiceID, invoice.invoiceID);
    }

    /**
     * Trả về mã băm của đối tượng `Invoice`.
     *
     * @return Mã băm
     */
    @Override
    public int hashCode() {
        return Objects.hash(invoiceID);
    }
}
