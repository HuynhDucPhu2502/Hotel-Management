package iuh.fit.models;

import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.time.LocalDateTime;
import java.util.Objects;

public class RoomUsageService {
    private String roomUsageServiceId;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
    private HotelService hotelService;
    private ReservationForm reservationForm;
    private Employee employee;
    private LocalDateTime dateAdded;

    public RoomUsageService(String roomUsageServiceId, int quantity, double unitPrice,
                            HotelService hotelService, ReservationForm reservationForm, Employee employee,
                            LocalDateTime dateAdded) {
        this.setRoomUsageServiceId(roomUsageServiceId);
        this.setQuantity(quantity);
        this.setUnitPrice(unitPrice);
        this.setHotelService(hotelService);
        this.setReservationForm(reservationForm);
        this.setEmployee(employee);
        this.setDateAdded(dateAdded);
        this.calculateTotalPrice();
    }

    public RoomUsageService(int quantity, double unitPrice, HotelService hotelService,
                            ReservationForm reservationForm, Employee employee, LocalDateTime dateAdded) {
        this.setQuantity(quantity);
        this.setUnitPrice(unitPrice);
        this.setHotelService(hotelService);
        this.setReservationForm(reservationForm);
        this.setEmployee(employee);
        this.setDateAdded(dateAdded);
        this.calculateTotalPrice();
    }

    public RoomUsageService() {
    }

    private void calculateTotalPrice() {
        if (this.quantity > 0 && this.unitPrice > 0) {
            this.totalPrice = this.quantity * this.unitPrice;
        } else {
            this.totalPrice = 0;
        }
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setRoomUsageServiceId(String roomUsageServiceId) {
        if (roomUsageServiceId == null || roomUsageServiceId.trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_SERVICE_INVALID_ID_ISNULL);
        }
        if (!RegexChecker.isValidIDFormat(GlobalConstants.ROOM_USAGE_SERVICE_PREFIX, roomUsageServiceId)) {
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_SERVICE_INVALID_ID_FORMAT);
        }
        this.roomUsageServiceId = roomUsageServiceId;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_SERVICE_INVALID_QUANTITY);
        this.quantity = quantity;
        this.calculateTotalPrice();
    }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice <= 0)
            throw new IllegalArgumentException(ErrorMessages.ROOM_USAGE_SERVICE_INVALID_UNIT_PRICE);
        this.unitPrice = unitPrice;
        this.calculateTotalPrice();
    }

    public void setHotelService(HotelService hotelService) {
        if (hotelService == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_HOTELSERVICE);
        this.hotelService = hotelService;
    }

    public void setReservationForm(ReservationForm reservationForm) {
        if (reservationForm == null)
            throw new IllegalArgumentException(ErrorMessages.NULL_RESERVATIONFORM);
        this.reservationForm = reservationForm;
    }

    public HotelService getHotelService() {
        return hotelService;
    }

    public ReservationForm getReservationForm() {
        return reservationForm;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getRoomUsageServiceId() {
        return roomUsageServiceId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public Employee getEmployee() {
        return employee;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomUsageService that = (RoomUsageService) o;
        return Objects.equals(roomUsageServiceId, that.roomUsageServiceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomUsageServiceId);
    }


    @Override
    public String toString() {
        return "RoomUsageService{" +
                "roomUsageServiceId='" + roomUsageServiceId + '\'' +
                ", quantity=" + quantity +
                ", hotelService=" + hotelService +
                '}';
    }
}
