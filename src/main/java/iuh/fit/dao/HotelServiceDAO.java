package iuh.fit.dao;

import iuh.fit.models.HotelService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HotelServiceDAO {
    public static List<HotelService> getHotelService() {
        ArrayList<HotelService> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ) {
            String sql = "SELECT a.hotelServiceId, a.serviceName, a.description, " +
                    "a.servicePrice, a.serviceCategoryID, b.serviceCategoryName, b.icon " +
                    "FROM HotelService a LEFT JOIN ServiceCategory b " +
                    "ON a.serviceCategoryID = b.serviceCategoryID " +
                    "WHERE a.isActivate = 'ACTIVATE'";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                data.add(extractData(rs));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static HotelService getDataByID(String hotelServiceId) {
        String SQLQueryStatement = "SELECT a.hotelServiceId, a.serviceName, a.description, " +
                "a.servicePrice, a.serviceCategoryID, b.serviceCategoryName, b.icon " +
                "FROM HotelService a LEFT JOIN ServiceCategory b " +
                "ON a.serviceCategoryID = b.serviceCategoryID " +
                "WHERE a.hotelServiceId = ? AND a.isActivate = 'ACTIVATE'";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, hotelServiceId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return extractData(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(HotelService hotelService) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO HotelService(hotelServiceID, serviceName, description, servicePrice, serviceCategoryID, isActivate) " +
                                "VALUES(?, ?, ?, ?, ?, 'ACTIVATE')"
                );

                // Câu lệnh để lấy giá trị nextID từ bảng GlobalSequence
                PreparedStatement selectSequenceStatement = connection.prepareStatement(
                        "SELECT nextID FROM GlobalSequence WHERE tableName = ?"
                );
                // Câu lệnh để cập nhật giá trị nextID trong bảng GlobalSequence
                PreparedStatement updateSequenceStatement = connection.prepareStatement(
                        "UPDATE GlobalSequence SET nextID = ? WHERE tableName = ?"
                )
        ) {
            insertStatement.setString(1, hotelService.getServiceId());
            insertStatement.setString(2, hotelService.getServiceName());
            insertStatement.setString(3, hotelService.getDescription());
            insertStatement.setDouble(4, hotelService.getServicePrice());
            insertStatement.setString(5, hotelService.getServiceCategory().getServiceCategoryID());
            insertStatement.executeUpdate();

            // Lấy giá trị nextID hiện tại từ bảng GlobalSequence cho HotelService
            selectSequenceStatement.setString(1, "HotelService");
            ResultSet rs = selectSequenceStatement.executeQuery();

            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.HOTELSERVICE_PREFIX + "-";

                // Tách phần số và tăng thêm 1
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;

                // Định dạng lại phần số, đảm bảo luôn có 6 chữ số
                String newNextID = prefix + String.format("%06d", nextIDNum);

                // Cập nhật giá trị nextID trong bảng GlobalSequence
                updateSequenceStatement.setString(1, newNextID);
                updateSequenceStatement.setString(2, "HotelService");
                updateSequenceStatement.executeUpdate();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String hotelServiceId) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE HotelService " +
                                "SET isActivate = 'DEACTIVATE' "
                                + "WHERE hotelServiceID = ?"
                )
        ){
            preparedStatement.setString(1, hotelServiceId);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void updateData(HotelService hotelService) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE HotelService " +
                                "SET serviceName = ?, description = ?, servicePrice = ?, serviceCategoryID = ? " +
                                "WHERE hotelServiceID = ?  AND isActivate = 'ACTIVATE'"
                )
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

    public static List<HotelService> findDataByContainsId(String input) {
        ArrayList<HotelService> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT a.hotelServiceId, a.serviceName, a.description, " +
                                "a.servicePrice, a.serviceCategoryID, b.serviceCategoryName, b.icon " +
                                "FROM HotelService a LEFT JOIN ServiceCategory b " +
                                "ON a.serviceCategoryID = b.serviceCategoryID " +
                                "WHERE LOWER(a.hotelServiceID) LIKE ? AND a.isActivate = 'ACTIVATE'"
                )
        ) {
            preparedStatement.setString(1, "%" + input + "%");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                data.add(extractData(rs));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static List<String> getTopThreeID() {
        ArrayList<String> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ) {
            String sql = "SELECT TOP 3 hotelServiceId " +
                    "FROM HotelService " +
                    "WHERE  isActivate = 'ACTIVATE' " +
                    "ORDER BY hotelServiceID DESC";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                data.add(rs.getString(1));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static String getNextHotelServiceID() {
        String res = "HS-000001";

        try (

                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT nextID " +
                                "FROM GlobalSequence " +
                                "WHERE tableName = ?"
                )
        ){
            preparedStatement.setString(1, "HotelService");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                res = rs.getString(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return res;
    }

    public static List<HotelService> searchHotelServices(
            String hotelServiceID, String serviceName,
            Double minPrice, Double maxPrice, String serviceCategory) {

        List<HotelService> data = new ArrayList<>();

        String sql = "SELECT a.hotelServiceID, a.serviceName, a.description, " +
                "a.servicePrice, a.serviceCategoryID, b.serviceCategoryName, b.icon " +
                "FROM HotelService a " +
                "LEFT JOIN ServiceCategory b ON a.serviceCategoryID = b.serviceCategoryID " +
                "WHERE (a.hotelServiceID LIKE ? OR ? IS NULL) AND " +
                "(a.serviceName LIKE ? OR ? IS NULL) AND " +
                "(a.servicePrice >= ? OR ? IS NULL) AND " +
                "(a.servicePrice <= ? OR ? IS NULL) AND " +
                "((? = 'ALL') OR (a.serviceCategoryID = ? OR (? = 'NULL' AND a.serviceCategoryID IS NULL))) " +
                "AND a.isActivate = 'ACTIVATE'";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, hotelServiceID != null ? "%" + hotelServiceID + "%" : null);
            preparedStatement.setString(2, hotelServiceID);
            preparedStatement.setString(3, serviceName != null ? "%" + serviceName + "%" : null);
            preparedStatement.setString(4, serviceName);
            preparedStatement.setObject(5, minPrice);
            preparedStatement.setObject(6, minPrice);
            preparedStatement.setObject(7, maxPrice);
            preparedStatement.setObject(8, maxPrice);

            preparedStatement.setString(9, serviceCategory);
            preparedStatement.setString(10, serviceCategory);
            preparedStatement.setString(11, serviceCategory);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                data.add(extractData(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static boolean isHotelServiceInUse(String hotelServiceId) {
        String sql =
            """
            SELECT 1
            FROM HotelService hs
            JOIN RoomUsageService rus ON hs.hotelServiceId = rus.hotelServiceId
            JOIN ReservationForm rf ON rf.reservationFormID = rus.reservationFormID
            JOIN Room r ON r.roomID = rf.roomID
            WHERE hs.hotelServiceId = ?
            AND r.roomStatus IN ('ON_USE', 'OVERDUE')
            """;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, hotelServiceId);

            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }



    private static HotelService extractData(ResultSet rs) throws Exception {
        HotelService hotelService = new HotelService();
        hotelService.setServiceId(rs.getString("hotelServiceId"));
        hotelService.setServiceName(rs.getString("serviceName"));
        hotelService.setDescription(rs.getString("description"));
        hotelService.setServicePrice(rs.getDouble("servicePrice"));

        String serviceCategoryID = rs.getString("serviceCategoryID");
        String serviceCategoryName = rs.getString("serviceCategoryName");
        String icon = rs.getString("icon");

        if (serviceCategoryID != null) {
            ServiceCategory serviceCategory = new ServiceCategory();
            serviceCategory.setServiceCategoryID(serviceCategoryID);
            serviceCategory.setServiceCategoryName(serviceCategoryName);
            serviceCategory.setIcon(icon);
            hotelService.setServiceCategory(serviceCategory);
        } else {
            hotelService.setServiceCategory(null);
        }

        return hotelService;
    }




}
