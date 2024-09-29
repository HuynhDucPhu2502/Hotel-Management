package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
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
            String sql = "SELECT  a.reservationFormID, a.reservationDate, a.approxCheckInDate, a.approxCheckOutDate, a.employeeID, a.roomID, a.customerID, " +
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
                reservationForm.setApproxCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(3)));
                reservationForm.setApproxCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));

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
                customer.setFullName(rs.getString(19));
                customer.setPhoneNumber(rs.getString(20));
                customer.setEmail(rs.getString(21));
                customer.setAddress(rs.getString(22));
                customer.setGender(ConvertHelper.genderConverter(rs.getString(23)));
                customer.setIdCardNumber(rs.getString(24));
                customer.setDob(ConvertHelper.LocalDateConverter(rs.getDate(25)));

                roomCategory.setRoomCategoryid(rs.getString(18));
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
}
