package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;

public class ShiftAssignment {
    private String shiftAssignmentId;
    private String description;
    private Shift shift;
    private Employee employee;

    public ShiftAssignment(String shiftAssignmentId, String description, Shift shift, Employee employee) {
        this.setShiftAssignmentId(shiftAssignmentId);
        this.setDescription(description);
        this.setShift(shift);
        this.setEmployee(employee);
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
        if (!RegexChecker.isValidIDFormat(GlobalConstants.SHIFTASSIGNMENT_PREFIX, shiftAssignmentId))
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
            throw new IllegalArgumentException(ErrorMessages.NULL_EMPLOYEE);
        this.employee = employee;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        if (shift == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_SHIFT);
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
