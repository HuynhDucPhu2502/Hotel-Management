package iuh.fit.controller.features.employee_information;

import com.dlsc.gemsfx.DialogPane;
import iuh.fit.controller.MainController;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.models.Employee;
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
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;

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

    private static String AVATAR_DEFAULT = "target/classes/iuh/fit/imgs/default_avatar.png";

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
        if(!employee.getAvatar().equals(AVATAR_DEFAULT)){
            Path filePath = Paths.get(employee.getAvatar());
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                dialogPane.showWarning("Lỗi", "Lỗi khi thay thế ảnh");
                e.printStackTrace();
            }
        }

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
                // Kiểm tra thư mục đích có tồn tại không
                File targetDir = new File("target/classes/iuh/fit/imgs");
                if (!targetDir.exists()) {
                    dialogPane.showWarning("Lỗi", "Không tìm thấy thư mục lưu");
                    return;
                }
                //Xóa ảnh cũ
                if(!employee.getAvatar().equals(AVATAR_DEFAULT)){
                    Path filePath = Paths.get(employee.getAvatar());
                    try {
                            Files.delete(filePath);
                    } catch (IOException e) {
                        dialogPane.showWarning("Lỗi", "Lỗi khi thay thế ảnh");
                        e.printStackTrace();
                    }
                }
                Path sourcePath = selectedFile.toPath();
                Path targetPath = Paths.get("target/classes/iuh/fit/imgs", selectedFile.getName());
                resizeAndCopyImage(sourcePath.toString(), targetPath.toString(), 100, 100);

                String finalpath = targetPath.toString().replace("\\", "/");
                employee.setAvatar(finalpath);

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


    public void resizeAndCopyImage(String sourcePath, String targetPath, int targetWidth, int targetHeight) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(sourcePath));

            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            double widthRatio = (double) targetWidth / originalWidth;
            double heightRatio = (double) targetHeight / originalHeight;
            double ratio = Math.min(widthRatio, heightRatio);

            int newWidth = (int) (originalWidth * ratio);
            int newHeight = (int) (originalHeight * ratio);

            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

            g2d.setComposite(AlphaComposite.Clear);
            g2d.fillRect(0, 0, newWidth, newHeight);

            g2d.setComposite(AlphaComposite.Src);

            BufferedImage tempImage = originalImage;
            double currentWidth = originalWidth;
            double currentHeight = originalHeight;

            while (currentWidth > newWidth * 1.1 || currentHeight > newHeight * 1.1) {
                currentWidth *= 0.7;
                currentHeight *= 0.7;

                BufferedImage tempResized = new BufferedImage((int)currentWidth, (int)currentHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D tempG2D = tempResized.createGraphics();

                tempG2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                tempG2D.drawImage(tempImage, 0, 0, (int)currentWidth, (int)currentHeight, null);
                tempG2D.dispose();

                tempImage = tempResized;
            }

            g2d.drawImage(tempImage, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(
                    targetPath.substring(targetPath.lastIndexOf(".") + 1).toLowerCase());

            if (writers.hasNext()) {
                ImageWriter writer = writers.next();
                ImageWriteParam param = writer.getDefaultWriteParam();

                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(0.95f);
                }

                ImageOutputStream output = ImageIO.createImageOutputStream(new File(targetPath));
                writer.setOutput(output);

                IIOImage image = new IIOImage(resizedImage, null, null);
                writer.write(null, image, param);

                output.close();
                writer.dispose();
            } else {
                ImageIO.write(resizedImage,
                        targetPath.substring(targetPath.lastIndexOf(".") + 1).toLowerCase(),
                        new File(targetPath));
            }

            System.out.println("Đã resize và copy ảnh thành công!");
            System.out.println("Kích thước mới: " + newWidth + "x" + newHeight);

        } catch (Exception e) {
            System.err.println("Lỗi khi resize và copy ảnh: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
