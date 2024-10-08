package iuh.fit.controller.features.service;

import iuh.fit.dao.ServiceCategoryDAO;
import iuh.fit.models.ServiceCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.util.List;

public class ServiceCategoryManagerController {
    @FXML
    private ComboBox<String> serviceCategoryIDCBox;
    @FXML
    private TextField serviceCategoryIDTextField;
    @FXML
    private TableView<ServiceCategory> serviceCategoryTableView;
    @FXML
    private TableColumn<ServiceCategory, String> serviceCategoryIDColumn;
    @FXML
    private TableColumn<ServiceCategory, String> serviceCategoryNameColumn;
    @FXML
    private TableColumn<ServiceCategory, Void> actionColumn;

    public void initialize() {
        setupServiceCategoryIDComboBox();
        setNextServiceCategoryIDToTextField();
        setupServiceCategoryTableView();
        addButtonToTable();
    }

    private void setupServiceCategoryIDComboBox() {
        List<String> data = ServiceCategoryDAO.getTopThreeCategoryService();

        serviceCategoryIDCBox.getItems().setAll(data);
    }

    private void setNextServiceCategoryIDToTextField() {
        serviceCategoryIDTextField.setText(ServiceCategoryDAO.getNextServiceCategoryID());
    }

    private void setupServiceCategoryTableView() {
        List<ServiceCategory> data = ServiceCategoryDAO.getServiceCategory();
        ObservableList<ServiceCategory> items = FXCollections.observableArrayList(data);

        serviceCategoryIDColumn.setCellValueFactory(new PropertyValueFactory<>("serviceCategoryID"));
        serviceCategoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceCategoryName"));

        serviceCategoryTableView.setItems(items);
        serviceCategoryTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void addButtonToTable() {
        Callback<TableColumn<ServiceCategory, Void>, TableCell<ServiceCategory, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<ServiceCategory, Void> call(final TableColumn<ServiceCategory, Void> param) {
                return new TableCell<>() {
                    private AnchorPane anchorPane;

                    {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/service/ButtonPanel.fxml"));
                            anchorPane = loader.load();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(anchorPane);
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void handleEditAction(ServiceCategory serviceCategory) {
        System.out.println("Sửa: " + serviceCategory.getServiceCategoryID());
    }

    private void handleDeleteAction(ServiceCategory serviceCategory) {
        System.out.println("Xóa: " + serviceCategory.getServiceCategoryID());
    }

}
