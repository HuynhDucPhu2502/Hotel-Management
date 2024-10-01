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
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO ServiceCategory(serviceCategoryID, serviceCategoryName) " +
                                "VALUES(?, ?)"
                )
        ){
            preparedStatement.setString(1, serviceCategory.getServiceCategoryID());
            preparedStatement.setString(2, serviceCategory.getServiceCategoryName());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String serviceCategoryID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM ServiceCategory "
                                + "WHERE serviceCategoryID = ?"
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
                Statement statement = connection.createStatement()
        ){
            String sql = "SELECT TOP 1 serviceCategoryID " +
                    "FROM ServiceCategory " +
                    "ORDER BY serviceCategoryID DESC";
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                String lastID = rs.getString(1);
                String nextNumberStr = lastID.substring(3);

                int nextNumber = Integer.parseInt(nextNumberStr);
                ++nextNumber;

                String formattedNumber = String.format("%06d", nextNumber);

                res =  GlobalConstants.SERVICECATEGORY_PREFIX + "-" + formattedNumber;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return res;
    }


}
