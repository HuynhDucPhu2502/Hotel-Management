package iuh.fit;

import iuh.fit.utils.DBHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

public class RoomCheckingInTest {
    @BeforeEach
    void setUp() {
        // Đặt default connect thành HotelTestDatabase
        DBHelper.setDatabaseName("HotelTestDatabase");
        insertReservationForm();
    }

    @AfterEach
    void tearDown() {
        // Đặt default connect thành HotelDatabase
        DBHelper.setDatabaseName("HotelDatabase");
    }

    public static boolean insertReservationForm() {
        String sql =
               """
               INSERT INTO ReservationForm (reservationFormID, reservationDate, checkInDate, checkOutDate, 
               employeeID, roomID, customerID, roomBookingDeposit, isActivate) 
               VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
               """;

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "RF-999999");
            stmt.setObject(2, LocalDateTime.now());
            stmt.setObject(3, LocalDateTime.now());
            stmt.setObject(4, LocalDateTime.now().plusDays(3));
            stmt.setString(5, "EMP-000001");
            stmt.setString(6, "T1101");
            stmt.setString(7, "CUS-000001");
            stmt.setInt(8, 500000);
            stmt.setString(9, "ACTIVATE");

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
