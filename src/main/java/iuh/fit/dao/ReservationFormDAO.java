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

public class ReservationFormDAO {
    public static List<ReservationForm> getReservationForm() {
        ArrayList<ReservationForm> data = new ArrayList<ReservationForm>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT  a.reservationFormID, a.reservationDate, a.checkInDate, a.checkOutDate, a.employeeID, a.roomID, a.customerID, " +
                    "b.fullName, b.phoneNumber, b.email, b.address, b.gender, b.idCardNumber, b.dob, b.position, " +
                    "c.roomStatus, c.dateOfCreation, c.roomCategoryID, " +
                    "d.fullName, d.phoneNumber, d.email, d.address, d.gender, d.idCardNumber, d.dob, " +
                    "e.roomCategoryName, e.numberOfBed " +
                    "FROM ReservationForm a inner join Employee b on a.employeeID = b.employeeID " +
                    "inner join Room c on a.roomID = c.roomID " +
                    "inner join Customer d on a.customerID = d.customerID " +
                    "inner join RoomCategory e on c.roomCategoryID = e.roomCategoryID";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                ReservationForm reservationForm = new ReservationForm();
                Employee employee = new Employee();
                Room room = new Room();
                Customer customer = new Customer();
                RoomCategory roomCategory = new RoomCategory();

                reservationForm.setReservationID(rs.getString(1));
                reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(2)));
                reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(3)));
                reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));

                employee.setEmployeeID(rs.getString(5));
                employee.setFullName(rs.getString(8));
                employee.setPhoneNumber(rs.getString(9));
                employee.setEmail(rs.getString(10));
                employee.setAddress(rs.getString(11));
                employee.setGender(ConvertHelper.genderConverter(rs.getString(12)));
                employee.setIdCardNumber(rs.getString(13));
                employee.setDob(ConvertHelper.LocalDateConverter(rs.getDate(14)));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString(15)));

                room.setRoomID(rs.getString(6));
                room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString(16)));
                room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp(17)));

                customer.setCustomerID(rs.getString(7));
                customer.setCusFullName(rs.getString(19));
                customer.setPhoneNumber(rs.getString(20));
                customer.setEmail(rs.getString(21));
                customer.setAddress(rs.getString(22));
                customer.setGender(ConvertHelper.genderConverter(rs.getString(23)));
                customer.setIdCardNumber(rs.getString(24));
                customer.setDob(ConvertHelper.LocalDateConverter(rs.getDate(25)));

                roomCategory.setRoomCategoryID(rs.getString(18));
                roomCategory.setRoomCategoryName(rs.getString(26));
                roomCategory.setNumberOfBed(rs.getInt(27));

                room.setRoomCategory(roomCategory);

                reservationForm.setEmployee(employee);
                reservationForm.setRoom(room);
                reservationForm.setCustomer(customer);

                data.add(reservationForm);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static ReservationForm getDataByID(String reservationFormID) {

        String SQLQueryStatement = "SELECT  a.reservationFormID, a.reservationDate, a.checkInDate, a.checkOutDate, a.employeeID, a.roomID, a.customerID, " +
                "b.fullName, b.phoneNumber, b.email, b.address, b.gender, b.idCardNumber, b.dob, b.position, " +
                "c.roomStatus, c.dateOfCreation, c.roomCategoryID, " +
                "d.fullName, d.phoneNumber, d.email, d.address, d.gender, d.idCardNumber, d.dob, " +
                "e.roomCategoryName, e.numberOfBed " +
                "FROM ReservationForm a inner join Employee b on a.employeeID = b.employeeID " +
                "inner join Room c on a.roomID = c.roomID " +
                "inner join Customer d on a.customerID = d.customerID " +
                "inner join RoomCategory e on c.roomCategoryID = e.roomCategoryID " +
                "WHERE reservationFormID = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, reservationFormID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    ReservationForm reservationForm = new ReservationForm();
                    Employee employee = new Employee();
                    Room room = new Room();
                    Customer customer = new Customer();
                    RoomCategory roomCategory = new RoomCategory();

                    reservationForm.setReservationID(rs.getString(1));
                    reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(2)));
                    reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(3)));
                    reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));

                    employee.setEmployeeID(rs.getString(5));
                    employee.setFullName(rs.getString(8));
                    employee.setPhoneNumber(rs.getString(9));
                    employee.setEmail(rs.getString(10));
                    employee.setAddress(rs.getString(11));
                    employee.setGender(ConvertHelper.genderConverter(rs.getString(12)));
                    employee.setIdCardNumber(rs.getString(13));
                    employee.setDob(ConvertHelper.LocalDateConverter(rs.getDate(14)));
                    employee.setPosition(ConvertHelper.positionConverter(rs.getString(15)));

                    room.setRoomID(rs.getString(6));
                    room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString(16)));
                    room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp(17)));

                    customer.setCustomerID(rs.getString(7));
                    customer.setCusFullName(rs.getString(19));
                    customer.setPhoneNumber(rs.getString(20));
                    customer.setEmail(rs.getString(21));
                    customer.setAddress(rs.getString(22));
                    customer.setGender(ConvertHelper.genderConverter(rs.getString(23)));
                    customer.setIdCardNumber(rs.getString(24));
                    customer.setDob(ConvertHelper.LocalDateConverter(rs.getDate(25)));

                    roomCategory.setRoomCategoryID(rs.getString(18));
                    roomCategory.setRoomCategoryName(rs.getString(26));
                    roomCategory.setNumberOfBed(rs.getInt(27));

                    room.setRoomCategory(roomCategory);

                    reservationForm.setEmployee(employee);
                    reservationForm.setRoom(room);
                    reservationForm.setCustomer(customer);

                    return reservationForm;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(ReservationForm reservationForm) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO ReservationForm(reservationFormID, reservationDate, approxCheckInDate, approxCheckOutDate, employeeID, roomID, customerID) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?)"
                )
        ){
            preparedStatement.setString(1, reservationForm.getReservationID());
            preparedStatement.setTimestamp(2, ConvertHelper.dateTimeConvertertoSQL(reservationForm.getReservationDate()));
            preparedStatement.setTimestamp(3, ConvertHelper.dateTimeConvertertoSQL(reservationForm.getCheckInDate()));
            preparedStatement.setTimestamp(4, ConvertHelper.dateTimeConvertertoSQL(reservationForm.getCheckOutDate()));
            preparedStatement.setString(5, reservationForm.getEmployee().getEmployeeID());
            preparedStatement.setString(6, reservationForm.getRoom().getRoomID());
            preparedStatement.setString(7, reservationForm.getCustomer().getCustomerID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String reservationFormID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM ReservationForm "
                                + "WHERE reservationFormID = ?"
                )
        ){
            preparedStatement.setString(1, reservationFormID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void updateData(ReservationForm reservationForm) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE ReservationForm " +
                                "SET reservationDate = ?, approxCheckInDate = ?, approxCheckOutDate = ?, " +
                                "employeeID = ?, roomID = ?, customerID = ? " +
                                "WHERE reservationFormID = ? "
                );
        ){
            preparedStatement.setTimestamp(1, ConvertHelper.dateTimeConvertertoSQL(reservationForm.getReservationDate()));
            preparedStatement.setTimestamp(2, ConvertHelper.dateTimeConvertertoSQL(reservationForm.getCheckInDate()));
            preparedStatement.setTimestamp(3, ConvertHelper.dateTimeConvertertoSQL(reservationForm.getCheckOutDate()));
            preparedStatement.setString(4, reservationForm.getEmployee().getEmployeeID());
            preparedStatement.setString(5, reservationForm.getRoom().getRoomID());
            preparedStatement.setString(6, reservationForm.getCustomer().getCustomerID());
            preparedStatement.setString(7, reservationForm.getReservationID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }
}
