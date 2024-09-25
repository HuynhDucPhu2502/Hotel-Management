module iuh.fit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens iuh.fit to javafx.fxml;
    exports iuh.fit;
}
