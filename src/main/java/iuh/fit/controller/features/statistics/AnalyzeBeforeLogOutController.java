package iuh.fit.controller.features.statistics;

import iuh.fit.controller.LoginController;
import iuh.fit.controller.MainController;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.misc.ShiftDetailDAO;
import iuh.fit.dao.misc.ShiftDetailForInvoiceDAO;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.misc.ShiftDetail;
import iuh.fit.models.misc.ShiftDetailForInvoice;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AnalyzeBeforeLogOutController {
    @FXML
    private Label employeeIDLabel, fullNameLabel, phoneNumberLabel;
    @FXML
    private Label numbOfPreOrderRoomLabel, numbOfCheckInForm, numbOfCheckOutForm;

    @FXML
    private TableView<ObservableList<String>> invoiceTableView;
    @FXML
    private TableColumn<ObservableList<String>, String> invoiceIDColumn, customerIDColumn, customerFullNameColumn, netDueColumn, createAtColumn;

    @FXML
    private Label netDueLabel;
    @FXML
    private Button logOutBtn;

    private MainController mainController;

    private Account currentAccount;
    private Stage tempStage;

    private List<ShiftDetailForInvoice> shiftDetailForInvoiceList;

    private final DecimalFormat df = new DecimalFormat("#.##");
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    public void initialize(MainController mainController,
                           Stage mainStage,
                           Stage tempStage
    ){
        this.tempStage = tempStage;
        this.mainController = mainController;
        this.currentAccount = mainController.getAccount();

        setupTable();
        loadData();

        logOutBtn.setOnAction(e-> {
            try {
                handleLogout(mainStage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    private void loadData(){
        Employee employee = EmployeeDAO.getEmployeeByAccountID(currentAccount.getAccountID());
        assert employee != null;
        employeeIDLabel.setText(employee.getEmployeeID());
        fullNameLabel.setText(employee.getFullName());
        phoneNumberLabel.setText(employee.getPhoneNumber());

        ShiftDetail shiftDetail = ShiftDetailDAO.getData(mainController.getShiftDetailID());

        numbOfPreOrderRoomLabel.setText(String.valueOf(shiftDetail.getNumbOfPreOrderRoom()));
        numbOfCheckInForm.setText(String.valueOf(shiftDetail.getNumbOfCheckInRoom()));
        numbOfCheckOutForm.setText(String.valueOf(shiftDetail.getNumbOfCheckOutRoom()));

        shiftDetailForInvoiceList = ShiftDetailForInvoiceDAO.getData(mainController.getShiftDetailID());
        setupTable();

        double[] netDueTotal = {0};
        shiftDetailForInvoiceList.forEach(x -> netDueTotal[0] += x.getInvoice().getNetDue());

        netDueLabel.setText(df.format(netDueTotal[0]) + " VND");
    }

    public void setupTable(){
        // Liên kết cột với dữ liệu
        invoiceIDColumn.setCellValueFactory(param ->
                new javafx.beans.property.SimpleStringProperty(param.getValue().getFirst()));
        customerIDColumn.setCellValueFactory(param ->
                new javafx.beans.property.SimpleStringProperty(param.getValue().get(1)));
        customerFullNameColumn.setCellValueFactory(param ->
                new javafx.beans.property.SimpleStringProperty(param.getValue().get(2)));
        netDueColumn.setCellValueFactory(param ->
                new javafx.beans.property.SimpleStringProperty(param.getValue().get(3)));
        createAtColumn.setCellValueFactory(param ->
                new javafx.beans.property.SimpleStringProperty(param.getValue().getLast()));

        // Thêm dữ liệu mẫu
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        if(shiftDetailForInvoiceList != null){
            shiftDetailForInvoiceList.forEach(x-> data.add(FXCollections.observableArrayList(
                    x.getInvoice().getInvoiceID(),
                    x.getInvoice().getReservationForm().getCustomer().getCustomerID(),
                    x.getInvoice().getReservationForm().getCustomer().getFullName(),
                    df.format(x.getInvoice().getNetDue()) + " VND",
                    x.getInvoice().getInvoiceDate().format(dtf)
            )));
        }

        // Gán dữ liệu cho TableView
        invoiceTableView.setItems(data);
    }

    private void handleLogout(Stage primaryStage) throws IOException {
        tempStage.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/LoginUI.fxml"));
        AnchorPane root = loader.load();

        Scene scene = new Scene(root);

        LoginController loginController = loader.getController();
        loginController.setupContext(primaryStage);
        primaryStage.setTitle("Quản Lý Khách Sạn");

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setWidth(610);
        primaryStage.setHeight(400);
        primaryStage.setMaximized(false);
        primaryStage.centerOnScreen();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }

}
