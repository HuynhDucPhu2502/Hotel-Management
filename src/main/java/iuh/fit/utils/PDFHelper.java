package iuh.fit.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import iuh.fit.dao.RoomUsageServiceDAO;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.RoomUsageService;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PDFHelper {
    public static void createInvoicePDF(ReservationForm reservationForm) throws DocumentException, IOException {
        ArrayList<RoomUsageService> roomUsageServices = (ArrayList<RoomUsageService>) RoomUsageServiceDAO
                .getByReservationFormID(reservationForm.getReservationID());


        // Hiển thị hộp thoại để chọn đường dẫn và tên tệp
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Lưu hóa đơn PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("HoaDon.pdf");
        File file = fileChooser.showSaveDialog(new Stage());

        if (file == null) {
            throw new IllegalArgumentException("Bạn đã hủy bỏ quy trình tạo PDF");
        }

        // Tạo tài liệu PDF
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        // Load font hỗ trợ UTF-8
        InputStream fontStream = PDFHelper.class.getResourceAsStream("/iuh/fit/fonts/arial-unicode-ms.ttf");
        if (fontStream == null) {
            throw new IOException("Font not found");
        }
        BaseFont unicodeFont = BaseFont.createFont("arial-unicode-ms.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, true, fontStream.readAllBytes(), null);

        Font font = new Font(unicodeFont, 12);
        Font titleFont = new Font(unicodeFont, 18, Font.BOLD, BaseColor.BLACK);
        Font headerFont = new Font(unicodeFont, 12, Font.BOLD, BaseColor.BLACK);

        // ======================================================================
        // Thêm Watermark logo
        // ======================================================================
        InputStream watermarkPath = PDFHelper.class.getResourceAsStream("/iuh/fit/imgs/hotel_logo.png");
        if (watermarkPath == null) {
            throw new IOException("Watermark image not found");
        }
        Image watermarkLogo = Image.getInstance(watermarkPath.readAllBytes());

        // Lấy kích thước của trang và căn giữa watermark
        float pageWidth = document.getPageSize().getWidth();
        float pageHeight = document.getPageSize().getHeight();
        watermarkLogo.scaleToFit(300, 300); // Điều chỉnh kích thước watermark
        float watermarkX = (pageWidth - watermarkLogo.getScaledWidth()) / 2;  // Căn giữa theo chiều ngang
        float watermarkY = (pageHeight - watermarkLogo.getScaledHeight()) / 2; // Căn giữa theo chiều dọc
        watermarkLogo.setAbsolutePosition(watermarkX, watermarkY);

        PdfContentByte canvas = writer.getDirectContentUnder();
        canvas.saveState();
        PdfGState gs1 = new PdfGState();
        gs1.setFillOpacity(0.2f); // Độ trong suốt của watermark (20% opacity)
        canvas.setGState(gs1);
        canvas.addImage(watermarkLogo);
        canvas.restoreState();

        // ======================================================================
        // Phần đầu hóa đơn với bảng 3 cột
        // ======================================================================
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new int[]{1, 3, 1});

        // Cột 1: Logo
        InputStream logoPath = PDFHelper.class.getResourceAsStream("/iuh/fit/icons/login_panel_icons/ic_hotel.png");
        if (logoPath == null) {
            throw new IOException("Logo image not found");
        }
        Image logo = Image.getInstance(logoPath.readAllBytes());
        logo.scaleToFit(100, 100); // Kích thước logo trên header
        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(PdfPCell.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerTable.addCell(logoCell);

        // Cột 2: Tiêu đề và ngày xuất hóa đơn
        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(PdfPCell.NO_BORDER);
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        Paragraph titleParagraph = new Paragraph("HÓA ĐƠN THANH TOÁN", titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleCell.addElement(titleParagraph);


        Paragraph dateParagraph = new Paragraph("Ngày 29 tháng 06 năm 2024", font);
        dateParagraph.setAlignment(Element.ALIGN_CENTER);
        titleCell.addElement(dateParagraph);

        headerTable.addCell(titleCell);

        // Cột 3: Mã hóa đơn
        PdfPCell invoiceInfoCell = new PdfPCell();
        invoiceInfoCell.setBorder(PdfPCell.NO_BORDER);
        invoiceInfoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceInfoCell.addElement(new Paragraph("Mã hóa đơn: 128", new Font(unicodeFont, 12, Font.BOLD, BaseColor.BLACK)));
        headerTable.addCell(invoiceInfoCell);

        // Thêm bảng vào tài liệu
        document.add(headerTable);
        document.add(new Paragraph("\n"));

        // ======================================================================
        // Table khách sạn và khách hàng
        // ======================================================================
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setSpacingBefore(10f);
        infoTable.setSpacingAfter(10f);

        // Cột "Thông tin khách sạn"
        PdfPCell hotelCell = new PdfPCell(new Phrase("Thông tin khách sạn", titleFont));
        hotelCell.setBorder(PdfPCell.NO_BORDER);
        hotelCell.setPaddingBottom(10f);
        infoTable.addCell(hotelCell);

        // Cột "Thông tin khách hàng"
        PdfPCell customerCell = new PdfPCell(new Phrase("Thông tin khách hàng", titleFont));
        customerCell.setBorder(PdfPCell.NO_BORDER);
        customerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        customerCell.setPaddingBottom(10f);
        infoTable.addCell(customerCell);

        // Các ô chi tiết của "Thông tin khách sạn"
        hotelCell = new PdfPCell(new Phrase("Tên khách sạn: Khách Sạn Chức Phú Gia Tiến", font));
        hotelCell.setBorder(PdfPCell.NO_BORDER);
        infoTable.addCell(hotelCell);

        customerCell = new PdfPCell(new Phrase("Tên khách hàng: Tran Van C", font)); // Dữ liệu mẫu
        customerCell.setBorder(PdfPCell.NO_BORDER);
        customerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        infoTable.addCell(customerCell);

        hotelCell = new PdfPCell(new Phrase("Địa chỉ: 120 Xóm Chiếu, P14, Q4, TPHCM", font));
        hotelCell.setBorder(PdfPCell.NO_BORDER);
        infoTable.addCell(hotelCell);

        customerCell = new PdfPCell(new Phrase("Email: tranvanc@gmail.com", font)); // Dữ liệu mẫu
        customerCell.setBorder(PdfPCell.NO_BORDER);
        customerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        infoTable.addCell(customerCell);

        hotelCell = new PdfPCell(new Phrase("Số điện thoại: (84) 123-456-789", font));
        hotelCell.setBorder(PdfPCell.NO_BORDER);
        infoTable.addCell(hotelCell);

        customerCell = new PdfPCell(new Phrase("SĐT: 0912345680", font)); // Dữ liệu mẫu
        customerCell.setBorder(PdfPCell.NO_BORDER);
        customerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        infoTable.addCell(customerCell);

        document.add(infoTable);
        document.add(new Paragraph("\n"));

        // ======================================================================
        // Thông Tin Phiếu Đặt Phòng
        // ======================================================================
        Paragraph bookingInfoTitle = new Paragraph("Thông Tin Phiếu Đặt Phòng", titleFont);
        document.add(bookingInfoTitle);
        document.add(new Paragraph("\n"));

        PdfPTable bookingInfoTable = new PdfPTable(2);
        bookingInfoTable.setWidthPercentage(100);

        PdfPCell leftCell = new PdfPCell(new Phrase("Số phòng: 102", font));  // Dữ liệu mẫu
        leftCell.setBorder(PdfPCell.NO_BORDER);
        bookingInfoTable.addCell(leftCell);

        PdfPCell rightCell = new PdfPCell(new Phrase("Loại phòng: Deluxe", font));  // Dữ liệu mẫu
        rightCell.setBorder(PdfPCell.NO_BORDER);
        bookingInfoTable.addCell(rightCell);

        leftCell = new PdfPCell(new Phrase("Ngày nhận phòng: 29-10-2024 12:24", font));  // Dữ liệu mẫu
        leftCell.setBorder(PdfPCell.NO_BORDER);
        bookingInfoTable.addCell(leftCell);

        rightCell = new PdfPCell(new Phrase("Ngày trả phòng: 02-11-2024 11:24", font));  // Dữ liệu mẫu
        rightCell.setBorder(PdfPCell.NO_BORDER);
        bookingInfoTable.addCell(rightCell);

        leftCell = new PdfPCell(new Phrase("Số ngày lưu trú: 4.0 ngày", font));  // Dữ liệu mẫu
        leftCell.setBorder(PdfPCell.NO_BORDER);
        bookingInfoTable.addCell(leftCell);

        rightCell = new PdfPCell(new Phrase(" ", font));  // Ô trống để cân bằng bảng
        rightCell.setBorder(PdfPCell.NO_BORDER);
        bookingInfoTable.addCell(rightCell);

        document.add(bookingInfoTable);
        document.add(new Paragraph("\n"));

        // ======================================================================
        // Bảng Dịch Vụ Đã Sử Dụng
        // ======================================================================
        Paragraph serviceTitle = new Paragraph("Dịch vụ đã sử dụng", titleFont);
        serviceTitle.setSpacingAfter(10f);
        document.add(serviceTitle);

        PdfPTable serviceTable = new PdfPTable(4); // 4 cột
        serviceTable.setWidthPercentage(100);
        serviceTable.setWidths(new int[]{3, 1, 2, 2});

        // Thêm tiêu đề bảng với màu nền xanh dương
        PdfPCell headerCell = new PdfPCell(new Phrase("Tên dịch vụ", headerFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8f);
        serviceTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("SL", headerFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8f);
        serviceTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Đơn giá (VND)", headerFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8f);
        serviceTable.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Thành tiền (VND)", headerFont));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setPadding(8f);
        serviceTable.addCell(headerCell);

        double totalServiceCost = 0;
        for (RoomUsageService service : roomUsageServices) {
            String serviceName = service.getHotelService().getServiceName();
            int quantity = service.getQuantity();
            double unitPrice = service.getUnitPrice();
            double serviceTotal = quantity * unitPrice;
            totalServiceCost += serviceTotal;

            String formattedUnitPrice = String.format("%,.0f", unitPrice);
            String formattedServiceTotal = String.format("%,.0f", serviceTotal);

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

        // ======================================================================
        // Bảng Tổng Tiền
        // ======================================================================
        PdfPTable totalTable = createTotalTable(totalServiceCost, 500, 100, headerFont, font);
        document.add(totalTable);

        // Đóng tài liệu
        document.close();
        System.out.println("PDF created successfully.");
    }

    private static PdfPTable createTotalTable(double totalServiceCost, double totalRoomCost, double deposit, Font headerFont, Font font) {
        PdfPTable totalTable = new PdfPTable(2); // 2 cột
        totalTable.setWidthPercentage(100);
        try {
            totalTable.setWidths(new int[]{3, 2});
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        double finalAmount = totalServiceCost + totalRoomCost - deposit; // Thành tiền

        // Tạo tiêu đề bảng với đường viền trên
        PdfPCell headerCell1 = new PdfPCell(new Phrase("Tên tiền thanh toán", headerFont));
        headerCell1.setBorder(PdfPCell.BOTTOM);
        headerCell1.setBorderWidthBottom(2f);
        headerCell1.setPadding(8f);
        totalTable.addCell(headerCell1);

        PdfPCell headerCell2 = new PdfPCell(new Phrase("Số tiền", headerFont));
        headerCell2.setBorder(PdfPCell.BOTTOM);
        headerCell2.setBorderWidthBottom(2f);
        headerCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell2.setPadding(8f);
        totalTable.addCell(headerCell2);

        // Dòng 1: Tổng tiền dịch vụ
        PdfPCell totalServiceCell = new PdfPCell(new Phrase("Tổng tiền dịch vụ", font));
        totalServiceCell.setBorder(PdfPCell.NO_BORDER);
        totalServiceCell.setPadding(8f);
        totalTable.addCell(totalServiceCell);

        PdfPCell totalServiceCostCell = new PdfPCell(new Phrase(String.format("%,.0f VND", totalServiceCost), font));
        totalServiceCostCell.setBorder(PdfPCell.NO_BORDER);
        totalServiceCostCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalServiceCostCell.setPadding(8f);
        totalTable.addCell(totalServiceCostCell);

        // Dòng 2: Tổng tiền phòng
        PdfPCell totalRoomCell = new PdfPCell(new Phrase("Tổng tiền phòng", font));
        totalRoomCell.setBorder(PdfPCell.NO_BORDER);
        totalRoomCell.setPadding(8f);
        totalTable.addCell(totalRoomCell);

        PdfPCell totalRoomCostCell = new PdfPCell(new Phrase(String.format("%,.0f VND", totalRoomCost), font));
        totalRoomCostCell.setBorder(PdfPCell.NO_BORDER);
        totalRoomCostCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalRoomCostCell.setPadding(8f);
        totalTable.addCell(totalRoomCostCell);

        // Dòng 3: Tiền đặt cọc
        PdfPCell depositCell = new PdfPCell(new Phrase("Tiền đặt cọc", font));
        depositCell.setBorder(PdfPCell.NO_BORDER);
        depositCell.setPadding(8f);
        totalTable.addCell(depositCell);

        PdfPCell depositCostCell = new PdfPCell(new Phrase(String.format("%,.0f VND", deposit), font));
        depositCostCell.setBorder(PdfPCell.NO_BORDER);
        depositCostCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        depositCostCell.setPadding(8f);
        totalTable.addCell(depositCostCell);

        // Dòng 4: Thành tiền với đường viền trên dày
        PdfPCell finalAmountCell = new PdfPCell(new Phrase("Thành tiền", font));
        finalAmountCell.setBorder(PdfPCell.TOP);
        finalAmountCell.setBorderWidthTop(2f);
        finalAmountCell.setPadding(8f);
        totalTable.addCell(finalAmountCell);

        PdfPCell finalAmountCostCell = new PdfPCell(new Phrase(String.format("%,.0f VND", finalAmount), font));
        finalAmountCostCell.setBorder(PdfPCell.TOP);
        finalAmountCostCell.setBorderWidthTop(2f);
        finalAmountCostCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        finalAmountCostCell.setPadding(8f);
        totalTable.addCell(finalAmountCostCell);

        return totalTable;
    }

}
