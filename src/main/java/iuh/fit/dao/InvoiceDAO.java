package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Invoice;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Tax;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    public static List<Invoice> getInvoice() {
        ArrayList<Invoice> data = new ArrayList<Invoice>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "select * from Invoice";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                Invoice invoice = new Invoice();

                invoice.setInvoiceID(rs.getString(1));
                invoice.setInvoiceDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(2)));
                invoice.setRoomCharge(rs.getDouble(3));
                invoice.setServicesCharge(rs.getDouble(4));
                invoice.setTotalDue(rs.getDouble(5));
                invoice.setNetDue(rs.getDouble(6));
                invoice.setTax(TaxDAO.getDataByID(rs.getString(7)));
                invoice.setReservationForm(ReservationFormDAO.getDataByID(rs.getString(8)));

                data.add(invoice);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
