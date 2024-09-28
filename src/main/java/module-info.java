module iuh.fit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    // Cho phép javafx.fxml truy cập các class trong gói controller
    opens iuh.fit.controller to javafx.fxml;

    // Mở gói chính của ứng dụng để sử dụng FXML
    opens iuh.fit to javafx.fxml;

    exports iuh.fit;
}
