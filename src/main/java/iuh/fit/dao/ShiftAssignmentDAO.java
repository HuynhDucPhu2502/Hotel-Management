package iuh.fit.dao;

import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.models.ShiftAssignment;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static iuh.fit.utils.GlobalConstants.SHIFTASSIGNMENT_PREFIX;

public class ShiftAssignmentDAO {
    public static List<ShiftAssignment> getShiftAssignments() {
        ArrayList<ShiftAssignment> data = new ArrayList<ShiftAssignment>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT a.shiftAssignmentId, a.description, a.shiftId, a.employeeId, " +
                    "b.startTime, b.endTime, b.modifiedDate, b.numberOfHour, b.shiftDaysSchedule, " +
                    "c.fullName, c.phoneNumber, c.email, c.address, c.gender, c.idCardNumber, c.dob, c.position " +
                    "FROM ShiftAssignment a INNER JOIN Shift b on a.shiftID = b.shiftID " +
                    "INNER JOIN Employee c on a.employeeID = c.employeeID";
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                ShiftAssignment shiftAssignment = new ShiftAssignment();
                Shift shift = new Shift();
                Employee employee = new Employee();

                shiftAssignment.setShiftAssignmentId(rs.getString(1));
                shiftAssignment.setDescription(rs.getString(2));


                shift.setStartTime(ConvertHelper.localTimeConverter(rs.getTime(5)));
                shift.setNumberOfHour(rs.getInt(8));
                shift.calcEndTime();

                shift.setShiftID(rs.getString(3));
                shift.setUpdatedDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(7)));
                shift.setShiftDaysSchedule(ConvertHelper.shiftDaysScheduleConverter(rs.getString(9)));

                employee.setEmployeeID(rs.getString(4));
                employee.setFullName(rs.getString(10));
                employee.setPhoneNumber(rs.getString(11));
                employee.setEmail(rs.getString(12));
                employee.setAddress(rs.getString(13));
                employee.setGender(ConvertHelper.genderConverter(rs.getString(14)));
                employee.setIdCardNumber(rs.getString(15));
                employee.setDob(ConvertHelper.localDateConverter(rs.getDate(16)));
                employee.setPosition(ConvertHelper.positionConverter(rs.getString(17)));

                shiftAssignment.setShift(shift);
                shiftAssignment.setEmployee(employee);

                data.add(shiftAssignment);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return data;
    }

    public static ShiftAssignment getDataByID(String shiftAssignmentID) {

        String SQLQueryStatement = "SELECT a.shiftAssignmentId, a.description, a.shiftId, a.employeeId, " +
                "b.startTime, b.endTime, b.modifiedDate, b.numberOfHour, b.shiftDaysSchedule, " +
                "c.fullName, c.phoneNumber, c.email, c.address, c.gender, c.idCardNumber, c.dob, c.position " +
                "FROM ShiftAssignment a INNER JOIN Shift b on a.shiftID = b.shiftID " +
                "INNER JOIN Employee c on a.employeeID = c.employeeID " +
                "WHERE shiftAssignmentID = ?";

        try (
                Connection con = DBHelper.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(SQLQueryStatement)
        ) {

            preparedStatement.setString(1, shiftAssignmentID);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    ShiftAssignment shiftAssignment = new ShiftAssignment();
                    Shift shift = new Shift();
                    Employee employee = new Employee();

                    shiftAssignment.setShiftAssignmentId(rs.getString(1));
                    shiftAssignment.setDescription(rs.getString(2));


                    shift.setStartTime(ConvertHelper.localTimeConverter(rs.getTime(5)));
                    shift.setNumberOfHour(rs.getInt(8));
                    shift.calcEndTime();

                    shift.setShiftID(rs.getString(3));
                    shift.setUpdatedDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp(7)));
                    shift.setShiftDaysSchedule(ConvertHelper.shiftDaysScheduleConverter(rs.getString(9)));

                    employee.setEmployeeID(rs.getString(4));
                    employee.setFullName(rs.getString(10));
                    employee.setPhoneNumber(rs.getString(11));
                    employee.setEmail(rs.getString(12));
                    employee.setAddress(rs.getString(13));
                    employee.setGender(ConvertHelper.genderConverter(rs.getString(14)));
                    employee.setIdCardNumber(rs.getString(15));
                    employee.setDob(ConvertHelper.localDateConverter(rs.getDate(16)));
                    employee.setPosition(ConvertHelper.positionConverter(rs.getString(17)));

                    shiftAssignment.setShift(shift);
                    shiftAssignment.setEmployee(employee);


                    return shiftAssignment;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createData(ShiftAssignment shiftAssignment) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO ShiftAssignment(shiftAssignmentId, description, shiftId, employeeId) " +
                                "VALUES(?, ?, ?, ?)"
                )
        ){

            preparedStatement.setString(1, shiftAssignment.getShiftAssignmentId());
            preparedStatement.setString(2, shiftAssignment.getDescription());
            preparedStatement.setString(3, shiftAssignment.getShift().getShiftID());
            preparedStatement.setString(4, shiftAssignment.getEmployee().getEmployeeID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String shiftAssignmentID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM ShiftAssignment "
                                + "WHERE shiftAssignmentID = ?"
                )
        ){
            preparedStatement.setString(1, shiftAssignmentID);
            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            System.exit(1);
        }
    }

    public static void deleteShiftWithShiftID(String shiftID) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM ShiftAssignment "
                                + "WHERE shiftID = ?"
                )
        ){
            preparedStatement.setString(1, shiftID);
            preparedStatement.executeUpdate();

            ShiftDAO.deleteData(shiftID);
        } catch (Exception exception) {
            System.exit(1);
        }
    }


    public static void updateData(ShiftAssignment shiftAssignment) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE ShiftAssignment " +
                                "SET description = ?, shiftId = ?, employeeId = ? " +
                                "WHERE shiftAssignmentID = ? "
                );
        ){

            preparedStatement.setString(1, shiftAssignment.getDescription());
            preparedStatement.setString(2, shiftAssignment.getShift().getShiftID());
            preparedStatement.setString(3, shiftAssignment.getEmployee().getEmployeeID());
            preparedStatement.setString(4, shiftAssignment.getShiftAssignmentId());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    public static void updateShiftIDForAllEmployee(String oldID, Shift shift){

    }

    public static void updateShiftAssignmentForOneEmployee(Shift oldShift, Shift newShift, Employee employee) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE ShiftAssignment " +
                                "SET shiftId = ? " +
                                "WHERE shiftId = ? AND employeeID = ?"
                );
        ){

            preparedStatement.setString(1, newShift.getShiftID());
            preparedStatement.setString(2, oldShift.getShiftID());
            preparedStatement.setString(3, employee.getEmployeeID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    public static void updateShiftAssignmentWithoutID(Shift oldShift, Shift newShift) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE ShiftAssignment " +
                                "SET shiftId = ? " +
                                "WHERE shiftId = ? "
                );
        ){

            preparedStatement.setString(1, newShift.getShiftID());
            preparedStatement.setString(2, oldShift.getShiftID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    public static void updateShiftAssignmentWithID(String oldShiftID, Shift newShift) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE ShiftAssignment " +
                                "SET shiftId = ? " +
                                "WHERE shiftId = ? "
                );
        ){

            preparedStatement.setString(1, newShift.getShiftID());
            preparedStatement.setString(2, oldShiftID);

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }


    public static String getShiftAssignmentId(){
        String sql = "SELECT nextID " +
                "FROM GlobalSequence " +
                "WHERE tableName = 'ShiftAssignment'";
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
                )
        {
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()){
                return rs.getString(1);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
        return "";
    }
    public static void createNextShiftAssignmentID(String shiftAssignmentID){

        int subShiftAssignmentIDNumb = Integer.parseInt(shiftAssignmentID.substring(shiftAssignmentID.length() - 6));
        subShiftAssignmentIDNumb++;
        String newID = String.format("%s%s%06d",SHIFTASSIGNMENT_PREFIX, "-", subShiftAssignmentIDNumb);

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE GlobalSequence " +
                                "SET nextID = ? " +
                                "WHERE tableName = 'ShiftAssignment'"
                )
        ){

            preparedStatement.setString(1, newID);

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }
}
