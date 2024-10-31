package iuh.fit.dao;

import iuh.fit.models.HotelService;
import iuh.fit.models.RoomUsageService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomUsageServiceDAO {

    public static List<RoomUsageService> getRoomUsageServices() {
        List<RoomUsageService> data = new ArrayList<>();
        String sql = """
            SELECT a.roomUsageServiceId, a.quantity, a.unitPrice,
                   a.hotelServiceId, b.serviceName, b.description,
                   b.servicePrice, b.serviceCategoryID, c.serviceCategoryName 
            FROM RoomUsageService a 
            JOIN HotelService b ON a.hotelServiceId = b.hotelServiceId 
            JOIN ServiceCategory c ON b.serviceCategoryID = c.serviceCategoryID
            """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                RoomUsageService roomUsageService = mapResultSetToRoomUsageService(rs);
                data.add(roomUsageService);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        return data;
    }

    public static RoomUsageService getDataByID(String roomUsageServiceId) {
        String sql = """
            SELECT a.roomUsageServiceId, a.quantity, a.unitPrice, a.hotelServiceId, 
                   b.serviceName, b.description, b.servicePrice, b.serviceCategoryID, 
                   c.serviceCategoryName 
            FROM RoomUsageService a 
            JOIN HotelService b ON a.hotelServiceId = b.hotelServiceId 
            JOIN ServiceCategory c ON b.serviceCategoryID = c.serviceCategoryID 
            WHERE a.roomUsageServiceId = ?
            """;

        try (Connection con = DBHelper.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setString(1, roomUsageServiceId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRoomUsageService(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<RoomUsageService> getByReservationFormID(String reservationFormID) {
        List<RoomUsageService> data = new ArrayList<>();
        String sql = """
            SELECT a.roomUsageServiceId, a.quantity, a.unitPrice, a.hotelServiceId,
                   b.serviceName, b.description, b.servicePrice, b.serviceCategoryID,
                   c.serviceCategoryName
            FROM RoomUsageService a
            JOIN HotelService b ON a.hotelServiceId = b.hotelServiceId
            JOIN ServiceCategory c ON b.serviceCategoryID = c.serviceCategoryID
            WHERE a.reservationFormID = ?
            """;

        try (Connection con = DBHelper.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(sql)) {

            preparedStatement.setString(1, reservationFormID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    RoomUsageService roomUsageService = mapResultSetToRoomUsageService(rs);
                    data.add(roomUsageService);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void createData(RoomUsageService roomUsageService) {
        String sql = "INSERT INTO RoomUsageService(roomUsageServiceId, quantity, unitPrice, hotelServiceId, reservationFormID) VALUES(?, ?, ?, ?, ?)";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, roomUsageService.getRoomUsageServiceId());
            preparedStatement.setInt(2, roomUsageService.getQuantity());
            preparedStatement.setDouble(3, roomUsageService.getUnitPrice());
            preparedStatement.setString(4, roomUsageService.getHotelService().getServiceId());
            preparedStatement.setString(5, roomUsageService.getReservationForm().getReservationID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String roomUsageServiceId) {
        String sql = "DELETE FROM RoomUsageService WHERE roomUsageServiceId = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, roomUsageServiceId);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void updateData(RoomUsageService roomUsageService) {
        String sql = "UPDATE RoomUsageService SET quantity = ?, unitPrice = ?, hotelServiceId = ? WHERE roomUsageServiceId = ?";
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, roomUsageService.getQuantity());
            preparedStatement.setDouble(2, roomUsageService.getUnitPrice());  // Update unitPrice
            preparedStatement.setString(3, roomUsageService.getHotelService().getServiceId());
            preparedStatement.setString(4, roomUsageService.getRoomUsageServiceId());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    private static RoomUsageService mapResultSetToRoomUsageService(ResultSet rs) throws Exception {
        RoomUsageService roomUsageService = new RoomUsageService();
        HotelService hotelService = new HotelService();
        ServiceCategory serviceCategory = new ServiceCategory();

        roomUsageService.setRoomUsageServiceId(rs.getString("roomUsageServiceId"));
        roomUsageService.setQuantity(rs.getInt("quantity"));
        roomUsageService.setUnitPrice(rs.getDouble("unitPrice"));  // Đơn giá

        hotelService.setServiceId(rs.getString("hotelServiceId"));
        hotelService.setServiceName(rs.getString("serviceName"));
        hotelService.setDescription(rs.getString("description"));
        hotelService.setServicePrice(rs.getDouble("servicePrice"));

        serviceCategory.setServiceCategoryID(rs.getString("serviceCategoryID"));
        serviceCategory.setServiceCategoryName(rs.getString("serviceCategoryName"));

        hotelService.setServiceCategory(serviceCategory);
        roomUsageService.setHotelService(hotelService);


        return roomUsageService;
    }
}
