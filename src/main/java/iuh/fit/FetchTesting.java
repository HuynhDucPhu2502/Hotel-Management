package iuh.fit;

import iuh.fit.dao.*;

public class FetchTesting {
    public static void main(String[] args) {
        EmployeeDAO.getEmployees().forEach(System.out::println);
        System.out.println();
        ShiftDAO.getShifts().forEach(System.out::println);
        System.out.println();
        RoomUsageServiceDAO.getRoomUsageService().forEach(System.out::println);
        System.out.println();
        HotelServiceDAO.getHotelService().forEach(System.out::println);
        System.out.println();
        ServiceCategoryDAO.getServiceCategory().forEach(System.out::println);


    }
}
