package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HistoryCheckinDAO {
    public static List<HistoryCheckIn> getHistoryCheckin() {
        ArrayList<HistoryCheckIn> data = new ArrayList<HistoryCheckIn>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT a.historyCheckInID, a.checkInDate, a.reservationFormID, " +
                    "b.reservationDate, b.approxCheckInDate, b.approxCheckOutDate, b.employeeID, b.roomID, b.customerID, " +
                    "c.fullName, c.phoneNumber, c.email, c.address, c.gender, c.idCardNumber, c.dob, c.position, " +
                    "d.roomStatus, d.dateOfCreation, d.roomCategoryID, " +
                    "e.fullName, e.phoneNumber, e.email, e.address, e.gender, e.idCardNumber, e.dob, " +
                    "f.roomCategoryName, f.numberOfBed " +
                    "FROM HistoryCheckin a inner join ReservationForm b on a.reservationFormID = b.reservationFormID " +
                    "inner join Employee c on b.employeeID = c.employeeID " +
                    "inner join Room d on b.roomID = d.roomID " +
                    "inner join Customer e on b.customerID = e.customerID " +
                    "inner join RoomCategory f on d.roomCategoryID = f.roomCategoryID";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                HistoryCheckIn historyCheckIn = new HistoryCheckIn();
                ReservationForm reservationForm = new ReservationForm();
                Employee employee = new Employee();
                Room room = new Room();
                Customer customer = new Customer();
                RoomCategory roomCategory = new RoomCategory();

                reservationForm.setReservationID(rs.getString(3));
                reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                reservationForm.setApproxCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                reservationForm.setApproxCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(6)));

                employee.setEmployeeID(rs.getString(7));
                employee.setFullName(rs.getString(10));
                employee.setPhoneNumber(rs.getString(11));
                employee.setEmail(rs.getString(12));
                employee.setAddress(rs.getString(13));
                employee.setGender(ConvertHelper.genderConverter(rs.getString(14)));
                employee.setIdCardNumber(rs.getString(15));
                employee.setDob(ConvertHelper.LocalDateConverter(rs.getDate(16)));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString(17)));

                room.setRoomID(rs.getString(8));
                room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString(18)));
                room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp(19)));

                customer.setCustomerID(rs.getString(9));
                customer.setFullName(rs.getString(21));
                customer.setPhoneNumber(rs.getString(22));
                customer.setEmail(rs.getString(23));
                customer.setAddress(rs.getString(24));
                customer.setGender(ConvertHelper.genderConverter(rs.getString(25)));
                customer.setIdCardNumber(rs.getString(26));
                customer.setDob(ConvertHelper.LocalDateConverter(rs.getDate(27)));

                roomCategory.setRoomCategoryid(rs.getString(20));
                roomCategory.setRoomCategoryName(rs.getString(28));
                roomCategory.setNumberOfBed(rs.getInt(29));

                room.setRoomCategory(roomCategory);

                reservationForm.setEmployee(employee);
                reservationForm.setRoom(room);
                reservationForm.setCustomer(customer);

                historyCheckIn.setHistoryCheckInID(rs.getString(1));
                historyCheckIn.setReservationForm(reservationForm);
                historyCheckIn.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(2)));

                data.add(historyCheckIn);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
