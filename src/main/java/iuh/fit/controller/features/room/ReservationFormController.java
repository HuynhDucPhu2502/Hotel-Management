package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.controller.MainController;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ReservationFormController {
    // Buttons
    @FXML
    private Button backBtn;

    // Input Fields
    // 1. Thông tin phòng
    @FXML
    private TextField roomNumberTextField;
    @FXML
    private TextField roomCategoryTextField;
    @FXML
    private DateRangePicker bookDateRangePicker;
    @FXML
    private TextField checkInDateTextField;
    @FXML
    private TextField checkOutDateTextField;
    // 2. Thông tin khách hàng
    // 3. Thông tin nhân viên
    @FXML
    private TextField employeeIDTextField;
    @FXML
    private TextField employeeFullNameTextField;

    // Context
    private MainController mainController;
    private Employee employee;
    private Room room;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.forLanguageTag("vi-VN"));

    public void initialize() {

    }

    public void setupContext(MainController mainController, Employee employee, Room room) {
        this.mainController = mainController;
        this.employee = employee;
        this.room = room;


        backBtn.setOnAction(e -> handleBackBtn());
        setupBookDateRangePicker();
        setupRoomInformation();
        setupEmployeeInformation();
    }

    private void setupBookDateRangePicker() {
        ObjectProperty<DateRange> selectedRangeProperty = bookDateRangePicker.valueProperty();

        checkInDateTextField.textProperty().bind(
                Bindings.createStringBinding(
                        () -> selectedRangeProperty.get() != null
                                ? dateTimeFormatter.format(selectedRangeProperty.get().getStartDate())
                                : "",
                        selectedRangeProperty
                )
        );

        checkOutDateTextField.textProperty().bind(
                Bindings.createStringBinding(
                        () -> selectedRangeProperty.get() != null
                                ? dateTimeFormatter.format(selectedRangeProperty.get().getEndDate())
                                : "",
                        selectedRangeProperty
                )
        );
    }

    private void setupRoomInformation() {
        roomNumberTextField.setText(room.getRoomNumber());
        roomCategoryTextField.setText(room.getRoomCategory().getRoomCategoryName());
    }

    private void setupEmployeeInformation() {
        employeeIDTextField.setText(employee.getEmployeeID());
        employeeFullNameTextField.setText(employee.getFullName());
    }

    private void handleBackBtn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
            AnchorPane layout = loader.load();

            RoomBookingController roomBookingController = loader.getController();
            roomBookingController.setupContext(mainController, employee);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
