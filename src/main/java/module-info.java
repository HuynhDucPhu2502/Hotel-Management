module iuh.fit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.dlsc.gemsfx;
    requires com.dlsc.unitfx;
    requires org.controlsfx.controls;

    opens iuh.fit to javafx.fxml;
    opens iuh.fit.controller to javafx.fxml;
    opens iuh.fit.controller.features to javafx.fxml;
    opens iuh.fit.controller.features.service to javafx.fxml;
    opens iuh.fit.controller.features.room to javafx.fxml;
    opens iuh.fit.controller.features.room.room_items to javafx.fxml;
    opens iuh.fit.models to javafx.base;
    opens iuh.fit.controller.features.customer to javafx.fxml;

    exports iuh.fit;

    exports iuh.fit.controller.features.service;
    exports iuh.fit.controller.features.room;
    exports iuh.fit.controller.features.customer;
    exports iuh.fit.controller;
    exports iuh.fit.models;
    exports iuh.fit.models.enums;

}
