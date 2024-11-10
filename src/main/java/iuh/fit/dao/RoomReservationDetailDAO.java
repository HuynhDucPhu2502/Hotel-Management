package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.GlobalConstants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomReservationDetailDAO {
    public static List<RoomReservationDetail> getAll() {
        List<RoomReservationDetail> data = new ArrayList<>();
        String sql = """
                SELECT rrd.roomReservationDetailID, rrd.dateChanged, rrd.roomID,\s
                       rrd.reservationFormID, rrd.employeeID,
                       r.roomStatus, r.dateOfCreation, r.roomCategoryID,
                       rf.reservationDate, rf.checkInDate, rf.checkOutDate,
                       e.fullName, e.phoneNumber, e.email, e.address, e.gender, e.idCardNumber, e.dob, e.position
                FROM RoomReservationDetail rrd
                INNER JOIN Room r ON rrd.roomID = r.roomID
                INNER JOIN ReservationForm rf ON rrd.reservationFormID = rf.reservationFormID
                INNER JOIN Employee e ON rrd.employeeID = e.employeeID
                WHERE r.isActivate = 'ACTIVATE' AND e.isActivate = 'ACTIVATE'
               \s""";

        try (
                Connection connection = DBHelper.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)
        ) {
            while (rs.next()) {
                RoomReservationDetail detail = extractRoomReservationDetailFromResultSet(rs);
                data.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return data;
    }

    public static RoomReservationDetail getById(String id) {
        String sql = """
                SELECT rrd.roomReservationDetailID, rrd.dateChanged, rrd.roomID, 
                       rrd.reservationFormID, rrd.employeeID,
                       r.roomStatus, r.dateOfCreation, r.roomCategoryID,
                       rf.reservationDate, rf.checkInDate, rf.checkOutDate,
                       e.fullName, e.phoneNumber, e.email, e.address, e.gender, e.idCardNumber, e.dob, e.position
                FROM RoomReservationDetail rrd
                INNER JOIN Room r ON rrd.roomID = r.roomID
                INNER JOIN ReservationForm rf ON rrd.reservationFormID = rf.reservationFormID
                INNER JOIN Employee e ON rrd.employeeID = e.employeeID
                WHERE rrd.roomReservationDetailID = ?
                AND r.isActivate = 'ACTIVATE' AND e.isActivate = 'ACTIVATE'
                """;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractRoomReservationDetailFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public static void createData(RoomReservationDetail detail) {
        String insertSql = """
            INSERT INTO RoomReservationDetail(roomReservationDetailID, dateChanged, roomID, 
                                              reservationFormID, employeeID) 
            VALUES (?, ?, ?, ?, ?)
            """;

        String selectNextIDSql = "SELECT nextID FROM GlobalSequence WHERE tableName = 'RoomReservationDetail'";
        String updateNextIDSql = "UPDATE GlobalSequence SET nextID = ? WHERE tableName = 'RoomReservationDetail'";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement selectNextIDStatement = connection.prepareStatement(selectNextIDSql);
                PreparedStatement updateNextIDStatement = connection.prepareStatement(updateNextIDSql);
                PreparedStatement insertStatement = connection.prepareStatement(insertSql)
        ) {
            // Lấy nextID hiện tại từ GlobalSequence
            ResultSet rs = selectNextIDStatement.executeQuery();
            if (rs.next()) {
                String currentNextID = rs.getString("nextID");
                String prefix = GlobalConstants.ROOM_RESERVATION_DETAIL_PREFIX + "-";

                // Tăng giá trị ID
                int nextIDNum = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
                String newNextID = prefix + String.format("%06d", nextIDNum);

                // Cập nhật nextID trong GlobalSequence
                updateNextIDStatement.setString(1, newNextID);
                updateNextIDStatement.executeUpdate();

                insertStatement.setString(1, currentNextID);
                insertStatement.setTimestamp(2, ConvertHelper.localDateTimeToSQLConverter(detail.getDateChanged()));
                insertStatement.setString(3, detail.getRoom().getRoomID());
                insertStatement.setString(4, detail.getReservationForm().getReservationID());
                insertStatement.setString(5, detail.getEmployee().getEmployeeID());

                insertStatement.executeUpdate();
            } else {
                throw new IllegalStateException("Không thể lấy nextID từ GlobalSequence.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void updateData(RoomReservationDetail detail) {
        String sql = """
                UPDATE RoomReservationDetail
                SET dateChanged = ?, roomID = ?, reservationFormID = ?, employeeID = ?
                WHERE roomReservationDetailID = ?
                """;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setTimestamp(1, ConvertHelper.localDateTimeToSQLConverter(detail.getDateChanged()));
            ps.setString(2, detail.getRoom().getRoomID());
            ps.setString(3, detail.getReservationForm().getReservationID());
            ps.setString(4, detail.getEmployee().getEmployeeID());
            ps.setString(5, detail.getRoomReservationDetailID());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void deleteData(String id) {
        String sql = "DELETE FROM RoomReservationDetail WHERE roomReservationDetailID = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static String getNextID() {
        String nextID = "RRD-000001";

        String query = "SELECT nextID FROM GlobalSequence WHERE tableName = ?";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, "RoomReservationDetail");
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                nextID = rs.getString(1);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

        return nextID;
    }

    public static List<RoomReservationDetail> getByReservationFormID(String reservationFormID) {
        List<RoomReservationDetail> data = new ArrayList<>();
        String sql = """
            SELECT rrd.roomReservationDetailID, rrd.dateChanged, rrd.roomID, 
                   rrd.reservationFormID, rrd.employeeID,
                   r.roomStatus, r.dateOfCreation, r.roomCategoryID,
                   rf.reservationDate, rf.checkInDate, rf.checkOutDate,
                   e.fullName, e.phoneNumber, e.email, e.address, e.gender, e.idCardNumber, e.dob, e.position
            FROM RoomReservationDetail rrd
            INNER JOIN Room r ON rrd.roomID = r.roomID
            INNER JOIN ReservationForm rf ON rrd.reservationFormID = rf.reservationFormID
            INNER JOIN Employee e ON rrd.employeeID = e.employeeID
            WHERE rrd.reservationFormID = ?
            AND r.isActivate = 'ACTIVATE' AND e.isActivate = 'ACTIVATE'
            """;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setString(1, reservationFormID);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                RoomReservationDetail detail = extractRoomReservationDetailFromResultSet(rs);
                data.add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return data;
    }

    private static RoomReservationDetail extractRoomReservationDetailFromResultSet(ResultSet rs) throws SQLException {
        RoomReservationDetail detail = new RoomReservationDetail();
        Room room = new Room();
        ReservationForm reservationForm = new ReservationForm();
        Employee employee = new Employee();

        detail.setRoomReservationDetailID(rs.getString("roomReservationDetailID"));
        detail.setDateChanged(ConvertHelper.localDateTimeConverter(rs.getTimestamp("dateChanged")));

        room.setRoomID(rs.getString("roomID"));
        room.setRoomStatus(ConvertHelper.roomStatusConverter(rs.getString("roomStatus")));
        room.setDateOfCreation(ConvertHelper.localDateTimeConverter(rs.getTimestamp("dateOfCreation")));

        reservationForm.setReservationID(rs.getString("reservationFormID"));
        reservationForm.setReservationDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("reservationDate")));
        reservationForm.setCheckInDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkInDate")));
        reservationForm.setCheckOutDate(ConvertHelper.localDateTimeConverter(rs.getTimestamp("checkOutDate")));

        employee.setEmployeeID(rs.getString("employeeID"));
        employee.setFullName(rs.getString("fullName"));
        employee.setPhoneNumber(rs.getString("phoneNumber"));
        employee.setEmail(rs.getString("email"));
        employee.setAddress(rs.getString("address"));
        employee.setGender(ConvertHelper.genderConverter(rs.getString("gender")));
        employee.setIdCardNumber(rs.getString("idCardNumber"));
        employee.setDob(ConvertHelper.localDateConverter(rs.getDate("dob")));
        employee.setPosition(ConvertHelper.positionConverter(rs.getString("position")));

        detail.setRoom(room);
        detail.setReservationForm(reservationForm);
        detail.setEmployee(employee);

        return detail;
    }
}
