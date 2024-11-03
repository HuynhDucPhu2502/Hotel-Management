package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.*;
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
               e.fullName AS employeeName,
               c.fullName AS customerName, c.phoneNumber, c.email, c.idCardNumber
        FROM Room r
        LEFT JOIN RoomCategory rc ON r.roomCategoryID = rc.roomCategoryID
        LEFT JOIN (
            SELECT rf.*
            FROM ReservationForm rf
            LEFT JOIN HistoryCheckOut hco ON rf.reservationFormID = hco.reservationFormID
            WHERE hco.historyCheckOutID IS NULL
              AND GETDATE() BETWEEN rf.checkInDate AND DATEADD(hour, 2, rf.checkOutDate)
        ) AS rf ON r.roomID = rf.roomID
        LEFT JOIN Employee e ON rf.employeeID = e.employeeID
        LEFT JOIN Customer c ON rf.customerID = c.customerID;
        """;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery()
        ) {
            while (rs.next()) {
                RoomWithReservation roomWithReservation = extractData(rs);
                data.add(roomWithReservation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static RoomWithReservation getRoomWithReservationByID(String reservationFormID, String roomID) {
        String sql = """
        SELECT r.roomID, r.roomStatus, r.dateOfCreation,
               rc.roomCategoryID, rc.roomCategoryName, rc.numberOfBed,
               rf.reservationFormID, rf.reservationDate, rf.checkInDate,
               rf.checkOutDate, rf.roomBookingDeposit, rf.employeeID, rf.customerID,
               e.fullName AS employeeName,
               c.fullName AS customerName, c.phoneNumber, c.email, c.idCardNumber
        FROM Room r
        LEFT JOIN RoomCategory rc ON r.roomCategoryID = rc.roomCategoryID
        LEFT JOIN (
            SELECT rf.*
            FROM ReservationForm rf
            LEFT JOIN HistoryCheckOut hco ON rf.reservationFormID = hco.reservationFormID
            WHERE hco.historyCheckOutID IS NULL
              AND rf.reservationFormID = ?
        ) AS rf ON r.roomID = rf.roomID
        LEFT JOIN Employee e ON rf.employeeID = e.employeeID
        LEFT JOIN Customer c ON rf.customerID = c.customerID
        WHERE r.roomID = ?;
        """;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, reservationFormID);
            preparedStatement.setString(2, roomID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return extractData(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static List<RoomWithReservation> getRoomOverDueWithLatestReservation() {
        List<RoomWithReservation> data = new ArrayList<>();

        String sql = """
            SELECT r.roomID, r.roomStatus, r.dateOfCreation,
                   rc.roomCategoryID, rc.roomCategoryName, rc.numberOfBed,
                   rf.reservationFormID, rf.reservationDate, rf.checkInDate,
                   rf.checkOutDate, rf.roomBookingDeposit, rf.employeeID, rf.customerID,
                   e.fullName AS employeeName,
                   c.fullName AS customerName, c.phoneNumber, c.email, c.idCardNumber
            FROM Room r
            LEFT JOIN RoomCategory rc ON r.roomCategoryID = rc.roomCategoryID
            LEFT JOIN (
                SELECT rf.*
                FROM ReservationForm rf
                JOIN (
                    SELECT roomID, MAX(checkOutDate) AS latestCheckOutDate
                    FROM ReservationForm
                    GROUP BY roomID
                ) AS latestRF ON rf.roomID = latestRF.roomID AND rf.checkOutDate = latestRF.latestCheckOutDate
                LEFT JOIN HistoryCheckOut hco ON rf.reservationFormID = hco.reservationFormID
                WHERE hco.historyCheckOutID IS NULL
                  AND GETDATE() > DATEADD(hour, 2, rf.checkOutDate)
            ) AS rf ON r.roomID = rf.roomID
            LEFT JOIN Employee e ON rf.employeeID = e.employeeID
            LEFT JOIN Customer c ON rf.customerID = c.customerID
            WHERE r.roomStatus = 'OVERDUE' AND rf.reservationFormID IS NOT NULL;
            """;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet rs = preparedStatement.executeQuery()
        ) {
            while (rs.next()) {
                RoomWithReservation roomWithReservation = extractData(rs);
                data.add(roomWithReservation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }



    private static RoomWithReservation extractData(ResultSet rs) throws SQLException {
        // Room and RoomCategory
        Room room = new Room();
        RoomCategory roomCategory = new RoomCategory();

        room.setRoomID(rs.getString("roomID"));
        room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString("roomStatus")));
        room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp("dateOfCreation")));

        roomCategory.setRoomCategoryID(rs.getString("roomCategoryID"));
        roomCategory.setRoomCategoryName(rs.getString("roomCategoryName"));
        roomCategory.setNumberOfBed(rs.getInt("numberOfBed"));
        room.setRoomCategory(roomCategory);

        // ReservationForm
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
            customer.setPhoneNumber(rs.getString("phoneNumber"));
            customer.setEmail(rs.getString("email"));
            customer.setIdCardNumber(rs.getString("idCardNumber"));
            reservationForm.setCustomer(customer);
        }

        return new RoomWithReservation(room, reservationForm);
    }


}
