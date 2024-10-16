package iuh.fit.controller.features.service;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.HotelServiceDAO;
import iuh.fit.dao.ServiceCategoryDAO;
import iuh.fit.models.HotelService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.ErrorMessages;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.LinkedHashMap;
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
    private TableColumn<HotelService, String> hotelServiceIDColumn;
    @FXML
    private TableColumn<HotelService, String> hotelServiceNameColumn;
    @FXML
    private TableColumn<HotelService, String> hotelServicePriceColumn;
    @FXML
    private TableColumn<HotelService, String> serviceCategoryNameColumn;
    @FXML
    private TableColumn<HotelService, String> hotelServiceDescriptionColumn;
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

        loadData();
        setupTable();

        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
        updateBtn.setOnAction(e -> handleUpdateAction());
        hotelServiceIDSearchField.setOnAction(e -> handleSearchAction());
    }

    // Phương thức load dữ liệu lên giao diện
    private void loadData() {
        serviceIDTextField.setText(HotelServiceDAO.getNextHotelServiceID());

        List<String> Ids = HotelServiceDAO.getTopThreeID();
        hotelServiceIDSearchField.getItems().setAll(Ids);

        List<String> comboBoxItems = ServiceCategoryDAO.getServiceCategory()
                .stream()
                .map(serviceCategory -> serviceCategory.getServiceCategoryID()
                        + " " + serviceCategory.getServiceCategoryName())
                .collect(Collectors.toList());

        ObservableList<String> observableComboBoxItems = FXCollections.observableArrayList(comboBoxItems);

        serviceCategoryCBox.getItems().setAll(comboBoxItems);

        if (!serviceCategoryCBox.getItems().isEmpty()) {
            serviceCategoryCBox.getSelectionModel().selectFirst();
        }

        List<HotelService> hotelServiceList = HotelServiceDAO.getHotelService();
        items = FXCollections.observableArrayList(hotelServiceList);
        hotelServiceTableView.setItems(items);
        hotelServiceTableView.refresh();
    }

    // Phương thức đổ dữ liệu vào bảng
    private void setupTable() {
        hotelServiceIDColumn.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        hotelServiceNameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
        hotelServicePriceColumn.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
        serviceCategoryNameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getServiceCategory().getServiceCategoryName())
        );

        setupHotelServiceDescriptionColumn();
        setupActionColumn();


        hotelServiceTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        hotelServiceTableView.setItems(items);
    }

    // setup cho cột mô tả
    // THAM KHẢO
    private void setupHotelServiceDescriptionColumn() {
        hotelServiceDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        hotelServiceDescriptionColumn.setCellFactory(column -> new TableCell<>() {
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
                    text.wrappingWidthProperty().bind(hotelServiceDescriptionColumn.widthProperty());
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
        Callback<TableColumn<HotelService, Void>, TableCell<HotelService, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<HotelService, Void> call(final TableColumn<HotelService, Void> param) {
                return new TableCell<>() {

                    private final Button updateButton = new Button("Cập nhật");
                    private final Button deleteButton = new Button("Xóa");
                    private final HBox hBox = new HBox(10);

                    {
                        updateButton.getStyleClass().add("button-update");
                        deleteButton.getStyleClass().add("button-delete");

                        hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                        updateButton.setOnAction(event -> {
                            HotelService hotelService = getTableView().getItems().get(getIndex());
                            handleUpdateBtn(hotelService);
                        });

                        deleteButton.setOnAction(event -> {
                            HotelService hotelService = getTableView().getItems().get(getIndex());
                            handleDeleteAction(hotelService);
                        });

                        hBox.setAlignment(Pos.CENTER);
                        hBox.getChildren().addAll(updateButton, deleteButton);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hBox);
                        }
                    }
                };
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
                    serviceCategory
            );

            HotelServiceDAO.createData(hotelService);
            handleResetAction();
            loadData();
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 3: Xóa
    private void handleDeleteAction(HotelService hotelService) {
        DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn xóa dịch vụ này?");

        dialog.onClose(buttonType -> {
            if (buttonType == ButtonType.YES) {
                HotelServiceDAO.deleteData(hotelService.getServiceId());
                handleResetAction();
                loadData();
            }
        });
    }

    // Chức năng 4: Cập nhật
    // 4.1 Xử lý sự kiện khi kích hoạt chức năng cập nhật
    private void handleUpdateBtn(HotelService hotelService) {
        serviceIDTextField.setText(hotelService.getServiceId());
        serviceNameTextField.setText(hotelService.getServiceName());
        servicePriceTextField.setText(String.valueOf(hotelService.getServicePrice()));

        String serviceCategoryDisplay = hotelService.getServiceCategory().getServiceCategoryID()
                + " " + hotelService.getServiceCategory().getServiceCategoryName();

        serviceCategoryCBox.getSelectionModel().select(serviceCategoryDisplay);

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

            HotelService hotelService = new HotelService(
                    serviceIDTextField.getText(),
                    serviceNameTextField.getText(),
                    ConvertHelper.doubleConverter(servicePriceTextField.getText(), ErrorMessages.HOTEL_SERVICE_INVALID_FORMAT),
                    descriptionTextField.getText(),
                    serviceCategory
            );

            DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn cập nhật dịch vụ này?");

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    HotelServiceDAO.updateData(hotelService);
                    handleResetAction();
                    loadData();
                }
            });
        } catch (Exception e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 5: Tìm kiếm
    private void handleSearchAction() {
        hotelSerivceNameSearchField.setText("");
        priceSearchField.setText("");
        serviceCategoryNameSearchField.setText("");
        descriptionSearchField.setText("");

        String searchText = hotelServiceIDSearchField.getValue();
        List<HotelService> hotelServices;

        if (searchText == null || searchText.isEmpty()) {
            hotelServices = HotelServiceDAO.getHotelService();
        } else {
            hotelServices = HotelServiceDAO.findDataByContainsId(searchText);

            if (hotelServices.size() == 1) {
                HotelService hotelService = hotelServices.getFirst();
                hotelSerivceNameSearchField.setText(hotelService.getServiceName());
                priceSearchField.setText(String.valueOf(hotelService.getServicePrice()));
                serviceCategoryNameSearchField.setText(hotelService.getServiceCategory().getServiceCategoryName());
                descriptionSearchField.setText(hotelService.getDescription());
            }
        }

        // Cập nhật lại bảng với dữ liệu đã tìm kiếm
        items.setAll(hotelServices);
        hotelServiceTableView.setItems(items);
    }


}
