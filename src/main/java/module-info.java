module iuh.fit {
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.dlsc.gemsfx;
    requires com.dlsc.unitfx;
    requires org.controlsfx.controls;
    requires com.calendarfx.view;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires itextpdf;
    requires net.synedra.validatorfx;


    opens iuh.fit to javafx.fxml;

    opens iuh.fit.controller to javafx.fxml;
    opens iuh.fit.controller.features to javafx.fxml;
    opens iuh.fit.controller.features.employee_information to javafx.fxml;
    opens iuh.fit.controller.features.service to javafx.fxml;
    opens iuh.fit.controller.features.room to javafx.fxml;
    opens iuh.fit.controller.features.room.creating_reservation_form_controllers to javafx.fxml;
    opens iuh.fit.controller.features.room.checking_in_reservation_list_controllers to javafx.fxml;
    opens iuh.fit.controller.features.room.checking_out_controllers to javafx.fxml;
    opens iuh.fit.controller.features.room.room_changing_controllers to javafx.fxml;
    opens iuh.fit.controller.features.room.service_ordering_controllers to javafx.fxml;
    opens iuh.fit.controller.features.room.invoice_controllers to javafx.fxml;
    opens iuh.fit.controller.features.room.group_booking_controllers to javafx.fxml;
    opens iuh.fit.controller.features.employee to javafx.fxml;
    opens iuh.fit.controller.features.customer to javafx.fxml;
    opens iuh.fit.controller.features.statistics to javafx.fxml;

    opens iuh.fit.models to javafx.base, javafx.fxml;
    opens iuh.fit.models.wrapper to javafx.base, javafx.fxml;

    exports iuh.fit;

    exports iuh.fit.controller;
    exports iuh.fit.controller.features;
    exports iuh.fit.controller.features.employee_information;
    exports iuh.fit.controller.features.service;
    exports iuh.fit.controller.features.room;
    exports iuh.fit.controller.features.room.creating_reservation_form_controllers;
    exports iuh.fit.controller.features.room.checking_in_reservation_list_controllers;
    exports iuh.fit.controller.features.room.checking_out_controllers;
    exports iuh.fit.controller.features.room.room_changing_controllers;
    exports iuh.fit.controller.features.customer;
    exports iuh.fit.controller.features.statistics;

    exports iuh.fit.models;
    exports iuh.fit.models.enums;
    exports iuh.fit.models.wrapper;




}
