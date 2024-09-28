package iuh.fit;

import iuh.fit.dao.*;
import iuh.fit.models.ServiceCategory;

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
//        testServiceCategoryDAO();
//        System.out.println();
//        AccountDAO.getEmployees().forEach(System.out::println);
//        System.out.println();
//        System.out.println(AccountDAO.getLogin("huynhducphu", "test123@"));
//        System.out.println();
        testServiceCategoryDAO();
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
