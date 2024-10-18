package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    public static List<Room> getRoom() {
        ArrayList<Room> data = new ArrayList<Room>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT a.roomID, a.roomStatus, a.dateOfCreation, a.roomCategoryID, " +
                    "b.roomCategoryName, b.numberOfBed " +
                    "FROM Room a inner join RoomCategory b on a.roomCategoryID = b.roomCategoryID";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                Room room = new Room();
                RoomCategory roomCategory = new RoomCategory();

                room.setRoomID(rs.getString(1));
                room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString(2)));
                room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp(3)));

                roomCategory.setRoomCategoryID(rs.getString(4));
                roomCategory.setRoomCategoryName(rs.getString(5));
                roomCategory.setNumberOfBed(rs.getInt(6));

                room.setRoomCategory(roomCategory);

                data.add(room);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        return data;
    }

    public static Room getDataByID(String roomID) {

        String SQLQueryStatement = "SELECT a.roomID, a.roomStatus, a.dateOfCreation, a.roomCategoryID, " +
                "b.roomCategoryName, b.numberOfBed " +
                "FROM Room a inner join RoomCategory b on a.roomCategoryID = b.roomCategoryID " +
                "WHERE roomID = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, roomID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room();
                    RoomCategory roomCategory = new RoomCategory();

                    room.setRoomID(rs.getString(1));
                    room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString(2)));
                    room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp(3)));

                    roomCategory.setRoomCategoryID(rs.getString(4));
                    roomCategory.setRoomCategoryName(rs.getString(5));
                    roomCategory.setNumberOfBed(rs.getInt(6));

                    room.setRoomCategory(roomCategory);

                    return room;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(Room room) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Room(roomID, roomStatus, dateOfCreation, roomCategoryID) " +
                                "VALUES(?, ?, ?, ?)"
                )
        ){
            preparedStatement.setString(1, room.getRoomID());
            preparedStatement.setString(2, ConvertHelper.roomStatusConverterToSQL(room.getRoomStatus()));
            preparedStatement.setTimestamp(3, ConvertHelper.dateTimeConvertertoSQL(room.getDateOfCreation()));
            preparedStatement.setString(4, room.getRoomCategory().getRoomCategoryID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String roomID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Room "
                                + "WHERE roomID = ?"
                )
        ){
            preparedStatement.setString(1, roomID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void updateData(Room room) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Room " +
                                "SET roomStatus = ?, dateOfCreation = ?, roomCategoryID = ? " +
                                "WHERE roomID = ? "
                );
        ){
            preparedStatement.setString(1, ConvertHelper.roomStatusConverterToSQL(room.getRoomStatus()));
            preparedStatement.setTimestamp(2, ConvertHelper.dateTimeConvertertoSQL(room.getDateOfCreation()));
            preparedStatement.setString(3, room.getRoomCategory().getRoomCategoryID());
            preparedStatement.setString(4, room.getRoomID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }
}
