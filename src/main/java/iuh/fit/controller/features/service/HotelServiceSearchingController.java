package iuh.fit.controller.features.service;

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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.util.List;
import java.util.stream.Collectors;

public class HotelServiceSearchingController {

    // Search Field
    @FXML
    private TextField serviceIDSearchField;
    @FXML
    private TextField serviceNameSearchField;
    @FXML
    private TextField priceLowerBoundSearchField;
    @FXML
    private TextField priceUpperBoundSearchField;
    @FXML
    private ComboBox<String> serviceCategorySearchField;

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

    // Buttons
    @FXML
    private Button searchBtn;
    @FXML
    private Button resetBtn;

    private ObservableList<HotelService> items;

    public void initialize() {
        loadData();
        setupTable();

        searchBtn.setOnAction(e -> handleSearchAction());
        resetBtn.setOnAction(e -> handleResetAction());
    }

    // Phương thức load dữ liệu lên giao diện
    private void loadData() {
        List<HotelService> hotelServiceList = HotelServiceDAO.getHotelService();
        items = FXCollections.observableArrayList(hotelServiceList);
        hotelServiceTableView.setItems(items);
        hotelServiceTableView.refresh();

        List<String> comboBoxItems = ServiceCategoryDAO.getServiceCategory()
                .stream()
                .map(serviceCategory -> serviceCategory.getServiceCategoryID() +
                        " " + serviceCategory.getServiceCategoryName()
                )
                .collect(Collectors.toList());
        comboBoxItems.addFirst("KHÔNG CÓ");
        comboBoxItems.addFirst("TẤT CẢ");

        ObservableList<String> observableComboBoxItems = FXCollections.observableArrayList(comboBoxItems);
        serviceCategorySearchField.getItems().setAll(observableComboBoxItems);

        if (!serviceCategorySearchField.getItems().isEmpty())
            serviceCategorySearchField.getSelectionModel().selectFirst();



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


    private void handleResetAction() {
        serviceIDSearchField.setText("");
        serviceNameSearchField.setText("");
        priceLowerBoundSearchField.setText("");
        priceUpperBoundSearchField.setText("");
        serviceCategorySearchField.getSelectionModel().selectFirst();

        loadData();
    }

    private void handleSearchAction() {
        try {
            String serviceID = serviceIDSearchField.getText().isBlank() ? null : serviceIDSearchField.getText().trim();
            String serviceName = serviceNameSearchField.getText().isBlank() ? null : serviceNameSearchField.getText().trim();
            Double minPrice = handlePriceInput(priceLowerBoundSearchField.getText());
            Double maxPrice = handlePriceInput(priceUpperBoundSearchField.getText());
            String selectedCategory = serviceCategorySearchField.getSelectionModel().getSelectedItem();
            String categoryID = handleCategoryIDInput(selectedCategory);

            List<HotelService> searchResults = HotelServiceDAO.searchHotelServices(
                    serviceID, serviceName, minPrice, maxPrice, categoryID);
            items.setAll(searchResults);
            hotelServiceTableView.setItems(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Double handlePriceInput(String numbStr) {
        if (numbStr.isBlank()) return null;
        else return ConvertHelper.doubleConverter(numbStr, ErrorMessages.HOTEL_SERVICE_INVALID_FORMAT);
    }

    private String handleCategoryIDInput(String selectedCategory) {
        if (selectedCategory == null) return null;
        if (selectedCategory.equalsIgnoreCase("TẤT CẢ")) return "ALL";
        if (selectedCategory.equalsIgnoreCase("KHÔNG CÓ")) return "NULL";
        return selectedCategory.split(" ")[0];
    }

}
