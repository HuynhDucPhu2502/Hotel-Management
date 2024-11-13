package iuh.fit.dao;

import iuh.fit.models.wrapper.InvoiceDisplayOnTable;
import iuh.fit.models.wrapper.RoomDisplayOnTable;
import iuh.fit.models.wrapper.ServiceDisplayOnTable;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomDisplayOnTableDAO {

    public static List<RoomDisplayOnTable> getAllData(){
        List<RoomDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT rs.roomID, rc.roomCategoryName, rs.reservationDate, rs.checkInDate, rs.checkOutDate, i.roomCharge\n" +
                "FROM ReservationForm rs \n" +
                "JOIN Invoice i ON rs.reservationFormID = i.reservationFormID\n" +
                "JOIN Room r ON rs.roomID = r.roomID\n" +
                "JOIN RoomCategory rc ON r.roomCategoryID = rc.roomCategoryID " +
                "WHERE r.isActivate = 'ACTIVATE' AND rc.isActivate = 'ACTIVATE'";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                RoomDisplayOnTable roomDisplayOnTable = new RoomDisplayOnTable();

                roomDisplayOnTable.setRoomID(rs.getString(1));
                roomDisplayOnTable.setRoomCategory(rs.getString(2));
                roomDisplayOnTable.setNumOfPeople(0);
                roomDisplayOnTable.setBookingDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(3)));
                roomDisplayOnTable.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                roomDisplayOnTable.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                roomDisplayOnTable.setTotalMoney(rs.getDouble(6));
                data.add(roomDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static List<RoomDisplayOnTable> getDataThreeYearsLatest(){
        List<RoomDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT rs.roomID, rc.roomCategoryName, rs.reservationDate, rs.checkInDate, rs.checkOutDate, i.roomCharge\n" +
                "FROM ReservationForm rs \n" +
                "JOIN Invoice i ON rs.reservationFormID = i.reservationFormID\n" +
                "JOIN Room r ON rs.roomID = r.roomID\n" +
                "JOIN RoomCategory rc ON r.roomCategoryID = rc.roomCategoryID " +
                "WHERE r.isActivate = 'ACTIVATE' AND rc.isActivate = 'ACTIVATE' \n" +
                "AND DATEPART(YEAR, i.invoiceDate) >= DATEPART(YEAR, GETDATE()) - 2 ";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                RoomDisplayOnTable roomDisplayOnTable = new RoomDisplayOnTable();

                roomDisplayOnTable.setRoomID(rs.getString(1));
                roomDisplayOnTable.setRoomCategory(rs.getString(2));
                roomDisplayOnTable.setNumOfPeople(0);
                roomDisplayOnTable.setBookingDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(3)));
                roomDisplayOnTable.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                roomDisplayOnTable.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                roomDisplayOnTable.setTotalMoney(rs.getDouble(6));
                data.add(roomDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
