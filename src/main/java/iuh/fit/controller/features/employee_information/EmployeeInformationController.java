package iuh.fit.controller.features.employee_information;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.models.Employee;
import iuh.fit.utils.ConvertImage;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class EmployeeInformationController {
    @FXML
    private TextField employeeIDTextField, employeeFullnameTextField,
            employeePhoneNumberTextField, employeeEmailTextField,
            employeeGenderTextField, employeeIDCardNumberTextField,
            employeeDOBTextField, employeeRoleTextField;

    @FXML
    private TextArea employeeAddressTextArea;

    @FXML
    private Button updateEmployeeInformationBtn, updateAccountPasswordBtn, updateAvatarBtn;

    @FXML
    private DialogPane dialogPane;

    private Employee employee;
    private MainController mainController;

    private static String AVATAR_DEFAULT = "iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34AAAACXBIWXMAAAsTAAALEwEAmpwYAAABRElEQVRIieXVSU/CQAAF4P7/EyfE4MkAmiBcBEwgokhAIwGUxi40gGA3QAmdtudnW1MMURjpcvLwkqYzed/MNJMy9kpBnGH+EfDxBmsxgr2cOM9ydIClCSBcBaSfA+llvZj9c1hi7QsMA1gav1X8I8952ItxcIDwld3l/m6kmxDAvtX7wKAYL0AGF3HvoBAcMNkCHeDKIQD+ig4MQ3xka9ymAtasEwLQBTpAuWyUmyyDsMXdwMvl3vI/AArWUvP38m4W61E7PEAUAbN6Ckb3u9zoZfBaS8KQuSgAzgGOIZYTHjStH0EsJTC9TnljgQFTFbES76C3M1AbJ1AaaW/Vk2oSym3ae+eOuXPcuVTA1CXnTDt4Z6ub0kOitU6xfCphNWxt7YwxVQHzx/zBhbS4nabzL2HmD7nIyzfI/RmYuMr9xA58As9AE1gjSRApAAAAAElFTkSuQmCC";
    private static String DEFAULT_PATH = "avatar";

    private static final String[] IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".bmp"};
    public void initialize() {

    }

    public void setupContext(Employee employee, MainController mainController) {
        this.employee = employee;
        this.mainController = mainController;

        setupButtonActions();
        loadData();
    }

    private void loadData() {
        Task<Void> loadDataTask = new Task<>() {
            @Override
            protected Void call() {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                String employeeID = employee.getEmployeeID();
                String fullName = employee.getFullName();
                String phoneNumber = employee.getPhoneNumber();
                String email = employee.getEmail();
                String gender = employee.getGender().toString();
                String idCardNumber = employee.getIdCardNumber();
                String dob = dateTimeFormatter.format(employee.getDob());
                String address = employee.getAddress();
                String role = employee.getPosition().toString();

                Platform.runLater(() -> {
                    employeeIDTextField.setText(employeeID);
                    employeeFullnameTextField.setText(fullName);
                    employeePhoneNumberTextField.setText(phoneNumber);
                    employeeEmailTextField.setText(email);
                    employeeGenderTextField.setText(gender);
                    employeeIDCardNumberTextField.setText(idCardNumber);
                    employeeDOBTextField.setText(dob);
                    employeeAddressTextArea.setText(address);
                    employeeRoleTextField.setText(role);
                });
                return null;
            }
        };

        new Thread(loadDataTask).start();
    }

    private void setupButtonActions() {
        updateEmployeeInformationBtn.setOnAction(e -> navigateToUpdatingEmployeePanel());
        updateAccountPasswordBtn.setOnAction(e -> navigateToPasswordChangingPanel());
        updateAvatarBtn.setOnAction(e -> choose());
    }

    private void navigateToUpdatingEmployeePanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/employee_information/UpdatingEmployeePanel.fxml"));
            AnchorPane layout = loader.load();

            UpdatingEmployeeController updatingEmployeeController = loader.getController();
            updatingEmployeeController.setupContext(employee, mainController);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToPasswordChangingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/employee_information/PasswordChangingPanel.fxml"));
            AnchorPane layout = loader.load();

            PasswordChangingController passwordChangingController = loader.getController();
            passwordChangingController.setupContext(employee, mainController);

            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void choose(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Chọn loại ảnh");
        alert.setHeaderText(null);
        alert.setContentText("Vui lòng chọn loại ảnh:");

        ButtonType chooseButton = new ButtonType("Chọn ảnh đại diện");
        ButtonType defaultButton = new ButtonType("Đặt ảnh mặc định");
        ButtonType cancelButton = new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(chooseButton, defaultButton, cancelButton);

        javafx.scene.control.DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: lightgrey;" +
                        "-fx-border-color: #cccccc;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 5px;"
        );

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == chooseButton) {
                navigateToChangeAvatar();
            } else if (result.get() == defaultButton) {
                setDefaultAvatar();
            } else if (result.get() == cancelButton || result.get() == ButtonType.CANCEL) {
                alert.close();
            }
        }
    }

    private void setDefaultAvatar(){

        employee.setAvatar(AVATAR_DEFAULT);
        EmployeeDAO.updateData(employee);

        mainController.getAccount().setEmployee(employee);
        mainController.initializeMenuBar();

        dialogPane.showInformation("Thành công", "Đổi ảnh đại diện thành công");
    }

    private void navigateToChangeAvatar(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh đại diện");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                Path sourcePath = selectedFile.toPath();

                File img = resizeImage(sourcePath.toString());

                String base64 = ConvertImage.encodeImageToBase64(img);

                employee.setAvatar(base64);

                Employee newEmp = employee;
                EmployeeDAO.updateData(employee);
                mainController.getAccount().setEmployee(newEmp);


                dialogPane.showInformation("Thành công", "Đổi ảnh đại diện thành công");


                mainController.initializeMenuBar();
            } catch (Exception e) {
                dialogPane.showWarning("Lỗi", "Lỗi khi lưu ảnh");
                e.printStackTrace();
            }
        } else {
            System.out.println("Người dùng đã hủy chọn ảnh.");
        }
    }


    public File resizeImage(String sourcePath) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(sourcePath));

            final int TARGET_WIDTH = 100;
            final int TARGET_HEIGHT = 100;

            BufferedImage resizedImage = new BufferedImage(TARGET_WIDTH, TARGET_HEIGHT, BufferedImage.TYPE_INT_ARGB);


            double widthRatio = (double) TARGET_WIDTH / originalImage.getWidth();
            double heightRatio = (double) TARGET_HEIGHT / originalImage.getHeight();
            double ratio = Math.max(widthRatio, heightRatio);

            int intermediateWidth = (int) (originalImage.getWidth() * ratio);
            int intermediateHeight = (int) (originalImage.getHeight() * ratio);

            int x = (intermediateWidth - TARGET_WIDTH) / 2;
            int y = (intermediateHeight - TARGET_HEIGHT) / 2;

            Graphics2D g2d = resizedImage.createGraphics();
            configureGraphicsQuality(g2d);

            g2d.drawImage(originalImage, -x, -y, intermediateWidth, intermediateHeight, null);
            g2d.dispose();

            File outputFile = File.createTempFile("resized_", ".png");
            ImageIO.write(resizedImage, "png", outputFile);
            return outputFile;

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi resize ảnh: " + e.getMessage(), e);
        }
    }

    private void configureGraphicsQuality(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    }
}
