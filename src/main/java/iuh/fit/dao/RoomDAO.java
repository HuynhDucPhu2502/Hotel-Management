package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Pricing;
import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
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

                roomCategory.setRoomCategoryid(rs.getString(4));
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
}
