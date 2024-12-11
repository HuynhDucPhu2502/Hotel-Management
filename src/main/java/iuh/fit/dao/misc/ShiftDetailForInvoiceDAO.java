package iuh.fit.dao.misc;

import iuh.fit.dao.InvoiceDAO;
import iuh.fit.models.misc.ShiftDetailForInvoice;
import iuh.fit.utils.DBHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Le Tran Gia Huy
 * @created 10/12/2024 - 9:45 PM
 * @project HotelManagement
 * @package iuh.fit.dao
 */
public class ShiftDetailForInvoiceDAO {

    public static List<ShiftDetailForInvoice> getData(int shiftDetailID){
        List<ShiftDetailForInvoice> temp = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement("""
                    SELECT FIID, SDTID, InvoiceID
                    FROM ShiftDetailForInvoice
                    WHERE SDTID = ?
                    """)
        ){
            statement.setInt(1, shiftDetailID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ShiftDetailForInvoice shiftDetailForInvoice = new ShiftDetailForInvoice(
                        rs.getInt(1),
                        InvoiceDAO.getInvoiceByInvoiceID(rs.getString(3)),
                        ShiftDetailDAO.getData(rs.getInt(2))
                );
                temp.add(shiftDetailForInvoice);
            }
        }
         catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return temp;
    }

    public static void addInvoiceID(int shiftDetailID, String roomWithReservationID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement("""
                        INSERT INTO ShiftDetailForInvoice(SDTID, InvoiceID)
                        VALUES(?, ?)
                        """, Statement.RETURN_GENERATED_KEYS);
        ){
            statement.setInt(1, shiftDetailID);
            statement.setString(2, Objects.requireNonNull(InvoiceDAO.getInvoiceByReservationFormID(roomWithReservationID)).getInvoiceID());
            int affectedRows = statement.executeUpdate();
            if(affectedRows == 0){
                System.out.println("Thêm thất bại");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }
}
