package iuh.fit.utils;

import iuh.fit.controller.features.NotificationButtonController;
import iuh.fit.dao.*;
import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    private static NotificationButtonController topBarController;
    private static final Set<Room> notifiedOverdueRooms = new HashSet<>();

    public static void setupController(NotificationButtonController controller){
        topBarController = controller;
    }

    public static void startAutoCheckoutScheduler(NotificationButtonController topBarController) {
        System.out.println("Setup thanh cong topBarController cho RoomManagerService o startAutoCheckout");
        SCHEDULER.scheduleAtFixedRate(
                () -> RoomManagementService.autoCheckoutOverdueRooms(topBarController),
                0,
                5,
                TimeUnit.SECONDS
        );
        System.out.println("KET QUA NOTIFYCONTROLLER (7): "+topBarController);
    }

    public static void autoCheckoutOverdueRooms(NotificationButtonController topBarController) {
        System.out.println("Setup thanh cong topBarController cho RoomManagerService o autoCheckout");
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
        System.out.println("Func autocheck dang hoat dong(3.1)");
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
                // Xử lý tự động checkout
                handleCheckOut(roomWithReservation, employee);
                room.setRoomStatus(RoomStatus.AVAILABLE);
                RoomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.AVAILABLE);
                System.out.println("\nCHECKOUT, phòng: " + room.getRoomID() + "\n");
                topBarController.getInfo(GlobalMessage.AUTO_CHECKOUT, "Phòng " + room.getRoomID() + " đã được tự động checkout do quá hạn quá 2h");

                // Xóa phòng khỏi danh sách đã thông báo
                notifiedOverdueRooms.remove(room);
            } else {
                // Chuyển trạng thái sang OVERDUE
                RoomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.OVERDUE);
                room.setRoomStatus(RoomStatus.OVERDUE);

                if (!notifiedOverdueRooms.contains(room)) {
                    // Hiện thông báo lần đầu tiên
                    notifiedOverdueRooms.add(room);
                    System.out.println("\nOVERDUE, phòng: " + room.getRoomID() + "\n");
                    topBarController.getInfo(GlobalMessage.AUTO_CHANGE_TO_OVERDUE, "Phòng " + room.getRoomID() + " đã quá hạn sử dụng.\nHãy liên hệ với khách hàng để chuẩn bị thủ tục checkout");
                } else {
                    System.out.println("Phòng " + room.getRoomID() + " đã được thông báo trạng thái Overdue trước đó.");
                }
            }
        }
        System.out.println("Func autocheck đang hoạt động (5)");
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