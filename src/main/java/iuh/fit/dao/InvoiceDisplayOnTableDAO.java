package iuh.fit.dao;

import iuh.fit.models.wrapper.InvoiceDisplayOnTable;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDisplayOnTableDAO {
    public static List<InvoiceDisplayOnTable> getAllData(){
        List<InvoiceDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "select i.invoiceID, c.fullName, r.roomID, e.fullName, i.invoiceDate, rs.roomBookingDeposit, i.servicesCharge, i.roomCharge, i.netDue\n" +
                "from Invoice i join ReservationForm rs on i.reservationFormID = rs.reservationFormID\n" +
                "join Customer c on c.customerID = rs.customerID\n" +
                "join Employee e on e.employeeID = rs.employeeID\n" +
                "join Room r on r.roomID = rs.roomID ";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                InvoiceDisplayOnTable invoiceDisplayOnTable = new InvoiceDisplayOnTable();

                invoiceDisplayOnTable.setInvoiceID(rs.getString(1));
                invoiceDisplayOnTable.setCusName(rs.getString(2));
                invoiceDisplayOnTable.setRoomID(rs.getString(3));
                invoiceDisplayOnTable.setEmpName(rs.getString(4));
                invoiceDisplayOnTable.setCreateDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                invoiceDisplayOnTable.setDeposit(rs.getDouble(6));
                invoiceDisplayOnTable.setServiceCharge(rs.getDouble(7));
                invoiceDisplayOnTable.setRoomCharge(rs.getDouble(8));
                invoiceDisplayOnTable.setNetDue(rs.getDouble(9));

                data.add(invoiceDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static List<InvoiceDisplayOnTable> getDataThreeYearsLatest(){
        List<InvoiceDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "select i.invoiceID, c.fullName, r.roomID, e.fullName, i.invoiceDate, rs.roomBookingDeposit, i.servicesCharge, i.roomCharge, i.netDue\n" +
                "from Invoice i join ReservationForm rs on i.reservationFormID = rs.reservationFormID\n" +
                "join Customer c on c.customerID = rs.customerID\n" +
                "join Employee e on e.employeeID = rs.employeeID\n" +
                "join Room r on r.roomID = rs.roomID\n" +
                "WHERE DATEPART(YEAR, i.invoiceDate) >= DATEPART(YEAR, GETDATE()) - 2 ";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                InvoiceDisplayOnTable invoiceDisplayOnTable = new InvoiceDisplayOnTable();

                invoiceDisplayOnTable.setInvoiceID(rs.getString(1));
                invoiceDisplayOnTable.setCusName(rs.getString(2));
                invoiceDisplayOnTable.setRoomID(rs.getString(3));
                invoiceDisplayOnTable.setEmpName(rs.getString(4));
                invoiceDisplayOnTable.setCreateDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                invoiceDisplayOnTable.setDeposit(rs.getDouble(6));
                invoiceDisplayOnTable.setServiceCharge(rs.getDouble(7));
                invoiceDisplayOnTable.setRoomCharge(rs.getDouble(8));
                invoiceDisplayOnTable.setNetDue(rs.getDouble(9));

                data.add(invoiceDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
