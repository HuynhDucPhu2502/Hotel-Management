package iuh.fit.controller.features.room.service_ordering_controllers;

import iuh.fit.models.HotelService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class ServiceItemController {
    @FXML
    private ImageView serviceCategoryImg;

    @FXML
    private Label serviceName, servicePrice;

    @FXML
    private Button addServiceBtn;

    @FXML
    private Spinner<Integer> amountField;

    private HotelService hotelService;


    public void setupContext(HotelService hotelService) {
        this.hotelService = hotelService;

        loadData();
    }

    private void loadData() {
        if (hotelService != null) {
            serviceName.setText(hotelService.getServiceName());
            servicePrice.setText(String.valueOf(hotelService.getServicePrice()) + " VND");
            String iconPath = "/iuh/fit/icons/service_icons/ic_" + hotelService.getServiceCategory().getIcon() + ".png";
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath)));
            serviceCategoryImg.setImage(image);
        }

        amountField.setValueFactory(new SpinnerValueFactory
                .IntegerSpinnerValueFactory(1, 100, 1));

    }

    public Spinner<Integer> getAmountField() {
        return amountField;
    }

    public Button getAddServiceBtn() {
        return addServiceBtn;
    }
}
