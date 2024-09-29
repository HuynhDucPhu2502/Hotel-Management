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

public class RoomCategoryDAO {
    public static List<RoomCategory> getRoomCategory() {
        ArrayList<RoomCategory> data = new ArrayList<RoomCategory>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT a.roomCategoryID, a.roomCategoryName, a.numberOfBed, b.pricingID, " +
                    "b.priceUnit, b.price " +
                    "FROM RoomCategory a inner join Pricing b on a.pricingID = b.pricingID";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                RoomCategory roomCategory = new RoomCategory();
                Pricing pricing = new Pricing();

                roomCategory.setRoomCategoryid(rs.getString(1));
                roomCategory.setRoomCategoryName(rs.getString(2));
                roomCategory.setNumberOfBed(rs.getInt(3));
                pricing.setPricingID(rs.getString(4));
                pricing.setPriceUnit(ConvertHelper.pricingConverter(rs.getString(5)));
                pricing.setPrice(rs.getDouble(6));
                roomCategory.setPricing(pricing);

                data.add(roomCategory);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
