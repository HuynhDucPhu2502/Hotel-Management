package iuh.fit.dao;

import iuh.fit.models.HotelService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HotelServiceDAO {
    public static List<HotelService> getHotelService() {
        ArrayList<HotelService> data = new ArrayList<HotelService>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT * " +
                    "FROM HotelService";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                HotelService hotelService = new HotelService();


                hotelService.setServiceId(rs.getString(1));
                hotelService.setServiceName(rs.getString(2));
                hotelService.setDescription(rs.getString(3));
                hotelService.setServicePrice(rs.getDouble(4));

                String serviceCategoryId = rs.getString(5);

                ServiceCategory serviceCategory = new ServiceCategory(serviceCategoryId);

                hotelService.setServiceCategory(serviceCategory);

                data.add(hotelService);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
