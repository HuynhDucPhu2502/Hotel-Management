package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationFormDAO {
    public static List<ReservationForm> getReservationForm() {
        ArrayList<ReservationForm> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ) {
            String sql = "SELECT a.reservationFormID, a.reservationDate, a.checkInDate, " +
                    "a.checkOutDate, a.roomBookingDeposit, a.employeeID, " +
                    "a.roomID, a.customerID, b.fullName, " +
                    "b.phoneNumber, b.email, b.address, " +
                    "b.gender, b.idCardNumber, b.dob, " +
                    "b.position, c.roomStatus, c.dateOfCreation,  " +
                    "c.roomCategoryID, d.fullName, d.phoneNumber, " +
                    "d.email, d.address, d.gender, " +
                    "d.idCardNumber, d.dob, e.roomCategoryName, " +
                    "e.numberOfBed " +
                    "FROM ReservationForm a " +
                    "INNER JOIN Employee b ON a.employeeID = b.employeeID " +
                    "INNER JOIN Room c ON a.roomID = c.roomID " +
                    "INNER JOIN Customer d ON a.customerID = d.customerID " +
                    "INNER JOIN RoomCategory e ON c.roomCategoryID = e.roomCategoryID";

            ResultSet rs = statement.executeQuery(sql);

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
        String sql = "SELECT a.reservationFormID, a.reservationDate, a.checkInDate, " +
                "a.checkOutDate, a.roomBookingDeposit, a.employeeID, " +
                "a.roomID, a.customerID, b.fullName, " +
                "b.phoneNumber, b.email, b.address, " +
                "b.gender, b.idCardNumber, b.dob, " +
                "b.position, c.roomStatus, c.dateOfCreation,  " +
                "c.roomCategoryID, d.fullName, d.phoneNumber, " +
                "d.email, d.address, d.gender, " +
                "d.idCardNumber, d.dob, e.roomCategoryName, " +
                "e.numberOfBed " +
                "FROM ReservationForm a " +
                "INNER JOIN Employee b ON a.employeeID = b.employeeID " +
                "INNER JOIN Room c ON a.roomID = c.roomID " +
                "INNER JOIN Customer d ON a.customerID = d.customerID " +
                "INNER JOIN RoomCategory e ON c.roomCategoryID = e.roomCategoryID";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
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
        String overlapCheckSQL = "SELECT COUNT(*) FROM ReservationForm " +
                "WHERE roomID = ? AND " +
                "(? < checkOutDate AND ? > checkInDate)";

        String overlapIDCardSQL = "SELECT COUNT(*) FROM ReservationForm a " +
                "INNER JOIN Customer b ON a.customerID = b.customerID " +
                "WHERE b.idCardNumber = ? AND " +
                "(? < a.checkOutDate AND ? > a.checkInDate)";

        try (
                Connection connection = DBHelper.getConnection();

                PreparedStatement overlapCheckStmt = connection.prepareStatement(overlapCheckSQL);
                PreparedStatement overlapIDCardStmt = connection.prepareStatement(overlapIDCardSQL);
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO ReservationForm(reservationFormID, reservationDate, checkInDate, " +
                                "checkOutDate, employeeID, roomID, customerID, roomBookingDeposit) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)"
                );
                PreparedStatement selectSequenceStatement = connection.prepareStatement(
                        "SELECT nextID FROM GlobalSequence WHERE tableName = ?"
                );
                PreparedStatement updateSequenceStatement = connection.prepareStatement(
                        "UPDATE GlobalSequence SET nextID = ? WHERE tableName = ?"
                )
        ) {
            overlapCheckStmt.setString(1, reservationForm.getRoom().getRoomID());
            overlapCheckStmt.setTimestamp(2, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getCheckInDate()));
            overlapCheckStmt.setTimestamp(3, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getCheckOutDate()));

            ResultSet overlapResult = overlapCheckStmt.executeQuery();
            if (overlapResult.next() && overlapResult.getInt(1) > 0)
                throw new IllegalArgumentException(ErrorMessages.RESERVATION_CHECK_DATE_OVERLAP);


            overlapIDCardStmt.setString(1, reservationForm.getCustomer().getIdCardNumber());
            overlapIDCardStmt.setTimestamp(2, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getCheckInDate()));
            overlapIDCardStmt.setTimestamp(3, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getCheckOutDate()));

            ResultSet overlapIDCardResult = overlapIDCardStmt.executeQuery();
            if (overlapIDCardResult.next() && overlapIDCardResult.getInt(1) > 0)
                throw new IllegalArgumentException(ErrorMessages.RESERVATION_ID_CARD_NUMBER_OVERLAP);


            selectSequenceStatement.setString(1, "ReservationForm");
            ResultSet rs = selectSequenceStatement.executeQuery();

            String newReservationFormID = "RF-000001";
            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.RESERVATIONID_PREFIX + "-";

                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
                newReservationFormID = prefix + String.format("%06d", nextIDNum);

                updateSequenceStatement.setString(1, newReservationFormID);
                updateSequenceStatement.setString(2, "ReservationForm");
                updateSequenceStatement.executeUpdate();
            }

            insertStatement.setString(1, newReservationFormID);
            insertStatement.setTimestamp(2, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getReservationDate()));
            insertStatement.setTimestamp(3, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getCheckInDate()));
            insertStatement.setTimestamp(4, ConvertHelper.localDateTimeToSQLConverter(reservationForm.getCheckOutDate()));
            insertStatement.setString(5, reservationForm.getEmployee().getEmployeeID());
            insertStatement.setString(6, reservationForm.getRoom().getRoomID());
            insertStatement.setString(7, reservationForm.getCustomer().getCustomerID());
            insertStatement.setDouble(8, reservationForm.getRoomBookingDeposit());

            insertStatement.executeUpdate();
        } catch (Exception exception) {
            if (exception.getMessage().equalsIgnoreCase(ErrorMessages.RESERVATION_CHECK_DATE_OVERLAP))
                throw new IllegalArgumentException(ErrorMessages.RESERVATION_CHECK_DATE_OVERLAP);
            else if (exception.getMessage().contains(ErrorMessages.RESERVATION_ID_CARD_NUMBER_OVERLAP))
                throw new IllegalArgumentException(ErrorMessages.RESERVATION_ID_CARD_NUMBER_OVERLAP);
            else {
                exception.printStackTrace();
                System.exit(1);
            }
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
                                "employeeID = ?, roomID = ?, customerID = ?, " +
                                "roomBookingDeposit = ? " +
                                "WHERE reservationFormID = ? "
                )
        ){
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

    public static String getNextReservationFormID() {
        String nextID = "RF-000001";

        String query = "SELECT nextID FROM GlobalSequence WHERE tableName = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, "ReservationForm");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                nextID = rs.getString(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return nextID;
    }

    public static List<ReservationForm> getReservationFormByRoomID(String roomID) {
        ArrayList<ReservationForm> data = new ArrayList<>();

        String sql = "SELECT a.reservationFormID, a.reservationDate, a.checkInDate, " +
                "a.checkOutDate, a.roomBookingDeposit, a.employeeID, " +
                "a.roomID, a.customerID, b.fullName, " +
                "b.phoneNumber, b.email, b.address, " +
                "b.gender, b.idCardNumber, b.dob, " +
                "b.position, c.roomStatus, c.dateOfCreation,  " +
                "c.roomCategoryID, d.fullName, d.phoneNumber, " +
                "d.email, d.address, d.gender, " +
                "d.idCardNumber, d.dob, e.roomCategoryName, " +
                "e.numberOfBed " +
                "FROM ReservationForm a " +
                "INNER JOIN Employee b ON a.employeeID = b.employeeID " +
                "INNER JOIN Room c ON a.roomID = c.roomID " +
                "INNER JOIN Customer d ON a.customerID = d.customerID " +
                "INNER JOIN RoomCategory e ON c.roomCategoryID = e.roomCategoryID " +
                "WHERE a.roomID = ? AND a.checkInDate >= ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, roomID);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now().minusHours(2)));
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                data.add(extractData(rs));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        return data;
    }

    public static List<ReservationForm> getUpcomingReservations(String roomID) {
        List<ReservationForm> reservations = new ArrayList<>();

        String sql =
                "SELECT a.reservationFormID, a.reservationDate, a.checkInDate, " +
                        "a.checkOutDate, a.roomBookingDeposit, a.employeeID, " +
                        "a.roomID, a.customerID, b.fullName, " +
                        "b.phoneNumber, b.email, b.address, " +
                        "b.gender, b.idCardNumber, b.dob, " +
                        "b.position, c.roomStatus, c.dateOfCreation, " +
                        "c.roomCategoryID, d.fullName, d.phoneNumber, " +
                        "d.email, d.address, d.gender, " +
                        "d.idCardNumber, d.dob, e.roomCategoryName, " +
                        "e.numberOfBed " +
                        "FROM ReservationForm a " +
                        "INNER JOIN Employee b ON a.employeeID = b.employeeID " +
                        "INNER JOIN Room c ON a.roomID = c.roomID " +
                        "INNER JOIN Customer d ON a.customerID = d.customerID " +
                        "INNER JOIN RoomCategory e ON c.roomCategoryID = e.roomCategoryID " +
                        "WHERE a.roomID = ? " +
                        "AND a.checkInDate >= ? " +
                        "AND NOT EXISTS ( " +
                        "    SELECT 1 FROM HistoryCheckin hci " +
                        "    WHERE hci.reservationFormID = a.reservationFormID " +
                        ") " +
                        "ORDER BY a.checkInDate";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, roomID);
            preparedStatement.setTimestamp(2,
                    ConvertHelper.localDateTimeToSQLConverter(LocalDateTime.now().minusHours(2))
            );

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
        String sql =
        """
        UPDATE ReservationForm\s
        SET roomID = ?\s
        WHERE reservationFormID = ?
        \s""";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            // Thiết lập tham số cho câu lệnh SQL
            preparedStatement.setString(1, newRoomID);
            preparedStatement.setString(2, reservationFormID);

            // Thực thi câu lệnh cập nhật
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

        String sql =
            """
            SELECT a.reservationFormID, a.reservationDate, a.checkInDate, 
                   a.checkOutDate, a.roomBookingDeposit, a.employeeID, 
                   a.roomID, a.customerID, b.fullName, 
                   b.phoneNumber, b.email, b.address, 
                   b.gender, b.idCardNumber, b.dob, 
                   b.position, c.roomStatus, c.dateOfCreation,  
                   c.roomCategoryID, d.fullName, d.phoneNumber, 
                   d.email, d.address, d.gender, 
                   d.idCardNumber, d.dob, e.roomCategoryName, 
                   e.numberOfBed 
            FROM ReservationForm a 
            INNER JOIN Employee b ON a.employeeID = b.employeeID 
            INNER JOIN Room c ON a.roomID = c.roomID 
            INNER JOIN Customer d ON a.customerID = d.customerID 
            INNER JOIN RoomCategory e ON c.roomCategoryID = e.roomCategoryID 
            WHERE a.customerID = ?
            """;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
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


    private static ReservationForm extractData(ResultSet rs) throws SQLException {
        ReservationForm reservationForm = new ReservationForm();
        Employee employee = new Employee();
        Room room = new Room();
        Customer customer = new Customer();
        RoomCategory roomCategory = new RoomCategory();

        // ReservationForm
        reservationForm.setReservationID(rs.getString("reservationFormID"));
        reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("reservationDate")));
        reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkInDate")));
        reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));
        reservationForm.setRoomBookingDeposit(rs.getDouble("roomBookingDeposit"));

        // Employee
        employee.setEmployeeID(rs.getString("employeeID"));
        employee.setFullName(rs.getString("fullName"));
        employee.setPhoneNumber(rs.getString("phoneNumber"));
        employee.setEmail(rs.getString("email"));
        employee.setAddress(rs.getString("address"));
        employee.setGender(ConvertHelper.genderConverter(rs.getString("gender")));
        employee.setIdCardNumber(rs.getString("idCardNumber"));
        employee.setDob(ConvertHelper.localDateConverter(rs.getDate("dob")));
        employee.setPosition(ConvertHelper.positionConverter(rs.getString("position")));

        // Room
        room.setRoomID(rs.getString("roomID"));
        room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString("roomStatus")));
        room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp("dateOfCreation")));

        // Room Category
        roomCategory.setRoomCategoryID(rs.getString("roomCategoryID"));
        roomCategory.setRoomCategoryName(rs.getString("roomCategoryName"));
        roomCategory.setNumberOfBed(rs.getInt("numberOfBed"));
        room.setRoomCategory(roomCategory);

        // Customer
        customer.setCustomerID(rs.getString("customerID"));
        customer.setFullName(rs.getString("fullName"));
        customer.setPhoneNumber(rs.getString("phoneNumber"));
        customer.setEmail(rs.getString("email"));
        customer.setAddress(rs.getString("address"));
        customer.setGender(ConvertHelper.genderConverter(rs.getString("gender")));
        customer.setIdCardNumber(rs.getString("idCardNumber"));
        customer.setDob(ConvertHelper.localDateConverter(rs.getDate("dob")));

        reservationForm.setEmployee(employee);
        reservationForm.setRoom(room);
        reservationForm.setCustomer(customer);

        return reservationForm;
    }


}
