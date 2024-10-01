package iuh.fit;

import iuh.fit.dao.*;
import iuh.fit.models.*;
import iuh.fit.models.enums.*;
import iuh.fit.utils.ConvertHelper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FetchTesting {
    public static void main(String[] args) {
//        EmployeeDAO.getEmployees().forEach(System.out::println);
//        System.out.println();
//        ShiftDAO.getShifts().forEach(System.out::println);
//        System.out.println();
//        RoomUsageServiceDAO.getRoomUsageService().forEach(System.out::println);
//        System.out.println();
//        HotelServiceDAO.getHotelService().forEach(System.out::println);
//        System.out.println();
//        ServiceCategoryDAO.getServiceCategory().forEach(System.out::println);
//        System.out.println();
//        AccountDAO.getEmployees().forEach(System.out::println);
//        System.out.println();
//        System.out.println(AccountDAO.getLogin("huynhducphu", "test123@"));
//        System.out.println();
//        testServiceCategoryDAO();
//        PricingDAO.getPricing().forEach(System.out::println);
//        RoomCategoryDAO.getRoomCategory().forEach(System.out::println);
//        RoomDAO.getRoom().forEach(System.out::println);
//        ReservationFormDAO.getReservationForm().forEach(System.out::println);
//        HistoryCheckinDAO.getHistoryCheckin().forEach(System.out::println);


//        Employee employee = new Employee("EMP-000007", "Vu Ba Hai", "0923456789", "vubachai@yahoo.com", "654 Long an", "004399012349", Gender.MALE, LocalDate.parse("1995-03-30"), Position.MANAGER);
//        EmployeeDAO.createData(employee);
//        System.out.println(EmployeeDAO.getDataByID("EMP-000007"));
//        Employee employee = new Employee("EMP-000007", "Vu Ba Chuc", "0923456789", "vubachai@yahoo.com", "654 Long an", "004399012349", Gender.MALE, LocalDate.parse("1995-03-30"), Position.MANAGER);
//        EmployeeDAO.updateData(employee);
//        System.out.println(EmployeeDAO.getDataByID("EMP-000007"));

//        Shift shift = new Shift("SHIFT-AM-0010", LocalTime.parse("05:00"), LocalTime.parse("11:00"), LocalDateTime.now(), ShiftDaysSchedule.SUNDAY);
//        ShiftDAO.createData(shift);
//        System.out.println(ShiftDAO.getDataByID("SHIFT-AM-0010"));
//        Shift shift = new Shift("SHIFT-AM-0010", LocalTime.parse("05:00"), LocalTime.parse("11:00"), LocalDateTime.now(), ShiftDaysSchedule.MON_WED_FRI);
//        ShiftDAO.updateData(shift);
//        System.out.println(ShiftDAO.getDataByID("SHIFT-AM-0001"));

//        Employee employee = new Employee("EMP-000005", "Vu Ba Hai", "0923456789", "vubachai@yahoo.com", "654 Long an", "004399012349", Gender.MALE, LocalDate.parse("1995-03-30"), Position.MANAGER);
//        Shift shift = new Shift("SHIFT-AM-0001", LocalTime.parse("05:00"), LocalTime.parse("11:00"), LocalDateTime.now(), ShiftDaysSchedule.SUNDAY);
//        ShiftAssignment shiftAssignment = new ShiftAssignment("SA-000005", "Test", shift, employee);
//        ShiftAssignmentDAO.createData(shiftAssignment);
//        System.out.println(ShiftAssignmentDAO.getDataByID("SA-000005"));
//        ShiftAssignment shiftAssignment = new ShiftAssignment("SA-000005", "Test1", shift, employee);
//        ShiftAssignmentDAO.updateData(shiftAssignment);
//        System.out.println(ShiftAssignmentDAO.getDataByID("SA-000005"));
//        ShiftAssignmentDAO.deleteData("SA-000005");


//        ShiftAssignmentDAO.getShiftAssignments().forEach(System.out::println);
//        Employee employee = new Employee("EMP-000005", "Vu Ba Hai", "0923456789", "vubachai@yahoo.com", "654 Long an", "004399012349", Gender.MALE, LocalDate.parse("1995-03-30"), Position.MANAGER);
//        Account account = new Account("ACC-000007", employee, "huynhducphu1", "test123@", AccountStatus.ACTIVE);
//        AccountDAO.createData(account);
//        System.out.println(AccountDAO.getDataByID("ACC-000007"));
//        AccountDAO.updateData(account);
//        System.out.println(AccountDAO.getDataByID("ACC-000007"));
//            AccountDAO.deleteData("ACC-000007");

//        RoomCategory roomCategory = new RoomCategory("RC-000007", "Test", 2);
//        RoomCategoryDAO.createData(roomCategory);
//        System.out.println(RoomCategoryDAO.getDataByID("RC-000007"));

//          RoomCategory roomCategory = new RoomCategory("RC-000002", "Test2", 2);
//          RoomCategoryDAO.updateData(roomCategory);
//          System.out.println(RoomCategoryDAO.getDataByID("RC-000007"));
//            RoomCategoryDAO.deleteData("RC-000007");

//        RoomCategory roomCategory = new RoomCategory("RC-000002", "Test2", 2);
//        Pricing pricing = new Pricing("P-000011", PriceUnit.DAY, 100000, roomCategory);
//        PricingDAO.createData(pricing);
//        System.out.println(PricingDAO.getDataByID("P-000011"));

//        RoomCategory roomCategory = new RoomCategory("RC-000002", "Test2", 2);
//        Pricing pricing = new Pricing("P-000011", PriceUnit.HOUR, 104000, roomCategory);
//        PricingDAO.updateData(pricing);
//        System.out.println(PricingDAO.getDataByID("P-000011"));
//        PricingDAO.deleteData("P-000011");
//        RoomCategory roomCategory = new RoomCategory("RC-000002", "Test2", 2);
//        Room room = new Room("T2551", RoomStatus.UNAVAILABLE, LocalDateTime.now(), roomCategory);
//        RoomDAO.updateData(room);
//        RoomDAO.createData(room);
//        System.out.println(RoomDAO.getDataByID("T2551"));
//        RoomDAO.deleteData("T2551");

//        ServiceCategory serviceCategory = new ServiceCategory("SC-000007", "Test1");
//        ServiceCategoryDAO.createData(serviceCategory);
//        ServiceCategoryDAO.updateData(serviceCategory);
//        System.out.println(ServiceCategoryDAO.getDataByID("SC-000007"));
//        ServiceCategoryDAO.deleteData("SC-000007");

//        ServiceCategory serviceCategory = new ServiceCategory("SC-000001", "Test1");
//        HotelService hotelService = new HotelService("HS-000007", "Test2",100000,"Test", serviceCategory);
//        HotelServiceDAO.createData(hotelService);
//        HotelServiceDAO.updateData(hotelService);
//        System.out.println(HotelServiceDAO.getDataByID("HS-000007"));
//        HotelServiceDAO.deleteData("HS-000007");


    }

    private static void testServiceCategoryDAO() {
        ServiceCategoryDAO.getServiceCategory().forEach(System.out::println);
        System.out.println();
        System.out.println(ServiceCategoryDAO.getDataByID("SC-000001"));
        ServiceCategoryDAO.createData(new ServiceCategory("SC-999999", "Test"));
        System.out.println(ServiceCategoryDAO.getDataByID("SC-999999"));
        ServiceCategoryDAO.deleteData("SC-999999");
    }
}
