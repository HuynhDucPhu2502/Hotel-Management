package iuh.fit;

import iuh.fit.dao.ServiceCategoryDAO;
import iuh.fit.models.ServiceCategory;

public class FetchTesting {
    public static void main(String[] args) {
        ServiceCategoryDAO.getServiceCategory().forEach(System.out::println);
    }
}
