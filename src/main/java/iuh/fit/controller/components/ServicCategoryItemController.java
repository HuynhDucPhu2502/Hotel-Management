package iuh.fit.controller.components;

import iuh.fit.models.ServiceCategory;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ServicCategoryItemController {
    @FXML
    private Text serviceCategoryIDLabel;
    @FXML
    private Text serviceCategoryNameLabel;

    public void setData(ServiceCategory serviceCategory) {
        serviceCategoryIDLabel.setText(serviceCategory.getServiceCategoryID());
        serviceCategoryNameLabel.setText(serviceCategory.getServiceCategoryName());
    }
}
