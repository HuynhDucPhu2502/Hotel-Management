package iuh.fit.controller.features.service;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.HotelServiceDAO;
import iuh.fit.dao.ServiceCategoryDAO;
import iuh.fit.models.HotelService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.ErrorMessages;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class HotelServiceManagerController {
    // Search Fields
    @FXML
    private ComboBox<String> hotelServiceIDSearchField;
    @FXML
    private TextField hotelSerivceNameSearchField;
    @FXML
    private TextField priceSearchField;
    @FXML
    private TextField serviceCategoryNameSearchField;
    @FXML
    private TextField descriptionSearchField;


    // Input Fields
    @FXML
    private TextField serviceIDTextField;
    @FXML
    private TextField serviceNameTextField;
    @FXML
    private TextField servicePriceTextField;
    @FXML
    private ComboBox<String> serviceCategoryCBox;
    @FXML
    private TextArea descriptionTextField;


    // Table
    @FXML
    private TableView<HotelService> hotelServiceTableView;
    @FXML
    private TableColumn<HotelService, String> serviceIDColumn;
    @FXML
    private TableColumn<HotelService, String> serviceNameColumn;
    @FXML
    private TableColumn<HotelService, String> priceColumn;
    @FXML
    private TableColumn<HotelService, String> serviceCategoryColumn;
    @FXML
    private TableColumn<HotelService, String> descriptionColumn;
    @FXML
    private TableColumn<HotelService, Void> actionColumn;

    // Buttons
    @FXML
    private Button resetBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;

    // Dialog
    @FXML
    private DialogPane dialogPane;

    private ObservableList<HotelService> items;

    // Gọi mấy phương thức để gắn sự kiện và dữ liệu cho lúc đầu khởi tạo giao diện
    public void initialize() {
        dialogPane.toFront();
        hotelServiceTableView.setFixedCellSize(40);
        setupTable();
        loadData();


        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
        updateBtn.setOnAction(e -> handleUpdateAction());
        hotelServiceIDSearchField.setOnAction(e -> handleSearchAction());
    }

    // Phương thức load dữ liệu lên giao diện
    private void loadData() {
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() {

                return null;
            }
        };

        List<HotelService> hotelServiceList = HotelServiceDAO.getHotelService();
        items = FXCollections.observableArrayList(hotelServiceList);

        List<String> comboBoxItems = ServiceCategoryDAO.getServiceCategory()
                .stream()
                .map(serviceCategory -> serviceCategory.getServiceCategoryID() + " " + serviceCategory.getServiceCategoryName())
                .collect(Collectors.toList());
        Platform.runLater(() -> {
            serviceCategoryCBox.getItems().setAll(comboBoxItems);
            if (!serviceCategoryCBox.getItems().isEmpty()) {
                serviceCategoryCBox.getSelectionModel().selectFirst();
            }
            hotelServiceTableView.setItems(items);
            hotelServiceTableView.refresh();

            List<String> Ids = HotelServiceDAO.getTopThreeID();
            hotelServiceIDSearchField.getItems().setAll(Ids);
            serviceIDTextField.setText(HotelServiceDAO.getNextHotelServiceID());
        });

        loadDataTask.setOnRunning(e -> setButtonsDisabled(true));
        loadDataTask.setOnSucceeded(e -> setButtonsDisabled(false));

        new Thread(loadDataTask).start();
    }

    // Phương thức đổ dữ liệu vào bảng
    private void setupTable() {
        serviceIDColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        serviceNameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));

        serviceCategoryColumn.setCellValueFactory(data -> {
            ServiceCategory category = data.getValue().getServiceCategory();
            String categoryName = (category != null && category.getServiceCategoryName() != null)
                    ? category.getServiceCategoryName()
                    : "KHÔNG CÓ";
            return new SimpleStringProperty(categoryName);
        });

        setupHotelServiceDescriptionColumn();
        setupActionColumn();

        hotelServiceTableView.setItems(items);
    }

    // setup cho cột mô tả
    // THAM KHẢO
    private void setupHotelServiceDescriptionColumn() {
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    setStyle(null);
                } else {
                    text.setText(item);
                    text.wrappingWidthProperty().bind(descriptionColumn.widthProperty());
                    setGraphic(text);

                    TableRow<?> currentRow = getTableRow();

                    currentRow.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> updateTextColor(currentRow));

                    hotelServiceTableView.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                        if (currentRow.isSelected()) {
                            updateTextColor(currentRow);
                        }
                    });

                }
            }

            private void updateTextColor(TableRow<?> currentRow) {
                if (currentRow.isSelected()) {
                    if (hotelServiceTableView.isFocused()) {
                        text.setStyle("-fx-fill: white;");
                    } else {
                        text.setStyle("-fx-fill: black;");
                    }
                } else {
                    text.setStyle("-fx-fill: black;");
                }
            }
        });
    }

    // setup cho cột thao tác
    // THAM KHẢO
    private void setupActionColumn() {
        Callback<TableColumn<HotelService, Void>, TableCell<HotelService, Void>> cellFactory = param -> new TableCell<>() {
            private final Button updateButton = new Button("Cập nhật");
            private final Button deleteButton = new Button("Xóa");
            private final HBox hBox = new HBox(10);

            {
                updateButton.getStyleClass().add("button-update");
                deleteButton.getStyleClass().add("button-delete");

                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().addAll(updateButton, deleteButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableView() == null || getTableRow() == null) {
                    setGraphic(null);
                    return;
                }

                HotelService hotelService = getTableRow().getItem();
                if (hotelService == null) {
                    setGraphic(null);
                    return;
                }

                updateButton.setOnAction(event -> handleUpdateBtn(hotelService));
                deleteButton.setOnAction(event -> handleDeleteAction(hotelService));

                boolean hasRoomInUse = HotelServiceDAO.isHotelServiceInUse(hotelService.getServiceId());
                updateButton.setDisable(hasRoomInUse);
                deleteButton.setDisable(hasRoomInUse);

                setGraphic(hBox);
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    // Chức năng 1: Làm mới
    private void handleResetAction() {
        serviceIDTextField.setText(HotelServiceDAO.getNextHotelServiceID());
        serviceNameTextField.setText("");
        servicePriceTextField.setText("");
        if (!serviceCategoryCBox.getItems().isEmpty()) {
            serviceCategoryCBox.getSelectionModel().selectFirst();
        }
        descriptionTextField.setText("");

        addBtn.setManaged(true);
        addBtn.setVisible(true);
        updateBtn.setManaged(false);
        updateBtn.setVisible(false);
    }

    // Chức năng 2: Thêm
    private void handleAddAction() {
        try {
            String selectedService = serviceCategoryCBox.getSelectionModel().getSelectedItem();
            String serviceCategoryId = selectedService.split(" ")[0];
            ServiceCategory serviceCategory = ServiceCategoryDAO.getDataByID(serviceCategoryId);

            HotelService hotelService = new HotelService(
                    serviceIDTextField.getText(),
                    serviceNameTextField.getText(),
                    ConvertHelper.doubleConverter(servicePriceTextField.getText(), ErrorMessages.HOTEL_SERVICE_INVALID_FORMAT),
                    descriptionTextField.getText(),
                    serviceCategory,
                    ObjectStatus.ACTIVATE
            );

            Task<Void> addTask = new Task<>() {
                @Override
                protected Void call() {
                    HotelServiceDAO.createData(hotelService);
                    return null;
                }
            };

            addTask.setOnRunning(e -> setButtonsDisabled(true));
            addTask.setOnSucceeded(e -> Platform.runLater(() -> {
                handleResetAction();
                loadData();
            }));

            new Thread(addTask).start();
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 3: Xóa
    private void handleDeleteAction(HotelService hotelService) {
        DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn xóa dịch vụ này?");
        dialog.onClose(buttonType -> {
            if (buttonType == ButtonType.YES) {
                Task<Void> deleteTask = new Task<>() {
                    @Override
                    protected Void call() {
                        HotelServiceDAO.deleteData(hotelService.getServiceId());
                        return null;
                    }
                };

                deleteTask.setOnRunning(e -> setButtonsDisabled(true));
                deleteTask.setOnSucceeded(e -> Platform.runLater(() -> {
                    handleResetAction();
                    loadData();
                }));

                new Thread(deleteTask).start();
            }
        });
    }

    // Chức năng 4: Cập nhật
    // 4.1 Xử lý sự kiện khi kích hoạt chức năng cập nhật
    private void handleUpdateBtn(HotelService hotelService) {
        serviceIDTextField.setText(hotelService.getServiceId());
        serviceNameTextField.setText(hotelService.getServiceName());
        servicePriceTextField.setText(String.valueOf(hotelService.getServicePrice()));

        if (hotelService.getServiceCategory() != null) {
            String serviceCategoryDisplay = hotelService.getServiceCategory().getServiceCategoryID()
                    + " " + hotelService.getServiceCategory().getServiceCategoryName();
            serviceCategoryCBox.getSelectionModel().select(serviceCategoryDisplay);
        } else {
            serviceCategoryCBox.getSelectionModel().clearSelection();
        }

        descriptionTextField.setText(hotelService.getDescription());
        serviceNameTextField.requestFocus();

        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    // 4.2 Chức năng cập nhật
    private void handleUpdateAction() {
        try {
            String selectedService = serviceCategoryCBox.getSelectionModel().getSelectedItem();
            String serviceCategoryId = selectedService.split(" ")[0];
            ServiceCategory serviceCategory = ServiceCategoryDAO.getDataByID(serviceCategoryId);
            System.out.println(serviceCategory);
            HotelService hotelService = new HotelService(
                    serviceIDTextField.getText(),
                    serviceNameTextField.getText(),
                    ConvertHelper.doubleConverter(servicePriceTextField.getText(), ErrorMessages.HOTEL_SERVICE_INVALID_FORMAT),
                    descriptionTextField.getText(),
                    serviceCategory,
                    ObjectStatus.ACTIVATE
            );

            DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn cập nhật dịch vụ này?");
            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    Task<Void> updateTask = new Task<>() {
                        @Override
                        protected Void call() {
                            HotelServiceDAO.updateData(hotelService);
                            return null;
                        }
                    };

                    updateTask.setOnRunning(e -> setButtonsDisabled(true));
                    updateTask.setOnSucceeded(e -> Platform.runLater(() -> {
                        loadData();
                        handleResetAction();
                        toggleAddUpdateButtons();
                        setButtonsDisabled(false);
                    }));

                    new Thread(updateTask).start();
                }
            });
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 5: Tìm kiếm
    private void handleSearchAction() {
        String searchText = hotelServiceIDSearchField.getValue();

        Task<ObservableList<HotelService>> searchTask = new Task<>() {
            @Override
            protected ObservableList<HotelService> call() {
                List<HotelService> hotelServices = (searchText == null || searchText.isEmpty())
                        ? HotelServiceDAO.getHotelService()
                        : HotelServiceDAO.findDataByContainsId(searchText);
                return FXCollections.observableArrayList(hotelServices);
            }
        };

        searchTask.setOnSucceeded(e -> Platform.runLater(() -> {
            items = searchTask.getValue();
            hotelServiceTableView.setItems(items);
            hotelServiceTableView.refresh();

            if (items.size() == 1) {
                HotelService hotelService = items.getFirst();
                hotelSerivceNameSearchField.setText(hotelService.getServiceName());
                priceSearchField.setText(String.valueOf(hotelService.getServicePrice()));
                serviceCategoryNameSearchField.setText(hotelService.getServiceCategory() != null
                        ? hotelService.getServiceCategory().getServiceCategoryName()
                        : "KHÔNG CÓ");
                descriptionSearchField.setText(hotelService.getDescription());
            } else {
                hotelSerivceNameSearchField.clear();
                priceSearchField.clear();
                serviceCategoryNameSearchField.clear();
                descriptionSearchField.clear();
            }
        }));

        Thread searchThread = new Thread(searchTask);
        searchThread.setDaemon(true);
        searchThread.start();
    }


    private void setButtonsDisabled(boolean disable) {
        addBtn.setDisable(disable);
        updateBtn.setDisable(disable);
        resetBtn.setDisable(disable);
    }

    private void toggleAddUpdateButtons() {
        addBtn.setVisible(true);
        updateBtn.setVisible(false);
        addBtn.setManaged(true);
        updateBtn.setManaged(false);
    }

    public void setInformation(HotelService service){
        Platform.runLater(() -> {
            hotelServiceIDSearchField.setValue(service.getServiceId());
            handleUpdateBtn(service);
        });
    }
}
