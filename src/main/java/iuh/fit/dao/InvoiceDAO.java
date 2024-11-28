package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    public static List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql =
                """
                SELECT i.invoiceID, i.invoiceDate, i.roomCharge, i.servicesCharge,
                       i.totalDue, i.netDue, -- Thêm hai cột computed vào SELECT
                       rf.reservationFormID, rf.reservationDate, rf.checkInDate, rf.checkOutDate, rf.roomBookingDeposit,
                       e.employeeID, e.fullName AS employeeName, e.position,
                       c.customerID, c.fullName AS customerName, c.phoneNumber, c.email, c.idCardNumber,
                       r.roomID, r.roomStatus, r.dateOfCreation AS roomDateOfCreation,
                       rc.roomCategoryID, rc.roomCategoryName, rc.numberOfBed
                FROM Invoice i
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

    public static void roomCheckingOut(String reservationFormID, String employeeID,
                                       double roomCharge, double serviceCharge)
    {
        String callProcedure = "{CALL roomCheckingOut(?, ?, ?, ?, ?)}";

        try (Connection connection = DBHelper.getConnection();
             CallableStatement callableStatement = connection.prepareCall(callProcedure)) {

            // Thiết lập tham số đầu vào
            callableStatement.setString(1, reservationFormID);
            callableStatement.setString(2, employeeID);
            callableStatement.setDouble(3, roomCharge);
            callableStatement.setDouble(4, serviceCharge);

            // Đăng ký tham số đầu ra
            callableStatement.registerOutParameter(5, Types.VARCHAR);

            // Thực thi stored procedure
            callableStatement.execute();

            // Lấy thông báo từ stored procedure
            String message = callableStatement.getString(5);

            // Xử lý thông báo trả về
            switch (message) {
                case "RESERVATION_FORM_NOT_FOUND":
                    throw new IllegalArgumentException("Phiếu đặt phòng không tồn tại hoặc không được kích hoạt.");
                case "ROOM_CHECKOUT_SUCCESS":
                    HistoryCheckOutDAO.incrementAndUpdateNextID();
                    incrementAndUpdateNextID();
                    break;
                default:
                    throw new IllegalArgumentException(ErrorMessages.STORE_PROCEDURE_ERROR);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void incrementAndUpdateNextID() {
        String selectQuery = "SELECT nextID FROM GlobalSequence WHERE tableName = 'Invoice'";
        String updateQuery = "UPDATE GlobalSequence SET nextID = ? WHERE tableName = 'Invoice'";
        String currentNextID;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery)
        ) {
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                currentNextID = resultSet.getString("nextID");

                String prefix = GlobalConstants.INVOICE_ID_PREFIX + "-";
                int numericPart = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
                String updatedNextID = prefix + String.format("%06d", numericPart);

                updateStatement.setString(1, updatedNextID);
                updateStatement.executeUpdate();

            } else {
                throw new IllegalArgumentException("Không thể tìm thấy nextID cho Invoice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Invoice extractData(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceID(rs.getString("invoiceID"));
        invoice.setInvoiceDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("invoiceDate")));
        invoice.setRoomCharge(rs.getDouble("roomCharge"));
        invoice.setServicesCharge(rs.getDouble("servicesCharge"));

        // Lấy giá trị computed columns từ ResultSet
        invoice.setTotalDue(rs.getDouble("totalDue"));
        invoice.setNetDue(rs.getDouble("netDue"));

        // Thiết lập các thông tin khác liên quan đến ReservationForm, Employee, Customer, Room, RoomCategory...
        ReservationForm reservationForm = new ReservationForm();
        reservationForm.setReservationID(rs.getString("reservationFormID"));
        reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("reservationDate")));
        reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkInDate")));
        reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));
        reservationForm.setRoomBookingDeposit(rs.getDouble("roomBookingDeposit"));

        Employee employee = new Employee();
        employee.setEmployeeID(rs.getString("employeeID"));
        employee.setFullName(rs.getString("employeeName"));
        employee.setPosition(ConvertHelper.positionConverter(rs.getString("position")));
        reservationForm.setEmployee(employee);

        Customer customer = new Customer();
        customer.setCustomerID(rs.getString("customerID"));
        customer.setFullName(rs.getString("customerName"));
        customer.setPhoneNumber(rs.getString("phoneNumber"));
        customer.setEmail(rs.getString("email"));
        customer.setIdCardNumber(rs.getString("idCardNumber"));
        reservationForm.setCustomer(customer);

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
