package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Pricing;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PricingDAO {
    public static List<Pricing> getPricing() {
        ArrayList<Pricing> data = new ArrayList<Pricing>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT pricingID, priceUnit, price " +
                    "FROM Pricing";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                Pricing pricing = new Pricing();

                pricing.setPricingID(rs.getString(1));
                pricing.setPriceUnit(ConvertHelper.pricingConverter(rs.getString(2)));
                pricing.setPrice(rs.getDouble(3));

                data.add(pricing);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
