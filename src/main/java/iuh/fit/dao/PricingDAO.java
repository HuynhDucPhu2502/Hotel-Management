package iuh.fit.dao;

import iuh.fit.models.Pricing;
import iuh.fit.models.RoomCategory;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PricingDAO {
    public static List<Pricing> getPricing() {
        ArrayList<Pricing> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ){
            String sql = "SELECT a.pricingID, a.priceUnit, a.price, a.roomCategoryID, " +
                    "b.roomCategoryName, b.numberOfBed " +
                    "FROM Pricing a inner join RoomCategory b on a.roomCategoryID = b.roomCategoryID";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                Pricing pricing = new Pricing();
                RoomCategory roomCategory = new RoomCategory();

                pricing.setPricingID(rs.getString(1));
                pricing.setPriceUnit(ConvertHelper.priceUnitConverter(rs.getString(2)));
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
                    pricing.setPriceUnit(ConvertHelper.priceUnitConverter(rs.getString(2)));
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

                // Câu lệnh để thêm dữ liệu vào Pricing
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO Pricing(pricingID, priceUnit, price, roomCategoryID) " +
                                "VALUES(?, ?, ?, ?)"
                );

                // Câu lệnh để lấy giá trị nextID từ GlobalSequence
                PreparedStatement selectSequenceStatement = connection.prepareStatement(
                        "SELECT nextID FROM GlobalSequence WHERE tableName = ?"
                );

                // Câu lệnh để cập nhật giá trị nextID trong GlobalSequence
                PreparedStatement updateSequenceStatement = connection.prepareStatement(
                        "UPDATE GlobalSequence SET nextID = ? WHERE tableName = ?"
                )
        ) {
            // Thiết lập các giá trị cho câu lệnh INSERT
            insertStatement.setString(1, pricing.getPricingID());
            insertStatement.setString(2, ConvertHelper.pricingConverterToSQL(pricing.getPriceUnit()));
            insertStatement.setDouble(3, pricing.getPrice());
            insertStatement.setString(4, pricing.getRoomCategory().getRoomCategoryid());
            insertStatement.executeUpdate();

            // Lấy nextID hiện tại cho Pricing từ GlobalSequence
            selectSequenceStatement.setString(1, "Pricing");
            ResultSet rs = selectSequenceStatement.executeQuery();

            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = "PR-";  // prefix cho Pricing

                // Tách phần số và tăng thêm 1
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;

                // Định dạng lại phần số, đảm bảo luôn có 6 chữ số
                String newNextID = prefix + String.format("%06d", nextIDNum);

                // Cập nhật giá trị nextID trong GlobalSequence
                updateSequenceStatement.setString(1, newNextID);
                updateSequenceStatement.setString(2, "Pricing");
                updateSequenceStatement.executeUpdate();
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
                        "UPDATE Pricing SET priceUnit = ?, price = ?, roomCategoryID = ? WHERE pricingID = ?"
                )
        ) {
            preparedStatement.setString(1, ConvertHelper.pricingConverterToSQL(pricing.getPriceUnit()));
            preparedStatement.setDouble(2, pricing.getPrice());
            preparedStatement.setString(3, pricing.getRoomCategory().getRoomCategoryid());
            preparedStatement.setString(4, pricing.getPricingID());

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            String sqlMessage = sqlException.getMessage();

            if (sqlMessage.contains("Mỗi loại phòng chỉ được phép có 2 bản ghi")) {
                throw new IllegalArgumentException(
                        "Mỗi loại phòng chỉ được phép có 2 bản ghi trong Pricing (1 DAY và 1 HOUR)"
                );

            } else if (sqlMessage.contains("Violation of UNIQUE KEY constraint")) {
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


    public static List<Pricing> findDataByContainsId(String input) {
        ArrayList<Pricing> data = new ArrayList<>();

        String query = "SELECT a.pricingID, a.priceUnit, a.price, a.roomCategoryID, " +
                "b.roomCategoryName, b.numberOfBed " +
                "FROM Pricing a INNER JOIN RoomCategory b " +
                "ON a.roomCategoryID = b.roomCategoryID " +
                "WHERE LOWER(a.pricingID) LIKE ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, "%" + input.toLowerCase() + "%");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Pricing pricing = new Pricing();
                RoomCategory roomCategory = new RoomCategory();

                pricing.setPricingID(rs.getString(1));
                pricing.setPriceUnit(ConvertHelper.priceUnitConverter(rs.getString(2)));
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

    public static List<String> getTopThreeID() {
        ArrayList<String> data = new ArrayList<>();

        String query = "SELECT TOP 3 pricingID FROM Pricing ORDER BY pricingID DESC";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(query)
        ) {
            while (rs.next()) {
                data.add(rs.getString(1));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static String getNextPricingID() {
        String nextID = "P-000001";

        String query = "SELECT nextID FROM GlobalSequence WHERE tableName = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, "Pricing");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                nextID = rs.getString(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return nextID;
    }

}
