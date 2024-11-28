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

public class RoomStatusHelper {

    private static final ScheduledExecutorService SCHEDULER =
            Executors.newScheduledThreadPool(1);
    public static final Employee SYSTEM_EMPLOYEE =
            EmployeeDAO.getEmployeeByEmployeeID("EMP-000000");

    public static void startAutoCheckoutScheduler() {
        SCHEDULER.scheduleAtFixedRate(
                RoomStatusHelper::autoCheckoutOverdueRooms,
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

    private static void handleCheckOut(RoomWithReservation roomWithReservation, Employee employee) {
        try {
            HistoryCheckOut historyCheckOut = new HistoryCheckOut();
            historyCheckOut.setHistoryCheckOutID(HistoryCheckOutDAO.getNextID());
            historyCheckOut.setCheckOutDate(LocalDateTime.now());
            historyCheckOut.setReservationForm(roomWithReservation.getReservationForm());
            historyCheckOut.setEmployee(employee);

            HistoryCheckOutDAO.createData(historyCheckOut);

            Invoice invoice = new Invoice();

            invoice.setInvoiceID(InvoiceDAO.getNextInvoiceID());
            invoice.setInvoiceDate(LocalDateTime.now());
            invoice.setRoomCharge(Calculator.calculateRoomCharge(
                    roomWithReservation.getRoom(),
                    roomWithReservation.getReservationForm().getCheckInDate(),
                    roomWithReservation.getReservationForm().getCheckOutDate()
            ));
            invoice.setServicesCharge(Calculator.calculateTotalServiceCharge(
                    roomWithReservation.getReservationForm().getReservationID()
            ));
            invoice.setReservationForm(roomWithReservation.getReservationForm());

            InvoiceDAO.createData(invoice);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




}