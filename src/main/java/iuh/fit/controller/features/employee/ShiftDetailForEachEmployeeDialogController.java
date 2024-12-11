package iuh.fit.controller.features.employee;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.ShiftAssignmentDAO;
import iuh.fit.dao.ShiftDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ShiftDetailForEachEmployeeDialogController {

    @FXML
    private Label shiftIDLabel;

    @FXML
    private Label startTimeLabel;

    @FXML
    private Label endTimeLabel;

    @FXML
    private Label scheduleLabel;

    @FXML
    private Label fixedLabel1;
    @FXML
    private Label fixedLabel2;

    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> employeeIDColumn;
    @FXML
    private TableColumn<Employee, String> fullNameColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;

    @FXML
    private ComboBox<Shift> shiftComboBox;

    @FXML
    private Button updateBtn;
    @FXML
    private Button forceDeteleBtn;
    @FXML
    private Button forceUpdateBtn;
    @FXML
    private Button cancelBtn;

    private ObservableList<Employee> items;
    private ShiftManagerController shiftManagerControllerReferences;
    @FXML
    private DialogPane dialogPane;

    public void setController(ShiftManagerController shiftManagerController) {
        this.shiftManagerControllerReferences = shiftManagerController;
    }


    public void getComponentFormController(){
    }

    public void getData(Shift shift, String func, String shiftID){
        dialogPane.toFront();
        loadData(shift, shiftID);
        setupTable();


        updateBtn.getStyleClass().add("button-update");
        forceDeteleBtn.getStyleClass().add("button-delete");
        forceUpdateBtn.getStyleClass().add("button-delete");
        updateBtn.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());
        forceDeteleBtn.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());
        forceUpdateBtn.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

        if(func.equals("delete")){
            handelDelete();
            updateBtn.setOnAction(e->handelUpdateForAllEmployeeWhenDelete(shift));
            forceDeteleBtn.setOnAction(e->handelForceDeleteShift(shift));
            cancelBtn.setOnAction(e->handeCancel());
        }else{
            handelUpdate();
            updateBtn.setOnAction(e->handelUpdateForAllEmployee(shiftID));
            forceUpdateBtn.setOnAction(e->handelForceUpdateForAllEmployee());
        }
    }

    private void loadData(Shift shift, String shiftID) {

        List<Employee> employees;
        if(shift != null){
            List<Shift> shiftList = ShiftDAO.getShifts();
            List<Shift> filteredShiftList = shiftList.stream()
                    .filter(x-> !x.getShiftID().equalsIgnoreCase(shift.getShiftID())).collect(Collectors.toList());
            shiftComboBox.getItems().setAll(filteredShiftList);
            shiftComboBox.getSelectionModel().selectFirst();

            employees = ShiftDAO.getEmployeeListByShift(shift.getShiftID());
            shiftIDLabel.setText(shift.getShiftID());
            startTimeLabel.setText(shift.getStartTime().toString());
            endTimeLabel.setText(shift.getEndTime().toString());
            scheduleLabel.setText(shift.getShiftDaysSchedule().toString());
        }else{
            List<Shift> shiftList = ShiftDAO.getShifts();
            List<Shift> filteredShiftList = shiftList.stream()
                    .filter(x-> !x.getShiftID().equalsIgnoreCase(shiftID)).collect(Collectors.toList());
            shiftComboBox.getItems().setAll(filteredShiftList);
            shiftComboBox.getSelectionModel().selectFirst();

            Shift getShift = ShiftDAO.getDataByID(shiftID);
            assert getShift != null;
            shiftIDLabel.setText(getShift.getShiftID());
            startTimeLabel.setText(getShift.getStartTime().toString());
            endTimeLabel.setText(getShift.getEndTime().toString());
            scheduleLabel.setText(getShift.getShiftDaysSchedule().toString());
            employees = ShiftDAO.getEmployeeListByShift(getShift.getShiftID());
        }

        items = FXCollections.observableArrayList(employees);
        employeeTable.setItems(items);
        employeeTable.refresh();

    }

    // Phương thức đổ dữ liệu vào bảng
    private void setupTable() {
        employeeIDColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        employeeTable.setItems(items);
    }

    private void handelUpdateForAllEmployeeWhenDelete(Shift currentShift){
        Shift newShift = shiftComboBox.getSelectionModel().getSelectedItem();
        try{

            DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                    "XÁC NHẬN",
                    "Đã cập nhật thành công ca làm nhân viên cho ca làm: "+ currentShift.getShiftID() +
                            "\nBạn có chắc chắn muốn xóa ca làm này?"
            );

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    ShiftAssignmentDAO.updateShiftAssignmentWithoutID(currentShift, newShift);
                    handeDelete(currentShift);
                }
            });

        }catch(Exception e){
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    private void handelForceDeleteShift(Shift currentShift){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Xác nhận");
        dialog.setContentText("Bạn có muốn xóa ca làm: "+ currentShift.getShiftID() + " hay không?\n" +
                "Một khi đã xóa thì không thể sử dụng lại được.\nSuy nghĩ kĩ trước khi xóa.");
        javafx.scene.control.DialogPane dialogPane = dialog.getDialogPane();

        // Tạo nút xác nhận và nút hủy
        ButtonType confirmButtonType = new ButtonType("Xác nhận (5)");
        ButtonType cancelButtonType = new ButtonType("Hủy");

        dialogPane.getButtonTypes().addAll(confirmButtonType, cancelButtonType);


        // Lấy Button từ DialogPane
        javafx.scene.control.Button confirmButton =
                (javafx.scene.control.Button) dialogPane.lookupButton(confirmButtonType);

        confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
            handeDelete(currentShift);
            dialog.close();
        });

        //vô hiệu hóa nút
        confirmButton.setDisable(true);

        // Đếm ngược thời gian
        final int[] countdown = {5}; // Thời gian đếm ngược bắt đầu từ 10 giây
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            countdown[0]--; // Giảm thời gian đếm ngược
            if(countdown[0] != 0){
                confirmButton.setText("Xác nhận (" + countdown[0] + ")");
            }else{
                confirmButton.setText("Xác nhận");
            }
            // Khi hết thời gian đếm ngược, cho phép sử dụng nút
            if (countdown[0] <= 0) {
                confirmButton.setDisable(false);
            }
        }));

        timeline.setCycleCount(5); // Chạy đếm ngược trong 5 lần (5 giây)
        timeline.play(); // Bắt đầu đếm ngược

        // Hiển thị Dialog và chờ kết quả
        dialog.showAndWait();
    }

    public void handeDelete(Shift shift){
        ShiftAssignmentDAO.deleteShiftWithShiftID(shift.getShiftID());
        handeCancel();
        shiftManagerControllerReferences.loadData();
    }

    public void handeUpdate(String oldID, Shift shift) throws SQLException {
        ShiftDAO.updateDataWithTwoTableAtTheSameTime(oldID, shift);
        ShiftDAO.updateDataWithOldID(oldID, shift);
        handeCancel();
        shiftManagerControllerReferences.loadData();
    }

    private void handeCancel() {
        scheduleLabel.setText("");
        endTimeLabel.setText("");
        shiftIDLabel.setText("");
        startTimeLabel.setText("");

        // Tắt cửa sổ hiện tại
        Stage stage = (Stage) scheduleLabel.getScene().getWindow(); // Lấy `Stage` chứa `scheduleLabel`
        stage.close();
    }

    private void handelUpdate(){
        forceDeteleBtn.setManaged(false);
        forceDeteleBtn.setVisible(false);

        cancelBtn.setManaged(false);
        cancelBtn.setVisible(false);

        fixedLabel1.setText("");
        fixedLabel2.setText("");
    }

    private void handelDelete(){
        forceUpdateBtn.setManaged(false);
        forceUpdateBtn.setVisible(false);
    }

    private void handelUpdateForAllEmployee(String shiftID){
        Shift newShift = shiftComboBox.getSelectionModel().getSelectedItem();
        try{

            DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation(
                    "XÁC NHẬN",
                    "Đã chuyển thành công ca làm nhân viên cho ca làm: "+ shiftID +
                            "\nBạn có chắc chắn muốn cập nhật ca làm này?"
            );

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    ShiftAssignmentDAO.updateShiftAssignmentWithID(shiftID, newShift);
                    try {
                        handeUpdate(shiftManagerControllerReferences.getOldShiftID(), shiftManagerControllerReferences.getShiftInfo());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        }catch(Exception e){
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    private void handelForceUpdateForAllEmployee(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Xác nhận");
        dialog.setContentText("Bạn có muốn cập nhật ca làm: "+ shiftManagerControllerReferences.getOldShiftID() + " hay không?\n" +
                "Mọi nhân viên trong ca làm này đều sẽ được cập nhật\nSuy nghĩ kĩ trước khi cập nhật.");
        javafx.scene.control.DialogPane dialogPane = dialog.getDialogPane();

        // Tạo nút xác nhận và nút hủy
        ButtonType confirmButtonType = new ButtonType("Xác nhận (5)");
        ButtonType cancelButtonType = new ButtonType("Hủy");

        dialogPane.getButtonTypes().addAll(confirmButtonType, cancelButtonType);


        // Lấy Button từ DialogPane
        javafx.scene.control.Button confirmButton =
                (javafx.scene.control.Button) dialogPane.lookupButton(confirmButtonType);

        confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
            try {
                handeUpdate(shiftManagerControllerReferences.getOldShiftID(), shiftManagerControllerReferences.getShiftInfo());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            dialog.close();
        });

        //vô hiệu hóa nút
        confirmButton.setDisable(true);

        // Đếm ngược thời gian
        final int[] countdown = {5}; // Thời gian đếm ngược bắt đầu từ 10 giây
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            countdown[0]--; // Giảm thời gian đếm ngược
            if(countdown[0] != 0){
                confirmButton.setText("Xác nhận (" + countdown[0] + ")");
            }else{
                confirmButton.setText("Xác nhận");
            }
            // Khi hết thời gian đếm ngược, cho phép sử dụng nút
            if (countdown[0] <= 0) {
                confirmButton.setDisable(false);
            }
        }));

        timeline.setCycleCount(5); // Chạy đếm ngược trong 5 lần (5 giây)
        timeline.play(); // Bắt đầu đếm ngược

        // Hiển thị Dialog và chờ kết quả
        dialog.showAndWait();

    }
}
