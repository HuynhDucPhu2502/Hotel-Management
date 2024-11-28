package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryCheckOutDAO {
    public static List<HistoryCheckOut> getHistoryCheckOut() {
        ArrayList<HistoryCheckOut> data = new ArrayList<>();
        String sql =
                """
                SELECT a.historyCheckOutID, a.checkOutDate, a.reservationFormID, a.employeeID,
                       b.reservationDate, b.checkInDate, b.checkOutDate, b.roomID, b.customerID,
                       c.fullName, c.phoneNumber, c.email, c.address, c.gender, c.idCardNumber,
                       c.dob, c.position, d.roomStatus, d.dateOfCreation, d.roomCategoryID,
                       e.fullName, e.phoneNumber, e.email, e.address, e.gender, e.idCardNumber,
                       e.dob, f.roomCategoryName, f.numberOfBed
                FROM HistoryCheckOut a
                INNER JOIN ReservationForm b ON a.reservationFormID = b.reservationFormID
                INNER JOIN Employee c ON a.employeeID = c.employeeID
                INNER JOIN Room d ON b.roomID = d.roomID
                INNER JOIN Customer e ON b.customerID = e.customerID
                INNER JOIN RoomCategory f ON d.roomCategoryID = f.roomCategoryID
                """;

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)
        ) {
            while (rs.next()) {
                data.add(extractData(rs));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return data;
    }

    public static HistoryCheckOut getDataByID(String historyCheckOutID) {
        String sql =
                """
                SELECT a.historyCheckOutID, a.checkOutDate, a.reservationFormID, a.employeeID,
                       b.reservationDate, b.checkInDate, b.checkOutDate, b.roomID, b.customerID,
                       c.fullName, c.phoneNumber, c.email, c.address, c.gender, c.idCardNumber,
                       c.dob, c.position, d.roomStatus, d.dateOfCreation, d.roomCategoryID,
                       e.fullName, e.phoneNumber, e.email, e.address, e.gender, e.idCardNumber,
                       e.dob, f.roomCategoryName, f.numberOfBed
                FROM HistoryCheckOut a
                INNER JOIN ReservationForm b ON a.reservationFormID = b.reservationFormID
                INNER JOIN Employee c ON a.employeeID = c.employeeID
                INNER JOIN Room d ON b.roomID = d.roomID
                INNER JOIN Customer e ON b.customerID = e.customerID
                INNER JOIN RoomCategory f ON d.roomCategoryID = f.roomCategoryID
                WHERE a.historyCheckOutID = ?
                """;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, historyCheckOutID);
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

    public static void incrementAndUpdateNextID() {
        String selectQuery = "SELECT nextID FROM GlobalSequence WHERE tableName = 'HistoryCheckOut'";
        String updateQuery = "UPDATE GlobalSequence SET nextID = ? WHERE tableName = 'HistoryCheckOut'";
        String currentNextID;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery)
        ) {
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                currentNextID = resultSet.getString("nextID");

                String prefix = GlobalConstants.HISTORY_CHECKOUT_ID_PREFIX + "-";
                int numericPart = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
                String updatedNextID = prefix + String.format("%06d", numericPart);

                updateStatement.setString(1, updatedNextID);
                updateStatement.executeUpdate();

            } else {
                throw new IllegalArgumentException("Không thể tìm thấy nextID cho HistoryCheckOut");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static LocalDateTime getActualCheckOutDate(String reservationFormID) {
        String sql =
                """
                SELECT checkOutDate FROM HistoryCheckOut
                WHERE reservationFormID = ? \s
               \s""";
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, reservationFormID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return ConvertHelper.localDateTimeConverter(rs.getTimestamp(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static HistoryCheckOut extractData(ResultSet rs) throws SQLException {
        HistoryCheckOut historyCheckOut = new HistoryCheckOut();
        ReservationForm reservationForm = new ReservationForm();
        Employee employee = new Employee();
        Room room = new Room();
        Customer customer = new Customer();
        RoomCategory roomCategory = new RoomCategory();

        // Lấy dữ liệu cho HistoryCheckOut
        historyCheckOut.setHistoryCheckOutID(rs.getString("historyCheckOutID"));
        historyCheckOut.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));

        // Lấy dữ liệu cho ReservationForm
        reservationForm.setReservationID(rs.getString("reservationFormID"));
        reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("reservationDate")));
        reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkInDate")));
        reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));
        reservationForm.setRoomBookingDeposit(rs.getDouble("roomBookingDeposit"));

        // Lấy dữ liệu cho Employee
        employee.setEmployeeID(rs.getString("employeeID"));
        employee.setFullName(rs.getString("fullName"));
        employee.setPhoneNumber(rs.getString("phoneNumber"));
        employee.setEmail(rs.getString("email"));
        employee.setAddress(rs.getString("address"));
        employee.setGender(ConvertHelper.genderConverter(rs.getString("gender")));
        employee.setIdCardNumber(rs.getString("idCardNumber"));
        employee.setDob(ConvertHelper.localDateConverter(rs.getDate("dob")));
        employee.setPosition(ConvertHelper.positionConverter(rs.getString("position")));
        reservationForm.setEmployee(employee);

        // Lấy dữ liệu cho Room
        room.setRoomID(rs.getString("roomID"));
        room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString("roomStatus")));
        room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp("dateOfCreation")));
        reservationForm.setRoom(room);

        // Lấy dữ liệu cho Customer
        customer.setCustomerID(rs.getString("customerID"));
        customer.setFullName(rs.getString("e.fullName"));
        customer.setPhoneNumber(rs.getString("e.phoneNumber"));
        customer.setEmail(rs.getString("e.email"));
        customer.setAddress(rs.getString("e.address"));
        customer.setGender(ConvertHelper.genderConverter(rs.getString("e.gender")));
        customer.setIdCardNumber(rs.getString("e.idCardNumber"));
        customer.setDob(ConvertHelper.localDateConverter(rs.getDate("e.dob")));
        reservationForm.setCustomer(customer);

        // Lấy dữ liệu cho RoomCategory
        roomCategory.setRoomCategoryID(rs.getString("roomCategoryID"));
        roomCategory.setRoomCategoryName(rs.getString("roomCategoryName"));
        roomCategory.setNumberOfBed(rs.getInt("numberOfBed"));
        room.setRoomCategory(roomCategory);

        // Liên kết các đối tượng vào HistoryCheckOut
        historyCheckOut.setReservationForm(reservationForm);
        historyCheckOut.setEmployee(employee);

        return historyCheckOut;
    }

}
