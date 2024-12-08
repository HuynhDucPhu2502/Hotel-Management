package iuh.fit.utils;

import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.dao.*;
import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RoomManagementService {

    private static final ScheduledExecutorService SCHEDULER =
            Executors.newScheduledThreadPool(1);
    public static final Employee SYSTEM_EMPLOYEE =
            EmployeeDAO.getEmployeeByEmployeeID("EMP-000000");
    private static final Set<Room> notifiedOverdueRooms = new HashSet<>();

    public static void startAutoCheckoutScheduler(NotificationButtonController topBarController) {
        SCHEDULER.scheduleAtFixedRate(
                () -> RoomManagementService.autoCheckoutOverdueRooms(topBarController),
                0,
                5,
                TimeUnit.SECONDS
        );
    }

    public static void autoCheckoutOverdueRooms(NotificationButtonController topBarController) {
        List<RoomWithReservation> overdueRooms =
                RoomWithReservationDAO.getRoomOverDueWithLatestReservation();

        for (RoomWithReservation roomWithReservation : overdueRooms) {
            checkAndUpdateRoomStatus(
                    roomWithReservation,
                    SYSTEM_EMPLOYEE,
                    topBarController
            );
        }

        List<RoomWithReservation> allRoomWithReservation =
                RoomWithReservationDAO.getRoomWithReservation();

        for (RoomWithReservation roomWithReservation : allRoomWithReservation) {
            checkAndUpdateRoomStatus(
                    roomWithReservation,
                    SYSTEM_EMPLOYEE,
                    topBarController
            );
        }
    }

    public static void checkAndUpdateRoomStatus(
            RoomWithReservation roomWithReservation,
            Employee employee,
            NotificationButtonController topBarController
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

                topBarController.getInfo(GlobalMessage.AUTO_CHECKOUT, "Phòng " + room.getRoomID() + " đã được tự động checkout do quá hạn quá 2h");
                notifiedOverdueRooms.remove(room);
            } else {
                RoomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.OVERDUE);
                room.setRoomStatus(RoomStatus.OVERDUE);

                if (!notifiedOverdueRooms.contains(room)) {
                    notifiedOverdueRooms.add(room);
                    topBarController.getInfo(
                            GlobalMessage.AUTO_CHANGE_TO_OVERDUE,
                            "Phòng " + room.getRoomID() + " đã quá hạn sử dụng.\nHãy liên hệ với khách hàng để chuẩn bị thủ tục checkout"
                    );
                }
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

    public static void handleCheckoutEarly(RoomWithReservation roomWithReservation, Employee employee) {
        try {
            double roomCharge = Calculator.calculateRoomCharge(
                    roomWithReservation.getRoom(),
                    roomWithReservation.getReservationForm().getCheckInDate(),
                    LocalDateTime.now()
            );
            double serviceCharge = Calculator.calculateTotalServiceCharge(
                    roomWithReservation.getReservationForm().getReservationID()
            );

            InvoiceDAO.roomCheckingOutEarly(
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