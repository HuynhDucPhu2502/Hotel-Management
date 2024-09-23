package iuh.fit.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBHelper class giúp thiết lập kết nối với cơ sở dữ liệu SQL Server.
 * Class này cung cấp các phương thức để kết nối đến cơ sở dữ liệu
 * sử dụng tên cơ sở dữ liệu, tên người dùng và mật khẩu.
 */
public class DBHelper {

    private static final String USER_NAME = "sa";
    private static final String USER_PASSWORD = "sapassword";
    private static final String DATABASE_NAME = "SellingManagement";

    // Đếm số lượng kết nối đã được thiết lập
    private static int connectCount = 0;

    /**
     * Phương thức getConnection() thiết lập kết nối với cơ sở dữ liệu
     * sử dụng tên cơ sở dữ liệu, tên người dùng và mật khẩu mặc định.
     *
     * @return Connection đối tượng kết nối với cơ sở dữ liệu.
     * @throws SQLException nếu có lỗi khi kết nối với cơ sở dữ liệu.
     */
    public static Connection getConnection() throws SQLException {
        return getConnection(DATABASE_NAME, USER_NAME, USER_PASSWORD);
    }

    /**
     * Phương thức getConnection() thiết lập kết nối với cơ sở dữ liệu
     * với tên cơ sở dữ liệu, tên người dùng và mật khẩu được truyền vào.
     *
     * @param dbName tên của cơ sở dữ liệu.
     * @param userName tên người dùng để kết nối với cơ sở dữ liệu.
     * @param userPassword mật khẩu để kết nối với cơ sở dữ liệu.
     * @return Connection đối tượng kết nối với cơ sở dữ liệu hoặc null nếu không thể kết nối.
     * @throws SQLException nếu có lỗi khi kết nối với cơ sở dữ liệu.
     */
    public static Connection getConnection(String dbName, String userName, String userPassword) throws SQLException {
        String dbURL = "jdbc:sqlserver://localhost;" +
                "databaseName=%s;" +
                "encrypt=true;" +
                "trustServerCertificate=true";
        Connection connection = DriverManager.getConnection(String.format(dbURL, dbName), userName, userPassword);

        // Kiểm tra nếu kết nối thành công và tăng bộ đếm số kết nối
        if (connection != null)  {
            System.out.println("Connect thành công [Connect thứ " + ++connectCount + "]");
            return connection;
        }
        return null;
    }
}
