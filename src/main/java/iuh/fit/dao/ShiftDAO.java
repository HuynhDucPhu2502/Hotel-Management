package iuh.fit.dao;

import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static iuh.fit.utils.ConvertHelper.currentDaysScheduleToSQLConverter;

public class ShiftDAO {
    public static List<Shift> getShifts() {
        ArrayList<Shift> data = new ArrayList<Shift>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT shiftID, startTime, numberOfHour, " +
                    "modifiedDate, shiftDaysSchedule " +
                    "FROM Shift";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                Shift shift = new Shift();

                shift.setStartTime(ConvertHelper.localTimeConverter(rs.getTime(2)));
                shift.setNumberOfHour(rs.getInt(3));
                shift.calcEndTime();

                shift.setShiftID(rs.getString(1));
                shift.setUpdatedDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                shift.setShiftDaysSchedule(ConvertHelper.shiftDaysScheduleConverter(rs.getString(5)));

                data.add(shift);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static Shift getDataByID(String shiftID) {

        String SQLQueryStatement = "SELECT shiftID, startTime, numberOfHour, " +
                "modifiedDate, shiftDaysSchedule " +
                "FROM Shift " +
                "WHERE shiftID = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, shiftID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Shift shift = new Shift();

                    shift.setStartTime(ConvertHelper.localTimeConverter(rs.getTime(2)));
                    shift.setNumberOfHour(rs.getInt(3));
                    shift.calcEndTime();

                    shift.setShiftID(rs.getString(1));
                    shift.setUpdatedDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                    shift.setShiftDaysSchedule(ConvertHelper.shiftDaysScheduleConverter(rs.getString(5)));

                    return shift;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Shift> findDataByAnyContainsId(String input) {
        List<Shift> data = new ArrayList<Shift>();
        String sql = "SELECT shiftID, startTime, endTime, modifiedDate, numberOfHour, shiftDaysSchedule " +
                "FROM Shift " +
                "WHERE LOWER(shiftID) LIKE ?";
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, "%" + input.toLowerCase() + "%");
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Shift shift = new Shift();

                shift.setStartTime(ConvertHelper.localTimeConverter(rs.getTime(2)));
                shift.setNumberOfHour(rs.getInt(5));
                shift.calcEndTime();

                shift.setShiftID(rs.getString(1));
                shift.setUpdatedDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(4)));
                shift.setShiftDaysSchedule(ConvertHelper.shiftDaysScheduleConverter(rs.getString(6)));

                data.add(shift);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void createData(Shift shift) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Shift(shiftID, startTime, endTime, modifiedDate, numberOfHour, shiftDaysSchedule) " +
                                "VALUES(?, ?, ?, ?, ?, ?)"
                )
        ){

            preparedStatement.setString(1, shift.getShiftID());
            preparedStatement.setTime(2, ConvertHelper.localTimeToSQLConverter(shift.getStartTime()));
            preparedStatement.setTime(3, ConvertHelper.localTimeToSQLConverter(shift.getEndTime()));
            preparedStatement.setTimestamp(4, ConvertHelper.localDateTimeToSQLConverter(shift.getUpdatedDate()));
            preparedStatement.setDouble(5, shift.getNumberOfHour());
            preparedStatement.setString(6, ConvertHelper.shiftDaysScheduleToSQLConverter(shift.getShiftDaysSchedule()));

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String shiftID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Shift "
                                + "WHERE shiftID = ?"
                )
        ){
            preparedStatement.setString(1, shiftID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void updateData(Shift shift) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Shift " +
                                "SET startTime = ?, endTime = ?, modifiedDate = ?, " +
                                "numberOfHour = ?, shiftDaysSchedule = ? " +
                                "WHERE shiftID = ? "
                );
        ){
            preparedStatement.setTime(1, ConvertHelper.localTimeToSQLConverter(shift.getStartTime()));
            preparedStatement.setTime(2, ConvertHelper.localTimeToSQLConverter(shift.getEndTime()));
            preparedStatement.setTimestamp(3, ConvertHelper.localDateTimeToSQLConverter(shift.getUpdatedDate()));
            preparedStatement.setDouble(4, shift.getNumberOfHour());
            preparedStatement.setString(5, ConvertHelper.shiftDaysScheduleToSQLConverter(shift.getShiftDaysSchedule()));
            preparedStatement.setString(6, shift.getShiftID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void updateDataWithTwoTableAtTheSameTime(String oldID, Shift shift) throws SQLException {
        try (Connection conn = DBHelper.getConnection()) {
            String updateTable1 = "UPDATE Shift SET shiftID = ? WHERE shiftID = ?";
            String updateTable2 = "UPDATE ShiftAssignment SET shiftID = ? WHERE shiftID = ?";

            try {
                // Tắt chế độ tự động xác nhận
                conn.setAutoCommit(false);

                try (
                        PreparedStatement stmt1 = conn.prepareStatement(updateTable1);
                        PreparedStatement stmt2 = conn.prepareStatement(updateTable2);
                ) {
                    // Cập nhật shiftID trong Table1
                    stmt1.setString(1, shift.getShiftID());
                    stmt1.setString(2, oldID);
                    stmt1.executeUpdate();

                    // Cập nhật shiftID trong Table2
                    stmt2.setString(1, shift.getShiftID());
                    stmt2.setString(2, oldID);
                    stmt2.executeUpdate();

                    // Xác nhận giao dịch
                    conn.commit();
                    System.out.println("Cập nhật shiftID thành công trong cả hai bảng.");
                } catch (SQLException e) {
                    // Hủy giao dịch nếu có lỗi
                    conn.rollback();
                    System.err.println("Cập nhật thất bại. Đã hoàn nguyên thay đổi.");
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDataWithOldID(String oldID, Shift shift) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Shift " +
                                "SET shiftID = ?, startTime = ?, endTime = ?, modifiedDate = ?, " +
                                "numberOfHour = ?, shiftDaysSchedule = ? " +
                                "WHERE shiftID = ? "
                )
        ){
            preparedStatement.setString(1, shift.getShiftID());
            preparedStatement.setTime(2, ConvertHelper.localTimeToSQLConverter(shift.getStartTime()));
            preparedStatement.setTime(3, ConvertHelper.localTimeToSQLConverter(shift.getEndTime()));
            preparedStatement.setTimestamp(4, ConvertHelper.localDateTimeToSQLConverter(shift.getUpdatedDate()));
            preparedStatement.setDouble(5, shift.getNumberOfHour());
            preparedStatement.setString(6, ConvertHelper.shiftDaysScheduleToSQLConverter(shift.getShiftDaysSchedule()));
            preparedStatement.setString(7, shift.getShiftID());


            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    public static String shiftIDGenerate(LocalTime endTime){
        String sql = "SELECT shiftID FROM Shift";
        String newShiftID;
        int maxIDNumb = 0;
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ) {
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    String shiftID = rs.getString(1);
                    String shiftIDSubStr = shiftID.substring(shiftID.length()-4);
                    int shiftOrdNumb = Integer.parseInt(shiftIDSubStr);

                    maxIDNumb = Math.max(maxIDNumb, shiftOrdNumb);
                }

                if (maxIDNumb >= 9999)
                    throw new IllegalArgumentException("Số lượng ca làm đã đạt đến giới hạn. Vui lòng không thêm hoặc xóa bớt để tạo thêm");

                int nextIDNumb = maxIDNumb + 1;
                String timeCode = endTime.getHour() > 12 ? "PM" : "AM";

                newShiftID = String.format("%s%s%04d", GlobalConstants.SHIFT_PREFIX + "-", timeCode + "-", nextIDNumb);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Lỗi truy vấn cơ sở dữ liệu", e);
        }

        return newShiftID;
    }


    public static boolean checkAllowUpdateOrDelete(String shiftID) {
        ArrayList<Shift> data = new ArrayList<Shift>();
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT a.shiftID, b.employeeID " +
                        "FROM Shift a " +
                        "INNER JOIN ShiftAssignment b ON a.shiftID = b.shiftID " +
                        "WHERE a.shiftID = ?");
        ){
            statement.setString(1,shiftID);
            ResultSet rs = statement.executeQuery();

            int rowCount = 0;
            while (rs.next()) {
                rowCount++;
            }
            if (rowCount >= 1) {
                return false;
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return true;
    }

//    public static boolean checkCurrentEmployeeWhileUpdateOrDelete(Shift shift, Employee employee) {
//        ArrayList<Shift> data = new ArrayList<Shift>();
//        ArrayList<Employee> employees = new ArrayList<Employee>();
//        try (
//                Connection connection = DBHelper.getConnection();
//                PreparedStatement statement = connection.prepareStatement
//                        ("SELECT a.shiftID, b.employeeID " +
//                        "FROM Shift a " +
//                        "INNER JOIN ShiftAssignment b ON a.shiftID = b.shiftID " +
//                        "WHERE a.shiftID = ?");
//        ){
//            statement.setString(1,shift.getShiftID());
//            ResultSet rs = statement.executeQuery();
//
//            while (rs.next()) {
//                String employeeID = rs.getString(2);
//                employees.add(EmployeeDAO.getEmployeeByEmployeeID(employeeID));
//            }
//            if (employees.contains(employee)) {
//                return false;
//            }
//
//        } catch (Exception exception) {
//            exception.printStackTrace();
//            System.exit(1);
//        }
//
//        return true;
//    }

    public static Shift getCurrentShiftForLogin(Employee employee) {
        ArrayList<Shift> data = new ArrayList<Shift>();
        Shift currentShift = null;
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement
                        ("SELECT s.shiftID, b.employeeID " +
                                "FROM Employee e " +
                                "INNER JOIN ShiftAssignment b ON e.employeeID = b.employeeID " +
                                "INNER JOIN Shift s ON b.shiftID = s.shiftID " +
                                "WHERE e.employeeID = ? AND startTime <= CONVERT(TIME, GETDATE()) AND endTime >= CONVERT(TIME, GETDATE()) " +
                                "AND s.shiftDaysSchedule = ?");
        ){
            statement.setString(1, employee.getEmployeeID());
            statement.setString(2, currentDaysScheduleToSQLConverter(LocalDateTime.now()));
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                currentShift = ShiftDAO.getDataByID(rs.getString(1));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return currentShift;
    }

    public static List<Employee> getEmployeeListByShift(String shiftID) {
        List<Employee> data = new ArrayList<Employee>();
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT a.shiftID, b.employeeID " +
                        "FROM Shift a " +
                        "INNER JOIN ShiftAssignment b ON a.shiftID = b.shiftID " +
                        "WHERE a.shiftID = ?");
        ){
            statement.setString(1,shiftID);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                String employeeID = rs.getString(2);
                Employee employee = EmployeeDAO.getDataByID(employeeID);
                System.out.println(employee.toString());
                data.add(employee);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }
}
