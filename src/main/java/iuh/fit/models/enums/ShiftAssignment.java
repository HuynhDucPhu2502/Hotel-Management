package iuh.fit.models.enums;

import iuh.fit.models.Employee;
import iuh.fit.models.Shift;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;

public class ShiftAssignment {
    private String shiftAssignmentId;
    private String description;
    private Shift shift;
    private Employee employee;

    public ShiftAssignment(String shiftAssignmentId, String description, Shift shift, Employee employee) {
        this.shiftAssignmentId = shiftAssignmentId;
        this.description = description;
        this.shift = shift;
        this.employee = employee;
    }

    public ShiftAssignment(String shiftAssignmentId) {
        this.shiftAssignmentId = shiftAssignmentId;
    }

    public ShiftAssignment() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShiftAssignment that)) return false;
        return Objects.equals(shiftAssignmentId, that.shiftAssignmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(shiftAssignmentId);
    }

    public String getShiftAssignmentId() {
        return shiftAssignmentId;
    }

    public void setShiftAssignmentId(String shiftAssignmentId) {
        if (!RegexChecker.isValidIDFormat("SA", shiftAssignmentId))
            throw new IllegalArgumentException(ErrorMessages.SHIFTASSIGNMENT_INVALID_ID);
        this.shiftAssignmentId = shiftAssignmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description.isBlank())
            throw new IllegalArgumentException(ErrorMessages.SHIFTASSIGNMENT_INVALID_DESCRIPTION);
        this.description = description;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        if (employee == null)
            throw new IllegalArgumentException(ErrorMessages.SHIFTASSIGNMENT_INVALID_EMPLOYEE_ISNULL);
        this.employee = employee;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        if (shift == null)
            throw new IllegalArgumentException(ErrorMessages.SHIFTASSIGNMENT_INVALID_SHIFT_ISNULL);
        this.shift = shift;
    }

    @Override
    public String toString() {
        return "ShiftAssignment{" +
                "shiftAssignmentId='" + shiftAssignmentId + '\'' +
                ", description='" + description + '\'' +
                ", shift=" + shift +
                ", employee=" + employee +
                '}';
    }
}
