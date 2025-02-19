package iuh.fit.controller.features.room;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.controller.features.room.creating_reservation_form_controllers.RoomAvailableItemController;
import iuh.fit.controller.features.room.creating_reservation_form_controllers.RoomOnUseItemController;
import iuh.fit.controller.features.room.creating_reservation_form_controllers.RoomOverDueController;
import iuh.fit.dao.RoomCategoryDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.RoomWithReservationDAO;
import iuh.fit.models.Employee;
import iuh.fit.models.Room;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.RoomManagementService;
import iuh.fit.utils.TimelineManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static iuh.fit.utils.GlobalConstants.*;

public class RoomBookingController {
    @FXML
    private GridPane roomGridPane;

    @FXML
    private ComboBox<String> roomCategoryCBox, roomFloorNumberCBox;

    @FXML
    private Button allBtn, availableBtn,
            onUseBtn, overDueBtn;

    @FXML
    private DialogPane dialogPane;

    private List<RoomWithReservation> roomWithReservations;
    private MainController mainController;
    private Employee employee;

    private Button activeButton;
    private RoomStatus selectedStatus = null;

    private NotificationButtonController notificationButtonController;

    public void initialize() {
        dialogPane.toFront();
        activeButton = allBtn;
        setActiveButtonStyle(allBtn);
        MainController.setRoomBookingLoaded(true);

    }

    public void setupContext(MainController mainController, Employee employee,
                             NotificationButtonController notificationButtonController) {
        this.mainController = mainController;
        this.employee = employee;
        this.notificationButtonController = notificationButtonController;
        loadData();
        setupEventHandlers();

    }

    private List<String> getRoomCategories() {
        List<String> roomCategoryList = RoomCategoryDAO.getRoomCategory().stream()
                .map(rc -> rc.getRoomCategoryID() + " " + rc.getRoomCategoryName())
                .collect(Collectors.toList());
        roomCategoryList.addFirst("TẤT CẢ");
        return roomCategoryList;
    }

    private List<String> getFloorNumbers() {
        return List.of("TẤT CẢ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10");
    }

    private void loadData() {
        Task<List<RoomWithReservation>> loadDataTask = new Task<>() {
            @Override
            protected List<RoomWithReservation> call() {
                List<String> roomCategories = getRoomCategories();
                List<String> floorNumbers = getFloorNumbers();

                Platform.runLater(() -> {
                    roomCategoryCBox.getItems().setAll(roomCategories);
                    roomCategoryCBox.getSelectionModel().selectFirst();

                    roomFloorNumberCBox.getItems().setAll(floorNumbers);
                    roomFloorNumberCBox.getSelectionModel().selectFirst();
                });

                RoomManagementService.autoCheckoutOverdueRooms(notificationButtonController, mainController);
                return RoomWithReservationDAO.getRoomWithReservation().stream()
                        .sorted(Comparator.comparing(r -> r.getRoom().getRoomNumber()))
                        .toList();
            }
        };

        loadDataTask.setOnSucceeded(event -> {
            roomWithReservations = loadDataTask.getValue();
            displayFilteredRooms(roomWithReservations);
            loadDataForBtn();
            TimelineManager.getInstance().printAllTimelines();
        });

        loadDataTask.setOnFailed(event -> {
            throw new IllegalArgumentException(loadDataTask.getException().getMessage());
        });

        new Thread(loadDataTask).start();
    }

    private void loadDataForBtn() {
        HashMap<RoomStatus, Integer> roomStatusCount = RoomDAO.getRoomStatusCount();
        availableBtn.setText(ROOM_BOOKING_AVAIL_BTN + "("+ roomStatusCount.get(RoomStatus.AVAILABLE) + ")");
        onUseBtn.setText(ROOM_BOOKING_ON_USE_BTN + "("+ roomStatusCount.get(RoomStatus.ON_USE) + ")");
        overDueBtn.setText(ROOM_BOOKING_OVER_DUE_BTN + "("+ roomStatusCount.get(RoomStatus.OVERDUE) + ")");
        allBtn.setText(ROOM_BOOKING_ALL_BTN + "("+roomWithReservations.size()+")");
    }

    private void displayFilteredRooms(List<RoomWithReservation> roomsWithReservations) {
        roomGridPane.getChildren().clear();

        int row = 0, col = 0;

        try {
            for (RoomWithReservation roomWithReservation : roomsWithReservations) {
                Pane roomItem = loadRoomItem(roomWithReservation);

                roomGridPane.add(roomItem, col, row);

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

    private Pane loadRoomItem(RoomWithReservation roomWithReservation) throws IOException {
        FXMLLoader loader;
        Pane roomItem;

        Room room = roomWithReservation.getRoom();

        switch (room.getRoomStatus()) {
            case AVAILABLE -> {
                loader = new FXMLLoader(getClass().getResource(
                        "/iuh/fit/view/features/room/creating_reservation_form_panels/RoomAvailableItem.fxml"));
                roomItem = loader.load();

                RoomAvailableItemController controller = loader.getController();
                controller.setupContext(mainController, employee, roomWithReservation, notificationButtonController);
            }
            case ON_USE -> {
                loader = new FXMLLoader(getClass().getResource(
                        "/iuh/fit/view/features/room/creating_reservation_form_panels/RoomOnUseItem.fxml"));
                roomItem = loader.load();

                RoomOnUseItemController controller = loader.getController();
                controller.setupContext(mainController, employee, roomWithReservation, notificationButtonController);
            }
            case OVERDUE -> {
                loader = new FXMLLoader(getClass().getResource(
                        "/iuh/fit/view/features/room/creating_reservation_form_panels/RoomOverDueItem.fxml"));
                roomItem = loader.load();

                RoomOverDueController controller = loader.getController();
                controller.setupContext(mainController, employee, roomWithReservation, notificationButtonController);
            }
            default -> throw new IllegalStateException("Unexpected value: " + room.getRoomStatus());
        }
        return roomItem;
    }

    private void handleSearch() {
        if (roomWithReservations == null) {
            return;
        }

        List<RoomWithReservation> filteredRooms = roomWithReservations;

        String selectedCategory = roomCategoryCBox.getSelectionModel().getSelectedItem();
        if (selectedCategory != null && !selectedCategory.equals("TẤT CẢ")) {
            String categoryID = selectedCategory.split(" ")[0];
            filteredRooms = filteredRooms.stream()
                    .filter(r -> r.getRoom().getRoomCategory().getRoomCategoryID().equals(categoryID))
                    .toList();
        }

        String selectedFloor = roomFloorNumberCBox.getSelectionModel().getSelectedItem();
        if (selectedFloor != null && !selectedFloor.equals("TẤT CẢ")) {
            filteredRooms = filteredRooms.stream()
                    .filter(r -> r.getRoom().getRoomID().charAt(2) == selectedFloor.charAt(0))
                    .toList();
        }

        if (selectedStatus != null) {
            filteredRooms = filteredRooms.stream()
                    .filter(r -> r.getRoom().getRoomStatus() == selectedStatus)
                    .toList();
        }

        filteredRooms = filteredRooms.stream()
                .sorted(Comparator.comparing(r -> r.getRoom().getRoomNumber()))
                .toList();

        displayFilteredRooms(filteredRooms);
        loadDataForBtn();
    }

    private void setupEventHandlers() {
        setupButtonAction(allBtn, null);
        setupButtonAction(availableBtn, RoomStatus.AVAILABLE);
        setupButtonAction(onUseBtn, RoomStatus.ON_USE);
        setupButtonAction(overDueBtn, RoomStatus.OVERDUE);

        roomCategoryCBox.setOnAction(e -> handleSearch());
        roomFloorNumberCBox.setOnAction(e -> handleSearch());
    }

    private void setupButtonAction(Button button, RoomStatus status) {
        button.setOnAction(e -> {
            if (activeButton == button) {
                resetButtonStyle(button);
                setActiveButtonStyle(allBtn);
                activeButton = allBtn;
                selectedStatus = null;
            } else {
                if (activeButton != null) resetButtonStyle(activeButton);
                setActiveButtonStyle(button);
                activeButton = button;
                selectedStatus = status;
            }
            handleSearch();
        });
    }

    private void setActiveButtonStyle(Button button) {
        switch (button.getId()) {
            case "allBtn" -> button.getStyleClass().add("button-All-selected");
            case "availableBtn" -> button.getStyleClass().add("button-Available-selected");
            case "onUseBtn" -> button.getStyleClass().add("button-OnUse-selected");
            case "overDueBtn" -> button.getStyleClass().add("button-OverDue-selected");
            default -> throw new IllegalArgumentException("Không tìm thấy button ID");
        }
    }

    private void resetButtonStyle(Button button) {
        switch (button.getId()) {
            case "allBtn" -> button.getStyleClass().remove("button-All-selected");
            case "availableBtn" -> button.getStyleClass().remove("button-Available-selected");
            case "onUseBtn" -> button.getStyleClass().remove("button-OnUse-selected");
            case "overDueBtn" -> button.getStyleClass().remove("button-OverDue-selected");
            default -> throw new IllegalArgumentException("Không tìm thấy button ID");
        }
    }


    public DialogPane getDialogPane() {
        return dialogPane;
    }

}
