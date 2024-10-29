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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @FXML
    private ComboBox<Image> iconSelector;

    // Table
    @FXML
    private TableView<ServiceCategory> serviceCategoryTableView;
    @FXML
    private TableColumn<ServiceCategory, String> serviceCategoryIDColumn;
    @FXML
    private TableColumn<ServiceCategory, String> serviceCategoryNameColumn;
    @FXML
    private TableColumn<ServiceCategory, Void> actionColumn;
    @FXML
    private TableColumn<ServiceCategory, String> iconColumn;

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
    private final Map<Image, String> iconServiceMap = new HashMap<>();

    public void initialize() {
        dialogPane.toFront();
        loadData();
        setupTable();

        resetBtn.setOnAction(e -> handleResetAction());
        addBtn.setOnAction(e -> handleAddAction());
        updateBtn.setOnAction(e -> handleUpdateAction());
        serviceCategoryIDSearchField.setOnAction(e -> handleSearchAction());
        setupIconSelector();
    }

    private void loadData() {
        List<String> Ids = ServiceCategoryDAO.getTopThreeID();
        serviceCategoryIDSearchField.getItems().setAll(Ids);

        serviceCategoryIDTextField.setText(ServiceCategoryDAO.getNextServiceCategoryID());

        List<ServiceCategory> serviceCategories = ServiceCategoryDAO.getServiceCategory();
        items = FXCollections.observableArrayList(serviceCategories);
        serviceCategoryTableView.setItems(items);
        serviceCategoryTableView.refresh();
        iconSelector.getSelectionModel().selectFirst();
    }

    private void setupTable() {
        serviceCategoryIDColumn.setCellValueFactory(new PropertyValueFactory<>("serviceCategoryID"));
        serviceCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceCategoryName"));
        setupActionColumn();
        setupIconColumn();

        serviceCategoryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Cập nhật");
            private final Button deleteButton = new Button("Xóa");
            private final HBox hBox = new HBox(10);

            {
                updateButton.getStyleClass().add("button-update");
                deleteButton.getStyleClass().add("button-delete");
                hBox.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/iuh/fit/styles/Button.css")).toExternalForm());
                updateButton.setOnAction(event -> handleUpdateBtn(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteAction(getTableView().getItems().get(getIndex())));
                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().addAll(updateButton, deleteButton);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hBox);
            }
        });
    }

    private void setupIconColumn() {
        iconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));

        iconColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String iconName, boolean empty) {
                super.updateItem(iconName, empty);
                if (empty || iconName == null) {
                    setGraphic(null);
                } else {
                    String iconPath = "/iuh/fit/icons/service_icons/ic_" + iconName + ".png";
                    ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
                    imageView.setFitWidth(24);
                    imageView.setFitHeight(24);

                    HBox hBox = new HBox(imageView);
                    hBox.setAlignment(Pos.CENTER);
                    setGraphic(hBox);
                }
            }
        });
    }

    private void handleResetAction() {
        serviceCategoryNameTextField.setText("");
        serviceCategoryIDTextField.setText(ServiceCategoryDAO.getNextServiceCategoryID());
        iconSelector.getSelectionModel().selectFirst();

        addBtn.setManaged(true);
        addBtn.setVisible(true);
        updateBtn.setManaged(false);
        updateBtn.setVisible(false);
    }

    private void handleAddAction() {
        try {
            Image selectedIcon = iconSelector.getSelectionModel().getSelectedItem();
            String iconName = iconServiceMap.get(selectedIcon);

            ServiceCategory serviceCategory = new ServiceCategory(
                    serviceCategoryIDTextField.getText(),
                    serviceCategoryNameTextField.getText(),
                    iconName
            );

            ServiceCategoryDAO.createData(serviceCategory);
            handleResetAction();
            loadData();
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

    private void handleUpdateBtn(ServiceCategory serviceCategory) {
        serviceCategoryNameTextField.setText(serviceCategory.getServiceCategoryName());
        serviceCategoryIDTextField.setText(serviceCategory.getServiceCategoryID());

        String iconName = serviceCategory.getIcon();
        iconSelector.getSelectionModel().select(
                iconServiceMap.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(iconName))
                        .map(Map.Entry::getKey)
                        .findFirst().orElse(null)
        );

        addBtn.setManaged(false);
        addBtn.setVisible(false);
        updateBtn.setManaged(true);
        updateBtn.setVisible(true);
    }

    private void handleUpdateAction() {
        try {
            String serviceCategoryID = serviceCategoryIDTextField.getText();
            String serviceCategoryName = serviceCategoryNameTextField.getText();
            Image selectedIcon = iconSelector.getSelectionModel().getSelectedItem();
            String iconName = iconServiceMap.get(selectedIcon);

            ServiceCategory serviceCategory = new ServiceCategory(serviceCategoryID, serviceCategoryName, iconName);

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

    private void handleSearchAction() {
        String searchText = serviceCategoryIDSearchField.getValue();
        List<ServiceCategory> serviceCategories = searchText == null || searchText.isEmpty()
                ? ServiceCategoryDAO.getServiceCategory()
                : ServiceCategoryDAO.findDataByContainsId(searchText);

        items.setAll(serviceCategories);
        serviceCategoryTableView.setItems(items);

        if (serviceCategories.size() == 1) {
            ServiceCategory serviceCategory =  serviceCategories.getFirst();
            serviceCategoryNameSearchField.setText(serviceCategory.getServiceCategoryName());
        }
    }

    private void setupIconSelector() {
        List<String> iconPaths = List.of(
                "/iuh/fit/icons/service_icons/ic_car.png",
                "/iuh/fit/icons/service_icons/ic_cleaning.png",
                "/iuh/fit/icons/service_icons/ic_food.png",
                "/iuh/fit/icons/service_icons/ic_karaoke.png",
                "/iuh/fit/icons/service_icons/ic_massage.png",
                "/iuh/fit/icons/service_icons/ic_meeting.png",
                "/iuh/fit/icons/service_icons/ic_washing.png"
        );

        List<String> services = List.of("car", "cleaning", "food", "karaoke", "massage", "meeting", "washing");

        ObservableList<Image> icons = FXCollections.observableArrayList();
        for (int i = 0; i < iconPaths.size(); i++) {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPaths.get(i))));
            icons.add(icon);
            iconServiceMap.put(icon, services.get(i));
        }

        iconSelector.setItems(icons);
        iconSelector.setCellFactory(comboBox -> new ListCell<>() {
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(item);
                    imageView.setFitWidth(24);
                    imageView.setFitHeight(24);
                    setGraphic(imageView);
                }
            }
        });

        iconSelector.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(item);
                    imageView.setFitWidth(24);
                    imageView.setFitHeight(24);
                    setGraphic(imageView);
                }
            }
        });

    }
}
