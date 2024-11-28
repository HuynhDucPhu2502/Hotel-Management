package iuh.fit.models.wrapper;

import iuh.fit.models.Shift;

/**
 * @author Le Tran Gia Huy
 * @created 20/11/2024 - 11:46 AM
 * @project HotelManagement
 * @package iuh.fit.models.wrapper
 */
public class CheckedShift {
    private Shift shift;
    private boolean isChecked;

    public CheckedShift() {
    }

    public CheckedShift(Shift shift, boolean isChecked) {
        this.shift = shift;
        this.isChecked = isChecked;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckedShift that)) return false;

        return shift.equals(that.shift);
    }

    @Override
    public int hashCode() {
        return shift.hashCode();
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "CheckedShift{" +
                "isChecked=" + isChecked +
                ", shift=" + shift +
                '}';
    }
}
