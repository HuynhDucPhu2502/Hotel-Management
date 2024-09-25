package iuh.fit.dao;

import iuh.fit.models.HotelService;
import iuh.fit.models.RoomUsageService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomUsageServiceDAO {
    public static List<RoomUsageService> getRoomUsageService() {
        ArrayList<RoomUsageService> data = new ArrayList<RoomUsageService>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT * " +
                    "FROM RoomUsageService";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                RoomUsageService roomUsageService = new RoomUsageService();

                roomUsageService.setRoomUsageServiceId(rs.getString(1));
                roomUsageService.setQuantity(rs.getInt(2));

                String hotelServiceId = rs.getString(3);
                HotelService hotelService = new HotelService(hotelServiceId);

                roomUsageService.setHotelService(hotelService);
                data.add(roomUsageService);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
