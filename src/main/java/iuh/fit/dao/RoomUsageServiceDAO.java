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

            String sql = "SELECT a.roomUsageServiceId, a.quantity, a.hotelServiceId, " +
                    "b.serviceName, b.description, b.servicePrice, b.serviceCategoryID, " +
                    "c.serviceCategoryName " +
                    "FROM RoomUsageService a join HotelService b on a.hotelServiceId = b.hotelServiceId " +
                    " join ServiceCategory c on b.serviceCategoryID = c.serviceCategoryID";

            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                RoomUsageService roomUsageService = new RoomUsageService();
                HotelService hotelService = new HotelService();
                ServiceCategory serviceCategory = new ServiceCategory();

                roomUsageService.setRoomUsageServiceId(rs.getString(1));
                roomUsageService.setQuantity(rs.getInt(2));

                hotelService.setServiceId(rs.getString(3));
                hotelService.setServiceName(rs.getString(4));
                hotelService.setDescription(rs.getString(5));
                hotelService.setServicePrice(rs.getDouble(6));

                serviceCategory.setServiceCategoryID(rs.getString(7));
                serviceCategory.setServiceCategoryName(rs.getString(8));

                hotelService.setServiceCategory(serviceCategory);
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
