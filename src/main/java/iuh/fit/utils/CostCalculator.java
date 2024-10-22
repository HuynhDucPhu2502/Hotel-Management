package iuh.fit.utils;

import iuh.fit.dao.PricingDAO;
import iuh.fit.models.Pricing;
import iuh.fit.models.Room;
import iuh.fit.models.enums.PriceUnit;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class CostCalculator {

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

    public static double calculateBookingDeposit(Room room, LocalDateTime checkIn, LocalDateTime checkOut) {
        return calculateRoomCharge(room, checkIn, checkOut) * 0.1;
    }

    private static double calculateHours(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException(ErrorMessages.ERROR_CHECK_OUT_BEFORE_CHECK_IN);
        }
        Duration duration = Duration.between(checkIn, checkOut);
        return duration.toHours();
    }

    private static double roundToNearestHalfDay(double days) {
        return Math.ceil(days * 2) / 2.0;
    }
}
