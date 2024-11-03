package iuh.fit.utils;

import iuh.fit.dao.HistoryCheckOutDAO;
import iuh.fit.dao.InvoiceDAO;
import iuh.fit.dao.RoomDAO;
import iuh.fit.dao.TaxDAO;
import iuh.fit.models.*;
import iuh.fit.models.enums.RoomStatus;
import iuh.fit.models.wrapper.RoomWithReservation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class RoomStatusHelper {

    public static void updateRoomStatusIfOverdue(Room room, ReservationForm
            reservationForm, RoomWithReservation roomWithReservation, Employee employee) {
        if (reservationForm == null || room == null) return;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime checkOutDate = reservationForm.getCheckOutDate();

        if (now.isAfter(checkOutDate)) {
            long hoursOverdue = ChronoUnit.HOURS.between(checkOutDate, now);

            if (hoursOverdue >= 2) {
                autoCheckoutRoom(room, roomWithReservation, employee);
            } else {
                RoomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.OVERDUE);
                room.setRoomStatus(RoomStatus.OVERDUE);
            }
        }
    }

    private static void autoCheckoutRoom(Room room, RoomWithReservation roomWithReservation, Employee employee) {
        handleCheckOut(roomWithReservation, employee);
        RoomDAO.updateRoomStatus(room.getRoomID(), RoomStatus.AVAILABLE);
        room.setRoomStatus(RoomStatus.AVAILABLE);
    }

    private static void handleCheckOut(RoomWithReservation roomWithReservation, Employee employee) {
        try {
            HistoryCheckOut historyCheckOut = new HistoryCheckOut();
            historyCheckOut.setHistoryCheckOutID(HistoryCheckOutDAO.getNextID());
            historyCheckOut.setCheckOutDate(LocalDateTime.now());
            historyCheckOut.setReservationForm(roomWithReservation.getReservationForm());
            historyCheckOut.setEmployee(employee);

            HistoryCheckOutDAO.createData(historyCheckOut);

            Tax tax = TaxDAO.getDataByID("tax-000001");

            Invoice invoice = new Invoice();
            invoice.setInvoiceID(InvoiceDAO.getNextInvoiceID());
            invoice.setInvoiceDate(LocalDateTime.now());

            double totalCharge = Calculator.calculateTotalCharge(roomWithReservation.getReservationForm());
            double totalDue = totalCharge * 0.9;
            double netDue = totalDue * (1 + Objects.requireNonNull(tax).getTaxRate());

            invoice.setRoomCharge(Calculator.calculateRoomCharge(
                    roomWithReservation.getRoom(),
                    roomWithReservation.getReservationForm().getCheckInDate(),
                    roomWithReservation.getReservationForm().getCheckOutDate()
            ));
            invoice.setServicesCharge(Calculator.calculateTotalServiceCharge(roomWithReservation.getReservationForm().getReservationID()));
            invoice.setTotalDue(totalDue);
            invoice.setNetDue(netDue);
            invoice.setTax(tax);
            invoice.setReservationForm(roomWithReservation.getReservationForm());

            InvoiceDAO.createData(invoice);

            RoomDAO.updateRoomStatus(roomWithReservation.getRoom().getRoomID(), RoomStatus.AVAILABLE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}