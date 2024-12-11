package iuh.fit.dao.misc;

import iuh.fit.dao.ShiftDAO;
import iuh.fit.models.misc.ShiftDetail;
import iuh.fit.models.misc.ShiftDetailForInvoice;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 10/12/2024 - 9:21 PM
 * @project HotelManagement
 * @package iuh.fit.dao
 */
public class ShiftDetailDAO {

    public static ShiftDetail getData(int shiftDetailID){
        ShiftDetail shiftDetail = null;
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement("""
                    SELECT SDTID, CreatedAt, NumbOfPreOrderRoom, NumbOfCheckOutRoom, NumbOfCheckInRoom
                    FROM ShiftDetail
                    WHERE SDTID = ?
                    """)
        ){
            statement.setInt(1, shiftDetailID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                shiftDetail = new ShiftDetail(
                        ConvertHelper.localDateTimeConverter(
                                rs.getTimestamp(2)),
                        rs.getInt(5),
                        rs.getInt(4),
                        rs.getInt(3),
                        rs.getInt(1)
                );
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return shiftDetail;
    }

    public static int createStartingPoint() {
        ResultSet rs;
        int generatedId = 0;
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO ShiftDetail(CreatedAt, NumbOfPreOrderRoom,
                        NumbOfCheckOutRoom, NumbOfCheckInRoom)
                        VALUES(GETDATE(), 0, 0, 0)
                        """, Statement.RETURN_GENERATED_KEYS);
        ){
            int affectedRows = statement.executeUpdate();


            if (affectedRows > 0) {
                // Lấy ID tự động sinh
                rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1); // Cột 1 chứa giá trị ID
                }
            } else {
                System.out.println("Thêm thất bại.");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        return generatedId;
    }

    public static void updateNumbOfPreOrderRoom(int id) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement("""
                        UPDATE ShiftDetail
                        SET NumbOfPreOrderRoom = NumbOfPreOrderRoom + 1
                        WHERE SDTID = ?
                        """, Statement.RETURN_GENERATED_KEYS);
        ){
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();


            if (affectedRows == 0) {
                System.out.println("Sửa thất bại.");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void updateNumbOfCheckOutRoom(int id) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement("""
                        UPDATE ShiftDetail
                        SET NumbOfCheckOutRoom = NumbOfCheckOutRoom + 1
                        WHERE SDTID = ?
                        """, Statement.RETURN_GENERATED_KEYS);
        ){
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();


            if (affectedRows == 0) {
                System.out.println("Sửa thất bại.");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void updateNumbOfCheckInRoom(int id) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement("""
                        UPDATE ShiftDetail
                        SET NumbOfCheckInRoom = NumbOfCheckInRoom + 1
                        WHERE SDTID = ?
                        """, Statement.RETURN_GENERATED_KEYS);
        ){
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();


            if (affectedRows == 0) {
                System.out.println("Sửa thất bại.");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

}
