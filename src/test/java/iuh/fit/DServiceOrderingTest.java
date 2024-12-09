package iuh.fit;

import iuh.fit.dao.*;
import iuh.fit.models.HotelService;
import iuh.fit.models.RoomUsageService;
import iuh.fit.models.enums.RoomStatus;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DServiceOrderingTest {
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
    void testServiceOrdering() {
        try {
            insertCheckedInReservationForm(
                    "RF-999994",
                    LocalDateTime.now().minusDays(2),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(3),
                    "EMP-000003",
                    "V2311",
                    "CUS-000008",
                    500000,
                    "ACTIVATE"
            );

            insertHistoryCheckIn(
                    "HCI-999994",
                    LocalDateTime.now().minusDays(4),
                    "RF-999994",
                    "EMP-000003"
            );
            insertRoomReservationDetail(
                    "RRD-999994",
                    LocalDateTime.now().minusDays(4),
                    "V2311",
                    "RF-999994",
                    "EMP-000003"
            );
            updateRoomStatus("V2311", RoomStatus.AVAILABLE);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Test failed due to unexpected error.");
        }

        HotelService hotelService = HotelServiceDAO.getDataByID("HS-000001");

        RoomUsageService roomUsageService = new RoomUsageService(
                1,
                hotelService.getServicePrice(),
                hotelService,
                ReservationFormDAO.getDataByID("RF-999994"),
                EmployeeDAO.getDataByID("EMP-000003"),
                LocalDateTime.now()
        );
        RoomUsageServiceDAO.serviceOrdering(roomUsageService);

        ArrayList<RoomUsageService> serviceArrayList =
                (ArrayList<RoomUsageService>) RoomUsageServiceDAO.getByReservationFormID("RF-999994");

         HotelService usedService =  serviceArrayList.getFirst().getHotelService();

        assertEquals(hotelService.getServiceId(), usedService.getServiceId());
    }

    @Test
    void testOverdueServiceOrdering() {
        try {
            insertCheckedInReservationForm(
                    "RF-999993",
                    LocalDateTime.now().minusDays(5),
                    LocalDateTime.now().minusDays(4),
                    LocalDateTime.now().minusHours(1),
                    "EMP-000003",
                    "V2312",
                    "CUS-000009",
                    500000,
                    "ACTIVATE"
            );

            insertHistoryCheckIn(
                    "HCI-999993",
                    LocalDateTime.now().minusDays(4),
                    "RF-999993",
                    "EMP-000003"
            );
            insertRoomReservationDetail(
                    "RRD-999993",
                    LocalDateTime.now().minusDays(4),
                    "V2312",
                    "RF-999993",
                    "EMP-000003"
            );
            updateRoomStatus("V2312", RoomStatus.OVERDUE);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        HotelService hotelService = HotelServiceDAO.getDataByID("HS-000001");

        RoomUsageService roomUsageService = new RoomUsageService(
                1,
                hotelService.getServicePrice(),
                hotelService,
                ReservationFormDAO.getDataByID("RF-999993"),
                EmployeeDAO.getDataByID("EMP-000003"),
                LocalDateTime.now()
        );


        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    RoomUsageServiceDAO.serviceOrdering(roomUsageService);
                }
        );

        assertEquals("Phiếu đặt phòng không tồn tại hoặc đã hết hạn.", thrown.getMessage());
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
