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
            String sql = "SELECT serviceCategoryID, serviceCategoryName " +
                    "FROM ServiceCategory";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                ServiceCategory serviceCategory = new ServiceCategory();
                serviceCategory.setServiceCategoryID(rs.getString(1));
                serviceCategory.setServiceCategoryName(rs.getString(2));

                data.add(serviceCategory);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static ServiceCategory getDataByID(String serviceCategoryID) {

        String SQLQueryStatement = "SELECT serviceCategoryID, serviceCategoryName "
                + "FROM ServiceCategory where serviceCategoryID = ?";

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
                        "INSERT INTO ServiceCategory(serviceCategoryID, serviceCategoryName) " +
                                "VALUES(?, ?)"
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
            insertStatement.executeUpdate();

            // Lấy giá trị nextID hiện tại từ bảng GlobalSequence
            selectSequenceStatement.setString(1, "ServiceCategory");
            ResultSet rs = selectSequenceStatement.executeQuery();

            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = "SC-";

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
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                            "DELETE FROM ServiceCategory " +
                                "WHERE serviceCategoryID = ?"
                )
        ){
            preparedStatement.setString(1, serviceCategoryID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void updateData(ServiceCategory serviceCategory) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                            "UPDATE ServiceCategory " +
                                "SET serviceCategoryName = ? " +
                                "WHERE serviceCategoryID = ? "
                )
        ){
            preparedStatement.setString(1, serviceCategory.getServiceCategoryName());
            preparedStatement.setString(2, serviceCategory.getServiceCategoryID());
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
                        "SELECT serviceCategoryID, serviceCategoryName " +
                                "FROM ServiceCategory " +
                                "WHERE LOWER(serviceCategoryID) LIKE ?"
                )
        ){
            preparedStatement.setString(1, "%" + input + "%");
            ResultSet rs = preparedStatement.executeQuery();


            while (rs.next()) {
                ServiceCategory serviceCategory = new ServiceCategory();
                serviceCategory.setServiceCategoryID(rs.getString(1));
                serviceCategory.setServiceCategoryName(rs.getString(2));

                data.add(serviceCategory);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static List<String> getTopThreeCategoryService() {
        ArrayList<String> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ){
            String sql = "SELECT TOP 3 serviceCategoryID " +
                    "FROM ServiceCategory " +
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


}
