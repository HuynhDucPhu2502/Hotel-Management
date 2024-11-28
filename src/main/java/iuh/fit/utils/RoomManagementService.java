package iuh.fit.utils;

import iuh.fit.dao.*;
import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RoomManagementService {

    private static final ScheduledExecutorService SCHEDULER =
            Executors.newScheduledThreadPool(1);
    public static final Employee SYSTEM_EMPLOYEE =
            EmployeeDAO.getEmployeeByEmployeeID("EMP-000000");

    public static void startAutoCheckoutScheduler() {
        SCHEDULER.scheduleAtFixedRate(
                RoomManagementService::autoCheckoutOverdueRooms,
                0,
                1,
                TimeUnit.MINUTES
        );
    }

    public static void autoCheckoutOverdueRooms() {
        List<RoomWithReservation> overdueRooms =
                RoomWithReservationDAO.getRoomOverDueWithLatestReservation();

        for (RoomWithReservation roomWithReservation : overdueRooms) {
            checkAndUpdateRoomStatus(
                    roomWithReservation,
                    SYSTEM_EMPLOYEE
            );
        }

        List<RoomWithReservation> allRoomWithReservation =
                RoomWithReservationDAO.getRoomWithReservation();

        for (RoomWithReservation roomWithReservation : allRoomWithReservation) {
            checkAndUpdateRoomStatus(
                    roomWithReservation,
                    SYSTEM_EMPLOYEE
            );
        }
    }

    public static void checkAndUpdateRoomStatus(
            RoomWithReservation roomWithReservation,
            Employee employee
    ) {
        ReservationForm reservationForm = roomWithReservation.getReservationForm();
        Room room = roomWithReservation.getRoom();

        if (reservationForm == null || room == null) return;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOutDate = reservationForm.getCheckOutDate();

        if (now.isAfter(checkOutDate)) {
            long hoursOverdue = ChronoUnit.HOURS.between(checkOutDate, now);

            if (hoursOverdue >= 2) {
                handleCheckOut(roomWithReservation, employee);
                room.setRoomStatus(RoomStatus.AVAILABLE);
                RoomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.AVAILABLE);
            } else {
                RoomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.OVERDUE);
                room.setRoomStatus(RoomStatus.OVERDUE);
            }
        }
    }

    public static void handleCheckOut(RoomWithReservation roomWithReservation, Employee employee) {
        try {
            double roomCharge = Calculator.calculateRoomCharge(
                    roomWithReservation.getRoom(),
                    roomWithReservation.getReservationForm().getCheckInDate(),
                    roomWithReservation.getReservationForm().getCheckOutDate()
            );
            double serviceCharge = Calculator.calculateTotalServiceCharge(
                    roomWithReservation.getReservationForm().getReservationID()
            );

            InvoiceDAO.roomCheckingOut(
                    roomWithReservation.getReservationForm().getReservationID(),
                    employee.getEmployeeID(),
                    roomCharge,
                    serviceCharge
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




}