package iuh.fit.controller.features.room;

import iuh.fit.dao.RoomDialogDAO;
import iuh.fit.models.Room;
import iuh.fit.models.RoomDialog;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class RoomDialogViewController {
    @FXML
    private TextField roomIDTextField, roomCategoryNameTextField,
            roomFloorNumberTextField, roomStatusTextField;

    @FXML
    private TableView<RoomDialog> roomDialogTableView;
    @FXML
    private TableColumn<RoomDialog, String> indexColumn;
    @FXML
    private TableColumn<RoomDialog, String> dialogTypeColumn;
    @FXML
    private TableColumn<RoomDialog, String> dialogColumn;
    @FXML
    private TableColumn<RoomDialog, String> timeStampColumn;
    @FXML
    private TableColumn<RoomDialog, String> reservationFormIDColumn;

    private Room room;
    private final ObservableList<RoomDialog> roomDialogItems = FXCollections.observableArrayList();

    public void setRoom(Room room) {
        this.room = room;

        roomIDTextField.setText(room.getRoomID());
        roomCategoryNameTextField.setText(room.getRoomCategory().getRoomCategoryName());
        roomFloorNumberTextField.setText(room.getRoomFloorNumber());
        roomStatusTextField.setText(room.getRoomStatus().toString());

        loadRoomDialogs();
        setupTable();
    }

    private void setupTable() {
        indexColumn.setCellValueFactory(param ->
                new SimpleStringProperty(String.valueOf(roomDialogTableView.getItems().indexOf(param.getValue()) + 1)));
        dialogTypeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDialogType().toString()));
        dialogColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDialog()));
        timeStampColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTimestamp().toString()));
        reservationFormIDColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReservationForm() != null
                        ? cellData.getValue().getReservationForm().getReservationID()
                        : "N/A"));

        roomDialogTableView.setItems(roomDialogItems);
    }

    private void loadRoomDialogs() {
        Task<List<RoomDialog>> loadTask = new Task<>() {
            @Override
            protected List<RoomDialog> call() {
                return RoomDialogDAO.getDataByRoomID(room.getRoomID());
            }
        };

        loadTask.setOnSucceeded(e -> Platform.runLater(() -> {
            roomDialogItems.setAll(loadTask.getValue());
            roomDialogTableView.refresh();
        }));

        loadTask.setOnFailed(e -> {
            System.out.println("Không tải được dữ liệu");
            e.getSource().getException().printStackTrace();
        });

        new Thread(loadTask).start();
    }


}
