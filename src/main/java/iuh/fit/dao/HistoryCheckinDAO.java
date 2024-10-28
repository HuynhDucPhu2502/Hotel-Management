package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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

                data.add(historyCheckIn);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createData(HistoryCheckIn historyCheckIn) {
        String sql = "INSERT INTO HistoryCheckin(historyCheckInID, checkInDate, reservationFormID, employeeID) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, historyCheckIn.getHistoryCheckInID());
            preparedStatement.setTimestamp(2, ConvertHelper.dateTimeToSQLConverter(historyCheckIn.getCheckInDate()));
            preparedStatement.setString(3, historyCheckIn.getReservationForm().getReservationID());
            preparedStatement.setString(4, historyCheckIn.getEmployee().getEmployeeID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String historyCheckInID) {
        String sql = "DELETE FROM HistoryCheckin WHERE historyCheckInID = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, historyCheckInID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void updateData(HistoryCheckIn historyCheckIn) {
        String sql = "UPDATE HistoryCheckin " +
                "SET checkInDate = ?, reservationFormID = ?, employeeID = ? " +
                "WHERE historyCheckInID = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setTimestamp(1, ConvertHelper.dateTimeToSQLConverter(historyCheckIn.getCheckInDate()));
            preparedStatement.setString(2, historyCheckIn.getReservationForm().getReservationID());
            preparedStatement.setString(3, historyCheckIn.getEmployee().getEmployeeID());
            preparedStatement.setString(4, historyCheckIn.getHistoryCheckInID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }
}
