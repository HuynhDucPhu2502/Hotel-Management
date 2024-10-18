package iuh.fit.dao;

import iuh.fit.models.HotelService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.DBHelper;

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
                    "a.servicePrice, a.serviceCategoryID, b.serviceCategoryName " +
                    "FROM HotelService a LEFT JOIN ServiceCategory b " +
                    "ON a.serviceCategoryID = b.serviceCategoryID";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                HotelService hotelService = new HotelService();

                hotelService.setServiceId(rs.getString(1));
                hotelService.setServiceName(rs.getString(2));
                hotelService.setDescription(rs.getString(3));
                hotelService.setServicePrice(rs.getDouble(4));

                String serviceCategoryID = rs.getString(5);
                String serviceCategoryName = rs.getString(6);

                if (serviceCategoryID != null) {
                    ServiceCategory serviceCategory = new ServiceCategory();
                    serviceCategory.setServiceCategoryID(serviceCategoryID);
                    serviceCategory.setServiceCategoryName(serviceCategoryName);
                    hotelService.setServiceCategory(serviceCategory);
                } else {
                    hotelService.setServiceCategory(null);
                }

                data.add(hotelService);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static HotelService getDataByID(String hotelServiceId) {

        String SQLQueryStatement = "SELECT a.hotelServiceId, a.serviceName, a.description, " +
                "a.servicePrice, a.serviceCategoryID, b.serviceCategoryName " +
                "FROM HotelService a inner join ServiceCategory b " +
                "ON a.serviceCategoryID = b.serviceCategoryID " +
                "WHERE hotelServiceId = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, hotelServiceId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                HotelService hotelService = new HotelService();

                hotelService.setServiceId(rs.getString(1));
                hotelService.setServiceName(rs.getString(2));
                hotelService.setDescription(rs.getString(3));
                hotelService.setServicePrice(rs.getDouble(4));

                String serviceCategoryID = rs.getString(5);
                String serviceCategoryName = rs.getString(6);

                if (serviceCategoryID != null) {
                    ServiceCategory serviceCategory = new ServiceCategory();
                    serviceCategory.setServiceCategoryID(serviceCategoryID);
                    serviceCategory.setServiceCategoryName(serviceCategoryName);
                    hotelService.setServiceCategory(serviceCategory);
                } else {
                    hotelService.setServiceCategory(null);
                }

                return hotelService;
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
                        "INSERT INTO HotelService(hotelServiceID, serviceName, description, servicePrice, serviceCategoryID) " +
                                "VALUES(?, ?, ?, ?, ?)"
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
                String prefix = "HS-";  // prefix cho HotelService

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
                                "a.servicePrice, a.serviceCategoryID, b.serviceCategoryName " +
                                "FROM HotelService a LEFT JOIN ServiceCategory b " +
                                "ON a.serviceCategoryID = b.serviceCategoryID " +
                                "WHERE LOWER(hotelServiceID) LIKE ?"
                )
        ) {
            preparedStatement.setString(1, "%" + input + "%");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                HotelService hotelService = new HotelService();

                hotelService.setServiceId(rs.getString(1));
                hotelService.setServiceName(rs.getString(2));
                hotelService.setDescription(rs.getString(3));
                hotelService.setServicePrice(rs.getDouble(4));

                String serviceCategoryID = rs.getString(5);
                String serviceCategoryName = rs.getString(6);

                if (serviceCategoryID != null) {
                    ServiceCategory serviceCategory = new ServiceCategory();
                    serviceCategory.setServiceCategoryID(serviceCategoryID);
                    serviceCategory.setServiceCategoryName(serviceCategoryName);
                    hotelService.setServiceCategory(serviceCategory);
                } else {
                    hotelService.setServiceCategory(null);
                }

                data.add(hotelService);
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
                "a.servicePrice, a.serviceCategoryID, b.serviceCategoryName " +
                "FROM HotelService a " +
                "LEFT JOIN ServiceCategory b ON a.serviceCategoryID = b.serviceCategoryID " +
                "WHERE (a.hotelServiceID LIKE ? OR ? IS NULL) AND " +
                "(a.serviceName LIKE ? OR ? IS NULL) AND " +
                "(a.servicePrice >= ? OR ? IS NULL) AND " +
                "(a.servicePrice <= ? OR ? IS NULL) AND " +
                "((? = 'ALL') OR (a.serviceCategoryID = ? OR (? = 'NULL' AND a.serviceCategoryID IS NULL)))";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, "%" + hotelServiceID + "%");
            preparedStatement.setString(2, hotelServiceID);
            preparedStatement.setString(3, "%" + serviceName + "%");
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
                HotelService hotelService = new HotelService();
                hotelService.setServiceId(rs.getString("hotelServiceID"));
                hotelService.setServiceName(rs.getString("serviceName"));
                hotelService.setDescription(rs.getString("description"));
                hotelService.setServicePrice(rs.getDouble("servicePrice"));

                String categoryId = rs.getString("serviceCategoryID");
                String categoryName = rs.getString("serviceCategoryName");

                if (categoryId != null) {
                    ServiceCategory category = new ServiceCategory();
                    category.setServiceCategoryID(categoryId);
                    category.setServiceCategoryName(categoryName);
                    hotelService.setServiceCategory(category);
                } else {
                    hotelService.setServiceCategory(null);
                }

                data.add(hotelService);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return data;
    }






}
