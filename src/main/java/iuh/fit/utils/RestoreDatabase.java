package iuh.fit.utils;

import java.sql.*;

public class RestoreDatabase {
    public static void restoreDif(String backupFullFilePath, String backupDifFilePath) throws SQLException {
        String sqlBackUpFullStatement =
                "RESTORE DATABASE HotelDatabase " +
                        "FROM DISK = '" + backupFullFilePath + "' " +
                        "WITH REPLACE, NORECOVERY;";

        String sqlBackUpDifStatement =
                "RESTORE DATABASE HotelDatabase " +
                        "FROM DISK = '" + backupDifFilePath + "' " +
                        "WITH RECOVERY;";

        String useMasterSQL = "USE master;";
        String setSingleUserSQL = "ALTER DATABASE HotelDatabase SET SINGLE_USER WITH ROLLBACK IMMEDIATE;";
        String setMultiUserSQL = "ALTER DATABASE HotelDatabase SET MULTI_USER;";

        try (Connection con = DBHelper.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute(useMasterSQL);
            stmt.execute(setSingleUserSQL);
            stmt.execute(sqlBackUpFullStatement);
            stmt.execute(sqlBackUpDifStatement);
            stmt.execute(setMultiUserSQL);
        } catch (SQLException e) {
            System.err.println("Errors : " + e.getMessage());
            throw e;
        } finally {
            try (Connection con = DBHelper.getConnection()) {
                Statement stmt = con.createStatement();
                stmt.execute(setMultiUserSQL);
            } catch (SQLException finalEx) {
                System.err.println("Errors : " + finalEx.getMessage());
            }
        }
    }

    public static void restoreFull(String backupFullFilePath) throws SQLException {
        String useMasterSQL = "USE master;";
        String setSingleUserSQL = "ALTER DATABASE HotelDatabase SET SINGLE_USER WITH ROLLBACK IMMEDIATE;";
        String restoreDatabaseSQL =
                "RESTORE DATABASE HotelDatabase " +
                        "FROM DISK = '" + backupFullFilePath + "' " +
                        "WITH REPLACE, RECOVERY;";
        String setMultiUserSQL = "ALTER DATABASE HotelDatabase SET MULTI_USER;";

        try (Connection con = DBHelper.getConnection();
             Statement stmt = con.createStatement()) {

            stmt.execute(useMasterSQL);
            stmt.execute(setSingleUserSQL);
            stmt.execute(restoreDatabaseSQL);
            stmt.execute(setMultiUserSQL);
        } catch (SQLException e) {
            System.err.println("Errors : " + e.getMessage());
            throw e;
        }
    }

    public static void restoreFullWhenNoDB(String backupFullFilePath) throws SQLException {
        String databaseName = "HotelDatabase";

        try (Connection con = DBHelper.getConnectWhenNoDB()) {
            Statement stmt = con.createStatement();

            String restoreQuery = "RESTORE DATABASE " + databaseName + " " +
                    "FROM DISK = '" + backupFullFilePath + "' " +
                    "WITH REPLACE;";

            stmt.execute(restoreQuery);
        }
    }

    public static boolean isDatabaseExist(String databaseName) throws SQLException {
        String checkDatabaseSQL = "SELECT database_id FROM sys.databases WHERE name = '" + databaseName + "';";

        try (Connection con = DBHelper.getConnectWhenNoDB();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(checkDatabaseSQL)) {

            return rs.next();
        }
    }

}
