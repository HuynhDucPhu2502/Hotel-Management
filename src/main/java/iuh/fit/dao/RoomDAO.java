package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    public static List<Room> getRoom() {
        ArrayList<Room> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
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
                )
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

    public static List<Room> searchRooms(
            String roomID, String roomStatus,
            LocalDateTime lowerBoundDate, LocalDateTime upperBoundDate,
            String roomCategoryID) {

        List<Room> data = new ArrayList<>();

        String sql = "SELECT a.roomID, a.roomStatus, a.dateOfCreation, a.roomCategoryID, " +
                "b.roomCategoryName, b.numberOfBed " +
                "FROM Room a " +
                "LEFT JOIN RoomCategory b ON a.roomCategoryID = b.roomCategoryID " +
                "WHERE (a.roomID LIKE ? OR ? IS NULL) " +
                "AND (a.roomStatus = ? OR ? IS NULL) " +
                "AND (a.dateOfCreation >= ? OR ? IS NULL) " +
                "AND (a.dateOfCreation <= ? OR ? IS NULL) " +
                "AND ((? = 'ALL') OR (a.roomCategoryID = ? OR (? = 'NULL' AND a.roomCategoryID IS NULL)))";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            // Thiết lập các tham số cho PreparedStatement
            preparedStatement.setString(1, "%" + roomID + "%");
            preparedStatement.setString(2, roomID);
            preparedStatement.setString(3, roomStatus);
            preparedStatement.setString(4, roomStatus);
            preparedStatement.setObject(5, lowerBoundDate);
            preparedStatement.setObject(6, lowerBoundDate);
            preparedStatement.setObject(7, upperBoundDate);
            preparedStatement.setObject(8, upperBoundDate);
            preparedStatement.setString(9, roomCategoryID);
            preparedStatement.setString(10, roomCategoryID);
            preparedStatement.setString(11, roomCategoryID);

            // Thực hiện truy vấn và lấy kết quả
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Room room = new Room();
                RoomCategory roomCategory = new RoomCategory();

                room.setRoomID(rs.getString("roomID"));
                room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString("roomStatus")));
                room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp("dateOfCreation")));

                roomCategory.setRoomCategoryID(rs.getString("roomCategoryID"));
                roomCategory.setRoomCategoryName(rs.getString("roomCategoryName"));
                roomCategory.setNumberOfBed(rs.getInt("numberOfBed"));

                room.setRoomCategory(roomCategory);
                data.add(room);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return data;
    }


}
