package iuh.fit.dao;

import iuh.fit.models.HistoryCheckIn;
import iuh.fit.models.RoomUsage;
import iuh.fit.utils.DBHelper;

import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomUsageDAO {

    public static List<RoomUsage> getData(){
        List<RoomUsage> roomUsageList = new ArrayList<>();
        String SQLStatement = "SELECT * FROM RoomUsage";

        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
                )
        {
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                String roomUsageID = resultSet.getString(1);
                double totalServiceCharge = resultSet.getDouble(2);
                double roomCharge = resultSet.getDouble(3);
                String historyCheckInID = resultSet.getString(4);

                HistoryCheckIn historyCheckIn = new HistoryCheckIn(historyCheckInID);
                RoomUsage roomUsage = new RoomUsage(roomUsageID, totalServiceCharge, roomCharge, historyCheckIn);
                roomUsageList.add(roomUsage);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return roomUsageList;
    }

    public static boolean insertData(RoomUsage roomUsage){
        int n = 0;
        String SQLStatement = "INSERT INTO RoomUsage(roomUsageID, totalServiceCharge, roomCharge, historyCheckInID)\n" +
                "VALUES \n" +
                "\t(?, ?, ?, ?)";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
                )
        {
            preparedStatement.setString(1, roomUsage.getRoomUsageID());
            preparedStatement.setDouble(2, roomUsage.getTotalServiceCharge());
            preparedStatement.setDouble(3, roomUsage.getRoomCharge());
            preparedStatement.setString(4, roomUsage.getHistoryCheckIn().getHistoryCheckInID());

            n = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    public static boolean deleteDate(String roomUsageID){
        int n = 0;

        String SQLStatement = "DELETE FROM RoomUsage\n" +
                "WHERE roomUsageID = ?";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
                )
        {
            preparedStatement.setString(1, roomUsageID);
            n = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    public static boolean updateData(RoomUsage newData){
        int n = 0;

        String SQLStatement = "UPDATE RoomUsage\n" +
                "SET totalServiceCharge = ?, roomCharge = ?\n" +
                "WHERE roomUsageID = ?";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
                )
        {
            preparedStatement.setDouble(1, newData.getTotalServiceCharge());
            preparedStatement.setDouble(2, newData.getRoomCharge());
            preparedStatement.setString(3, newData.getRoomUsageID());

            n = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return n > 0;
    }

    public static RoomUsage getDataByID(String id){
        String SQLStatement = "SELECT * FROM RoomUsage WHERE roomUsageID = ?";
        try(
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLStatement);
                )
        {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String roomUsageID = resultSet.getString(1);
                double totalServiceCharge = resultSet.getDouble(2);
                double roomCharge = resultSet.getDouble(3);
                String historyCheckInID = resultSet.getString(4);

                HistoryCheckIn historyCheckIn = new HistoryCheckIn(historyCheckInID);
                return new RoomUsage(roomUsageID, totalServiceCharge, roomCharge, historyCheckIn);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
