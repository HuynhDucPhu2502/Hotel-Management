package iuh.fit.controller.features.service;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.ServiceCategoryDAO;
import iuh.fit.models.ServiceCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.List;
import java.util.Objects;

public class ServiceCategoryManagerController {
    // Search Fields
    @FXML
    private ComboBox<String> serviceCategoryIDSearchField;
    @FXML
    private TextField serviceCategoryNameSearchField;

    // Input Fields
    @FXML
    private TextField serviceCategoryIDTextField;
    @FXML
    private TextField serviceCategoryNameTextField;

    // Table
    @FXML
    private TableView<ServiceCategory> serviceCategoryTableView;
    @FXML
    private TableColumn<ServiceCategory, String> serviceCategoryIDColumn;
    @FXML
    private TableColumn<ServiceCategory, String> serviceCategoryNameColumn;
    @FXML
    private TableColumn<ServiceCategory, Void> actionColumn;

    // Buttons
    @FXML
    private Button addBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Button updateBtn;

    // Dialog
    @FXML
    private DialogPane dialogPane;

    private ObservableList<ServiceCategory> items;


    // Gọi mấy phương thức để gắn sự kiện và dữ liệu cho lúc đầu khởi tạo giao diện
    public void initialize() {
        dialogPane.toFront();

        loadData();
        setupTable();

        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
        updateBtn.setOnAction(e -> handleUpdateAction());
        serviceCategoryIDSearchField.setOnAction(e -> handleSearchAction());

    }


    // Phương thức load dữ liệu lên giao diện
    private void loadData() {
        List<String> Ids = ServiceCategoryDAO.getTopThreeID();
        serviceCategoryIDSearchField.getItems().setAll(Ids);

        serviceCategoryIDTextField.setText(ServiceCategoryDAO.getNextServiceCategoryID());

        List<ServiceCategory> serviceCategories = ServiceCategoryDAO.getServiceCategory();
        items = FXCollections.observableArrayList(serviceCategories);
        serviceCategoryTableView.setItems(items);
        serviceCategoryTableView.refresh();
    }

    // Phương thức đổ dữ liệu vào bảng
    private void setupTable() {
        serviceCategoryIDColumn.setCellValueFactory(new PropertyValueFactory<>("serviceCategoryID"));
        serviceCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceCategoryName"));
        setupActionColumn();

        serviceCategoryTableView.setItems(items);
        serviceCategoryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    // setup cho cột thao tác
    // THAM KHẢO
    private void setupActionColumn() {
        actionColumn.setMinWidth(300);
        Callback<TableColumn<ServiceCategory, Void>, TableCell<ServiceCategory, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ServiceCategory, Void> call(final TableColumn<ServiceCategory, Void> param) {
                return new TableCell<>() {

                    private final Button updateButton = new Button("Cập nhật");
                    private final Button deleteButton = new Button("Xóa");
                    private final HBox hBox = new HBox(10);

                    {
                        // Thêm class CSS cho các button
                        updateButton.getStyleClass().add("button-update");
                        deleteButton.getStyleClass().add("button-delete");

                        // Thêm file CSS vào HBox
                        hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());

                        // Set hành động cho các button
                        updateButton.setOnAction(event -> {
                            ServiceCategory serviceCategory = getTableView().getItems().get(getIndex());
                            handleUpdateBtn(serviceCategory);
                        });

                        deleteButton.setOnAction(event -> {
                            ServiceCategory serviceCategory = getTableView().getItems().get(getIndex());
                            handleDeleteAction(serviceCategory);
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
        serviceCategoryNameTextField.setText("");

        serviceCategoryIDTextField.setText(ServiceCategoryDAO.getNextServiceCategoryID());

        addBtn.setManaged(true);
        addBtn.setVisible(true);
        updateBtn.setManaged(false);
        updateBtn.setVisible(false);
    }

    // Chức năng 2: Thêm
    private void handleAddAction() {
        try {
            ServiceCategory serviceCategory = new ServiceCategory(
                    serviceCategoryIDTextField.getText(),
                    serviceCategoryNameTextField.getText()
            );

            ServiceCategoryDAO.createData(serviceCategory);
            handleResetAction();
            loadData();
        } catch (IllegalArgumentException e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 3: Xóa
    private void handleDeleteAction(ServiceCategory serviceCategory) {
        DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn xóa loại dịch vụ này?");

        dialog.onClose(buttonType -> {
            if (buttonType == ButtonType.YES) {
                ServiceCategoryDAO.deleteData(serviceCategory.getServiceCategoryID());

                loadData();
            }
        });
    }

    // Chức năng 4: Cập nhật
    // 4.1 Xử lý sự kiện khi kích hoạt chức năng cập nhật
    private void handleUpdateBtn(ServiceCategory serviceCategory) {
        serviceCategoryNameTextField.setText(serviceCategory.getServiceCategoryName());
        serviceCategoryNameTextField.requestFocus();
        serviceCategoryIDTextField.setText(serviceCategory.getServiceCategoryID());

        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);

    }

    // 4.2 Chức năng cập nhật
    private void handleUpdateAction() {
        try {
            String serviceCategoryID = serviceCategoryIDTextField.getText();
            String serviceCategoryName = serviceCategoryNameTextField.getText();

            ServiceCategory serviceCategory = new ServiceCategory(serviceCategoryID, serviceCategoryName);

            DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn cập nhật loại dịch vụ này?");

            dialog.onClose(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    ServiceCategoryDAO.updateData(serviceCategory);
                    handleResetAction();
                    loadData();
                }
            });


        } catch (IllegalArgumentException e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }

    // Chức năng 5: Tìm kiếm
    private void handleSearchAction() {
        serviceCategoryNameSearchField.setText("");

        String searchText = serviceCategoryIDSearchField.getValue();
        List<ServiceCategory> serviceCategories;

        if (searchText == null || searchText.isEmpty()) {
            serviceCategories = ServiceCategoryDAO.getServiceCategory();
        } else {
            serviceCategories = ServiceCategoryDAO.findDataByContainsId(searchText);
            if (serviceCategories.size() == 1) {
                serviceCategoryNameSearchField.setText(serviceCategories.getFirst().getServiceCategoryName());
            }
        }

        items.setAll(serviceCategories);
        serviceCategoryTableView.setItems(items);
    }





}
