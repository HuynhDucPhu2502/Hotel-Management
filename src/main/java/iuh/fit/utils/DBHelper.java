package iuh.fit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

    private static final String USER_NAME = "sa";
    private static final String USER_PASSWORD = "sapassword";
    private static String DATABASE_NAME = "HotelDatabase";
    private static final String DATABASE_URL =
            """
            jdbc:sqlserver://localhost;
            databaseName=%s;
            encrypt=true;
            trustServerCertificate=true
            """;
    private static final String URL_NO_DB =
            """
            jdbc:sqlserver://localhost;
            encrypt=true;
            trustServerCertificate=true
            """;

    private static int connectCount = 0;

    public static Connection getConnection(
            String dbName,
            String userName,
            String userPassword)
            throws SQLException
    {
        Connection connection = DriverManager.getConnection(
                String.format(DATABASE_URL, dbName),
                userName,
                userPassword
        );

        if (connection != null)  {
            System.out.println("Connect thành công [Connect thứ " + ++connectCount + "]");
            return connection;
        }

        return null;
    }

    public static Connection getConnection(String dbName)
            throws SQLException
    {
        return getConnection(
                dbName,
                USER_NAME,
                USER_PASSWORD
        );
    }

    public static Connection getConnection()
            throws SQLException
    {
        return getConnection(
                DATABASE_NAME,
                USER_NAME,
                USER_PASSWORD
        );
    }

    public static Connection getConnectWhenNoDB() throws SQLException {
        return DriverManager.getConnection(URL_NO_DB, USER_NAME, USER_PASSWORD);
    }

    public static void setDatabaseName(String databaseName) {
        DATABASE_NAME = databaseName;
    }
}
