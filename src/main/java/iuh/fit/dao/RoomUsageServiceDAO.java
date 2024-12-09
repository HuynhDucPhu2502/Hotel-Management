package iuh.fit.dao;

import iuh.fit.models.Employee;
import iuh.fit.models.HotelService;
import iuh.fit.models.RoomUsageService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomUsageServiceDAO {
    public static List<RoomUsageService> getByReservationFormID(String reservationFormID) {
    List<RoomUsageService> data = new ArrayList<>();
    String sql = """
    SELECT a.roomUsageServiceId, a.quantity, a.unitPrice, a.dateAdded, a.hotelServiceId,
           b.serviceName, b.description, b.servicePrice, b.serviceCategoryID,
           c.serviceCategoryName, a.employeeID, e.fullName AS employeeName, e.phoneNumber AS employeePhone
    FROM RoomUsageService a
    JOIN HotelService b ON a.hotelServiceId = b.hotelServiceId
    JOIN ServiceCategory c ON b.serviceCategoryID = c.serviceCategoryID
    LEFT JOIN Employee e ON a.employeeID = e.employeeID
    WHERE a.reservationFormID = ?
    AND b.isActivate = 'ACTIVATE' AND c.isActivate = 'ACTIVATE' AND e.isActivate = 'ACTIVATE'
    """;

    try (Connection con = DBHelper.getConnection();
         PreparedStatement preparedStatement = con.prepareStatement(sql)) {

        preparedStatement.setString(1, reservationFormID);

        try (ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                RoomUsageService roomUsageService = extractData(rs);
                data.add(roomUsageService);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    return data;
}

    public static void serviceOrdering(RoomUsageService roomUsageService) {
        String callProcedure = "{CALL ServiceOrdering(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection connection = DBHelper.getConnection();
             CallableStatement callableStatement = connection.prepareCall(callProcedure)) {

            // Thiết lập tham số đầu vào cho Stored Procedure
            callableStatement.setInt(1, roomUsageService.getQuantity()); // quantity
            callableStatement.setBigDecimal(2, BigDecimal.valueOf(roomUsageService.getUnitPrice())); // unitPrice
            callableStatement.setString(3, roomUsageService.getHotelService().getServiceId()); // serviceID
            callableStatement.setString(4, roomUsageService.getReservationForm().getReservationID()); // reservationFormID
            callableStatement.setString(5, roomUsageService.getEmployee().getEmployeeID()); // employeeID
            callableStatement.setTimestamp(6, Timestamp.valueOf(roomUsageService.getDateAdded())); // dateAdded

            // Đăng ký tham số đầu ra cho message
            callableStatement.registerOutParameter(7, Types.VARCHAR);

            // Thực thi Stored Procedure
            callableStatement.execute();

            // Lấy thông báo từ Stored Procedure
            String message = callableStatement.getString(7);

            // Kiểm tra kết quả trả về từ Stored Procedure
            switch (message) {
                case "ROOM_SERVICE_ORDERING_RESERVATION_NOT_FOUND_OR_EXPIRED":
                    throw new IllegalArgumentException("Phiếu đặt phòng không tồn tại hoặc đã hết hạn.");
                case "SERVICE_ORDERING_INVALID_QUANTITY":
                    throw new IllegalArgumentException(ErrorMessages.SERVICE_ORDERING_INVALID_QUANTITY);
                case "SERVICE_ORDERING_SUCCESS":
                    incrementAndUpdateNextID();
                    break;
                default:
                    throw new IllegalArgumentException(ErrorMessages.STORE_PROCEDURE_ERROR);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void incrementAndUpdateNextID() {
        String selectQuery = "SELECT nextID FROM GlobalSequence WHERE tableName = 'RoomUsageService'";
        String updateQuery = "UPDATE GlobalSequence SET nextID = ? WHERE tableName = 'RoomUsageService'";
        String currentNextID;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                currentNextID = resultSet.getString("nextID");

                String prefix = GlobalConstants.ROOM_USAGE_SERVICE_PREFIX + "-";
                int numericPart = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
                String updatedNextID = prefix + String.format("%06d", numericPart);

                updateStatement.setString(1, updatedNextID);
                updateStatement.executeUpdate();

            } else {
                throw new IllegalArgumentException("Không tìm thấy bản ghi với tableName: RoomUsageService");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static RoomUsageService extractData(ResultSet rs) throws Exception {
            RoomUsageService roomUsageService = new RoomUsageService();
            HotelService hotelService = new HotelService();
            ServiceCategory serviceCategory = new ServiceCategory();
            Employee employee = new Employee();

            roomUsageService.setRoomUsageServiceId(rs.getString("roomUsageServiceId"));
            roomUsageService.setQuantity(rs.getInt("quantity"));
            roomUsageService.setUnitPrice(rs.getDouble("unitPrice"));
            roomUsageService.setDateAdded(rs.getTimestamp("dateAdded").toLocalDateTime());

            hotelService.setServiceId(rs.getString("hotelServiceId"));
            hotelService.setServiceName(rs.getString("serviceName"));
            hotelService.setDescription(rs.getString("description"));
            hotelService.setServicePrice(rs.getDouble("servicePrice"));

            serviceCategory.setServiceCategoryID(rs.getString("serviceCategoryID"));
            serviceCategory.setServiceCategoryName(rs.getString("serviceCategoryName"));

            employee.setEmployeeID(rs.getString("employeeID"));
            employee.setFullName(rs.getString("employeeName"));
            employee.setPhoneNumber(rs.getString("employeePhone"));

            hotelService.setServiceCategory(serviceCategory);
            roomUsageService.setHotelService(hotelService);
            roomUsageService.setEmployee(employee);

            return roomUsageService;
        }

}
