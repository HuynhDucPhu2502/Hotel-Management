package iuh.fit;

import iuh.fit.models.enums.Month;

import java.time.LocalDateTime;

public class FetchTesting {
    public static void main(String[] args) {
//        List<ReservationForm> reservationForm = ReservationFormDAO.getReservationForm();
//        reservationForm.forEach(r -> {
//            System.out.println(r.getEmployee());
//            System.out.println(r.getCustomer());
//            System.out.println(r.getRoom());
//        });

//        List<Invoice> invoiceList = InvoiceDAO.getInvoice();
//        invoiceList.forEach(i -> {
//            System.out.println(new InvoiceDisplayOnTable(i, i.getReservationForm().getCustomer(),
//                    i.getReservationForm().getRoom(), i.getReservationForm().getEmployee()));
//        });

//        System.out.println(InvoiceDAO.getInvoice());

        //System.out.println(InvoiceDisplayOnTableDAO.getData());

        String month = LocalDateTime.now().getMonth().toString();
        System.out.println(Month.valueOf(month).getName());
    }
}
