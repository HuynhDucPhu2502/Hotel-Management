package iuh.fit.dao;

import iuh.fit.models.ServiceCategory;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategoryDAO {
    public static List<ServiceCategory> getServiceCategory() {
        ArrayList<ServiceCategory> data = new ArrayList<ServiceCategory>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT * " +
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
}
