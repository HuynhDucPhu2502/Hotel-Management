package iuh.fit;

import iuh.fit.dao.*;
import iuh.fit.models.*;
import iuh.fit.models.enums.*;
import iuh.fit.utils.ConvertHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FetchTesting {
    public static void main(String[] args) {
        ServiceCategoryDAO.getServiceCategory().forEach(System.out::println);
        HotelServiceDAO.getHotelService().forEach(System.out::println);
        HistoryCheckinDAO.getHistoryCheckin().forEach(System.out::println);
        RoomDAO.getRoom().forEach(System.out::println);
        ReservationFormDAO.getReservationForm().forEach(System.out::println);
    }

}
