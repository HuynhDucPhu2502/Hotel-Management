package iuh.fit.controller.features.statistics;

import iuh.fit.controller.MainController;
import iuh.fit.controller.features.TopController;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 10/12/2024 - 9:59 AM
 * @project HotelManagement
 * @package iuh.fit.controller.features.statistics
 */
public class AnalyzeBeforeLogOutController {
    private static final Logger log = LoggerFactory.getLogger(AnalyzeBeforeLogOutController.class);
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

    private Button eventBtn;
    private MainController mainController;
    private Account currentAccount;
    private Stage PrevStage;
    private TopController topController;
    private List<ShiftDetailForInvoice> shiftDetailForInvoiceList;
    private Stage loginStage;

    private DecimalFormat df = new DecimalFormat("#.##");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public void initialize(MainController mainController, TopController topController){
        if(topController != null){
            setupController(mainController, topController);
            setupTable();
            setupContext();
            loadData();
            logOutBtn.setOnAction(e-> {
                try {
                    handelLogOut();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }else{
            this.mainController = mainController;
            setupTable();
            setupContext();
            loadData();
            logOutBtn.setText("Thoát");
            logOutBtn.setOnAction(e->{
                Stage currentStage = (Stage) logOutBtn.getScene().getWindow();
                Stage mainStage = (Stage) (mainController.getMainPanel().getScene().getWindow()); // Lấy `Stage` chứa `scheduleLabel`
                if(mainStage != null){
                    mainStage.close();
                }
                if(loginStage != null){
                    loginStage.close();
                }
                currentStage.close();

                Platform.exit();
                System.exit(0);
            });
        }
    }

    public void setupController(MainController mainController, TopController topController){
        this.mainController = mainController;
        this.topController = topController;
//        eventBtn = button;
    }

    public void setupContext(){
        currentAccount = mainController.getAccount();
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

    private void handelLogOut() throws IOException {
        // Tắt cửa sổ hiện tại
        Stage stage = (Stage) logOutBtn.getScene().getWindow(); // Lấy `Stage` chứa `scheduleLabel`
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/iuh/fit/view/ui/LoginUI.fxml"));
        AnchorPane loginPane = fxmlLoader.load();

        Stage currentStage = (Stage) (mainController.getMainPanel().getScene().getWindow());

        this.loginStage = currentStage;

        Scene loginScene = new Scene(loginPane);

        currentStage.setScene(loginScene);

        currentStage.setResizable(false);
        currentStage.setWidth(610);
        currentStage.setHeight(400);
        currentStage.setMaximized(false);
        currentStage.centerOnScreen();

        currentStage.show();
        currentStage.centerOnScreen();
        currentStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void getComponentFormController(){
        PrevStage = topController.getStage();
    }

}
