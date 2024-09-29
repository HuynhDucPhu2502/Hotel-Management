package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Pricing;
import iuh.fit.models.RoomCategory;
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
            String sql = "SELECT a.pricingID, a.priceUnit, a.price, a.roomCategoryID, " +
                    "b.roomCategoryName, b.numberOfBed " +
                    "FROM Pricing a inner join RoomCategory b on a.roomCategoryID = b.roomCategoryID";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                Pricing pricing = new Pricing();
                RoomCategory roomCategory = new RoomCategory();

                pricing.setPricingID(rs.getString(1));
                pricing.setPriceUnit(ConvertHelper.pricingConverter(rs.getString(2)));
                pricing.setPrice(rs.getDouble(3));

                roomCategory.setRoomCategoryid(rs.getString(4));
                roomCategory.setRoomCategoryName(rs.getString(5));
                roomCategory.setNumberOfBed(rs.getInt(6));

                pricing.setRoomCategory(roomCategory);

                data.add(pricing);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
