package iuh.fit;

import iuh.fit.dao.CustomerDAO;
import iuh.fit.dao.EmployeeDAO;
import iuh.fit.dao.ReservationFormDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.models.Customer;
import iuh.fit.models.Employee;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.ErrorMessages;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CreateReservationFormTest {
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

    @Test
    void testOverlappingDateReservationForm() {
        Room room = RoomDAO.getDataByID("T1101");
        Employee employee = EmployeeDAO.getDataByID("EMP-000001");

        Customer customerOne = CustomerDAO.getDataByID("CUS-000001");
        Customer customerTwo = CustomerDAO.getDataByID("CUS-000002");

        // Tạo 2 phiếu đặt phòng của 2 khách hàng cùng phòng nhưng trùng ngày
        ReservationForm reservationFormOne = new ReservationForm(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(8),
                employee,
                room,
                customerOne
        );
        ReservationForm reservationFormTwo = new ReservationForm(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(4),
                LocalDateTime.now().plusDays(6),
                employee,
                room,
                customerTwo
        );

        String nextId = DBHelper.getNextID("ReservationForm");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    ReservationFormDAO.createData(reservationFormOne);
                    ReservationFormDAO.createData(reservationFormTwo);
                }
        );

        // Kiểm tra thông báo lỗi
        assertEquals(ErrorMessages.CREATING_RESERVATION_FORM_CHECK_DATE_OVERLAP, thrown.getMessage());

        // Xóa dữ liệu sau khi test
        ReservationFormDAO.deleteData(nextId);
    }

    @Test
    void testOverlappingIDCardNumberReservationForm() {
        Room roomOne = RoomDAO.getDataByID("T1101");
        Room roomTwo = RoomDAO.getDataByID("V2102");

        Employee employee = EmployeeDAO.getDataByID("EMP-000001");

        Customer customer = CustomerDAO.getDataByID("CUS-000001");

        // Tạo 2 phiếu đặt phòng của 1 khách hàng khác phòng nhưng trùng ngày
        ReservationForm reservationFormOne = new ReservationForm(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3),
                LocalDateTime.now().plusDays(8),
                employee,
                roomOne,
                customer
        );

        ReservationForm reservationFormTwo = new ReservationForm(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(4),
                LocalDateTime.now().plusDays(6),
                employee,
                roomTwo,
                customer
        );

        String nextId = DBHelper.getNextID("ReservationForm");

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> {
                    ReservationFormDAO.createData(reservationFormOne);
                    ReservationFormDAO.createData(reservationFormTwo);
                }
        );

        // Kiểm tra thông báo lỗi
        assertEquals(ErrorMessages.CREATING_RESERVATION_FORM_ID_CARD_NUMBER_OVERLAP, thrown.getMessage());

        // Xóa dữ liệu sau khi test
        ReservationFormDAO.deleteData(nextId);
    }
}
