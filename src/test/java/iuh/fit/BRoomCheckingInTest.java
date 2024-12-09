package iuh.fit;

import iuh.fit.dao.*;
import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.ErrorMessages;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BRoomCheckingInTest {
    // ==================================================================================================================
    // 1. Setup cho Test
    // ==================================================================================================================
    @BeforeEach
    void setUp() {
        // Đặt default connect thành HotelTestDatabase
        DBHelper.setDatabaseName("HotelTestDatabase");
    }

    @AfterEach
    void tearDown() {
        // Đặt default connect thành HotelDatabase
        DBHelper.setDatabaseName("HotelDatabase");
    }
    // ==================================================================================================================
    // 2. Các testcase
    // ==================================================================================================================
    @Test
    void testRoomCheckingIn() {
        try {
            insertReservableReservationForm(
                    "RF-999999",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(3),
                    "V2304",
                    "CUS-000004"
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        ReservationForm reservationForm = ReservationFormDAO.getDataByID("RF-999999");
        Employee employee = EmployeeDAO.getDataByID("EMP-000001");

        RoomReservationDetailDAO.roomCheckingIn(Objects.requireNonNull(reservationForm).getReservationID(), Objects.requireNonNull(employee).getEmployeeID());

        RoomWithReservation roomWithReservation = RoomWithReservationDAO.getRoomWithReservationByID(
                reservationForm.getReservationID(),
                reservationForm.getRoom().getRoomID()
        );

        Room room = roomWithReservation.getRoom();
        LocalDateTime actualCheckinTime = HistoryCheckinDAO.getActualCheckInDate(reservationForm.getReservationID());

        // Kiểm tra kết quả
        Assertions.assertEquals(room.getRoomStatus(), RoomStatus.ON_USE);
        Assertions.assertNotNull(actualCheckinTime);
    }

    @Test
    void testRoomCheckingInTooLate() {
        try {
            insertReservableReservationForm(
                    "RF-999998",
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(3),
                    LocalDateTime.now().plusDays(3),
                    "T1105",
                    "CUS-000005"
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        ReservationForm reservationForm = ReservationFormDAO.getDataByID("RF-999998");
        Employee employee = EmployeeDAO.getDataByID("EMP-000001");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> RoomReservationDetailDAO.roomCheckingIn(Objects.requireNonNull(reservationForm).getReservationID(), Objects.requireNonNull(employee).getEmployeeID())
        );

        // Kiểm tra kết quả
        assertEquals(ErrorMessages.ROOM_CHECKING_IN_INVALID_RESERVATION, thrown.getMessage());
    }

    @Test
    void testRoomCheckingInEarly() {
        try {
            insertReservableReservationForm(
                    "RF-999997",
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(10),
                    LocalDateTime.now().plusDays(3),
                    "V2206",
                    "CUS-000006"
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        ReservationForm reservationForm = ReservationFormDAO.getDataByID("RF-999997");
        Employee employee = EmployeeDAO.getDataByID("EMP-000001");

        RoomReservationDetailDAO.roomEarlyCheckingIn(Objects.requireNonNull(reservationForm).getReservationID(), Objects.requireNonNull(employee).getEmployeeID());
        RoomWithReservation roomWithReservation = RoomWithReservationDAO.getRoomWithReservationByID(
                reservationForm.getReservationID(),
                reservationForm.getRoom().getRoomID()
        );
        Room room = roomWithReservation.getRoom();
        LocalDateTime actualCheckinTime = HistoryCheckinDAO.getActualCheckInDate(reservationForm.getReservationID());

        // Kiểm tra kết quả
        Assertions.assertEquals(room.getRoomStatus(), RoomStatus.ON_USE);
        Assertions.assertNotNull(actualCheckinTime);
        Assertions.assertEquals(500000 + 50000, roomWithReservation.getReservationForm().getRoomBookingDeposit());
    }


    // ==================================================================================================================
    // 3. Các phương thức hỗ trợ
    // ==================================================================================================================
    private void insertReservableReservationForm(String reservationFormID, LocalDateTime reservationDate,
                                                 LocalDateTime checkInDate, LocalDateTime checkOutDate,
                                                 String roomID, String customerID) throws SQLException {
        String sql =
        """
        INSERT INTO ReservationForm\s
        (reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit, isActivate)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
       \s""";

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, reservationFormID);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(reservationDate));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(checkInDate));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(checkOutDate));
            preparedStatement.setString(5, "EMP-000001");
            preparedStatement.setString(6, roomID);
            preparedStatement.setString(7, customerID);
            preparedStatement.setDouble(8, 500000);
            preparedStatement.setString(9, "ACTIVATE");
            preparedStatement.executeUpdate();
        }
    }


}
