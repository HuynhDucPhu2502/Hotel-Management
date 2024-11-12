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
        String SqlQuery =
                """
                select r.roomID, c.fullName , e.fullName, i.invoiceDate, rs.roomBookingDeposit, i.servicesCharge, i.roomCharge, t.taxRate, i.netDue
                from Invoice i join ReservationForm rs on i.reservationFormID = rs.reservationFormID
                join Customer c on c.customerID = rs.customerID
                join Employee e on e.employeeID = rs.employeeID
                join Room r on r.roomID = rs.roomID
                join Tax t on t.taxID = i.
                """;

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
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

        data.sort(Comparator.comparing(UsingRoomDisplayOnTable::getRoomID)
                .thenComparing(UsingRoomDisplayOnTable::getCreateDate));

        return data;
    }

    public static List<UsingRoomDisplayOnTable> getDataByYear(int year) {
        List<UsingRoomDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT r.roomID, c.fullName, e.fullName, i.invoiceDate, rs.roomBookingDeposit, i.servicesCharge, i.roomCharge, t.taxRate, i.netDue " +
                "FROM Invoice i " +
                "JOIN ReservationForm rs ON i.reservationFormID = rs.reservationFormID " +
                "JOIN Customer c ON c.customerID = rs.customerID " +
                "JOIN Employee e ON e.employeeID = rs.employeeID " +
                "JOIN Room r ON r.roomID = rs.roomID " +
                "JOIN Tax t ON t.taxID = i.taxID " +
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
        String SqlQuery = "SELECT r.roomID, c.fullName, e.fullName, i.invoiceDate, rs.roomBookingDeposit, i.servicesCharge, i.roomCharge, t.taxRate, i.netDue " +
                "FROM Invoice i " +
                "JOIN ReservationForm rs ON i.reservationFormID = rs.reservationFormID " +
                "JOIN Customer c ON c.customerID = rs.customerID " +
                "JOIN Employee e ON e.employeeID = rs.employeeID " +
                "JOIN Room r ON r.roomID = rs.roomID " +
                "JOIN Tax t ON t.taxID = i.taxID " +
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
        List<UsingRoomDetailDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT r.roomID, COUNT(r.roomID) AS roomCount, SUM(i.netDue) AS totalNetDue, " +
                "(COUNT(r.roomID) * 100.0 / SUM(COUNT(r.roomID)) OVER()) AS percentUsing, " +
                "(SUM(i.netDue) * 100.0 / SUM(SUM(i.netDue)) OVER()) AS percentNetDue " +
                "FROM Invoice i JOIN ReservationForm rs ON i.reservationFormID = rs.reservationFormID " +
                "JOIN Customer c ON c.customerID = rs.customerID " +
                "JOIN Employee e ON e.employeeID = rs.employeeID " +
                "JOIN Room r ON r.roomID = rs.roomID " +
                "JOIN Tax t ON t.taxID = i.taxID " +
                "GROUP BY r.roomID;";
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ){
            ResultSet rs = statement.executeQuery(SqlQuery);


            while (rs.next()) {
                UsingRoomDetailDisplayOnTable usingRoomDisplayOnTable = new UsingRoomDetailDisplayOnTable();

                usingRoomDisplayOnTable.setRoomID(rs.getString(1));
                usingRoomDisplayOnTable.setTimesUsing(rs.getInt(2));
                usingRoomDisplayOnTable.setNetDue(rs.getDouble(3));
                usingRoomDisplayOnTable.setPercentUsing(rs.getFloat(4));
                usingRoomDisplayOnTable.setPercentNetDue(rs.getFloat(5));

                data.add(usingRoomDisplayOnTable);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        data.sort(Comparator.comparing(UsingRoomDetailDisplayOnTable::getRoomID));

        return data;
    }

    public static List<UsingRoomDetailDisplayOnTable> getDataDetailByYear(int year) {
        List<UsingRoomDetailDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT r.roomID, " +
                "COUNT(r.roomID) AS roomCount, " +
                "SUM(i.netDue) AS totalNetDue, " +
                "(COUNT(r.roomID) * 100.0 / (SELECT COUNT(*) FROM Invoice WHERE YEAR(invoiceDate) = ?)) AS percentUsing, " +
                "(SUM(i.netDue) * 100.0 / (SELECT SUM(i2.netDue) FROM Invoice i2 WHERE YEAR(i2.invoiceDate) = ?)) AS percentNetDue " +
                "FROM Invoice i " +
                "JOIN ReservationForm rs ON i.reservationFormID = rs.reservationFormID " +
                "JOIN Customer c ON c.customerID = rs.customerID " +
                "JOIN Employee e ON e.employeeID = rs.employeeID " +
                "JOIN Room r ON r.roomID = rs.roomID " +
                "JOIN Tax t ON t.taxID = i.taxID " +
                "WHERE YEAR(i.invoiceDate) = ? " +
                "GROUP BY r.roomID";

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

                    usingRoomDisplayOnTable.setRoomID(rs.getString(1));
                    usingRoomDisplayOnTable.setTimesUsing(rs.getInt(2));
                    usingRoomDisplayOnTable.setNetDue(rs.getDouble(3));
                    usingRoomDisplayOnTable.setPercentUsing(rs.getFloat(4));
                    usingRoomDisplayOnTable.setPercentNetDue(rs.getFloat(5));

                    data.add(usingRoomDisplayOnTable);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        data.sort(Comparator.comparing(UsingRoomDetailDisplayOnTable::getRoomID));

        return data;
    }


    public static List<UsingRoomDetailDisplayOnTable> getDataDetailDateRange(LocalDateTime beginDate, LocalDateTime endDate) {
        List<UsingRoomDetailDisplayOnTable> data = new ArrayList<>();
        String SqlQuery = "SELECT r.roomID, COUNT(r.roomID) AS roomCount, SUM(i.netDue) AS totalNetDue, " +
                "(COUNT(r.roomID) * 100.0 / (SELECT COUNT(*) FROM Invoice WHERE invoiceDate >= ? AND invoiceDate <= ?)) AS percentUsing, " +
                "(SUM(i.netDue) * 100.0 / (SELECT SUM(i2.netDue) FROM Invoice i2 WHERE i2.invoiceDate >= ? AND i2.invoiceDate <= ?)) AS percentNetDue " +
                "FROM Invoice i " +
                "JOIN ReservationForm rs ON i.reservationFormID = rs.reservationFormID " +
                "JOIN Customer c ON c.customerID = rs.customerID " +
                "JOIN Employee e ON e.employeeID = rs.employeeID " +
                "JOIN Room r ON r.roomID = rs.roomID " +
                "JOIN Tax t ON t.taxID = i.taxID " +
                "WHERE i.invoiceDate >= ? AND i.invoiceDate <= ? " +
                "GROUP BY r.roomID";

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

                    usingRoomDisplayOnTable.setRoomID(rs.getString(1));
                    usingRoomDisplayOnTable.setTimesUsing(rs.getInt(2));
                    usingRoomDisplayOnTable.setNetDue(rs.getDouble(3));
                    usingRoomDisplayOnTable.setPercentUsing(rs.getFloat(4));
                    usingRoomDisplayOnTable.setPercentNetDue(rs.getFloat(5));

                    data.add(usingRoomDisplayOnTable);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        data.sort(Comparator.comparing(UsingRoomDetailDisplayOnTable::getRoomID));

        return data;
    }

}
