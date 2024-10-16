package iuh.fit.dao;

import iuh.fit.models.Customer;
import iuh.fit.models.Pricing;
import iuh.fit.models.RoomCategory;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.*;
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

    public static Pricing getDataByID(String pricingID) {

        String SQLQueryStatement = "SELECT a.pricingID, a.priceUnit, a.price, a.roomCategoryID, " +
                "b.roomCategoryName, b.numberOfBed " +
                "FROM Pricing a inner join RoomCategory b on a.roomCategoryID = b.roomCategoryID " +
                "WHERE pricingID = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, pricingID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Pricing pricing = new Pricing();
                    RoomCategory roomCategory = new RoomCategory();

                    pricing.setPricingID(rs.getString(1));
                    pricing.setPriceUnit(ConvertHelper.pricingConverter(rs.getString(2)));
                    pricing.setPrice(rs.getDouble(3));

                    roomCategory.setRoomCategoryid(rs.getString(4));
                    roomCategory.setRoomCategoryName(rs.getString(5));
                    roomCategory.setNumberOfBed(rs.getInt(6));

                    pricing.setRoomCategory(roomCategory);

                    return pricing;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(Pricing pricing) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Pricing(pricingID, priceUnit, price, roomCategoryID) " +
                                "VALUES(?, ?, ?, ?)"
                )
        ){
            preparedStatement.setString(1, pricing.getPricingID());
            preparedStatement.setString(2, ConvertHelper.pricingConverterToSQL(pricing.getPriceUnit()));
            preparedStatement.setDouble(3, pricing.getPrice());
            preparedStatement.setString(4, pricing.getRoomCategory().getRoomCategoryid());

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            String sqlMessage = sqlException.getMessage();

            if (sqlMessage.contains("Mỗi loại phòng chỉ được phép có 2 bản ghi")) {
                throw new IllegalArgumentException(
                        "Mỗi loại phòng chỉ được phép có 2 bản ghi trong Pricing (1 DAY và 1 HOUR)"
                );

            } else if (sqlException.getErrorCode() == 2627) {
                throw new IllegalArgumentException(
                        "Mỗi loại phòng chỉ được phép có 1 bản ghi cho mỗi đơn vị thời gian (DAY hoặc HOUR)."
                );

            } else {
                sqlException.printStackTrace();
                System.exit(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }


    public static void deleteData(String pricingID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Pricing "
                                + "WHERE pricingID = ?"
                )
        ){
            preparedStatement.setString(1, pricingID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void updateData(Pricing pricing) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Pricing " +
                                "SET priceUnit = ?, price = ?, roomCategoryID = ? " +
                                "WHERE pricingID = ? "
                );
        ){
            preparedStatement.setString(1, ConvertHelper.pricingConverterToSQL(pricing.getPriceUnit()));
            preparedStatement.setDouble(2, pricing.getPrice());
            preparedStatement.setString(3, pricing.getRoomCategory().getRoomCategoryid());
            preparedStatement.setString(4, pricing.getPricingID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }
}
