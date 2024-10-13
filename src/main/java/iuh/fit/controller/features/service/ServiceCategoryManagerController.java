package iuh.fit.controller.features.service;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.dao.ServiceCategoryDAO;
import iuh.fit.models.ServiceCategory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;

public class ServiceCategoryManagerController {
    @FXML
    private ComboBox<String> serviceCategoryIDCBox;
    @FXML
    private TextField serviceCategoryNameSearchField;


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
    private DialogPane dialogPane;

    @FXML
    private Button addBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Button updateBtn;

    private Timeline searchDelayTimeline;
    private ObservableList<ServiceCategory> items;

    public void initialize() {
        dialogPane.toFront();

        loadData();
        setupTable();

        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
        updateBtn.setOnAction(e -> handleUpdateAction());

        searchDelayTimeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> handleSearchAction()));
        searchDelayTimeline.setCycleCount(1);

        serviceCategoryIDCBox.setOnAction(event -> {
            searchDelayTimeline.stop();
            searchDelayTimeline.playFromStart();
        });
    }


    private void loadData() {
        List<String> Ids = ServiceCategoryDAO.getTopThreeCategoryService();
        serviceCategoryIDCBox.getItems().setAll(Ids);

        serviceCategoryIDTextField.setText(ServiceCategoryDAO.getNextServiceCategoryID());

        List<ServiceCategory> serviceCategories = ServiceCategoryDAO.getServiceCategory();
        items = FXCollections.observableArrayList(serviceCategories);
        serviceCategoryTableView.setItems(items);
    }

    private void setupTable() {
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

    private void handleResetAction() {
        serviceCategoryNameTextField.setText("");

        serviceCategoryIDTextField.setText(ServiceCategoryDAO.getNextServiceCategoryID());

        addBtn.setManaged(true);
        addBtn.setVisible(true);
        updateBtn.setManaged(false);
        updateBtn.setVisible(false);
    }

    private void handleAddAction() {
        try {
            String serviceCategoryID = serviceCategoryIDTextField.getText();
            String serviceCategoryName = serviceCategoryNameTextField.getText();

            ServiceCategory serviceCategory = new ServiceCategory(serviceCategoryID, serviceCategoryName);

            ServiceCategoryDAO.createData(serviceCategory);
            handleResetAction();
            loadData();

        } catch (IllegalArgumentException e) {
            dialogPane.showWarning("LỖI", e.getMessage());
        }
    }


    private void handleUpdateBtn(ServiceCategory serviceCategory) {
        serviceCategoryNameTextField.setText(serviceCategory.getServiceCategoryName());
        serviceCategoryNameTextField.requestFocus();
        serviceCategoryIDTextField.setText(serviceCategory.getServiceCategoryID());

        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);

    }

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

    private void handleDeleteAction(ServiceCategory serviceCategory) {
        DialogPane.Dialog<ButtonType> dialog = dialogPane.showConfirmation("XÁC NHẬN", "Bạn có chắc chắn muốn xóa loại dịch vụ này?");

        dialog.onClose(buttonType -> {
            if (buttonType == ButtonType.YES) {
                ServiceCategoryDAO.deleteData(serviceCategory.getServiceCategoryID());

                loadData();
            }
        });
    }

    private void handleSearchAction() {
        String searchText = serviceCategoryIDCBox.getValue();
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
