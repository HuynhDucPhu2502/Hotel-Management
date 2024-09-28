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
            String sql = "SELECT a.hotelServiceId, a.serviceName, a.description, a.servicePrice, a.serviceCategoryID, b.serviceCategoryName " +
                    "FROM HotelService a inner join ServiceCategory b on a.serviceCategoryID = b.serviceCategoryID";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                HotelService hotelService = new HotelService();
                ServiceCategory serviceCategory = new ServiceCategory();

                hotelService.setServiceId(rs.getString(1));
                hotelService.setServiceName(rs.getString(2));
                hotelService.setDescription(rs.getString(3));
                hotelService.setServicePrice(rs.getDouble(4));

                serviceCategory.setServiceCategoryID(rs.getString(5));
                serviceCategory.setServiceCategoryName(rs.getString(6));

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
