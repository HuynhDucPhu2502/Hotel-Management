package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomWithReservationDAO {

    public static List<RoomWithReservation> getRoomWithReservation() {
        List<RoomWithReservation> data = new ArrayList<>();

        String sql = """
            SELECT r.roomID, r.roomStatus, r.dateOfCreation, 
                   rc.roomCategoryID, rc.roomCategoryName, rc.numberOfBed, 
                   rf.reservationFormID, rf.reservationDate, rf.checkInDate, 
                   rf.checkOutDate, rf.roomBookingDeposit, rf.employeeID, rf.customerID, 
                   e.fullName AS employeeName, c.fullName AS customerName 
            FROM Room r
            LEFT JOIN RoomCategory rc ON r.roomCategoryID = rc.roomCategoryID
            LEFT JOIN ReservationForm rf ON r.roomID = rf.roomID 
            AND GETDATE() BETWEEN rf.checkInDate AND DATEADD(hour, 2, rf.checkOutDate)
            LEFT JOIN Employee e ON rf.employeeID = e.employeeID
            LEFT JOIN Customer c ON rf.customerID = c.customerID;
            """;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                // Room
                Room room = new Room();
                RoomCategory roomCategory = new RoomCategory();

                room.setRoomID(rs.getString("roomID"));
                room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString("roomStatus")));
                room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp("dateOfCreation")));

                roomCategory.setRoomCategoryID(rs.getString("roomCategoryID"));
                roomCategory.setRoomCategoryName(rs.getString("roomCategoryName"));
                roomCategory.setNumberOfBed(rs.getInt("numberOfBed"));

                room.setRoomCategory(roomCategory);

                // ReservationForm (if exists)
                ReservationForm reservationForm = null;
                if (rs.getString("reservationFormID") != null) {
                    reservationForm = new ReservationForm();
                    reservationForm.setReservationID(rs.getString("reservationFormID"));
                    reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("reservationDate")));
                    reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkInDate")));
                    reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));
                    reservationForm.setRoomBookingDeposit(rs.getDouble("roomBookingDeposit"));

                    // Employee
                    Employee employee = new Employee();
                    employee.setEmployeeID(rs.getString("employeeID"));
                    employee.setFullName(rs.getString("employeeName"));
                    reservationForm.setEmployee(employee);

                    // Customer
                    Customer customer = new Customer();
                    customer.setCustomerID(rs.getString("customerID"));
                    customer.setFullName(rs.getString("customerName"));
                    reservationForm.setCustomer(customer);
                }

                // Add to RoomWithReservation wrapper
                RoomWithReservation roomWithReservation = new RoomWithReservation(room, reservationForm);
                data.add(roomWithReservation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return data;
    }

}
