package iuh.fit.dao;

import iuh.fit.models.Shift;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;

import java.sql.Connection;
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
}
