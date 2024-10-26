package iuh.fit.dao;

import iuh.fit.models.Room;
import iuh.fit.models.RoomCategory;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomCategoryDAO {
    public static List<RoomCategory> getRoomCategory() {
        ArrayList<RoomCategory> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement()
        ){
            String sql = "SELECT roomCategoryID, roomCategoryName, numberOfBed " +
                    "FROM RoomCategory";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                RoomCategory roomCategory = new RoomCategory();

                roomCategory.setRoomCategoryID(rs.getString(1));
                roomCategory.setRoomCategoryName(rs.getString(2));
                roomCategory.setNumberOfBed(rs.getInt(3));

                data.add(roomCategory);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static RoomCategory getDataByID(String roomCategoryID) {

        String SQLQueryStatement = "SELECT roomCategoryID, roomCategoryName, numberOfBed "
                + "FROM RoomCategory " +
                "WHERE roomCategoryID = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, roomCategoryID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    RoomCategory roomCategory = new RoomCategory();
                    roomCategory.setRoomCategoryID(rs.getString(1));
                    roomCategory.setRoomCategoryName(rs.getString(2));
                    roomCategory.setNumberOfBed(rs.getInt(3));

                    return roomCategory;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(RoomCategory roomCategory) {
        try (
                Connection connection = DBHelper.getConnection();

                // Câu lệnh thêm dữ liệu vào RoomCategory
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO RoomCategory(roomCategoryID, roomCategoryName, numberOfBed) " +
                                "VALUES(?, ?, ?)"
                );

                // Câu lệnh lấy giá trị nextID từ GlobalSequence
                PreparedStatement selectSequenceStatement = connection.prepareStatement(
                        "SELECT nextID FROM GlobalSequence WHERE tableName = ?"
                );

                // Câu lệnh cập nhật giá trị nextID trong GlobalSequence
                PreparedStatement updateSequenceStatement = connection.prepareStatement(
                        "UPDATE GlobalSequence SET nextID = ? WHERE tableName = ?"
                )
        ) {
            // Thiết lập các giá trị cho câu lệnh INSERT
            insertStatement.setString(1, roomCategory.getRoomCategoryID());
            insertStatement.setString(2, roomCategory.getRoomCategoryName());
            insertStatement.setInt(3, roomCategory.getNumberOfBed());
            insertStatement.executeUpdate();

            // Lấy nextID hiện tại cho RoomCategory từ GlobalSequence
            selectSequenceStatement.setString(1, "RoomCategory");
            ResultSet rs = selectSequenceStatement.executeQuery();

            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.ROOMCATEGORY_PREFIX + "-";

                // Tách phần số và tăng thêm 1
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length() + 1)) + 1;
                // Định dạng lại phần số để đảm bảo luôn có 6 chữ số
                String newNextID = prefix + String.format("%06d", nextIDNum);

                // Cập nhật giá trị nextID trong GlobalSequence
                updateSequenceStatement.setString(1, newNextID);
                updateSequenceStatement.setString(2, "RoomCategory");
                updateSequenceStatement.executeUpdate();
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);  // Thoát nếu có lỗi xảy ra
        }
    }

    public static void deleteData(String roomCategoryID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM RoomCategory "
                                + "WHERE roomCategoryID = ?"
                )
        ){
            preparedStatement.setString(1, roomCategoryID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void updateData(RoomCategory roomCategory) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE RoomCategory " +
                                "SET roomCategoryName = ?, numberOfBed = ? " +
                                "WHERE roomCategoryID = ? "
                )
        ){
            preparedStatement.setString(1, roomCategory.getRoomCategoryName());
            preparedStatement.setInt(2, roomCategory.getNumberOfBed());
            preparedStatement.setString(3, roomCategory.getRoomCategoryID());
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    public static List<RoomCategory> findDataByContainsId(String input) {
        List<RoomCategory> data = new ArrayList<>();
        String sql = "SELECT roomCategoryID, roomCategoryName, numberOfBed " +
                "FROM RoomCategory " +
                "WHERE LOWER(roomCategoryID) LIKE ?";
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, "%" + input.toLowerCase() + "%");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                RoomCategory roomCategory = new RoomCategory(
                        rs.getString("roomCategoryID"),
                        rs.getString("roomCategoryName"),
                        rs.getInt("numberOfBed")
                );
                data.add(roomCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static List<String> getTopThreeID() {
        List<String> data = new ArrayList<>();
        String sql = "SELECT TOP 3 roomCategoryID FROM RoomCategory ORDER BY roomCategoryID DESC";
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)
        ) {
            while (rs.next()) {
                data.add(rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getNextRoomCategoryID() {
        String sql = "SELECT nextID FROM GlobalSequence WHERE tableName = 'RoomCategory'";
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)
        ) {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "RC-000001";
    }

    public static boolean checkAllowUpdateOrDelete(String roomCategoryID) {
        int count = 0;

        String sql = "SELECT roomID " +
                "FROM Room " +
                "WHERE roomCategoryID = ? AND (roomStatus = ? OR roomStatus = ?)";
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1, roomCategoryID);
            preparedStatement.setString(2, "ON_USE");
            preparedStatement.setString(3, "OVERDUE");

            try(ResultSet rs = preparedStatement.executeQuery()){
                while (rs.next()) {
                    count++;
                }

                if(count > 0){
                    return false;
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return true;
    }
}
