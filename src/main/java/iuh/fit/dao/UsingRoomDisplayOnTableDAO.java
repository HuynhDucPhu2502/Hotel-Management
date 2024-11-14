package iuh.fit.dao;

import iuh.fit.models.wrapper.UsingRoomDetailDisplayOnTable;
import iuh.fit.models.wrapper.UsingRoomDisplayOnTable;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UsingRoomDisplayOnTableDAO {
    public static List<UsingRoomDisplayOnTable> getData(){
        List<UsingRoomDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "select rc.roomCategoryID, rc.roomCategoryName, r.roomID, c.fullName , e.fullName, i.invoiceDate, rs.roomBookingDeposit, i.servicesCharge, i.roomCharge, i.netDue\n" +
                "from Invoice i join ReservationForm rs on i.reservationFormID = rs.reservationFormID\n" +
                "join Customer c on c.customerID = rs.customerID\n" +
                "join Employee e on e.employeeID = rs.employeeID\n" +
                "join Room r on r.roomID = rs.roomID\n" +
                "join RoomCategory rc on rc.roomCategoryID = r.roomCategoryID";
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                UsingRoomDisplayOnTable usingRoomDisplayOnTable = new UsingRoomDisplayOnTable();

                usingRoomDisplayOnTable.setRoomCategoryID(rs.getString(1));
                usingRoomDisplayOnTable.setNameRoomCategory(rs.getString(2));
                usingRoomDisplayOnTable.setRoomID(rs.getString(3));
                usingRoomDisplayOnTable.setCusName(rs.getString(4));
                usingRoomDisplayOnTable.setEmpName(rs.getString(5));
                usingRoomDisplayOnTable.setCreateDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(6)));
                usingRoomDisplayOnTable.setDeposit(rs.getDouble(7));
                usingRoomDisplayOnTable.setServiceCharge(rs.getDouble(8));
                usingRoomDisplayOnTable.setRoomCharge(rs.getDouble(9));
                usingRoomDisplayOnTable.setNetDue(rs.getDouble(10));

                data.add(usingRoomDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        data.sort(Comparator.comparing(UsingRoomDisplayOnTable::getRoomID)
                .thenComparing(UsingRoomDisplayOnTable::getCreateDate));

        return data;
    }

    public static List<UsingRoomDisplayOnTable> getDataByYear(int year) {
        List<UsingRoomDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "select rc.roomCategoryID, rc.roomCategoryName, r.roomID, c.fullName , e.fullName, i.invoiceDate, rs.roomBookingDeposit, i.servicesCharge, i.roomCharge, i.netDue\n" +
                "from Invoice i join ReservationForm rs on i.reservationFormID = rs.reservationFormID\n" +
                "join Customer c on c.customerID = rs.customerID\n" +
                "join Employee e on e.employeeID = rs.employeeID\n" +
                "join Room r on r.roomID = rs.roomID\n" +
                "join RoomCategory rc on rc.roomCategoryID = r.roomCategoryID " +
                "WHERE YEAR(i.invoiceDate) = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery)
        ) {
            // Set the year parameter
            preparedStatement.setInt(1, year);

            // Execute the query and process the result set
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    UsingRoomDisplayOnTable usingRoomDisplayOnTable = new UsingRoomDisplayOnTable();

                    usingRoomDisplayOnTable.setRoomCategoryID(rs.getString(1));
                    usingRoomDisplayOnTable.setNameRoomCategory(rs.getString(2));
                    usingRoomDisplayOnTable.setRoomID(rs.getString(3));
                    usingRoomDisplayOnTable.setCusName(rs.getString(4));
                    usingRoomDisplayOnTable.setEmpName(rs.getString(5));
                    usingRoomDisplayOnTable.setCreateDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(6)));
                    usingRoomDisplayOnTable.setDeposit(rs.getDouble(7));
                    usingRoomDisplayOnTable.setServiceCharge(rs.getDouble(8));
                    usingRoomDisplayOnTable.setRoomCharge(rs.getDouble(9));
                    usingRoomDisplayOnTable.setNetDue(rs.getDouble(10));

                    data.add(usingRoomDisplayOnTable);
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            // Thay thế System.exit(1) bằng một cách xử lý lỗi tốt hơn
        }

        // Sắp xếp dữ liệu theo roomID
        data.sort(Comparator.comparing(UsingRoomDisplayOnTable::getRoomID)
                .thenComparing(UsingRoomDisplayOnTable::getCreateDate));

        return data;
    }


    public static List<UsingRoomDisplayOnTable> getDataByDateRange(LocalDateTime beginDate, LocalDateTime endDate){
        List<UsingRoomDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "select rc.roomCategoryID, rc.roomCategoryName, r.roomID, c.fullName , e.fullName, i.invoiceDate, rs.roomBookingDeposit, i.servicesCharge, i.roomCharge, i.netDue\n" +
                "from Invoice i join ReservationForm rs on i.reservationFormID = rs.reservationFormID\n" +
                "join Customer c on c.customerID = rs.customerID\n" +
                "join Employee e on e.employeeID = rs.employeeID\n" +
                "join Room r on r.roomID = rs.roomID\n" +
                "join RoomCategory rc on rc.roomCategoryID = r.roomCategoryID " +
                "WHERE i.invoiceDate >= ? AND i.invoiceDate <= ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery)
        ) {
            preparedStatement.setTimestamp(1, ConvertHelper.localDateTimeToSQLConverter(beginDate));
            preparedStatement.setTimestamp(2, ConvertHelper.localDateTimeToSQLConverter(endDate));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    UsingRoomDisplayOnTable usingRoomDisplayOnTable = new UsingRoomDisplayOnTable();

                    usingRoomDisplayOnTable.setRoomCategoryID(rs.getString(1));
                    usingRoomDisplayOnTable.setNameRoomCategory(rs.getString(2));
                    usingRoomDisplayOnTable.setRoomID(rs.getString(3));
                    usingRoomDisplayOnTable.setCusName(rs.getString(4));
                    usingRoomDisplayOnTable.setEmpName(rs.getString(5));
                    usingRoomDisplayOnTable.setCreateDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(6)));
                    usingRoomDisplayOnTable.setDeposit(rs.getDouble(7));
                    usingRoomDisplayOnTable.setServiceCharge(rs.getDouble(8));
                    usingRoomDisplayOnTable.setRoomCharge(rs.getDouble(9));
                    usingRoomDisplayOnTable.setNetDue(rs.getDouble(10));

                    data.add(usingRoomDisplayOnTable);
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        // Sort data by roomID
        data.sort(Comparator.comparing(UsingRoomDisplayOnTable::getRoomID)
                .thenComparing(UsingRoomDisplayOnTable::getCreateDate));

        return data;
    }


    public static List<UsingRoomDetailDisplayOnTable> getDataDetail(){
        List<UsingRoomDetailDisplayOnTable> data = new ArrayList<UsingRoomDetailDisplayOnTable>();
        String SqlQuery = "SELECT rc.roomCategoryID, rc.roomCategoryName, " +
                "COUNT(r.roomID) AS timesUsing, " +
                "SUM(i.netDue) AS totalNetDue, " +
                "(COUNT(r.roomID) * 100.0 / SUM(COUNT(r.roomID)) OVER()) AS percentUsing, " +
                "(SUM(i.netDue) * 100.0 / SUM(SUM(i.netDue)) OVER()) AS percentNetDue " +
                "FROM Invoice i " +
                "JOIN ReservationForm rs ON i.reservationFormID = rs.reservationFormID " +
                "JOIN Room r ON r.roomID = rs.roomID " +
                "JOIN RoomCategory rc ON rc.roomCategoryID = r.roomCategoryID " +
                "GROUP BY rc.roomCategoryID, rc.roomCategoryName;";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                UsingRoomDetailDisplayOnTable usingRoomDisplayOnTable = new UsingRoomDetailDisplayOnTable();

                usingRoomDisplayOnTable.setRoomCategoryID(rs.getString(1));
                usingRoomDisplayOnTable.setNameRoomCategory(rs.getString(2));
                usingRoomDisplayOnTable.setTimesUsing(rs.getInt(3));
                usingRoomDisplayOnTable.setNetDue(rs.getDouble(4));
                usingRoomDisplayOnTable.setPercentUsing(rs.getFloat(5));
                usingRoomDisplayOnTable.setPercentNetDue(rs.getFloat(6));

                data.add(usingRoomDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        data.sort(Comparator.comparing(UsingRoomDetailDisplayOnTable::getRoomCategoryID));

        return data;
    }

    public static List<UsingRoomDetailDisplayOnTable> getDataDetailByYear(int year) {
        List<UsingRoomDetailDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT rc.roomCategoryID, rc.roomCategoryName, " +
                "COUNT(r.roomID) AS timesUsing, " +
                "SUM(i.netDue) AS totalNetDue, " +
                "(COUNT(r.roomID) * 100.0 / (SELECT COUNT(r2.roomID) FROM Invoice i2 " +
                "JOIN ReservationForm rs2 ON i2.reservationFormID = rs2.reservationFormID " +
                "JOIN Room r2 ON r2.roomID = rs2.roomID " +
                "JOIN RoomCategory rc2 ON rc2.roomCategoryID = r2.roomCategoryID " +
                "WHERE YEAR(i2.invoiceDate) = ?)) AS percentUsing, " +
                "(SUM(i.netDue) * 100.0 / (SELECT SUM(i2.netDue) FROM Invoice i2 WHERE YEAR(i2.invoiceDate) = ?)) AS percentNetDue " +
                "FROM Invoice i " +
                "JOIN ReservationForm rs ON i.reservationFormID = rs.reservationFormID " +
                "JOIN Room r ON r.roomID = rs.roomID " +
                "JOIN RoomCategory rc ON rc.roomCategoryID = r.roomCategoryID " +
                "WHERE YEAR(i.invoiceDate) = ? " +
                "GROUP BY rc.roomCategoryID, rc.roomCategoryName;";


        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery)
        ) {
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, year);
            preparedStatement.setInt(3, year);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    UsingRoomDetailDisplayOnTable usingRoomDisplayOnTable = new UsingRoomDetailDisplayOnTable();

                    usingRoomDisplayOnTable.setRoomCategoryID(rs.getString(1));
                    usingRoomDisplayOnTable.setNameRoomCategory(rs.getString(2));
                    usingRoomDisplayOnTable.setTimesUsing(rs.getInt(3));
                    usingRoomDisplayOnTable.setNetDue(rs.getDouble(4));
                    usingRoomDisplayOnTable.setPercentUsing(rs.getFloat(5));
                    usingRoomDisplayOnTable.setPercentNetDue(rs.getFloat(6));

                    data.add(usingRoomDisplayOnTable);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        data.sort(Comparator.comparing(UsingRoomDetailDisplayOnTable::getRoomCategoryID));

        return data;
    }


    public static List<UsingRoomDetailDisplayOnTable> getDataDetailDateRange(LocalDateTime beginDate, LocalDateTime endDate) {
        List<UsingRoomDetailDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT rc.roomCategoryID, rc.roomCategoryName, COUNT(r.roomID) AS roomCount, SUM(i.netDue) AS totalNetDue, " +
                "(COUNT(r.roomID) * 100.0 / (SELECT COUNT(*) FROM Invoice i2 " +
                "JOIN ReservationForm rs2 ON i2.reservationFormID = rs2.reservationFormID " +
                "JOIN Room r2 ON r2.roomID = rs2.roomID " +
                "JOIN RoomCategory rc2 ON rc2.roomCategoryID = r2.roomCategoryID " +
                "WHERE i2.invoiceDate >= ? AND i2.invoiceDate <= ?)) AS percentUsing, " +
                "(SUM(i.netDue) * 100.0 / (SELECT SUM(i2.netDue) FROM Invoice i2 WHERE i2.invoiceDate >= ? AND i2.invoiceDate <= ?)) AS percentNetDue " +
                "FROM Invoice i " +
                "JOIN ReservationForm rs ON i.reservationFormID = rs.reservationFormID " +
                "JOIN Room r ON r.roomID = rs.roomID " +
                "JOIN RoomCategory rc ON rc.roomCategoryID = r.roomCategoryID " +
                "WHERE i.invoiceDate >= ? AND i.invoiceDate <= ? " +
                "GROUP BY rc.roomCategoryID, rc.roomCategoryName;";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SqlQuery)
        ) {
            preparedStatement.setTimestamp(1, ConvertHelper.localDateTimeToSQLConverter(beginDate));
            preparedStatement.setTimestamp(2, ConvertHelper.localDateTimeToSQLConverter(endDate));
            preparedStatement.setTimestamp(3, ConvertHelper.localDateTimeToSQLConverter(beginDate));
            preparedStatement.setTimestamp(4, ConvertHelper.localDateTimeToSQLConverter(endDate));
            preparedStatement.setTimestamp(5, ConvertHelper.localDateTimeToSQLConverter(beginDate));
            preparedStatement.setTimestamp(6, ConvertHelper.localDateTimeToSQLConverter(endDate));

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    UsingRoomDetailDisplayOnTable usingRoomDisplayOnTable = new UsingRoomDetailDisplayOnTable();

                    usingRoomDisplayOnTable.setRoomCategoryID(rs.getString(1));
                    usingRoomDisplayOnTable.setNameRoomCategory(rs.getString(2));
                    usingRoomDisplayOnTable.setTimesUsing(rs.getInt(3));
                    usingRoomDisplayOnTable.setNetDue(rs.getDouble(4));
                    usingRoomDisplayOnTable.setPercentUsing(rs.getFloat(5));
                    usingRoomDisplayOnTable.setPercentNetDue(rs.getFloat(6));

                    data.add(usingRoomDisplayOnTable);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        data.sort(Comparator.comparing(UsingRoomDetailDisplayOnTable::getRoomCategoryID));

        return data;
    }

}
