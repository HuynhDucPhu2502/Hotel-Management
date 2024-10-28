package iuh.fit.utils;

import iuh.fit.dao.PricingDAO;
import iuh.fit.models.Pricing;
import iuh.fit.models.Room;
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
    public static String calculateStayLength(LocalDateTime checkInTime, LocalDateTime checkOutTime) {
        long hours = java.time.Duration.between(checkInTime, checkOutTime).toHours();
        double days = hours / 24.0;

        if (hours < 12) {
            return hours + " giờ";
        } else {
            double roundedDays = Math.ceil(days * 2) / 2.0;
            return roundedDays + " ngày";
        }
    }

    // Hàm phụ
    private static double calculateHours(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException(ErrorMessages.ERROR_CHECK_OUT_BEFORE_CHECK_IN);
        }
        Duration duration = Duration.between(checkIn, checkOut);
        return duration.toHours();
    }

    // Hàm phụ
    private static double roundToNearestHalfDay(double days) {
        return Math.ceil(days * 2) / 2.0;
    }
}
