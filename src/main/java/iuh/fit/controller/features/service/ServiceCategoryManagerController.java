package iuh.fit.controller.features.service;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.ServiceCategoryDAO;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.RegexChecker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.List;

public class ServiceCategoryManagerController {
    @FXML
    private ComboBox<String> serviceCategoryIDCBox;
    @FXML
    private TextField serviceCategoryIDTextField;
    @FXML
    private TextField serviceCategoryNameTextField;

    @FXML
    private TableView<ServiceCategory> serviceCategoryTableView;
    @FXML
    private TableColumn<ServiceCategory, String> serviceCategoryIDColumn;
    @FXML
    private TableColumn<ServiceCategory, String> serviceCategoryNameColumn;
    @FXML
    private TableColumn<ServiceCategory, Void> actionColumn;

    @FXML
    private DialogPane dialog;

    @FXML
    private Button addBtn;
    @FXML
    private Button resetBtn;

    public void initialize() {
        dialog.toFront();

        setupServiceCategoryIDComboBox();
        setNextServiceCategoryIDToTextField();
        setupServiceCategoryTableView();

        resetBtn.setOnAction(e -> handleResetBtn());
        addBtn.setOnAction(e -> handleAddBtn());

    }

    private void setupServiceCategoryIDComboBox() {
        List<String> data = ServiceCategoryDAO.getTopThreeCategoryService();

        serviceCategoryIDCBox.getItems().setAll(data);
    }

    private void setNextServiceCategoryIDToTextField() {
        serviceCategoryIDTextField.setText(ServiceCategoryDAO.getNextServiceCategoryID());
    }

    // setup table
    private void setupServiceCategoryTableView() {
        List<ServiceCategory> data = ServiceCategoryDAO.getServiceCategory();
        ObservableList<ServiceCategory> items = FXCollections.observableArrayList(data);

        serviceCategoryIDColumn.setCellValueFactory(new PropertyValueFactory<>("serviceCategoryID"));
        serviceCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceCategoryName"));
        setupButtonColumn();

        serviceCategoryTableView.setItems(items);
        serviceCategoryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    // setup cho cột thao tác
    private void setupButtonColumn() {
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
                        hBox.getStylesheets().add(getClass().getResource("/iuh/fit/styles/Button.css").toExternalForm());

                        // Set hành động cho các button
                        updateButton.setOnAction(event -> {
                            ServiceCategory serviceCategory = getTableView().getItems().get(getIndex());
                            handleEditAction(serviceCategory);
                        });

                        deleteButton.setOnAction(event -> {
                            ServiceCategory serviceCategory = getTableView().getItems().get(getIndex());
                            handleDeleteAction(serviceCategory);
                        });

                        // Set alignment cho HBox
                        hBox.setAlignment(Pos.CENTER);
                        hBox.getChildren().addAll(updateButton, deleteButton);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {

                            setGraphic(hBox); // Hiển thị HBox với các button
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void handleResetBtn() {
        serviceCategoryNameTextField.setText("");
    }

    private void handleAddBtn() {

        try {
            String serviceCategoryID = serviceCategoryIDTextField.getText();
            String serviceCategoryName = serviceCategoryNameTextField.getText();

            // Kiểm tra tính hợp lệ của tên dịch vụ
            if (!RegexChecker.isValidName(serviceCategoryName, 3, 30)) {
                throw new IllegalArgumentException("Tên loại dịch vụ phải từ 3 đến 30 ký tự và không chứa ký tự đặc biệt.");
            }

            // Tạo đối tượng serviceCategory nếu dữ liệu hợp lệ
            ServiceCategory serviceCategory = new ServiceCategory(serviceCategoryID, serviceCategoryName);

            // Sau khi xử lý, có thể lưu vào database hoặc thực hiện hành động tiếp theo

        } catch (IllegalArgumentException e) {

            dialog.showWarning("LỖI", e.getMessage());
        }
    }


    private void handleEditAction(ServiceCategory serviceCategory) {
        System.out.println("Sửa: " + serviceCategory.getServiceCategoryID());
    }

    private void handleDeleteAction(ServiceCategory serviceCategory) {
        System.out.println("Xóa: " + serviceCategory.getServiceCategoryID());
    }

}
