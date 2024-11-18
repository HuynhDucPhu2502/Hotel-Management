package iuh.fit.models.wrapper;

import iuh.fit.models.Employee;

/**
 * @author Le Tran Gia Huy
 * @created 09/11/2024 - 2:43 PM
 * @project HotelManagement
 * @package iuh.fit.models
 */
public class CheckedEmployee {
    private Employee employee;
    private boolean isChecked;

    public CheckedEmployee(Employee employee, boolean isChecked) {
        this.employee = employee;
        this.isChecked = isChecked;
    }

    public CheckedEmployee() {
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckedEmployee that)) return false;

        return employee.equals(that.employee);
    }

    @Override
    public int hashCode() {
        return employee.hashCode();
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "CheckedEmployee{" +
                "employee=" + employee +
                ", isChecked=" + isChecked +
                '}';
    }
}
