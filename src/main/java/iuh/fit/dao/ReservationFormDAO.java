package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.ErrorMessages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationFormDAO {
    public static List<ReservationForm> getReservationForm() {
        List<ReservationForm> data = new ArrayList<>();
        String sql = """
                SELECT a.reservationFormID, a.reservationDate, a.checkInDate, a.checkOutDate, a.roomBookingDeposit,
                       a.employeeID, a.roomID, a.customerID,
                       b.fullName AS employeeFullName, b.phoneNumber AS employeePhoneNumber, b.email AS employeeEmail,
                       b.address AS employeeAddress, b.gender AS employeeGender, b.idCardNumber AS employeeIDCardNumber,
                       b.dob AS employeeDob, b.position AS employeePosition,
                       c.roomStatus, c.dateOfCreation, c.roomCategoryID,
                       d.fullName AS customerFullName, d.phoneNumber AS customerPhoneNumber, d.email AS customerEmail,
                       d.address AS customerAddress, d.gender AS customerGender, d.idCardNumber AS customerIDCardNumber,
                       d.dob AS customerDob,
                       e.roomCategoryName, e.numberOfBed
                FROM ReservationForm a
                INNER JOIN Employee b ON a.employeeID = b.employeeID
                INNER JOIN Room c ON a.roomID = c.roomID
                INNER JOIN Customer d ON a.customerID = d.customerID
                INNER JOIN RoomCategory e ON c.roomCategoryID = e.roomCategoryID
                """;

        try (Connection connection = DBHelper.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                data.add(extractData(rs));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        return data;
    }

    public static ReservationForm getDataByID(String reservationFormID) {
        String sql = """
                SELECT a.reservationFormID, a.reservationDate, a.checkInDate, a.checkOutDate, a.roomBookingDeposit,
                       a.employeeID, a.roomID, a.customerID,
                       b.fullName AS employeeFullName, b.phoneNumber AS employeePhoneNumber, b.email AS employeeEmail,
                       b.address AS employeeAddress, b.gender AS employeeGender, b.idCardNumber AS employeeIDCardNumber,
                       b.dob AS employeeDob, b.position AS employeePosition,
                       c.roomStatus, c.dateOfCreation, c.roomCategoryID,
                       d.fullName AS customerFullName, d.phoneNumber AS customerPhoneNumber, d.email AS customerEmail,
                       d.address AS customerAddress, d.gender AS customerGender, d.idCardNumber AS customerIDCardNumber,
                       d.dob AS customerDob,
                       e.roomCategoryName, e.numberOfBed
                FROM ReservationForm a
                INNER JOIN Employee b ON a.employeeID = b.employeeID
                INNER JOIN Room c ON a.roomID = c.roomID
                INNER JOIN Customer d ON a.customerID = d.customerID
                INNER JOIN RoomCategory e ON c.roomCategoryID = e.roomCategoryID
                WHERE a.reservationFormID = ?
                """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, reservationFormID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return extractData(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createData(ReservationForm reservationForm) {
        String callProcedure = "{CALL CreatingReservationForm(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBHelper.getConnection();
             CallableStatement callableStatement = connection.prepareCall(callProcedure)) {

            // Thiết lập tham số đầu vào cho Stored Procedure
            callableStatement.setTimestamp(1, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getCheckInDate()));
            callableStatement.setTimestamp(2, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getCheckOutDate()));
            callableStatement.setString(3, reservationForm.getEmployee().getEmployeeID());
            callableStatement.setString(4, reservationForm.getRoom().getRoomID());
            callableStatement.setString(5, reservationForm.getCustomer().getCustomerID());
            callableStatement.setDouble(6, reservationForm.getRoomBookingDeposit());

            // Đăng ký tham số đầu ra cho message
            callableStatement.registerOutParameter(7, Types.VARCHAR);

            // Thực thi Stored Procedure
            callableStatement.execute();

            // Lấy thông báo từ Stored Procedure
            String message = callableStatement.getString(7);

            // Kiểm tra kết quả trả về từ Stored Procedure
            switch (message) {
                case "CREATING_RESERVATION_FORM_CHECK_DATE_OVERLAP":
                    throw new IllegalArgumentException(ErrorMessages.CREATING_RESERVATION_FORM_CHECK_DATE_OVERLAP);
                case "CREATING_RESERVATION_FORM_ID_CARD_NUMBER_OVERLAP":
                    throw new IllegalArgumentException(ErrorMessages.CREATING_RESERVATION_FORM_ID_CARD_NUMBER_OVERLAP);
                case "CREATING_RESERVATION_FORM_SUCCESS":
                    incrementAndUpdateNextID();
                    break;
                default:
                    throw new IllegalArgumentException("Lỗi không xác định từ Stored Procedure.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String reservationFormID) {
        String sql = "DELETE FROM ReservationForm WHERE reservationFormID = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, reservationFormID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void updateData(ReservationForm reservationForm) {
        String sql = """
                UPDATE ReservationForm\s
                SET reservationDate = ?, checkInDate = ?, checkOutDate = ?, employeeID = ?,\s
                    roomID = ?, customerID = ?, roomBookingDeposit = ?\s
                WHERE reservationFormID = ?\s
               \s""";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setTimestamp(1, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getReservationDate()));
            preparedStatement.setTimestamp(2, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getCheckInDate()));
            preparedStatement.setTimestamp(3, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getCheckOutDate()));
            preparedStatement.setString(4, reservationForm.getEmployee().getEmployeeID());
            preparedStatement.setString(5, reservationForm.getRoom().getRoomID());
            preparedStatement.setString(6, reservationForm.getCustomer().getCustomerID());
            preparedStatement.setDouble(7, reservationForm.getRoomBookingDeposit());
            preparedStatement.setString(8, reservationForm.getReservationID());
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static List<ReservationForm> getUpcomingReservations(String roomID) {
        List<ReservationForm> reservations = new ArrayList<>();

        String sql = """
        SELECT a.reservationFormID, a.reservationDate, a.checkInDate, a.checkOutDate, a.roomBookingDeposit,
               a.employeeID, a.roomID, a.customerID,
               b.fullName AS employeeFullName, b.phoneNumber AS employeePhoneNumber, b.email AS employeeEmail,
               b.address AS employeeAddress, b.gender AS employeeGender, b.idCardNumber AS employeeIDCardNumber,
               b.dob AS employeeDob, b.position AS employeePosition,
               c.roomStatus, c.dateOfCreation, c.roomCategoryID,
               d.fullName AS customerFullName, d.phoneNumber AS customerPhoneNumber, d.email AS customerEmail,
               d.address AS customerAddress, d.gender AS customerGender, d.idCardNumber AS customerIDCardNumber,
               d.dob AS customerDob,
               e.roomCategoryName, e.numberOfBed
        FROM ReservationForm a
        INNER JOIN Employee b ON a.employeeID = b.employeeID
        INNER JOIN Room c ON a.roomID = c.roomID
        INNER JOIN Customer d ON a.customerID = d.customerID
        INNER JOIN RoomCategory e ON c.roomCategoryID = e.roomCategoryID
        WHERE a.roomID = ?
          AND a.checkInDate >= DATEADD(HOUR, -2, GETDATE())
          AND NOT EXISTS (
              SELECT 1 FROM HistoryCheckin hci
              WHERE hci.reservationFormID = a.reservationFormID
          )
        ORDER BY a.checkInDate
        """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roomID);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                reservations.add(extractData(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public static void updateRoomInReservationForm(String reservationFormID, String newRoomID) {
        String sql = """
        UPDATE ReservationForm
        SET roomID = ?
        WHERE reservationFormID = ?
        """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newRoomID);
            preparedStatement.setString(2, reservationFormID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new IllegalArgumentException(
                        "Không tìm thấy phiếu đặt phòng với ID: " + reservationFormID
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi cập nhật phòng cho phiếu đặt phòng", e);
        }
    }

    public static List<ReservationForm> getReservationFormByCustomerID(String customerID) {
        List<ReservationForm> data = new ArrayList<>();

        String sql = """
        SELECT a.reservationFormID, a.reservationDate, a.checkInDate, a.checkOutDate, a.roomBookingDeposit,
               a.employeeID, a.roomID, a.customerID,
               b.fullName AS employeeFullName, b.phoneNumber AS employeePhoneNumber, b.email AS employeeEmail,
               b.address AS employeeAddress, b.gender AS employeeGender, b.idCardNumber AS employeeIDCardNumber,
               b.dob AS employeeDob, b.position AS employeePosition,
               c.roomStatus, c.dateOfCreation, c.roomCategoryID,
               d.fullName AS customerFullName, d.phoneNumber AS customerPhoneNumber, d.email AS customerEmail,
               d.address AS customerAddress, d.gender AS customerGender, d.idCardNumber AS customerIDCardNumber,
               d.dob AS customerDob,
               e.roomCategoryName, e.numberOfBed
        FROM ReservationForm a
        INNER JOIN Employee b ON a.employeeID = b.employeeID
        INNER JOIN Room c ON a.roomID = c.roomID
        INNER JOIN Customer d ON a.customerID = d.customerID
        INNER JOIN RoomCategory e ON c.roomCategoryID = e.roomCategoryID
        WHERE a.customerID = ?
        """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, customerID);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                data.add(extractData(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return data;
    }

    public static void incrementAndUpdateNextID() {
        String selectQuery = "SELECT nextID FROM GlobalSequence WHERE tableName = 'ReservationForm'";
        String updateQuery = "UPDATE GlobalSequence SET nextID = ? WHERE tableName = 'ReservationForm'";
        String currentNextID;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                currentNextID = resultSet.getString("nextID");

                String prefix = "RF-";
                int numericPart = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
                String updatedNextID = prefix + String.format("%06d", numericPart);

                updateStatement.setString(1, updatedNextID);
                updateStatement.executeUpdate();

            } else {
                throw new IllegalArgumentException("Không tìm thấy bản ghi với tableName: ReservationForm");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy và cập nhật nextID trong GlobalSequence", e);
        }
    }

    private static ReservationForm extractData(ResultSet rs) throws SQLException {
        ReservationForm reservationForm = new ReservationForm();
        Employee employee = new Employee();
        Room room = new Room();
        Customer customer = new Customer();
        RoomCategory roomCategory = new RoomCategory();

        reservationForm.setReservationID(rs.getString("reservationFormID"));
        reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("reservationDate")));
        reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkInDate")));
        reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));
        reservationForm.setRoomBookingDeposit(rs.getDouble("roomBookingDeposit"));

        employee.setEmployeeID(rs.getString("employeeID"));
        employee.setFullName(rs.getString("employeeFullName"));
        employee.setPhoneNumber(rs.getString("employeePhoneNumber"));
        employee.setEmail(rs.getString("employeeEmail"));
        employee.setAddress(rs.getString("employeeAddress"));
        employee.setGender(ConvertHelper.genderConverter(rs.getString("employeeGender")));
        employee.setIdCardNumber(rs.getString("employeeIDCardNumber"));
        employee.setDob(ConvertHelper.localDateConverter(rs.getDate("employeeDob")));
        employee.setPosition(ConvertHelper.positionConverter(rs.getString("employeePosition")));

        room.setRoomID(rs.getString("roomID"));
        room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString("roomStatus")));
        room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp("dateOfCreation")));

        roomCategory.setRoomCategoryID(rs.getString("roomCategoryID"));
        roomCategory.setRoomCategoryName(rs.getString("roomCategoryName"));
        roomCategory.setNumberOfBed(rs.getInt("numberOfBed"));
        room.setRoomCategory(roomCategory);

        customer.setCustomerID(rs.getString("customerID"));
        customer.setFullName(rs.getString("customerFullName"));
        customer.setPhoneNumber(rs.getString("customerPhoneNumber"));
        customer.setEmail(rs.getString("customerEmail"));
        customer.setAddress(rs.getString("customerAddress"));
        customer.setGender(ConvertHelper.genderConverter(rs.getString("customerGender")));
        customer.setIdCardNumber(rs.getString("customerIDCardNumber"));
        customer.setDob(ConvertHelper.localDateConverter(rs.getDate("customerDob")));

        reservationForm.setEmployee(employee);
        reservationForm.setRoom(room);
        reservationForm.setCustomer(customer);

        return reservationForm;
    }


}
