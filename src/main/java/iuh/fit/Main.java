package iuh.fit;

import iuh.fit.dao.*;
import iuh.fit.models.Account;
import iuh.fit.models.Employee;
import iuh.fit.models.RoomUsageService;
import iuh.fit.models.ServiceCategory;
import iuh.fit.models.enums.AccountStatus;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//        EmployeeDAO.getEmployees().forEach(System.out::println);
//        ShiftDAO.getShifts().forEach(System.out::println);
//        ServiceCategoryDAO.getServiceCategory().forEach(System.out::println);
//        HotelServiceDAO.getHotelService().forEach(System.out::println);
//        RoomUsageServiceDAO.getRoomUsageService().forEach(System.out::println);
        CustomerDAO.getCustomer().forEach(System.out::println);
    }
}