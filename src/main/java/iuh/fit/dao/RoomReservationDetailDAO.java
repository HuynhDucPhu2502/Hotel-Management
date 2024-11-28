package iuh.fit.dao;

import iuh.fit.models.*;
import iuh.fit.utils.ConvertHelper;
import iuh.fit.utils.DBHelper;
import iuh.fit.utils.ErrorMessages;
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

    public static void createData(RoomReservationDetail detail) {
        String insertSql = """
            INSERT INTO RoomReservationDetail(roomReservationDetailID, dateChanged, roomID,\s
                                              reservationFormID, employeeID)\s
            VALUES (?, ?, ?, ?, ?)
           \s""";

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

    public static void changingRoom(String currentRoomID, String newRoomID, String reservationFormID, String employeeID) {
        String callProcedure = "{CALL RoomChanging(?, ?, ?, ?, ?)}";

        try (Connection connection = DBHelper.getConnection();
             CallableStatement callableStatement = connection.prepareCall(callProcedure)) {

            // Thiết lập tham số đầu vào cho Stored Procedure
            callableStatement.setString(1, currentRoomID); // currentRoomID
            callableStatement.setString(2, newRoomID); // newRoomID
            callableStatement.setString(3, reservationFormID); // reservationFormID
            callableStatement.setString(4, employeeID); // employeeID

            // Đăng ký tham số đầu ra cho message
            callableStatement.registerOutParameter(5, Types.VARCHAR);

            // Thực thi Stored Procedure
            callableStatement.execute();

            // Lấy thông báo từ Stored Procedure
            String message = callableStatement.getString(5);

            // Kiểm tra kết quả trả về từ Stored Procedure
            switch (message) {
                case "ROOM_CHANGING_RESERVATION_NOT_FOUND_OR_EXPIRED":
                    throw new IllegalArgumentException("Phiếu đặt phòng không tồn tại hoặc đã hết hạn.");
                case "ROOM_CHANGING_NEW_ROOM_NOT_AVAILABLE":
                    throw new IllegalArgumentException("Phòng mới không khả dụng.");
                case "ROOM_CHANGING_SUCCESS":
                    incrementAndUpdateNextID();
                    break;
                default:
                    throw new IllegalArgumentException(ErrorMessages.STORE_PROCEDURE_ERROR);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thực thi RoomChanging", e);
        }
    }

    public static void roomCheckingIn(String reservationFormID, String employeeID) {
        String callProcedure = "{CALL RoomCheckingIn(?, ?, ?)}";

        try (Connection connection = DBHelper.getConnection();
             CallableStatement callableStatement = connection.prepareCall(callProcedure)) {

            // Thiết lập tham số đầu vào cho Stored Procedure
            callableStatement.setString(1, reservationFormID);
            callableStatement.setString(2, employeeID);

            // Đăng ký tham số đầu ra
            callableStatement.registerOutParameter(3, Types.VARCHAR);

            // Thực thi Stored Procedure
            callableStatement.execute();

            // Lấy thông báo từ Stored Procedure
            String message = callableStatement.getString(3);

            // Xử lý thông báo trả về
            switch (message) {
                case "ROOM_CHECKING_IN_INVALID_RESERVATION":
                    throw new IllegalArgumentException("Phiếu đặt phòng không hợp lệ hoặc không tồn tại.");
                case "ROOM_CHECKING_IN_TIME_INVALID":
                    throw new IllegalArgumentException("Thời gian check-in không nằm trong khoảng cho phép.");
                case "ROOM_CHECKING_IN_SUCCESS":
                    HistoryCheckinDAO.incrementAndUpdateNextID();
                    incrementAndUpdateNextID();
                    break;
                default:
                    throw new IllegalArgumentException(ErrorMessages.STORE_PROCEDURE_ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thực thi RoomCheckingIn", e);
        }
    }

    private static void incrementAndUpdateNextID() {
        String selectQuery = "SELECT nextID FROM GlobalSequence WHERE tableName = 'RoomReservationDetail'";
        String updateQuery = "UPDATE GlobalSequence SET nextID = ? WHERE tableName = 'RoomReservationDetail'";
        String currentNextID;

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery)
        ) {
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                currentNextID = resultSet.getString("nextID");

                String prefix = GlobalConstants.ROOM_RESERVATION_DETAIL_PREFIX + "-";
                int numericPart = Integer.parseInt(currentNextID.substring(prefix.length())) + 1;
                String updatedNextID = prefix + String.format("%06d", numericPart);


                updateStatement.setString(1, updatedNextID);
                updateStatement.executeUpdate();

            } else {
                throw new IllegalArgumentException("Không thể tìm thấy nextID cho RoomReservationDetail");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
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
           \s""";

        try (
                Connection connection = DBHelper.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
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
