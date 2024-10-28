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
                ReservationForm reservationForm = new ReservationForm();
                Employee employee = new Employee();
                Room room = new Room();
                Customer customer = new Customer();
                RoomCategory roomCategory = new RoomCategory();

                // ReservationForm
                reservationForm.setReservationID(rs.getString(1));
                reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(2)));
                reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(3)));
                reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                reservationForm.setRoomBookingDeposit(rs.getDouble(5));

                // Employee
                employee.setEmployeeID(rs.getString(6));
                employee.setFullName(rs.getString(9));
                employee.setPhoneNumber(rs.getString(10));
                employee.setEmail(rs.getString(11));
                employee.setAddress(rs.getString(12));
                employee.setGender(ConvertHelper.genderConverter(rs.getString(13)));
                employee.setIdCardNumber(rs.getString(14));
                employee.setDob(ConvertHelper.localDateConverter(rs.getDate(15)));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString(16)));

                // Room
                room.setRoomID(rs.getString(7));
                room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString(17)));
                room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp(18)));

                // Room Category
                roomCategory.setRoomCategoryID(rs.getString(19));
                roomCategory.setRoomCategoryName(rs.getString(27));
                roomCategory.setNumberOfBed(rs.getInt(28));
              
                room.setRoomCategory(roomCategory);

                // Customer
                customer.setCustomerID(rs.getString(8));
                customer.setFullName(rs.getString(20));
                customer.setPhoneNumber(rs.getString(21));
                customer.setEmail(rs.getString(22));
                customer.setAddress(rs.getString(23));
                customer.setGender(ConvertHelper.genderConverter(rs.getString(24)));
                customer.setIdCardNumber(rs.getString(25));
                customer.setDob(ConvertHelper.localDateConverter(rs.getDate(26)));

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
                    ReservationForm reservationForm = new ReservationForm();
                    Employee employee = new Employee();
                    Room room = new Room();
                    Customer customer = new Customer();
                    RoomCategory roomCategory = new RoomCategory();

                    // ReservationForm
                    reservationForm.setReservationID(rs.getString(1));
                    reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(2)));
                    reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(3)));
                    reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                    reservationForm.setRoomBookingDeposit(rs.getDouble(5));

                    // Employee
                    employee.setEmployeeID(rs.getString(6));
                    employee.setFullName(rs.getString(9));
                    employee.setPhoneNumber(rs.getString(10));
                    employee.setEmail(rs.getString(11));
                    employee.setAddress(rs.getString(12));
                    employee.setGender(ConvertHelper.genderConverter(rs.getString(13)));
                    employee.setIdCardNumber(rs.getString(14));
                    employee.setDob(ConvertHelper.localDateConverter(rs.getDate(15)));
                    employee.setPosition(ConvertHelper.positionConverter(rs.getString(16)));

                    // Room
                    room.setRoomID(rs.getString(7));
                    room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString(17)));
                    room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp(18)));

                    // Room Category
                    roomCategory.setRoomCategoryID(rs.getString(19));
                    roomCategory.setRoomCategoryName(rs.getString(27));
                    roomCategory.setNumberOfBed(rs.getInt(28));

                    room.setRoomCategory(roomCategory);

                    // Customer
                    customer.setCustomerID(rs.getString(8));
                    customer.setFullName(rs.getString(20));
                    customer.setPhoneNumber(rs.getString(21));
                    customer.setEmail(rs.getString(22));
                    customer.setAddress(rs.getString(23));
                    customer.setGender(ConvertHelper.genderConverter(rs.getString(24)));
                    customer.setIdCardNumber(rs.getString(25));
                    customer.setDob(ConvertHelper.localDateConverter(rs.getDate(26)));

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
            overlapCheckStmt.setTimestamp(2, ConvertHelper.dateTimeToSQLConverter(reservationForm.getCheckInDate()));
            overlapCheckStmt.setTimestamp(3, ConvertHelper.dateTimeToSQLConverter(reservationForm.getCheckOutDate()));

            ResultSet overlapResult = overlapCheckStmt.executeQuery();
            if (overlapResult.next() && overlapResult.getInt(1) > 0)
                throw new IllegalArgumentException(ErrorMessages.RESERVATION_CHECK_DATE_OVERLAP);


            overlapIDCardStmt.setString(1, reservationForm.getCustomer().getIdCardNumber());
            overlapIDCardStmt.setTimestamp(2, ConvertHelper.dateTimeToSQLConverter(reservationForm.getCheckInDate()));
            overlapIDCardStmt.setTimestamp(3, ConvertHelper.dateTimeToSQLConverter(reservationForm.getCheckOutDate()));

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
            insertStatement.setTimestamp(2, ConvertHelper.dateTimeToSQLConverter(reservationForm.getReservationDate()));
            insertStatement.setTimestamp(3, ConvertHelper.dateTimeToSQLConverter(reservationForm.getCheckInDate()));
            insertStatement.setTimestamp(4, ConvertHelper.dateTimeToSQLConverter(reservationForm.getCheckOutDate()));
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
            preparedStatement.setTimestamp(1, ConvertHelper.dateTimeToSQLConverter(reservationForm.getReservationDate()));
            preparedStatement.setTimestamp(2, ConvertHelper.dateTimeToSQLConverter(reservationForm.getCheckInDate()));
            preparedStatement.setTimestamp(3, ConvertHelper.dateTimeToSQLConverter(reservationForm.getCheckOutDate()));
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
                ReservationForm reservationForm = new ReservationForm();
                Employee employee = new Employee();
                Room room = new Room();
                Customer customer = new Customer();
                RoomCategory roomCategory = new RoomCategory();

                // Reservation Form
                reservationForm.setReservationID(rs.getString(1));
                reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(2)));
                reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(3)));
                reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                reservationForm.setRoomBookingDeposit(rs.getDouble(5));

                // Employee
                employee.setEmployeeID(rs.getString(6));
                employee.setFullName(rs.getString(9));
                employee.setPhoneNumber(rs.getString(10));
                employee.setEmail(rs.getString(11));
                employee.setAddress(rs.getString(12));
                employee.setGender(ConvertHelper.genderConverter(rs.getString(13)));
                employee.setIdCardNumber(rs.getString(14));
                employee.setDob(ConvertHelper.localDateConverter(rs.getDate(15)));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString(16)));

                // Room
                room.setRoomID(rs.getString(7));
                room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString(17)));
                room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp(18)));

                roomCategory.setRoomCategoryID(rs.getString(19));
                roomCategory.setRoomCategoryName(rs.getString(27));
                roomCategory.setNumberOfBed(rs.getInt(28));
                room.setRoomCategory(roomCategory);

                // Customer
                customer.setCustomerID(rs.getString(8));
                customer.setFullName(rs.getString(20));
                customer.setPhoneNumber(rs.getString(21));
                customer.setEmail(rs.getString(22));
                customer.setAddress(rs.getString(23));
                customer.setGender(ConvertHelper.genderConverter(rs.getString(24)));
                customer.setIdCardNumber(rs.getString(25));
                customer.setDob(ConvertHelper.localDateConverter(rs.getDate(26)));

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
                    ConvertHelper.dateTimeToSQLConverter(LocalDateTime.now().minusHours(2))
            );

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                ReservationForm reservationForm = new ReservationForm();
                Employee employee = new Employee();
                Room room = new Room();
                Customer customer = new Customer();
                RoomCategory roomCategory = new RoomCategory();

                // ReservationForm
                reservationForm.setReservationID(rs.getString(1));
                reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(2)));
                reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(3)));
                reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                reservationForm.setRoomBookingDeposit(rs.getDouble(5));

                // Employee
                employee.setEmployeeID(rs.getString(6));
                employee.setFullName(rs.getString(9));
                employee.setPhoneNumber(rs.getString(10));
                employee.setEmail(rs.getString(11));
                employee.setAddress(rs.getString(12));
                employee.setGender(ConvertHelper.genderConverter(rs.getString(13)));
                employee.setIdCardNumber(rs.getString(14));
                employee.setDob(ConvertHelper.localDateConverter(rs.getDate(15)));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString(16)));

                // Room
                room.setRoomID(rs.getString(7));
                room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString(17)));
                room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp(18)));

                // Room Category
                roomCategory.setRoomCategoryID(rs.getString(19));
                roomCategory.setRoomCategoryName(rs.getString(27));
                roomCategory.setNumberOfBed(rs.getInt(28));
                room.setRoomCategory(roomCategory);

                // Customer
                customer.setCustomerID(rs.getString(8));
                customer.setFullName(rs.getString(20));
                customer.setPhoneNumber(rs.getString(21));
                customer.setEmail(rs.getString(22));
                customer.setAddress(rs.getString(23));
                customer.setGender(ConvertHelper.genderConverter(rs.getString(24)));
                customer.setIdCardNumber(rs.getString(25));
                customer.setDob(ConvertHelper.localDateConverter(rs.getDate(26)));

                reservationForm.setEmployee(employee);
                reservationForm.setRoom(room);
                reservationForm.setCustomer(customer);

                reservations.add(reservationForm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }


}
