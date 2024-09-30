package iuh.fit.dao;

import iuh.fit.models.HotelService;
import iuh.fit.models.RoomUsageService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public static RoomUsageService getDataByID(String roomUsageServiceId) {

        String SQLQueryStatement = "SELECT a.roomUsageServiceId, a.quantity, a.hotelServiceId, " +
                "b.serviceName, b.description, b.servicePrice, b.serviceCategoryID, " +
                "c.serviceCategoryName " +
                "FROM RoomUsageService a join HotelService b on a.hotelServiceId = b.hotelServiceId " +
                " join ServiceCategory c on b.serviceCategoryID = c.serviceCategoryID " +
                "WHERE pricingID = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, roomUsageServiceId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
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

                    return roomUsageService;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(RoomUsageService roomUsageService) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO RoomUsageService(roomUsageServiceId, quantity, hotelServiceId) " +
                                "VALUES(?, ?, ?)"
                )
        ){
            preparedStatement.setString(1, roomUsageService.getRoomUsageServiceId());
            preparedStatement.setInt(2, roomUsageService.getQuantity());
            preparedStatement.setString(3, roomUsageService.getHotelService().getServiceId());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String roomUsageServiceId) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM RoomUsageService "
                                + "WHERE roomUsageServiceId = ?"
                )
        ){
            preparedStatement.setString(1, roomUsageServiceId);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void updateData(RoomUsageService roomUsageService) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE RoomUsageService " +
                                "SET quantity = ?, hotelServiceId = ? " +
                                "WHERE roomUsageServiceID = ? "
                );
        ){
            preparedStatement.setInt(1, roomUsageService.getQuantity());
            preparedStatement.setString(2, roomUsageService.getHotelService().getServiceId());
            preparedStatement.setString(3, roomUsageService.getRoomUsageServiceId());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }
}
