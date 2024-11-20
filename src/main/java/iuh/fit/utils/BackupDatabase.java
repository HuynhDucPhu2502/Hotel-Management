package iuh.fit.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class BackupDatabase {

    public static void backupDatabase(String filePath) throws SQLException {
        Connection connection = DBHelper.getConnection();
        String backupQuery = "BACKUP DATABASE [HotelDatabase] TO DISK = '" + filePath + "' ";
        if(connection == null) throw new IllegalArgumentException("Connection is NULL!!!");
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(backupQuery);
            preparedStatement.executeQuery();
            System.out.println("Backup completed: " + filePath);
        }catch (Exception ignored){

        }
    }
}
