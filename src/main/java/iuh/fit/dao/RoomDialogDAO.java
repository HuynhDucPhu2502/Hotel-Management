package iuh.fit.dao;

import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.models.RoomDialog;
import iuh.fit.models.enums.DialogType;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoomDialogDAO {
    public static boolean createData(RoomDialog roomDialog) {
        String sql = """
            INSERT INTO RoomDialog (roomID, reservationFormID, dialog, dialogType, timestamp)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, roomDialog.getRoom().getRoomID());
            preparedStatement.setString(2, roomDialog.getReservationForm().getReservationID());
            preparedStatement.setString(3, roomDialog.getDialog());
            preparedStatement.setString(4, ConvertHelper.dialogTypeToSQLConverter(roomDialog.getDialogType()));
            preparedStatement.setTimestamp(5, ConvertHelper.localDateTimeToSQLConverter(roomDialog.getTimestamp()));

            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Phương thức để lấy danh sách RoomDialog theo roomID
    public static List<RoomDialog> getDataByRoomID(String roomID) {
        List<RoomDialog> roomDialogs = new ArrayList<>();

        String sql = """
            SELECT rd.roomID, rd.reservationFormID, rd.dialog, rd.dialogType, rd.timestamp,
                   r.roomID, rf.reservationFormID
            FROM RoomDialog rd
            LEFT JOIN Room r ON rd.roomID = r.roomID
            LEFT JOIN ReservationForm rf ON rd.reservationFormID = rf.reservationFormID
            WHERE rd.roomID = ?
            ORDER BY rd.timestamp DESC
            """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, roomID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    RoomDialog roomDialog = extractData(rs);
                    roomDialogs.add(roomDialog);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomDialogs;
    }

    // Phương thức để lấy RoomDialog theo reservationFormID
    public static List<RoomDialog> getDataByReservationFormID(String reservationFormID) {
        List<RoomDialog> roomDialogs = new ArrayList<>();

        String sql = """
            SELECT rd.roomID, rd.reservationFormID, rd.dialog, rd.dialogType, rd.timestamp,
                   r.roomID, rf.reservationFormID
            FROM RoomDialog rd
            LEFT JOIN Room r ON rd.roomID = r.roomID
            LEFT JOIN ReservationForm rf ON rd.reservationFormID = rf.reservationFormID
            WHERE rd.reservationFormID = ?
            ORDER BY rd.timestamp DESC
            """;

        try (Connection connection = DBHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, reservationFormID);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    RoomDialog roomDialog = extractData(rs);
                    roomDialogs.add(roomDialog);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomDialogs;
    }

    // Phương thức extractData để chuyển ResultSet thành đối tượng RoomDialog
    private static RoomDialog extractData(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomID(rs.getString("roomID"));

        ReservationForm reservationForm = new ReservationForm();
        reservationForm.setReservationID(rs.getString("reservationFormID"));

        DialogType dialogType = ConvertHelper.dialogTypeConverter(rs.getString("dialogType"));
        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();

        return new RoomDialog(
                room,
                reservationForm,
                rs.getString("dialog"),
                dialogType,
                timestamp
        );
    }
}
