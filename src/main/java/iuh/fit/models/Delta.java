package iuh.fit.models;

/**
 * @author Le Tran Gia Huy
 * @created 09/11/2024 - 3:14 PM
 * @project HotelManagement
 * @package iuh.fit.models
 */
public class Delta {
    double x, y;

    public Delta(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Delta() {
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Delta{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}