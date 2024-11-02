package iuh.fit.utils;

import iuh.fit.dao.PricingDAO;
import iuh.fit.dao.RoomUsageServiceDAO;
import iuh.fit.models.Pricing;
import iuh.fit.models.ReservationForm;
import iuh.fit.models.Room;
import iuh.fit.models.RoomUsageService;
import iuh.fit.models.enums.PriceUnit;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Calculator {

    // 1. Tính  tiền phòng theo số ngày lưu trú
    public static double calculateRoomCharge(Room room, LocalDateTime checkIn, LocalDateTime checkOut) {
        List<Pricing> pricingList = PricingDAO.findDataByCategoryID(room.getRoomCategory().getRoomCategoryID());

        Pricing dayPricing = pricingList.stream()
                .filter(x -> x.getPriceUnit() == PriceUnit.DAY)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PRICING_NOT_FOUND));

        Pricing hourPricing = pricingList.stream()
                .filter(x -> x.getPriceUnit() == PriceUnit.HOUR)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.PRICING_NOT_FOUND));

        double hours = calculateHours(checkIn, checkOut);


        if (hours < 12) {
            return hours * hourPricing.getPrice();
        } else {
            double roundedDays = roundToNearestHalfDay(hours / 24);
            return roundedDays * dayPricing.getPrice();
        }
    }


    // 2. Tính  tiền đặt cọc theo số ngày lưu trú
    public static double calculateBookingDeposit(Room room, LocalDateTime checkIn, LocalDateTime checkOut) {
        return calculateRoomCharge(room, checkIn, checkOut) * 0.1;
    }

    // 3. Tính số ngày lưu trú ra chuỗi
    public static String calculateStayLengthToString(LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        long hours = java.time.Duration.between(checkInTime, checkOutTime).toHours();
        double days = hours / 24.0;

        if (hours < 12) {
            return hours + " giờ";
        } else {
            double roundedDays = Math.ceil(days * 2) / 2.0;
            return roundedDays + " ngày";
        }
    }

    // 4. Tính số ngày lưu trú ra số
    public static double calculateStayLengthToDouble(LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        long hours = java.time.Duration.between(checkInTime, checkOutTime).toHours();
        double days = hours / 24.0;

        if (hours < 12) {
            return hours;
        } else {
            return Math.ceil(days * 2) / 2.0;
        }
    }

    // 5. Tính tổng chi phí dịch vụ cho reservationFormID
    public static double calculateTotalServiceCharge(String reservationFormID) {
        List<RoomUsageService> services = RoomUsageServiceDAO.getByReservationFormID(reservationFormID);

        return services.stream()
                .mapToDouble(service -> service.getQuantity() * service.getUnitPrice())
                .sum();
    }

    // 6. Tính tổng chi phí cho phòng và dịch vụ
    // (chưa trừ tiền đặt cọc và cộng thuế)
    public static double calculateTotalCharge(
            ReservationForm reservationForm
    ) {
        double roomCharge = calculateRoomCharge(reservationForm.getRoom(),
                reservationForm.getCheckInDate(), reservationForm.getCheckOutDate());

        double serviceCharge = calculateTotalServiceCharge(reservationForm.getReservationID());

        return roomCharge + serviceCharge;
    }

    // ======================================================================================================
    // Các hàm phụ

    // Tính số giờ giữa checkin và checkout
    private static double calculateHours(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException(ErrorMessages.ERROR_CHECK_OUT_BEFORE_CHECK_IN);
        }
        Duration duration = Duration.between(checkIn, checkOut);
        return duration.toHours();
    }

    // Làm tròn đến 0.5
    private static double roundToNearestHalfDay(double days) {
        return Math.ceil(days * 2) / 2.0;
    }

}
