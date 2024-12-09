package iuh.fit;

import iuh.fit.dao.*;
import iuh.fit.models.Invoice;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.RoomManagementService;
import org.junit.jupiter.api.AfterEach;
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

public class ERoomCheckingOutTest {
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
    void testCheckout() {
        try {
            insertCheckedInReservationForm(
                    "RF-999992",
                    LocalDateTime.now().minusDays(5),
                    LocalDateTime.now().minusDays(4),
                    LocalDateTime.now().minusHours(1),
                    "EMP-000003",
                    "V2313",
                    "CUS-000010",
                    500000,
                    "ACTIVATE"
            );

            insertHistoryCheckIn(
                    "HCI-999992",
                    LocalDateTime.now().minusDays(4),
                    "RF-999992",
                    "EMP-000003"
            );
            insertRoomReservationDetail(
                    "RRD-999992",
                    LocalDateTime.now().minusDays(4),
                    "V2313",
                    "RF-999992",
                    "EMP-000003"
            );
            updateRoomStatus("V2313", RoomStatus.OVERDUE);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        RoomWithReservation roomWithReservation = RoomWithReservationDAO
                .getRoomWithReservationByID("RF-999992", "V2313");

        RoomManagementService.handleCheckOut(roomWithReservation, RoomManagementService.SYSTEM_EMPLOYEE);

        Invoice invoice = InvoiceDAO.getInvoiceByReservationFormID("RF-999992");

        assertEquals("CUS-000010", Objects.requireNonNull(invoice).getReservationForm().getCustomer().getCustomerID());
        assertEquals("EMP-000003", invoice.getReservationForm().getEmployee().getEmployeeID());
    }

    @Test
    void testCheckoutEarly() {
        try {
            insertCheckedInReservationForm(
                    "RF-999991",
                    LocalDateTime.now().minusDays(2),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(3),
                    "EMP-000003",
                    "V2314",
                    "CUS-000011",
                    500000,
                    "ACTIVATE"
            );

            insertHistoryCheckIn(
                    "HCI-999991",
                    LocalDateTime.now().minusDays(4),
                    "RF-999991",
                    "EMP-000003"
            );
            insertRoomReservationDetail(
                    "RRD-999991",
                    LocalDateTime.now().minusDays(4),
                    "V2314",
                    "RF-999991",
                    "EMP-000003"
            );
            updateRoomStatus("V2314", RoomStatus.ON_USE);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        RoomWithReservation roomWithReservation = RoomWithReservationDAO
                .getRoomWithReservationByID("RF-999991", "V2314");

        RoomManagementService.handleCheckoutEarly(roomWithReservation, RoomManagementService.SYSTEM_EMPLOYEE);

        Invoice invoice = InvoiceDAO.getInvoiceByReservationFormID("RF-999991");

        assertEquals("CUS-000011", Objects.requireNonNull(invoice).getReservationForm().getCustomer().getCustomerID());
        assertEquals("EMP-000003", invoice.getReservationForm().getEmployee().getEmployeeID());
        assertEquals(0, invoice.getReservationForm().getRoomBookingDeposit());
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
