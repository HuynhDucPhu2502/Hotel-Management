package iuh.fit.dao;

import iuh.fit.models.wrapper.InvoiceDisplayOnTable;
import iuh.fit.models.wrapper.UsingRoomDetailDisplayOnTable;
import iuh.fit.models.wrapper.UsingRoomDisplayOnTable;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UsingRoomDisplayOnTableDAO {
    public static List<UsingRoomDisplayOnTable> getData(){
        List<UsingRoomDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "select r.roomID, c.fullName , e.fullName, i.invoiceDate, rs.roomBookingDeposit, i.servicesCharge, i.roomCharge, t.taxRate, i.netDue\n" +
                "from Invoice i join ReservationForm rs on i.reservationFormID = rs.reservationFormID\n" +
                "join Customer c on c.customerID = rs.customerID\n" +
                "join Employee e on e.employeeID = rs.employeeID\n" +
                "join Room r on r.roomID = rs.roomID\n" +
                "join Tax t on t.taxID = i.taxID";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                UsingRoomDisplayOnTable usingRoomDisplayOnTable = new UsingRoomDisplayOnTable();

                usingRoomDisplayOnTable.setRoomID(rs.getString(1));
                usingRoomDisplayOnTable.setCusName(rs.getString(2));
                usingRoomDisplayOnTable.setEmpName(rs.getString(3));
                usingRoomDisplayOnTable.setCreateDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                usingRoomDisplayOnTable.setDeposit(rs.getDouble(5));
                usingRoomDisplayOnTable.setServiceCharge(rs.getDouble(6));
                usingRoomDisplayOnTable.setRoomCharge(rs.getDouble(7));
                usingRoomDisplayOnTable.setTax(rs.getDouble(8));
                usingRoomDisplayOnTable.setNetDue(rs.getDouble(9));

                data.add(usingRoomDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static List<UsingRoomDetailDisplayOnTable> getDataDetail(){
        List<UsingRoomDetailDisplayOnTable> data = new ArrayList<UsingRoomDetailDisplayOnTable>();
        String SqlQuery = "select r.roomID, COUNT(r.roomID) as roomCount, SUM(i.netDue) as totalNetDue\n" +
                "from Invoice i\n" +
                "join ReservationForm rs on i.reservationFormID = rs.reservationFormID\n" +
                "join Customer c on c.customerID = rs.customerID\n" +
                "join Employee e on e.employeeID = rs.employeeID\n" +
                "join Room r on r.roomID = rs.roomID\n" +
                "join Tax t on t.taxID = i.taxID\n" +
                "group by r.roomID";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                UsingRoomDetailDisplayOnTable usingRoomDisplayOnTable = new UsingRoomDetailDisplayOnTable();

                usingRoomDisplayOnTable.setRoomID(rs.getString(1));
                usingRoomDisplayOnTable.setTimesUsing(rs.getInt(2));
                usingRoomDisplayOnTable.setNetDue(rs.getDouble(3));

                data.add(usingRoomDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
