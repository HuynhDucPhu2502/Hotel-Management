package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.daterange.DateRange;
import com.dlsc.gemsfx.daterange.DateRangePicker;
import iuh.fit.controller.MainController;
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
    @FXML
    private DateRangePicker bookDateRangePicker;
    @FXML
    private TextField checkInDateTextField;
    @FXML
    private TextField checkOutDateTextField;

    // Main Controller
    private MainController mainController;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.forLanguageTag("vi-VN"));

    public void initialize() {

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;


        backBtn.setOnAction(e -> handleBackBtn());
        setupBookDateRangePicker();
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

    private void handleBackBtn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
            AnchorPane layout = loader.load();

            RoomBookingController roomBookingController = loader.getController();
            roomBookingController.setMainController(mainController);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
