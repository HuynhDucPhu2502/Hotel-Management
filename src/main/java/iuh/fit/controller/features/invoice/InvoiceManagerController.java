package iuh.fit.controller.features.invoice;

import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.controller.features.invoice.invoice_controllers.InvoiceItemController;
import iuh.fit.dao.InvoiceDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.Invoice;
import iuh.fit.utils.EditDateRangePicker;
import iuh.fit.utils.RoomManagementService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class InvoiceManagerController {
    @FXML
    private DateRangePicker invoiceDateRangeSearchField;
    @FXML
    private TextField invoiceIDSearchField;
    @FXML
    private GridPane invoiceGridPane;

    // Context
    private MainController mainController;
    private Employee employee;
    private List<Invoice> invoiceList;

    private static NotificationButtonController topBarController;
    public static void setupController(NotificationButtonController controller){
        topBarController = controller;
    }

    public void initialize() {
        setupSearchListeners();
    }

    public void setupContext(MainController mainController, Employee employee) {
        this.mainController = mainController;
        this.employee = employee;

        loadData();
    }

    private void loadData() {
        Task<List<Invoice>> loadDataTask = new Task<>() {
            @Override
            protected List<Invoice> call() {
                RoomManagementService.autoCheckoutOverdueRooms(topBarController);

                return InvoiceDAO.getAllInvoices();
            }
        };

        loadDataTask.setOnSucceeded(event -> {
            invoiceList = loadDataTask.getValue();
            displayInvoices(invoiceList);
            EditDateRangePicker.editDateRangePicker(invoiceDateRangeSearchField);
            invoiceDateRangeSearchField
                    .getDateRangeView()
                    .presetTitleProperty()
                    .set("Thời điểm tạo hóa đơn");
        });

        new Thread(loadDataTask).start();
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

    private void setupSearchListeners() {
        invoiceIDSearchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearchAction());

        invoiceDateRangeSearchField.valueProperty().addListener((observable, oldValue, newValue) -> handleSearchAction());
    }

    private void handleSearchAction() {
        if (invoiceList == null)
            return;

        String invoiceID = invoiceIDSearchField.getText().trim();
        LocalDate startDate = invoiceDateRangeSearchField.getValue().getStartDate();
        LocalDate endDate = invoiceDateRangeSearchField.getValue().getEndDate();

        Task<List<Invoice>> searchTask = new Task<>() {
            @Override
            protected List<Invoice> call() {
                return invoiceList.stream()
                        .filter(invoice -> (invoiceID.isEmpty() || invoice.getInvoiceID().contains(invoiceID)) &&
                                (startDate == null || endDate == null ||
                                        (!invoice.getInvoiceDate().toLocalDate().isBefore(startDate) &&
                                                !invoice.getInvoiceDate().toLocalDate().isAfter(endDate))))
                        .collect(Collectors.toList());
            }
        };

        searchTask.setOnSucceeded(event -> displayInvoices(searchTask.getValue()));

        new Thread(searchTask).start();
    }

}
