package iuh.fit.dao;

import iuh.fit.models.Shift;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ShiftDAO {
    public static List<Shift> getShifts() {
        ArrayList<Shift> data = new ArrayList<Shift>();
        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
        ){
            String sql = "SELECT shiftID, startTime, endTime, " +
                    "modifiedDate, shiftDaysSchedule " +
                    "FROM Shift";
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                Shift shift = new Shift();

                shift.setStartTime(ConvertHelper.localTimeConverter(rs.getTime(2)));
                shift.setEndTime(ConvertHelper.localTimeConverter(rs.getTime(3)));
                shift.calcNumberOfHour();

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

        String SQLQueryStatement = "SELECT shiftID, startTime, endTime, " +
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
                    shift.setEndTime(ConvertHelper.localTimeConverter(rs.getTime(3)));
                    shift.calcNumberOfHour();

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

    public static void createData(Shift shift) {
        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Shift(shiftID, startTime, endTime, modifiedDate, numberOfHour, shiftDaysSchedule) " +
                                "VALUES(?, ?, ?, ?, ?, ?)"
                )
        ){
            preparedStatement.setString(1, shift.getShiftID());
            preparedStatement.setTime(2, ConvertHelper.timeConvertertoSQL(shift.getStartTime()));
            preparedStatement.setTime(3, ConvertHelper.timeConvertertoSQL(shift.getEndTime()));
            preparedStatement.setTimestamp(4, ConvertHelper.dateTimeToSQLConverter(shift.getUpdatedDate()));
            preparedStatement.setDouble(5, shift.getNumberOfHour());
            preparedStatement.setString(6, ConvertHelper.shiftDaysScheduleConverterToSQL(shift.getShiftDaysSchedule()));

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
            preparedStatement.setTime(1, ConvertHelper.timeConvertertoSQL(shift.getStartTime()));
            preparedStatement.setTime(2, ConvertHelper.timeConvertertoSQL(shift.getEndTime()));
            preparedStatement.setTimestamp(3, ConvertHelper.dateTimeToSQLConverter(shift.getUpdatedDate()));
            preparedStatement.setDouble(4, shift.getNumberOfHour());
            preparedStatement.setString(5, ConvertHelper.shiftDaysScheduleConverterToSQL(shift.getShiftDaysSchedule()));
            preparedStatement.setString(6, shift.getShiftID());

            preparedStatement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }
}
