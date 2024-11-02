package iuh.fit.controller.features.room.checking_out_controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import iuh.fit.controller.MainController;
import iuh.fit.controller.features.room.RoomBookingController;
import iuh.fit.dao.RoomReservationDetailDAO;
import iuh.fit.dao.RoomUsageServiceDAO;
import iuh.fit.models.*;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.Calculator;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


public class CheckingOutReservationFormController {
    // ==================================================================================================================
    // 1. Các biến
    // ==================================================================================================================
    @FXML
    private Button backBtn, bookingRoomNavigate, printBtn;

    @FXML
    private Label roomNumberLabel, roomCategoryLabel, checkInDateLabel,
            checkOutDateLabel, stayLengthLabel;

    @FXML
    private Label customerIDLabel, customerFullnameLabel,
            cusomerPhoneNumberLabel, customerEmailLabel,
            customerIDCardNumberLabel;

    @FXML
    private TableView<RoomUsageService> roomUsageServiceTableView;
    @FXML
    private TableColumn<RoomUsageService, String> roomUsageServiceIDColumn;
    @FXML
    private TableColumn<RoomUsageService, String> serviceNameColumn;
    @FXML
    private TableColumn<RoomUsageService, Integer> quantityColumn;
    @FXML
    private TableColumn<RoomUsageService, Double> unitPriceColumn;
    @FXML
    private TableColumn<RoomUsageService, Double> totalPriceColumn;

    @FXML
    private TableView<RoomReservationDetail> roomReservationDetailTableView;
    @FXML
    private TableColumn<RoomReservationDetail, String> roomReservationDetailID;
    @FXML
    private TableColumn<RoomReservationDetail, String> roomReservationDetailDateChanged;
    @FXML
    private TableColumn<RoomReservationDetail, String> roomReservationDetailRoomNumber;
    @FXML
    private TableColumn<RoomReservationDetail, String> roomReservationEmployeeFullname;

    @FXML
    private TitledPane titledPane;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm", Locale.forLanguageTag("vi-VN"));

    // Context
    private MainController mainController;
    private RoomWithReservation roomWithReservation;
    private Employee employee;

    private List<RoomUsageService> roomUsageServices;

    // ==================================================================================================================
    // 2. Khởi tạo và nạp dữ liệu vào giao diện
    // ==================================================================================================================
    public void initialize() {
        setupRoomReservationDetailTableView();
        setupRoomUsageServiceTableView();
    }

    public void setupContext(MainController mainController, Employee employee,
                             RoomWithReservation roomWithReservation) {
        this.mainController = mainController;
        this.employee = employee;
        this.roomWithReservation = roomWithReservation;

        titledPane.setText("Quản lý đặt phòng " + roomWithReservation.getRoom().getRoomNumber());

        setupReservationForm();
        setupButtonActions();
        loadData();
    }

    private void loadData() {
        List<RoomReservationDetail> roomReservationDetails = RoomReservationDetailDAO.getByReservationFormID(
                roomWithReservation.getReservationForm().getReservationID()
        );
        ObservableList<RoomReservationDetail> roomReservationDetailsData = FXCollections.observableArrayList(roomReservationDetails);
        roomReservationDetailTableView.setItems(roomReservationDetailsData);
        roomReservationDetailTableView.refresh();

        this.roomUsageServices = RoomUsageServiceDAO.getByReservationFormID(roomWithReservation.getReservationForm().getReservationID());
        ObservableList<RoomUsageService> roomUsageServicesData = FXCollections.observableArrayList(roomUsageServices);
        roomUsageServiceTableView.setItems(roomUsageServicesData);
        roomUsageServiceTableView.refresh();
    }

    private void setupButtonActions() {
        // Label Navigate Button
        backBtn.setOnAction(e -> navigateToRoomBookingPanel());
        bookingRoomNavigate.setOnAction(e -> navigateToRoomBookingPanel());

        // Gắn sự kiện cho nút printBtn để in hóa đơn ra file PDF
        printBtn.setOnAction(e -> {
            try {
                createInvoicePDF();
                // Thông báo cho người dùng sau khi hóa đơn đã được lưu
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Hóa đơn đã được lưu thành công!");
                alert.showAndWait();
            } catch (IOException | DocumentException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Không thể lưu hóa đơn. Vui lòng thử lại!");
                alert.showAndWait();
            }
        });

    }

    // ==================================================================================================================
    // 3. Xử lý chức năng hiển thị panel khác
    // ==================================================================================================================
    private void navigateToRoomBookingPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/iuh/fit/view/features/room/RoomBookingPanel.fxml"));
            AnchorPane layout = loader.load();

            RoomBookingController roomBookingController = loader.getController();
            roomBookingController.setupContext(mainController, employee);


            mainController.getMainPanel().getChildren().clear();
            mainController.getMainPanel().getChildren().addAll(layout.getChildren());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================================================================================================================
    // 4.  Đẩy dữ liệu lên giao diện
    // ==================================================================================================================
    private void setupReservationForm() {
        ReservationForm reservationForm = roomWithReservation.getReservationForm();

        Room reservationFormRoom = roomWithReservation.getRoom();
        Customer reservationFormCustomer = roomWithReservation.getReservationForm().getCustomer();

        roomNumberLabel.setText(reservationFormRoom.getRoomNumber());
        roomCategoryLabel.setText(reservationFormRoom.getRoomNumber());
        checkInDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckInDate()));
        checkOutDateLabel.setText(dateTimeFormatter.format(reservationForm.getCheckOutDate()));
        stayLengthLabel.setText(Calculator.calculateStayLengthToString(
                reservationForm.getCheckInDate(),
                reservationForm.getCheckOutDate()
        ));
        customerIDLabel.setText(reservationFormCustomer.getCustomerID());
        customerFullnameLabel.setText(reservationFormCustomer.getFullName());
        cusomerPhoneNumberLabel.setText(reservationFormCustomer.getPhoneNumber());
        customerEmailLabel.setText(reservationFormCustomer.getEmail());
        customerIDCardNumberLabel.setText(reservationFormCustomer.getIdCardNumber());
    }

    private void setupRoomReservationDetailTableView() {
        roomReservationDetailID.setCellValueFactory(new PropertyValueFactory<>("roomReservationDetailID"));
        roomReservationDetailDateChanged.setCellValueFactory(data -> {
            LocalDateTime dateChanged = data.getValue().getDateChanged();
            String formattedDate = dateChanged != null ? dateChanged.format(dateTimeFormatter) : "KHÔNG CÓ";
            return new SimpleStringProperty(formattedDate);
        });
        roomReservationDetailRoomNumber.setCellValueFactory(data -> {
            Room room = data.getValue().getRoom();
            String roomNumber = (room != null && room.getRoomNumber() != null) ? room.getRoomNumber() : "KHÔNG CÓ";
            return new SimpleStringProperty(roomNumber);
        });
        roomReservationEmployeeFullname.setCellValueFactory(data -> {
            Employee employee = data.getValue().getEmployee();
            String fullName = (employee != null && employee.getFullName() != null) ? employee.getFullName() : "KHÔNG CÓ";
            return new SimpleStringProperty(fullName);
        });

    }

    private void setupRoomUsageServiceTableView() {
        roomUsageServiceIDColumn.setCellValueFactory(new PropertyValueFactory<>("roomUsageServiceId"));
        serviceNameColumn.setCellValueFactory(data -> {
            HotelService service = data.getValue().getHotelService();
            String serviceName = (service != null && service.getServiceName() != null) ? service.getServiceName() : "KHÔNG CÓ";
            return new SimpleStringProperty(serviceName);
        });
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        totalPriceColumn.setCellValueFactory(data -> {
            double totalPrice = data.getValue().getQuantity() * data.getValue().getUnitPrice();
            return new SimpleDoubleProperty(totalPrice).asObject();
        });
    }

    public void createInvoicePDF() throws DocumentException, IOException {
        // Hiển thị hộp thoại để chọn đường dẫn và tên tệp
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu hóa đơn PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("HoaDon.pdf");
        File file = fileChooser.showSaveDialog(new Stage());

        if (file == null) {
            return;
        }

        // Tạo tài liệu PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        // Load font hỗ trợ UTF-8
        InputStream fontStream = getClass().getResourceAsStream("/iuh/fit/fonts/arial-unicode-ms.ttf");
        if (fontStream == null) {
            throw new IOException("Font not found");
        }
        BaseFont unicodeFont = BaseFont.createFont("arial-unicode-ms.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontStream.readAllBytes(), null);

        Font font = new Font(unicodeFont, 12);
        Font titleFont = new Font(unicodeFont, 18, Font.BOLD);
        Font headerFont = new Font(unicodeFont, 12, Font.BOLD, BaseColor.WHITE);

        // Tiêu đề hóa đơn
        Paragraph title = new Paragraph("Hóa Đơn Thanh Toán Khách Sạn", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));

        // Thông tin khách sạn và khách hàng (ẩn border)
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(10f);
        infoTable.setSpacingAfter(10f);

        PdfPCell hotelCell = new PdfPCell(new Phrase("Thông tin khách sạn", titleFont));
        hotelCell.setBorder(PdfPCell.NO_BORDER);
        infoTable.addCell(hotelCell);

        PdfPCell customerCell = new PdfPCell(new Phrase("Thông tin khách hàng", titleFont));
        customerCell.setBorder(PdfPCell.NO_BORDER);
        infoTable.addCell(customerCell);

        hotelCell = new PdfPCell(new Phrase("Tên khách sạn: Luxury Hotel", font));
        hotelCell.setBorder(PdfPCell.NO_BORDER);
        infoTable.addCell(hotelCell);

        customerCell = new PdfPCell(new Phrase("Tên khách hàng: " + roomWithReservation.getReservationForm().getCustomer().getFullName(), font));
        customerCell.setBorder(PdfPCell.NO_BORDER);
        infoTable.addCell(customerCell);

        hotelCell = new PdfPCell(new Phrase("Địa chỉ: 123 Đường ABC, TP.HCM", font));
        hotelCell.setBorder(PdfPCell.NO_BORDER);
        infoTable.addCell(hotelCell);

        customerCell = new PdfPCell(new Phrase("Email: " + roomWithReservation.getReservationForm().getCustomer().getEmail(), font));
        customerCell.setBorder(PdfPCell.NO_BORDER);
        infoTable.addCell(customerCell);

        hotelCell = new PdfPCell(new Phrase("Số điện thoại: (84) 123-456-789", font));
        hotelCell.setBorder(PdfPCell.NO_BORDER);
        infoTable.addCell(hotelCell);

        customerCell = new PdfPCell(new Phrase("SĐT: " + roomWithReservation.getReservationForm().getCustomer().getPhoneNumber(), font));
        customerCell.setBorder(PdfPCell.NO_BORDER);
        infoTable.addCell(customerCell);

        document.add(infoTable);
        document.add(new Paragraph("\n"));

        // Tiêu đề cho bảng "Dịch vụ đã sử dụng"
        Paragraph serviceTitle = new Paragraph("Dịch vụ đã sử dụng", titleFont);
        serviceTitle.setSpacingAfter(10f);  // Thêm khoảng cách dưới tiêu đề
        document.add(serviceTitle);

        // Bảng dịch vụ đã sử dụng
        PdfPTable serviceTable = new PdfPTable(4); // 4 cột
        serviceTable.setWidthPercentage(100);
        serviceTable.setWidths(new int[]{3, 1, 2, 2});

        // Thêm tiêu đề bảng với màu nền xanh dương
        PdfPCell headerCell = new PdfPCell(new Phrase("Tên dịch vụ", headerFont));
        headerCell.setBackgroundColor(BaseColor.BLUE);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8f);
        serviceTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("SL", headerFont));
        headerCell.setBackgroundColor(BaseColor.BLUE);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8f);
        serviceTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Đơn giá", headerFont));
        headerCell.setBackgroundColor(BaseColor.BLUE);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8f);
        serviceTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Thành tiền", headerFont));
        headerCell.setBackgroundColor(BaseColor.BLUE);
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8f);
        serviceTable.addCell(headerCell);

        // Nội dung bảng từ danh sách dịch vụ
        double totalServiceCost = 0;
        for (RoomUsageService service : roomUsageServices) {
            String serviceName = service.getHotelService().getServiceName();
            int quantity = service.getQuantity();
            double unitPrice = service.getUnitPrice();
            double serviceTotal = quantity * unitPrice;
            totalServiceCost += serviceTotal;

            String formattedUnitPrice = String.format("%,.0f VND", unitPrice);
            String formattedServiceTotal = String.format("%,.0f VND", serviceTotal);

            PdfPCell contentCell = new PdfPCell(new Phrase(serviceName, font));
            contentCell.setPadding(10f);
            serviceTable.addCell(contentCell);

            contentCell = new PdfPCell(new Phrase(String.valueOf(quantity), font));
            contentCell.setPadding(10f);
            serviceTable.addCell(contentCell);

            contentCell = new PdfPCell(new Phrase(formattedUnitPrice, font));
            contentCell.setPadding(10f);
            serviceTable.addCell(contentCell);

            contentCell = new PdfPCell(new Phrase(formattedServiceTotal, font));
            contentCell.setPadding(10f);
            serviceTable.addCell(contentCell);
        }

        document.add(serviceTable);
        document.add(new Paragraph("\n"));

        // Tổng tiền dịch vụ
        String formattedTotalServiceCost = String.format("%,.0f VND", totalServiceCost);
        document.add(new Paragraph("Tổng tiền dịch vụ: " + formattedTotalServiceCost, font));

        document.add(new Paragraph("\n"));

        // Tiêu đề cho bảng "Lịch sử dùng phòng"
        Paragraph historyTitle = new Paragraph("Lịch sử dùng phòng", titleFont);
        historyTitle.setSpacingAfter(10f);  // Thêm khoảng cách dưới tiêu đề
        document.add(historyTitle);

        // Bảng lịch sử dùng phòng
        PdfPTable historyTable = new PdfPTable(3); // 3 cột cho lịch sử dùng phòng
        historyTable.setWidthPercentage(100);
        historyTable.setWidths(new int[]{2, 2, 3});

        // Thêm tiêu đề bảng với màu nền xanh dương
        PdfPCell historyHeaderCell = new PdfPCell(new Phrase("Ngày thay đổi", headerFont));
        historyHeaderCell.setBackgroundColor(BaseColor.BLUE);
        historyHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        historyHeaderCell.setPadding(8f);
        historyTable.addCell(historyHeaderCell);

        historyHeaderCell = new PdfPCell(new Phrase("Phòng", headerFont));
        historyHeaderCell.setBackgroundColor(BaseColor.BLUE);
        historyHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        historyHeaderCell.setPadding(8f);
        historyTable.addCell(historyHeaderCell);

        historyHeaderCell = new PdfPCell(new Phrase("Nhân viên thực hiện", headerFont));
        historyHeaderCell.setBackgroundColor(BaseColor.BLUE);
        historyHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        historyHeaderCell.setPadding(8f);
        historyTable.addCell(historyHeaderCell);

        // Thêm dữ liệu vào bảng lịch sử dùng phòng
        for (RoomReservationDetail detail : roomReservationDetailTableView.getItems()) {
            String dateChanged = dateTimeFormatter.format(detail.getDateChanged());
            String roomNumber = detail.getRoom().getRoomNumber();
            String employeeName = detail.getEmployee().getFullName();

            historyTable.addCell(new PdfPCell(new Phrase(dateChanged, font)));
            historyTable.addCell(new PdfPCell(new Phrase(roomNumber, font)));
            historyTable.addCell(new PdfPCell(new Phrase(employeeName, font)));
        }

        document.add(historyTable);

        // Đóng tài liệu
        document.close();
        System.out.println("PDF created successfully.");
    }





}
