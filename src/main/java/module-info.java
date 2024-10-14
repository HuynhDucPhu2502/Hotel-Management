module iuh.fit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.dlsc.gemsfx;
    requires com.dlsc.unitfx;

    opens iuh.fit to javafx.fxml;
    opens iuh.fit.controller to javafx.fxml;
    opens iuh.fit.controller.features.service to javafx.fxml;
    opens iuh.fit.models to javafx.base;

    exports iuh.fit;
    exports iuh.fit.controller.features.service;
    exports iuh.fit.controller.features;
    opens iuh.fit.controller.features to javafx.fxml;
}
