package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistoryCheckOutDAO {
    public static List<HistoryCheckOut> getHistoryCheckOut() {
        ArrayList<HistoryCheckOut> data = new ArrayList<>();
        String sql =
                "SELECT a.historyCheckOutID, a.checkOutDate, a.reservationFormID, a.employeeID, " +
                        "b.reservationDate, b.checkInDate, b.checkOutDate, b.roomID, b.customerID, " +
                        "c.fullName, c.phoneNumber, c.email, c.address, c.gender, c.idCardNumber, " +
                        "c.dob, c.position, d.roomStatus, d.dateOfCreation, d.roomCategoryID, " +
                        "e.fullName, e.phoneNumber, e.email, e.address, e.gender, e.idCardNumber, " +
                        "e.dob, f.roomCategoryName, f.numberOfBed " +
                        "FROM HistoryCheckOut a " +
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
                HistoryCheckOut historyCheckOut = new HistoryCheckOut();
                ReservationForm reservationForm = new ReservationForm();
                Employee employee = new Employee();
                Room room = new Room();
                Customer customer = new Customer();
                RoomCategory roomCategory = new RoomCategory();

                reservationForm.setReservationID(rs.getString("reservationFormID"));
                reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("reservationDate")));
                reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkInDate")));
                reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));

                employee.setEmployeeID(rs.getString("employeeID"));
                employee.setFullName(rs.getString("fullName"));
                employee.setPhoneNumber(rs.getString("phoneNumber"));
                employee.setEmail(rs.getString("email"));
                employee.setAddress(rs.getString("address"));
                employee.setGender(ConvertHelper.genderConverter(rs.getString("gender")));
                employee.setIdCardNumber(rs.getString("idCardNumber"));
                employee.setDob(ConvertHelper.localDateConverter(rs.getDate("dob")));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString("position")));

                room.setRoomID(rs.getString("roomID"));
                room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString("roomStatus")));
                room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp("dateOfCreation")));

                customer.setCustomerID(rs.getString("customerID"));
                customer.setFullName(rs.getString("e.fullName"));
                customer.setPhoneNumber(rs.getString("e.phoneNumber"));
                customer.setEmail(rs.getString("e.email"));
                customer.setAddress(rs.getString("e.address"));
                customer.setGender(ConvertHelper.genderConverter(rs.getString("e.gender")));
                customer.setIdCardNumber(rs.getString("e.idCardNumber"));
                customer.setDob(ConvertHelper.localDateConverter(rs.getDate("e.dob")));

                roomCategory.setRoomCategoryID(rs.getString("roomCategoryID"));
                roomCategory.setRoomCategoryName(rs.getString("roomCategoryName"));
                roomCategory.setNumberOfBed(rs.getInt("numberOfBed"));

                room.setRoomCategory(roomCategory);
                reservationForm.setEmployee(employee);
                reservationForm.setRoom(room);
                reservationForm.setCustomer(customer);

                historyCheckOut.setHistoryCheckOutID(rs.getString("historyCheckOutID"));
                historyCheckOut.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));
                historyCheckOut.setReservationForm(reservationForm);
                historyCheckOut.setEmployee(employee);

                data.add(historyCheckOut);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        return data;
    }

    public static HistoryCheckOut getDataByID(String historyCheckOutID) {
        String sql =
                "SELECT a.historyCheckOutID, a.checkOutDate, a.reservationFormID, a.employeeID, " +
                        "b.reservationDate, b.checkInDate, b.checkOutDate, b.roomID, b.customerID, " +
                        "c.fullName, c.phoneNumber, c.email, c.address, c.gender, c.idCardNumber, " +
                        "c.dob, c.position, d.roomStatus, d.dateOfCreation, d.roomCategoryID, " +
                        "e.fullName, e.phoneNumber, e.email, e.address, e.gender, e.idCardNumber, " +
                        "e.dob, f.roomCategoryName, f.numberOfBed " +
                        "FROM HistoryCheckOut a " +
                        "INNER JOIN ReservationForm b ON a.reservationFormID = b.reservationFormID " +
                        "INNER JOIN Employee c ON a.employeeID = c.employeeID " +
                        "INNER JOIN Room d ON b.roomID = d.roomID " +
                        "INNER JOIN Customer e ON b.customerID = e.customerID " +
                        "INNER JOIN RoomCategory f ON d.roomCategoryID = f.roomCategoryID " +
                        "WHERE a.historyCheckOutID = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, historyCheckOutID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    HistoryCheckOut historyCheckOut = new HistoryCheckOut();
                    ReservationForm reservationForm = new ReservationForm();
                    Employee employee = new Employee();
                    Room room = new Room();
                    Customer customer = new Customer();
                    RoomCategory roomCategory = new RoomCategory();

                    reservationForm.setReservationID(rs.getString("reservationFormID"));
                    reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("reservationDate")));
                    reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkInDate")));
                    reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));

                    employee.setEmployeeID(rs.getString("employeeID"));
                    employee.setFullName(rs.getString("fullName"));
                    employee.setPhoneNumber(rs.getString("phoneNumber"));
                    employee.setEmail(rs.getString("email"));
                    employee.setAddress(rs.getString("address"));
                    employee.setGender(ConvertHelper.genderConverter(rs.getString("gender")));
                    employee.setIdCardNumber(rs.getString("idCardNumber"));
                    employee.setDob(ConvertHelper.localDateConverter(rs.getDate("dob")));
                    employee.setPosition(ConvertHelper.positionConverter(rs.getString("position")));

                    room.setRoomID(rs.getString("roomID"));
                    room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString("roomStatus")));
                    room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp("dateOfCreation")));

                    customer.setCustomerID(rs.getString("customerID"));
                    customer.setFullName(rs.getString("e.fullName"));
                    customer.setPhoneNumber(rs.getString("e.phoneNumber"));
                    customer.setEmail(rs.getString("e.email"));
                    customer.setAddress(rs.getString("e.address"));
                    customer.setGender(ConvertHelper.genderConverter(rs.getString("e.gender")));
                    customer.setIdCardNumber(rs.getString("e.idCardNumber"));
                    customer.setDob(ConvertHelper.localDateConverter(rs.getDate("e.dob")));

                    roomCategory.setRoomCategoryID(rs.getString("roomCategoryID"));
                    roomCategory.setRoomCategoryName(rs.getString("roomCategoryName"));
                    roomCategory.setNumberOfBed(rs.getInt("numberOfBed"));

                    room.setRoomCategory(roomCategory);
                    reservationForm.setEmployee(employee);
                    reservationForm.setRoom(room);
                    reservationForm.setCustomer(customer);

                    historyCheckOut.setHistoryCheckOutID(rs.getString("historyCheckOutID"));
                    historyCheckOut.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));
                    historyCheckOut.setReservationForm(reservationForm);
                    historyCheckOut.setEmployee(employee);

                    return historyCheckOut;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNextID() {
        String nextID = "HCO-000001"; // Default ID nếu không có trong GlobalSequence
        String query = "SELECT nextID FROM GlobalSequence WHERE tableName = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, "HistoryCheckOut");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                nextID = rs.getString(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return nextID;
    }

    public static void createData(HistoryCheckOut historyCheckOut) {
        String insertSQL =
                "INSERT INTO HistoryCheckOut(historyCheckOutID, checkOutDate, reservationFormID, employeeID) " +
                        "VALUES (?, ?, ?, ?)";

        String selectNextIDSQL =
                "SELECT nextID FROM GlobalSequence WHERE tableName = ?";

        String updateNextIDSQL =
                "UPDATE GlobalSequence SET nextID = ? WHERE tableName = ?";

        try (
                Connection connection = DBHelper.getConnection();

                PreparedStatement insertStatement = connection.prepareStatement(insertSQL);
                PreparedStatement selectSequenceStatement = connection.prepareStatement(selectNextIDSQL);
                PreparedStatement updateSequenceStatement = connection.prepareStatement(updateNextIDSQL)
        ) {
            // Lấy nextID hiện tại cho HistoryCheckOut từ GlobalSequence
            selectSequenceStatement.setString(1, "HistoryCheckOut");
            ResultSet rs = selectSequenceStatement.executeQuery();

            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.HISTORY_CHECKOUT_ID_PREFIX + "-";

                // Tách phần số và tăng thêm 1
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;

                // Định dạng lại phần số với 6 chữ số
                String newNextID = prefix + String.format("%06d", nextIDNum);

                // Thiết lập giá trị cho câu lệnh INSERT
                insertStatement.setString(1, currentNextID);
                insertStatement.setTimestamp(2,
                        ConvertHelper.dateTimeToSQLConverter(historyCheckOut.getCheckOutDate()));
                insertStatement.setString(3,
                        historyCheckOut.getReservationForm().getReservationID());
                insertStatement.setString(4,
                        historyCheckOut.getEmployee().getEmployeeID());

                // Thực thi câu lệnh INSERT
                insertStatement.executeUpdate();

                // Cập nhật nextID trong GlobalSequence
                updateSequenceStatement.setString(1, newNextID);
                updateSequenceStatement.setString(2, "HistoryCheckOut");
                updateSequenceStatement.executeUpdate();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
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

}
