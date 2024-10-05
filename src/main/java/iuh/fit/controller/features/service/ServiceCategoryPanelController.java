package iuh.fit.controller.features.service;

import iuh.fit.controller.components.ServicCategoryItemController;
import iuh.fit.dao.ServiceCategoryDAO;
import iuh.fit.models.ServiceCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ServiceCategoryPanelController {
    @FXML
    private ComboBox<String> serviceCategoryIDCBox;
    @FXML
    private TextField serviceCategoryIDTextField;
    @FXML
    private ListView<ServiceCategory> serviceCategoryListView;


    public void initialize() {
        setupServiceCategoryIDComboBox();
        setNextServiceCategoryIDToTextField();
        setupServiceCategoryListView();
    }

    private void setupServiceCategoryIDComboBox() {
        List<String> data = ServiceCategoryDAO.getTopThreeCategoryService();

        serviceCategoryIDCBox.getItems().setAll(data);
    }

    private void setNextServiceCategoryIDToTextField() {
        serviceCategoryIDTextField.setText(ServiceCategoryDAO.getNextServiceCategoryID());
    }

    private void setupServiceCategoryListView() {
        List<ServiceCategory> data = ServiceCategoryDAO.getServiceCategory();
        ObservableList<ServiceCategory> items = FXCollections.observableArrayList(data);
        serviceCategoryListView.setItems(items);

        serviceCategoryListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ServiceCategory serviceCategory, boolean empty) {
                super.updateItem(serviceCategory, empty);
                if (empty || serviceCategory == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/iuh/fit/view/components/ServiceCategoryItemList.fxml"));
                        AnchorPane pane = loader.load();

                        ServicCategoryItemController controller = loader.getController();
                        controller.setData(serviceCategory);

                        setGraphic(pane);
                    } catch (Exception e) {
                        e.printStackTrace();
                        setText("Error loading cell");
                    }
                }
            }
        });

        serviceCategoryListView.setStyle("-fx-control-inner-background: RGB(70, 130, 180);" +
                "-fx-background-insets: 0;");


    }
}
