package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    public static List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = """
            SELECT i.invoiceID, i.invoiceDate, i.roomCharge, i.servicesCharge,
                   i.totalDue, i.netDue, t.taxID, t.taxName, t.taxRate, 
                   t.dateOfCreation, t.activate, rf.reservationFormID,
                   rf.reservationDate, rf.checkInDate, rf.checkOutDate, rf.roomBookingDeposit,
                   e.employeeID, e.fullName AS employeeName, e.position,
                   c.customerID, c.fullName AS customerName, c.phoneNumber, c.email, c.idCardNumber,
                   r.roomID, r.roomStatus, r.dateOfCreation AS roomDateOfCreation,
                   rc.roomCategoryID, rc.roomCategoryName, rc.numberOfBed
            FROM Invoice i
            LEFT JOIN Tax t ON i.taxID = t.taxID
            LEFT JOIN ReservationForm rf ON i.reservationFormID = rf.reservationFormID
            LEFT JOIN Employee e ON rf.employeeID = e.employeeID
            LEFT JOIN Customer c ON rf.customerID = c.customerID
            LEFT JOIN Room r ON rf.roomID = r.roomID
            LEFT JOIN RoomCategory rc ON r.roomCategoryID = rc.roomCategoryID
            WHERE EXISTS (
                SELECT 1 FROM HistoryCheckIn hci WHERE hci.reservationFormID = rf.reservationFormID
            ) AND EXISTS (
                SELECT 1 FROM HistoryCheckOut hco WHERE hco.reservationFormID = rf.reservationFormID
            );
            """;

        try (Connection connection = DBHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                Invoice invoice = extractData(rs);
                invoices.add(invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public static Invoice getInvoiceByID(String invoiceID) {
        String sql = """
            SELECT i.invoiceID, i.invoiceDate, i.roomCharge, i.servicesCharge,
                   i.totalDue, i.netDue, t.taxID, t.taxName, t.taxRate, 
                   t.dateOfCreation, t.activate, rf.reservationFormID,
                   rf.reservationDate, rf.checkInDate, rf.checkOutDate, rf.roomBookingDeposit,
                   e.employeeID, e.fullName AS employeeName, e.position,
                   c.customerID, c.fullName AS customerName, c.phoneNumber, c.email, c.idCardNumber,
                   r.roomID, r.roomStatus, r.dateOfCreation AS roomDateOfCreation,
                   rc.roomCategoryID, rc.roomCategoryName, rc.numberOfBed
            FROM Invoice i
            LEFT JOIN Tax t ON i.taxID = t.taxID
            LEFT JOIN ReservationForm rf ON i.reservationFormID = rf.reservationFormID
            LEFT JOIN Employee e ON rf.employeeID = e.employeeID
            LEFT JOIN Customer c ON rf.customerID = c.customerID
            LEFT JOIN Room r ON rf.roomID = r.roomID
            LEFT JOIN RoomCategory rc ON r.roomCategoryID = rc.roomCategoryID
            WHERE i.invoiceID = ?
              AND EXISTS (
                SELECT 1 FROM HistoryCheckIn hci WHERE hci.reservationFormID = rf.reservationFormID
              ) AND EXISTS (
                SELECT 1 FROM HistoryCheckOut hco WHERE hco.reservationFormID = rf.reservationFormID
              );
            """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, invoiceID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return extractData(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createData(Invoice invoice) {
        String insertSQL = "INSERT INTO Invoice(invoiceID, invoiceDate, roomCharge, servicesCharge, totalDue, netDue, taxID, reservationFormID) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        String selectNextIDSQL = "SELECT nextID FROM GlobalSequence WHERE tableName = ?";
        String updateNextIDSQL = "UPDATE GlobalSequence SET nextID = ? WHERE tableName = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(insertSQL);
                PreparedStatement selectSequenceStatement = connection.prepareStatement(selectNextIDSQL);
                PreparedStatement updateSequenceStatement = connection.prepareStatement(updateNextIDSQL)
        ) {
            // Lấy nextID hiện tại cho Invoice từ GlobalSequence
            selectSequenceStatement.setString(1, "Invoice");
            ResultSet rs = selectSequenceStatement.executeQuery();

            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.INVOICE_ID_PREFIX + "-";

                // Tách phần số và tăng thêm 1
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;

                // Định dạng lại phần số, đảm bảo luôn có 3 chữ số
                String newNextID = prefix + String.format("%06d", nextIDNum);

                // Thiết lập các giá trị cho câu lệnh INSERT
                insertStatement.setString(1, currentNextID);
                insertStatement.setTimestamp(2, java.sql.Timestamp.valueOf(invoice.getInvoiceDate()));
                insertStatement.setDouble(3, invoice.getRoomCharge());
                insertStatement.setDouble(4, invoice.getServicesCharge());
                insertStatement.setDouble(5, invoice.getTotalDue());
                insertStatement.setDouble(6, invoice.getNetDue());
                insertStatement.setString(7, invoice.getTax().getTaxID());
                insertStatement.setString(8, invoice.getReservationForm().getReservationID());

                // Thực thi câu lệnh INSERT
                insertStatement.executeUpdate();

                // Cập nhật nextID trong GlobalSequence
                updateSequenceStatement.setString(1, newNextID);
                updateSequenceStatement.setString(2, "Invoice");
                updateSequenceStatement.executeUpdate();
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm dữ liệu Invoice", sqlException);
        }
    }

    public static String getNextInvoiceID() {
        String nextID = "INV-000001";
        String query = "SELECT nextID FROM GlobalSequence WHERE tableName = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, "Invoice");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                nextID = rs.getString("nextID");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return nextID;
    }

    private static Invoice extractData(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceID(rs.getString("invoiceID"));
        invoice.setInvoiceDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("invoiceDate")));
        invoice.setRoomCharge(rs.getDouble("roomCharge"));
        invoice.setServicesCharge(rs.getDouble("servicesCharge"));
        invoice.setTotalDue(rs.getDouble("totalDue"));
        invoice.setNetDue(rs.getDouble("netDue"));

        // Thiết lập thông tin Tax
        Tax tax = new Tax();
        tax.setTaxID(rs.getString("taxID"));
        tax.setTaxName(rs.getString("taxName"));
        tax.setTaxRate(rs.getDouble("taxRate"));
        tax.setDateOfCreation(rs.getDate("dateOfCreation").toLocalDate());
        tax.setActivate(rs.getBoolean("activate"));
        invoice.setTax(tax);

        // Thiết lập thông tin ReservationForm và các bảng liên quan
        ReservationForm reservationForm = new ReservationForm();
        reservationForm.setReservationID(rs.getString("reservationFormID"));
        reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("reservationDate")));
        reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkInDate")));
        reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));
        reservationForm.setRoomBookingDeposit(rs.getDouble("roomBookingDeposit"));

        // Thiết lập Employee
        Employee employee = new Employee();
        employee.setEmployeeID(rs.getString("employeeID"));
        employee.setFullName(rs.getString("employeeName"));
        employee.setPosition(ConvertHelper.positionConverter(rs.getString("position")));
        reservationForm.setEmployee(employee);

        // Thiết lập Customer
        Customer customer = new Customer();
        customer.setCustomerID(rs.getString("customerID"));
        customer.setFullName(rs.getString("customerName"));
        customer.setPhoneNumber(rs.getString("phoneNumber"));
        customer.setEmail(rs.getString("email"));
        customer.setIdCardNumber(rs.getString("idCardNumber"));
        reservationForm.setCustomer(customer);

        // Thiết lập Room và RoomCategory
        Room room = new Room();
        room.setRoomID(rs.getString("roomID"));
        room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString("roomStatus")));
        room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp("roomDateOfCreation")));

        RoomCategory roomCategory = new RoomCategory();
        roomCategory.setRoomCategoryID(rs.getString("roomCategoryID"));
        roomCategory.setRoomCategoryName(rs.getString("roomCategoryName"));
        roomCategory.setNumberOfBed(rs.getInt("numberOfBed"));
        room.setRoomCategory(roomCategory);

        reservationForm.setRoom(room);
        invoice.setReservationForm(reservationForm);

        return invoice;
    }


}
