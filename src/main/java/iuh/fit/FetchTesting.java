package iuh.fit;

import iuh.fit.dao.RoomDisplayOnTableDAO;
import iuh.fit.dao.ServiceDisplayOnTableDAO;
import iuh.fit.models.enums.Month;
import iuh.fit.security.PasswordHashing;
import iuh.fit.utils.PropertiesFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;

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
//        String month = Month.valueOf(Arrays.stream(Month.values()).toList().get(0).toString()).getName();
//        System.out.println(month);
        //System.out.println(RoomDisplayOnTableDAO.getData());
        String key = PasswordHashing.hashPassword("admin");
        String value = PasswordHashing.hashPassword("admin123");
        String nameFile = PasswordHashing.hashPassword("password") + ".properties";

        PropertiesFile.writeFile(nameFile, key, value, null);
        System.out.println(PropertiesFile
                .readFile(nameFile, key) + value);
    }
}
