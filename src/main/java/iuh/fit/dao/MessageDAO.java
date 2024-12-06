package iuh.fit.dao;

import iuh.fit.models.misc.Notifications;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Le Tran Gia Huy
 * @created 05/12/2024 - 10:02 AM
 * @project HotelManagement
 * @package iuh.fit.dao
 */
public class MessageDAO {
    public static List<Notifications> getMessage(String employeeID) {
        ArrayList<Notifications> data = new ArrayList<>();
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * " +
                                "FROM Notifications " +
                                "WHERE entityID != 'EMP-000000' AND entityID = ?"
                )
        ) {
            statement.setString(1, employeeID);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Notifications notifications = new Notifications();

                notifications.setNotificationID(rs.getInt(1));
                notifications.setEntityID(rs.getString(2));
                notifications.setTitle(rs.getString(3));
                notifications.setContent(rs.getString(4));
                notifications.setCreatedAt(ConvertHelper.localDateTimeConverter(rs.getTimestamp(5)));
                notifications.setRead(rs.getBoolean(6));

                data.add(notifications);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static void createData(Notifications notification) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO Notifications(EntityID, Title, Content, CreatedAt, IsRead) " +
                                "VALUES(?, ?, ?, ?, ?)"
                );
        ) {
            insertStatement.setString(1, notification.getEntityID());
            insertStatement.setString(2, notification.getTitle());
            insertStatement.setString(3, notification.getContent());
            insertStatement.setTimestamp(4, ConvertHelper.localDateTimeToSQLConverter(notification.getCreatedAt()));
            insertStatement.setBoolean(5, notification.isRead());

            insertStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void updateStatus(int notificationID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "Update Notifications " +
                                "SET IsRead = 1"
                                + "WHERE NotificationID = ?"
                )
        ){
            preparedStatement.setInt(1, notificationID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void deleteData(int notificationID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Notifications "
                                + "WHERE NotificationID = ?"
                )
        ){
            preparedStatement.setInt(1, notificationID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }
}
