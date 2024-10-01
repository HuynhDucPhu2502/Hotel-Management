package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Pricing;
import iuh.fit.models.RoomCategory;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomCategoryDAO {
    public static List<RoomCategory> getRoomCategory() {
        ArrayList<RoomCategory> data = new ArrayList<RoomCategory>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT roomCategoryID, roomCategoryName, numberOfBed " +
                    "FROM RoomCategory";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                RoomCategory roomCategory = new RoomCategory();

                roomCategory.setRoomCategoryid(rs.getString(1));
                roomCategory.setRoomCategoryName(rs.getString(2));
                roomCategory.setNumberOfBed(rs.getInt(3));

                data.add(roomCategory);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static RoomCategory getDataByID(String roomCategoryID) {

        String SQLQueryStatement = "SELECT roomCategoryID, roomCategoryName, numberOfBed "
                + "FROM RoomCategory " +
                "WHERE roomCategoryID = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, roomCategoryID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    RoomCategory roomCategory = new RoomCategory();
                    roomCategory.setRoomCategoryid(rs.getString(1));
                    roomCategory.setRoomCategoryName(rs.getString(2));
                    roomCategory.setNumberOfBed(rs.getInt(3));

                    return roomCategory;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(RoomCategory roomCategory) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO RoomCategory(roomCategoryID, roomCategoryName, numberOfBed) " +
                                "VALUES(?, ?, ?)"
                )
        ){
            preparedStatement.setString(1, roomCategory.getRoomCategoryid());
            preparedStatement.setString(2, roomCategory.getRoomCategoryName());
            preparedStatement.setInt(3, roomCategory.getNumberOfBed());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String roomCategoryID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM RoomCategory "
                                + "WHERE roomCategoryID = ?"
                )
        ){
            preparedStatement.setString(1, roomCategoryID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void updateData(RoomCategory roomCategory) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE RoomCategory " +
                                "SET roomCategoryName = ?, numberOfBed = ? " +
                                "WHERE roomCategoryID = ? "
                );
        ){
            preparedStatement.setString(1, roomCategory.getRoomCategoryName());
            preparedStatement.setInt(2, roomCategory.getNumberOfBed());
            preparedStatement.setString(3, roomCategory.getRoomCategoryid());
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }
}
