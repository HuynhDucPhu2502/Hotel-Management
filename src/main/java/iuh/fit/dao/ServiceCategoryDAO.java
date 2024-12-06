package iuh.fit.dao;

import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategoryDAO {
    public static List<ServiceCategory> getServiceCategory() {
        ArrayList<ServiceCategory> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ){
            String sql = "SELECT serviceCategoryID, serviceCategoryName, icon " +
                    "FROM ServiceCategory " +
                    "WHERE isActivate = 'ACTIVATE'";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                ServiceCategory serviceCategory = new ServiceCategory();
                serviceCategory.setServiceCategoryID(rs.getString(1));
                serviceCategory.setServiceCategoryName(rs.getString(2));
                serviceCategory.setIcon(rs.getString(3));

                data.add(serviceCategory);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static boolean isServiceCategoryInUse(String serviceCategoryId) {
        String sql =
                """
                SELECT 1
                FROM HotelService hs
                JOIN RoomUsageService rus ON hs.hotelServiceId = rus.hotelServiceId
                JOIN ReservationForm rf ON rf.reservationFormID = rus.reservationFormID
                JOIN Room r ON r.roomID = rf.roomID
                WHERE hs.serviceCategoryID = ?
                AND r.roomStatus IN ('ON_USE', 'OVERDUE')
                """;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, serviceCategoryId);

            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ServiceCategory getDataByID(String serviceCategoryID) {

        String SQLQueryStatement = "SELECT serviceCategoryID, serviceCategoryName, icon "
                + "FROM ServiceCategory where serviceCategoryID = ? AND isActivate = 'ACTIVATE'";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, serviceCategoryID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    ServiceCategory serviceCategory = new ServiceCategory();
                    serviceCategory.setServiceCategoryID(rs.getString(1));
                    serviceCategory.setServiceCategoryName(rs.getString(2));
                    serviceCategory.setIcon(rs.getString(3));

                    return serviceCategory;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(ServiceCategory serviceCategory) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO ServiceCategory(serviceCategoryID, serviceCategoryName, icon, isActivate) " +
                                "VALUES(?, ?, ?, 'ACTIVATE')"
                );
                PreparedStatement selectSequenceStatement = connection.prepareStatement(
                        "SELECT nextID FROM GlobalSequence WHERE tableName = ?"
                );
                PreparedStatement updateSequenceStatement = connection.prepareStatement(
                        "UPDATE GlobalSequence SET nextID = ? WHERE tableName = ?"
                )
        ) {
            // Thêm bản ghi mới vào bảng ServiceCategory
            insertStatement.setString(1, serviceCategory.getServiceCategoryID());
            insertStatement.setString(2, serviceCategory.getServiceCategoryName());
            insertStatement.setString(3, serviceCategory.getIcon());
            insertStatement.executeUpdate();

            // Lấy giá trị nextID hiện tại từ bảng GlobalSequence
            selectSequenceStatement.setString(1, "ServiceCategory");
            ResultSet rs = selectSequenceStatement.executeQuery();

            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.SERVICECATEGORY_PREFIX + "-";

                // tách phần số và tăng thêm 1
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;

                // Định dạng lại phần số, đảm bảo luôn có 6 chữ số
                String newNextID = prefix + String.format("%06d", nextIDNum);

                // Cập nhật giá trị nextID trong bảng GlobalSequence
                updateSequenceStatement.setString(1, newNextID);
                updateSequenceStatement.setString(2, "ServiceCategory");
                updateSequenceStatement.executeUpdate();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String serviceCategoryID) {
        String updateServiceCategorySQL = "UPDATE ServiceCategory SET isActivate = 'DEACTIVATE' WHERE serviceCategoryID = ?";
        String updateHotelServiceSQL = "UPDATE HotelService SET serviceCategoryID = NULL WHERE serviceCategoryID = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement updateServiceCategoryStmt = connection.prepareStatement(updateServiceCategorySQL);
                PreparedStatement updateHotelServiceStmt = connection.prepareStatement(updateHotelServiceSQL)
        ) {
            // Update ServiceCategory
            updateServiceCategoryStmt.setString(1, serviceCategoryID);
            updateServiceCategoryStmt.executeUpdate();

            // Update HotelService
            updateHotelServiceStmt.setString(1, serviceCategoryID);
            updateHotelServiceStmt.executeUpdate();

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void updateData(ServiceCategory serviceCategory) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                            "UPDATE ServiceCategory " +
                                "SET serviceCategoryName = ?, icon = ? " +
                                "WHERE serviceCategoryID = ? "
                )
        ){
            preparedStatement.setString(1, serviceCategory.getServiceCategoryName());
            preparedStatement.setString(2, serviceCategory.getIcon());
            preparedStatement.setString(3, serviceCategory.getServiceCategoryID());
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    public static List<ServiceCategory> findDataByContainsId(String input) {
        ArrayList<ServiceCategory> data = new ArrayList<>();
        try (

                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "SELECT serviceCategoryID, serviceCategoryName, icon " +
                                "FROM ServiceCategory " +
                                "WHERE LOWER(serviceCategoryID) LIKE ? AND isActivate = 'ACTIVATE'"
                )
        ){
            preparedStatement.setString(1, "%" + input + "%");
            ResultSet rs = preparedStatement.executeQuery();


            while (rs.next()) {
                ServiceCategory serviceCategory = new ServiceCategory();
                serviceCategory.setServiceCategoryID(rs.getString(1));
                serviceCategory.setServiceCategoryName(rs.getString(2));
                serviceCategory.setIcon(rs.getString(3));

                data.add(serviceCategory);
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
        ){
            String sql = "SELECT TOP 3 serviceCategoryID " +
                    "FROM ServiceCategory " +
                    "WHERE isActivate = 'ACTIVATE' " +
                    "ORDER BY serviceCategoryID DESC";
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

    public static String getNextServiceCategoryID() {
        String res = "SC-000001";

        try (

                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                            "SELECT nextID " +
                                "FROM GlobalSequence " +
                                "WHERE tableName = ?"
                )
        ){
            preparedStatement.setString(1, "ServiceCategory");
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

    public static List<String> getServiceCategoryNames() {
        ArrayList<String> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ){
            String sql = "SELECT serviceCategoryName from ServiceCategory WHERE isActivate = 'ACTIVATE'";
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


}
