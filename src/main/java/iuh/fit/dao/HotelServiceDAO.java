package iuh.fit.dao;

import iuh.fit.models.HotelService;
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

    public static HotelService getDataByID(String hotelServiceId) {

        String SQLQueryStatement = "SELECT a.hotelServiceId, a.serviceName, a.description, a.servicePrice, a.serviceCategoryID, b.serviceCategoryName " +
                "FROM HotelService a inner join ServiceCategory b on a.serviceCategoryID = b.serviceCategoryID " +
                "WHERE hotelServiceId = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, hotelServiceId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    HotelService hotelService = new HotelService();
                    ServiceCategory serviceCategory = new ServiceCategory();

                    hotelService.setServiceId(rs.getString(1));
                    hotelService.setServiceName(rs.getString(2));
                    hotelService.setDescription(rs.getString(3));
                    hotelService.setServicePrice(rs.getDouble(4));

                    serviceCategory.setServiceCategoryID(rs.getString(5));
                    serviceCategory.setServiceCategoryName(rs.getString(6));

                    hotelService.setServiceCategory(serviceCategory);

                    return hotelService;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(HotelService hotelService) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO HotelService(hotelServiceId, serviceName, description, servicePrice, serviceCategoryID) " +
                                "VALUES(?, ?, ?, ?, ?)"
                )
        ){
            preparedStatement.setString(1, hotelService.getServiceId());
            preparedStatement.setString(2, hotelService.getServiceName());
            preparedStatement.setString(3, hotelService.getDescription());
            preparedStatement.setDouble(4, hotelService.getServicePrice());
            preparedStatement.setString(5, hotelService.getServiceCategory().getServiceCategoryID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String hotelServiceId) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM HotelService "
                                + "WHERE hotelServiceID = ?"
                )
        ){
            preparedStatement.setString(1, hotelServiceId);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void updateData(HotelService hotelService) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE HotelService " +
                                "SET serviceName = ?, description = ?, servicePrice = ?, serviceCategoryID = ? " +
                                "WHERE hotelServiceID = ? "
                );
        ){
            preparedStatement.setString(1, hotelService.getServiceName());
            preparedStatement.setString(2, hotelService.getDescription());
            preparedStatement.setDouble(3, hotelService.getServicePrice());
            preparedStatement.setString(4, hotelService.getServiceCategory().getServiceCategoryID());
            preparedStatement.setString(5, hotelService.getServiceId());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }
}
