package iuh.fit.dao;

import iuh.fit.models.HotelService;
import iuh.fit.models.RoomUsageService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
        try (
                Connection connection = DBHelper.getConnection();

                // Câu lệnh để thêm dữ liệu vào RoomUsageService
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO RoomUsageService(" +
                                "roomUsageServiceId, quantity, unitPrice, hotelServiceId, reservationFormID" +
                                ") VALUES(?, ?, ?, ?, ?)"
                );

                // Câu lệnh để lấy nextID từ GlobalSequence
                PreparedStatement selectSequenceStatement = connection.prepareStatement(
                        "SELECT nextID FROM GlobalSequence WHERE tableName = ?"
                );

                // Câu lệnh để cập nhật nextID trong GlobalSequence
                PreparedStatement updateSequenceStatement = connection.prepareStatement(
                        "UPDATE GlobalSequence SET nextID = ? WHERE tableName = ?"
                )
        ) {
            // Lấy nextID hiện tại cho RoomUsageService từ GlobalSequence
            selectSequenceStatement.setString(1, "RoomUsageService");
            ResultSet rs = selectSequenceStatement.executeQuery();

            String newRoomUsageServiceID = "RUS-000001"; // ID mặc định nếu không có trong DB
            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.ROOMUSAGESERVICE_PREFIX + "-"; // Tiền tố cho RoomUsageService ID

                // Tách phần số từ nextID và tăng thêm 1
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;

                // Định dạng lại phần số, đảm bảo luôn có 6 chữ số
                newRoomUsageServiceID = prefix + String.format("%06d", nextIDNum);

                // Cập nhật nextID mới trong GlobalSequence
                updateSequenceStatement.setString(1, currentNextID);
                updateSequenceStatement.setString(2, "RoomUsageService");
                updateSequenceStatement.executeUpdate();
            }

            // Thiết lập các giá trị cho câu lệnh INSERT
            insertStatement.setString(1, newRoomUsageServiceID);
            insertStatement.setInt(2, roomUsageService.getQuantity());
            insertStatement.setDouble(3, roomUsageService.getUnitPrice());
            insertStatement.setString(4, roomUsageService.getHotelService().getServiceId());
            insertStatement.setString(5, roomUsageService.getReservationForm().getReservationID());

            insertStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static String getNextRoomUsageServiceID() {
        String sql = "SELECT nextID FROM GlobalSequence WHERE tableName = 'RoomUsageService'";
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)
        ) {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "RUS-000001";
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
