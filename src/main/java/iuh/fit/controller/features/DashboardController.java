package iuh.fit.controller.features;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardController {
    @FXML
    private Text dateText;

    public void initialize() {
        setupDateText();
    }

    private void setupDateText() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Ngày' dd 'Tháng' MM 'Năm' yyyy");
        String formattedDate = currentDate.format(formatter);
        dateText.setText(formattedDate);
    }
}
