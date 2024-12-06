package iuh.fit.controller.features;

import iuh.fit.Main;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.invoice.InvoiceManagerController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.controller.features.room.checking_out_controllers.CheckingOutReservationFormController;
import iuh.fit.controller.features.room.creating_reservation_form_controllers.RoomOnUseItemController;
import iuh.fit.controller.features.room.creating_reservation_form_controllers.RoomOverDueController;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.MessageDAO;
import iuh.fit.models.Account;
import iuh.fit.models.misc.Notifications;
import iuh.fit.utils.RoomManagementService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Le Tran Gia Huy
 * @created 05/12/2024 - 3:48 PM
 * @project HotelManagement
 * @package iuh.fit.controller.features
 */
public class NotificationButtonController {
    @FXML
    private Button messageBtn;
    @FXML
    private Circle notifCircle;
    @FXML
    private Label notifNumber;
    @FXML
    private ImageView img;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Account currentAccount;
    private List<Notifications> notificationList = new ArrayList<Notifications>();
    private AnchorPane emptyNotification;
    private AnchorPane buttonPane;
    Button deleteAllBtn = new Button("Xóa tất cả thông báo");
    // Tạo formatter với định dạng dd/MM/yyyy HH:mm:ss
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    Popup popup = new Popup();
    ScrollPane contentScrollPane = new ScrollPane();
    VBox messageList = new VBox();

    private MainController mainController;

    public NotificationButtonController initialize(Account account, MainController mainController){
        createEmptyNotification();
        createDeleteButton();
        createEventListener();
        setupContext(account, mainController);
        createPopupMessage();
        Platform.runLater(this::drawingBeforeShowing);
        loadData();
        handelButton();

        System.out.println("KET QUA NOTIFYCONTROLLER (1): "+this);
        return this;
    }

    public void setupContext(Account account, MainController mainController){
        this.currentAccount = account;
        this.mainController = mainController;
        createEventListener();
    }

    private void drawingBeforeShowing(){
        Platform.runLater(()->{
            popup.show(messageBtn.getScene().getWindow(), 0, 0);
            popup.hide();
        });
    }

    private void loadData() {
        notificationList.clear();
        notificationList = MessageDAO.getMessage(Objects.requireNonNull(EmployeeDAO.getEmployeeByAccountID(currentAccount.getAccountID())).getEmployeeID());
        insertNotify();
    }

    private void createPopupMessage() {
        if (!popup.getContent().contains(contentScrollPane)) {
            popup.getContent().add(contentScrollPane);
        }
        contentScrollPane.setStyle("-fx-background-color: lightgray; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10;");
        contentScrollPane.setContent(messageList);
        contentScrollPane.setFocusTraversable(false);
        messageList.setSpacing(10);
        messageList.setStyle("-fx-background-color: lightgray;");
        contentScrollPane.setFitToWidth(true);
    }

    private void checkNotification(){
        if(notificationList.isEmpty()){
            if(messageList.getChildren().contains(emptyNotification)){
                notifCircle.setVisible(false);
                notifNumber.setVisible(false);
            }else{
                messageList.getChildren().add(emptyNotification);
                notifCircle.setVisible(false);
                notifNumber.setVisible(false);
            }
            checkSize();
        }else if(notificationList.stream().filter(Notifications::isRead).count() == notificationList.size()){
            messageList.getChildren().remove(emptyNotification);
            notifCircle.setVisible(false);
            notifNumber.setVisible(false);
            checkSize();
            if(!messageList.getChildren().contains(buttonPane)){
                messageList.getChildren().addLast(buttonPane);
            }
        }else{
            messageList.getChildren().remove(emptyNotification);
            notifCircle.setVisible(true);
            notifNumber.setVisible(true);
            Platform.runLater(()->{
                notifNumber.setText(String.valueOf(notificationList.stream().filter(x-> !x.isRead()).count()));
            });
            checkSize();
            if(!messageList.getChildren().contains(buttonPane)){
                messageList.getChildren().addLast(buttonPane);
            }
        }
    }

    private void checkSize(){
        if(messageList.getChildren().size() <= 1 && messageList.getChildren().contains(emptyNotification)){
            contentScrollPane.setPrefSize(400, 68.5);
        }else if(messageList.getChildren().size() <= 2){
            contentScrollPane.setPrefSize(400, 177.6);
        }else{
            contentScrollPane.setPrefSize(400, 200);
        }
    }

    private void insertNotify(){
        messageList.getChildren().clear();
        notificationList.forEach(x->{
            messageList.getChildren().addFirst(createTextMessage(x));
        });
        checkNotification();
    }

    public void handelButton(){
        // Tạo hiệu ứng khi hover
        ColorAdjust hoverEffect = new ColorAdjust();
        hoverEffect.setBrightness(-0.1); // Làm màu đậm hơn

        ColorAdjust hoverEffect2 = new ColorAdjust();
        hoverEffect2.setBrightness(-0.2); // Làm màu đậm hơn

        // Khi hover vào button
        messageBtn.setOnMouseEntered(event -> img.setEffect(hoverEffect));

        // Khi rời chuột khỏi button
        messageBtn.setOnMouseExited(event -> img.setEffect(null));

        messageBtn.setOnMousePressed(event -> img.setEffect(hoverEffect2));

        messageBtn.setOnMouseReleased(event -> img.setEffect(null));

        try{
            messageBtn.setOnAction(event -> {
                if (!popup.isShowing()) {
                    contentScrollPane.setFocusTraversable(false);
                    contentScrollPane.setVvalue(0);
                    double popupWidth = contentScrollPane.getWidth(); // Lấy chiều rộng thực tế

                    // Định vị popup ngay dưới nút
                    Point2D screenCoords = messageBtn.localToScreen(messageBtn.getLayoutBounds().getMinX(), messageBtn.getLayoutBounds().getMaxY());
                    double x = screenCoords.getX() - popupWidth + 35; // Tính toán vị trí popup
                    double y = screenCoords.getY();

                    // Hiển thị popup ở vị trí đã tính toán
                    Window mainWindow = messageBtn.getScene().getWindow();
                    popup.show(mainWindow, x, y);
                    notificationList.forEach(notify->{
                        notify.setRead(true);
                        MessageDAO.updateStatus(notify.getNotificationID());
                    });
                    checkNotification();
                } else {
                    contentScrollPane.setFocusTraversable(false);
                    popup.hide();
                    notifCircle.setVisible(false);
                    notifNumber.setVisible(false);
                }
            });

            // Lắng nghe sự kiện chuột trên toàn bộ cửa sổ (hoặc root node của scene)
            Platform.runLater(() -> {
                if (messageBtn.getScene() != null) {
                    contentScrollPane.setFocusTraversable(false);
                    messageBtn.getScene().getWindow().addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                        // Kiểm tra nếu con trỏ chuột không nằm trong popup và không phải là nút thông báo
                        if (!contentScrollPane.isHover() && !messageBtn.isHover()) {
                            popup.hide();  // Ẩn popup nếu chuột không ở trong popup hay nút
                        }
                    });
                }
            });

            deleteAllBtn.setOnAction(event->{
                handelDeleteAllNotification();
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getInfo(String title, String message){
        Notifications notification = new Notifications();
        notification.setTitle(title);
        notification.setContent(message);
        notification.setEntityID(Objects.requireNonNull(EmployeeDAO.getEmployeeByAccountID(currentAccount.getAccountID())).getEmployeeID());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        MessageDAO.createData(notification);
        loadData();
        Platform.runLater(this::drawingBeforeShowing);
    }

    private AnchorPane createTextMessage(Notifications notification){
        AnchorPane content = new AnchorPane();
        VBox alignment = new VBox();
        Label text = new Label(notification.getTitle());
        text.setStyle("-fx-padding: 5; -fx-font-weight: bold");
        Label text2 = new Label(notification.getContent());
        text2.setStyle("-fx-padding: 5;");
        Label text3 = new Label("Ngày tạo: "+notification.getCreatedAt().format(formatter));
        text3.setStyle("-fx-padding: 5;");
        alignment.getChildren().add(text);
        alignment.getChildren().add(text2);
        alignment.getChildren().add(text3);
        content.getChildren().add(alignment);
        content.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10;");
        content.setPrefWidth(260);
        System.out.println(content.getHeight());

        return content;
    }

    private void createEmptyNotification(){
        AnchorPane content = new AnchorPane();
        VBox alignment = new VBox();
        Label text = new Label("Không có thông báo nào");
        text.setStyle("-fx-padding: 5; -fx-font-weight: bold");
        alignment.getChildren().add(text);
        content.getChildren().add(alignment);
        content.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-radius: 10; -fx-background-radius: 10;");
        content.setPrefWidth(260);
        this.emptyNotification = content;
    }

    private void createDeleteButton(){
        AnchorPane content = new AnchorPane();
        VBox alignment = new VBox();
        deleteAllBtn.setStyle("-fx-padding: 5; -fx-font-weight: bold");
        deleteAllBtn.setFocusTraversable(false);
        alignment.getChildren().add(deleteAllBtn);
        content.getChildren().add(alignment);
        content.setStyle("-fx-background-color: lightgray; -fx-border-radius: 10; -fx-background-radius: 10;");
        content.setPrefWidth(260);
        this.buttonPane = content;
    }

    private void handelDeleteAllNotification(){
        notificationList.forEach(x->{
            MessageDAO.deleteData(x.getNotificationID());
        });
        notificationList.clear();
        messageList.getChildren().clear();
        checkNotification();
    }

    private void createEventListener(){
//        Main.setupController(this);
//        MainController.setupController(this);
        RoomManagementService.setupController(this);
        CheckingOutReservationFormController.setupController(this);
        RoomOverDueController.setupController(this);
        InvoiceManagerController.setupController(this);
        RoomBookingController.setupController(this);
        RoomOnUseItemController.setupController(this);
    }
}
