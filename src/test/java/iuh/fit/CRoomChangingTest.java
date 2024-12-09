package iuh.fit;

import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.RoomReservationDetailDAO;
import iuh.fit.dao.RoomWithReservationDAO;
import iuh.fit.models.Room;
import iuh.fit.models.RoomReservationDetail;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.DBHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CRoomChangingTest {
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
    void testChangingRoom() {
        try {
            insertCheckedInReservationForm(
                    "RF-999996",
                    LocalDateTime.now().minusDays(2),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(3),
                    "EMP-000002",
                    "T1307",
                    "CUS-000007",
                    500000,
                    "ACTIVATE"
            );
            insertHistoryCheckIn(
                    "HCI-999996",
                    LocalDateTime.now().minusDays(1),
                    "RF-999996",
                    "EMP-000002"
            );
            insertRoomReservationDetail(
                    "RRD-999996",
                    LocalDateTime.now().minusDays(1),
                    "T1307",
                    "RF-999996",
                    "EMP-000002"
            );
            updateRoomStatus("T1307", RoomStatus.ON_USE);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        RoomReservationDetailDAO.changingRoom(
                "T1307",
                "V2408",
                "RF-999996",
                "EMP-000002"
        );

        RoomWithReservation roomWithReservation = RoomWithReservationDAO.getRoomWithReservationByID("RF-999996", "V2408");
        Room oldRoom = RoomDAO.getDataByID("T1307");
        Room newRoom = RoomDAO.getDataByID("V2408");

        Assertions.assertEquals(roomWithReservation.getRoom().getRoomID(), "V2408");
        Assertions.assertEquals(newRoom.getRoomStatus(), RoomStatus.ON_USE);
        Assertions.assertEquals(oldRoom.getRoomStatus(), RoomStatus.AVAILABLE);
    }


    // ==================================================================================================================
    // 3. Các phương thức hỗ trợ
    // ==================================================================================================================
    private void insertCheckedInReservationForm(String reservationFormID, LocalDateTime reservationDate,
                                                LocalDateTime checkInDate, LocalDateTime checkOutDate,
                                                String employeeID, String roomID, String customerID,
                                                double roomBookingDeposit, String isActivate) throws SQLException {
        String sql =
        """
        INSERT INTO ReservationForm 
        (reservationFormID, reservationDate, checkInDate, checkOutDate, employeeID, roomID, customerID, roomBookingDeposit, isActivate)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, reservationFormID);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(reservationDate));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(checkInDate));
            preparedStatement.setTimestamp(4, Timestamp.valueOf(checkOutDate));
            preparedStatement.setString(5, employeeID);
            preparedStatement.setString(6, roomID);
            preparedStatement.setString(7, customerID);
            preparedStatement.setDouble(8, roomBookingDeposit);
            preparedStatement.setString(9, isActivate);
            preparedStatement.executeUpdate();
        }
    }

    private void insertHistoryCheckIn(String historyCheckInID, LocalDateTime checkInDate,
                                      String reservationFormID, String employeeID) throws SQLException {
        String sql =
        """
        INSERT INTO HistoryCheckin 
        (historyCheckInID, checkInDate, reservationFormID, employeeID)
        VALUES (?, ?, ?, ?);
        """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, historyCheckInID);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(checkInDate));
            preparedStatement.setString(3, reservationFormID);
            preparedStatement.setString(4, employeeID);
            preparedStatement.executeUpdate();
        }
    }

    private void insertRoomReservationDetail(String roomReservationDetailID, LocalDateTime dateChanged,
                                             String roomID, String reservationFormID,
                                             String employeeID) throws SQLException {
        String sql =
        """
        INSERT INTO RoomReservationDetail 
        (roomReservationDetailID, dateChanged, roomID, reservationFormID, employeeID)
        VALUES (?, ?, ?, ?, ?);
        """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roomReservationDetailID);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(dateChanged));
            preparedStatement.setString(3, roomID);
            preparedStatement.setString(4, reservationFormID);
            preparedStatement.setString(5, employeeID);
            preparedStatement.executeUpdate();
        }
    }

    private void updateRoomStatus(String roomID, RoomStatus roomStatus) throws SQLException {
        String sql =
        """
        UPDATE Room
        SET roomStatus = ?
        WHERE roomID = ?;
        """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roomStatus.name());
            preparedStatement.setString(2, roomID);
            preparedStatement.executeUpdate();
        }
    }

}
