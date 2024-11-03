package iuh.fit.dao;

import iuh.fit.models.wrapper.InvoiceDisplayOnTable;
import iuh.fit.models.wrapper.ServiceDisplayOnTable;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceDisplayOnTableDAO {
    public static List<ServiceDisplayOnTable> getData(){
        List<ServiceDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT rs.hotelServiceId, h.serviceName, sc.serviceCategoryName, e.fullName, rs.dateAdded, rs.quantity, rs.unitPrice\n" +
                "FROM ReservationForm r join RoomUsageService rs ON r.reservationFormID = rs.reservationFormID\n" +
                "join HotelService h ON rs.hotelServiceId = h.hotelServiceId\n" +
                "join ServiceCategory sc ON h.serviceCategoryID = sc.serviceCategoryID\n" +
                "join Employee e ON rs.employeeID = e.employeeID ";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                ServiceDisplayOnTable serviceDisplayOnTable = new ServiceDisplayOnTable();

                serviceDisplayOnTable.setServiceId(rs.getString(1));
                serviceDisplayOnTable.setServiceName(rs.getString(2));
                serviceDisplayOnTable.setServiceCategory(rs.getString(3));
                serviceDisplayOnTable.setEmployeeName(rs.getString(4));
                serviceDisplayOnTable.setDateAdded(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                serviceDisplayOnTable.setQuantity(rs.getInt(6));
                serviceDisplayOnTable.setUnitPrice(rs.getDouble(7));
                double totalMoney = rs.getInt(6) * rs.getDouble(7);
                serviceDisplayOnTable.setTotalMoney(totalMoney);
                data.add(serviceDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
