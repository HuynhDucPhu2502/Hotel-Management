module iuh.fit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens iuh.fit to javafx.fxml;
    opens iuh.fit.controller to javafx.fxml;
    opens iuh.fit.controller.components to javafx.fxml;
    opens iuh.fit.controller.features.service to javafx.fxml;
    opens iuh.fit.controller.features.bar to javafx.fxml;

    exports iuh.fit;
    exports iuh.fit.controller.components;
    exports iuh.fit.controller.features.service;
    exports iuh.fit.controller.features.bar;
}
