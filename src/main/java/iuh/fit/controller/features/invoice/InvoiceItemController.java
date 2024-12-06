package iuh.fit.controller.features.invoice;

import iuh.fit.controller.MainController;
import iuh.fit.models.Employee;
import iuh.fit.models.Invoice;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InvoiceItemController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Text invoiceIDText, customerNameLabel;
    @FXML
    private Label invoiceDateLabel;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));

    private MainController mainController;
    private Employee employee;
    private Invoice invoice;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {

    }

    public void setupContext(MainController mainController, Employee employee, Invoice invoice) {
        this.mainController = mainController;
        this.employee = employee;
        this.invoice = invoice;

        invoiceIDText.setText(invoice.getInvoiceID());
        customerNameLabel.setText(invoice.getReservationForm().getCustomer().getFullName());
        invoiceDateLabel.setText("Ngày lập: " + dateTimeFormatter.format(invoice.getInvoiceDate()));
    }

    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    @FXML
    private void navigateToInvoiceDetailsPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/iuh/fit/view/features/room/invoice_panels/InvoiceDetailsPanel.fxml"));
            AnchorPane layout = loader.load();

            InvoiceDetailsController invoiceDetailsController = loader.getController();
            invoiceDetailsController.setupContext(
                    mainController, employee, invoice
            );

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
