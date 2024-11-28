package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryCheckinDAO {
    public static List<HistoryCheckIn> getHistoryCheckin() {
        ArrayList<HistoryCheckIn> data = new ArrayList<>();
        String sql =
                "SELECT a.historyCheckInID, a.checkInDate, a.reservationFormID, a.employeeID, " +
                        "       b.reservationDate, b.checkInDate, b.checkOutDate, b.roomID, b.customerID, " +
                        "       c.fullName, c.phoneNumber, c.email, c.address, c.gender, c.idCardNumber, " +
                        "       c.dob, c.position, d.roomStatus, d.dateOfCreation, d.roomCategoryID, " +
                        "       e.fullName, e.phoneNumber, e.email, e.address, e.gender, e.idCardNumber, " +
                        "       e.dob, f.roomCategoryName, f.numberOfBed " +
                        "FROM HistoryCheckin a " +
                        "INNER JOIN ReservationForm b ON a.reservationFormID = b.reservationFormID " +
                        "INNER JOIN Employee c ON a.employeeID = c.employeeID " +
                        "INNER JOIN Room d ON b.roomID = d.roomID " +
                        "INNER JOIN Customer e ON b.customerID = e.customerID " +
                        "INNER JOIN RoomCategory f ON d.roomCategoryID = f.roomCategoryID";

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
            System.exit(1);
        }
        return data;
    }

    public static HistoryCheckIn getDataByID(String historyCheckInID) {
        String sql =
                "SELECT a.historyCheckInID, a.checkInDate, a.reservationFormID, a.employeeID, " +
                        "       b.reservationDate, b.checkInDate, b.checkOutDate, b.roomID, b.customerID, " +
                        "       c.fullName, c.phoneNumber, c.email, c.address, c.gender, c.idCardNumber, " +
                        "       c.dob, c.position, d.roomStatus, d.dateOfCreation, d.roomCategoryID, " +
                        "       e.fullName, e.phoneNumber, e.email, e.address, e.gender, e.idCardNumber, " +
                        "       e.dob, f.roomCategoryName, f.numberOfBed " +
                        "FROM HistoryCheckin a " +
                        "INNER JOIN ReservationForm b ON a.reservationFormID = b.reservationFormID " +
                        "INNER JOIN Employee c ON a.employeeID = c.employeeID " +
                        "INNER JOIN Room d ON b.roomID = d.roomID " +
                        "INNER JOIN Customer e ON b.customerID = e.customerID " +
                        "INNER JOIN RoomCategory f ON d.roomCategoryID = f.roomCategoryID " +
                        "WHERE a.historyCheckInID = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, historyCheckInID);
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
        String selectQuery = "SELECT nextID FROM GlobalSequence WHERE tableName = 'HistoryCheckIn'";
        String updateQuery = "UPDATE GlobalSequence SET nextID = ? WHERE tableName = 'HistoryCheckIn'";
        String currentNextID;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery)
        ) {
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                currentNextID = resultSet.getString("nextID");

                String prefix = GlobalConstants.HISTORY_CHECKIN_ID_PREFIX + "-";
                int numericPart = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
                String updatedNextID = prefix + String.format("%06d", numericPart);

                updateStatement.setString(1, updatedNextID);
                updateStatement.executeUpdate();

            } else {
                throw new IllegalArgumentException("Không thể tìm thấy nextID cho HistoryCheckIn");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static LocalDateTime getActualCheckInDate(String reservationFormID) {
        String sql =
                """
                SELECT checkInDate FROM HistoryCheckin
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

    private static HistoryCheckIn extractData(ResultSet rs) throws SQLException {
        // Tạo các đối tượng
        HistoryCheckIn historyCheckIn = new HistoryCheckIn();
        ReservationForm reservationForm = new ReservationForm();
        Employee employee = new Employee();
        Room room = new Room();
        Customer customer = new Customer();
        RoomCategory roomCategory = new RoomCategory();

        // Gán giá trị cho ReservationForm
        reservationForm.setReservationID(rs.getString("reservationFormID"));
        reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("reservationDate")));
        reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkInDate")));
        reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));

        // Gán giá trị cho Employee
        employee.setEmployeeID(rs.getString("employeeID"));
        employee.setFullName(rs.getString("fullName"));
        employee.setPhoneNumber(rs.getString("phoneNumber"));
        employee.setEmail(rs.getString("email"));
        employee.setAddress(rs.getString("address"));
        employee.setGender(ConvertHelper.genderConverter(rs.getString("gender")));
        employee.setIdCardNumber(rs.getString("idCardNumber"));
        employee.setDob(ConvertHelper.localDateConverter(rs.getDate("dob")));
        employee.setPosition(ConvertHelper.positionConverter(rs.getString("position")));

        // Gán giá trị cho Room
        room.setRoomID(rs.getString("roomID"));
        room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString("roomStatus")));
        room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp("dateOfCreation")));

        // Gán giá trị cho Customer
        customer.setCustomerID(rs.getString("customerID"));
        customer.setFullName(rs.getString(21));
        customer.setPhoneNumber(rs.getString(22));
        customer.setEmail(rs.getString(23));
        customer.setAddress(rs.getString(24));
        customer.setGender(ConvertHelper.genderConverter(rs.getString(25)));
        customer.setIdCardNumber(rs.getString(26));
        customer.setDob(ConvertHelper.localDateConverter(rs.getDate(27)));

        // Gán giá trị cho RoomCategory
        roomCategory.setRoomCategoryID(rs.getString("roomCategoryID"));
        roomCategory.setRoomCategoryName(rs.getString("roomCategoryName"));
        roomCategory.setNumberOfBed(rs.getInt("numberOfBed"));

        // Set mối quan hệ giữa các đối tượng
        room.setRoomCategory(roomCategory);
        reservationForm.setEmployee(employee);
        reservationForm.setRoom(room);
        reservationForm.setCustomer(customer);

        // Gán dữ liệu cho HistoryCheckIn
        historyCheckIn.setHistoryCheckInID(rs.getString("historyCheckInID"));
        historyCheckIn.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkInDate")));
        historyCheckIn.setReservationForm(reservationForm);
        historyCheckIn.setEmployee(employee);

        return historyCheckIn;
    }
}
