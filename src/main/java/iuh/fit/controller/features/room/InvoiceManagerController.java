package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.invoice_controllers.InvoiceDetailsController;
import iuh.fit.controller.features.room.invoice_controllers.InvoiceItemController;
import iuh.fit.dao.InvoiceDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.Invoice;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;


public class InvoiceManagerController {
    @FXML
    private DateRangePicker invoiceDateRangeSearchField;
    @FXML
    private TextField invoiceIDSearchField;
    @FXML
    private Button searchBtn;
    @FXML
    private GridPane invoiceGridPane;

    // Context
    private MainController mainController;
    private Employee employee;
    private List<Invoice> invoiceList;

    public void initialize() {

    }

    public void setupContext(MainController mainController, Employee employee) {
        this.mainController = mainController;
        this.employee = employee;

        loadData();
    }

    private void loadData() {
        invoiceList = InvoiceDAO.getAllInvoices();
        displayInvoices(invoiceList);
    }

    private void displayInvoices(List<Invoice> invoices) {
        invoiceGridPane.getChildren().clear();

        int row = 0;
        int col = 0;

        try {
            for (Invoice invoice : invoices) {
                Pane invoiceItem = loadInvoiceItem(invoice);

                invoiceGridPane.add(invoiceItem, col, row);

                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pane loadInvoiceItem(Invoice invoice) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/invoice_panels/InvoiceItem.fxml"));
        Pane invoiceItem = loader.load();

        InvoiceItemController controller = loader.getController();
        controller.setupContext(mainController, employee, invoice);

        return invoiceItem;
    }

}
