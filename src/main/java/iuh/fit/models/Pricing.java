package iuh.fit.models;

import iuh.fit.models.enums.PriceUnit;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;

/**
 * Lớp Pricing đại diện cho thông tin về định giá về phòng khách sạn.
 */
public class Pricing {
    // ID định giá
    private String pricingID;
    // Đơn vị tính giá (ví dụ: theo giờ, theo ngày)
    private PriceUnit priceUnit;
    // Mức giá
    private double price;
    private RoomCategory roomCategory;

    /**
     * Hàm khởi tạo đầy đủ cho lớp Pricing.
     *
     * @param pricingID Mã định danh của định giá.
     * @param priceUnit Đơn vị tính giá (PriceUnit).
     * @param price Mức giá.
     */
    public Pricing(String pricingID, PriceUnit priceUnit, double price, RoomCategory roomCategory) {
        setPricingID(pricingID);
        setPriceUnit(priceUnit);
        setPrice(price);
        setRoomCategory(roomCategory);
    }

    /**
     * Hàm khởi tạo mặc định cho lớp Pricing.
     */
    public Pricing() {
    }

    /**
     * Hàm khởi tạo với chỉ pricingID.
     *
     * @param pricingID Mã định danh của định giá.
     */
    public Pricing(String pricingID) {
        setPricingID(pricingID);
    }

    /**
     * Phương thức equals để so sánh hai đối tượng Pricing dựa trên pricingID.
     *
     * @param o Đối tượng khác cần so sánh.
     * @return true nếu pricingID của hai đối tượng là giống nhau, ngược lại false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pricing pricing)) return false;
        return Objects.equals(pricingID, pricing.pricingID);
    }

    /**
     * Phương thức hashCode để tạo ra mã hash dựa trên pricingID.
     *
     * @return mã hash của pricingID.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(pricingID);
    }

    // Getter và Setter cho các thuộc tính

    /**
     * Trả về mã định danh của định giá.
     *
     * @return Mã định danh của định giá.
     */
    public String getPricingID() {
        return pricingID;
    }

    /**
     * Thiết lập mã định danh cho định giá, kiểm tra tính hợp lệ của ID bằng Regex.
     *
     * @param pricingID Mã định danh của định giá.
     */
    public void setPricingID(String pricingID) {
        if (!RegexChecker.isValidIDFormat(GlobalConstants.PRICING_PREFIX, pricingID))
            throw new IllegalArgumentException(ErrorMessages.PRICING_INVALID_ID);
        this.pricingID = pricingID;
    }

    /**
     * Trả về đơn vị tính giá (PriceUnit).
     *
     * @return Đơn vị tính giá (PriceUnit).
     */
    public PriceUnit getPriceUnit() {
        return priceUnit;
    }

    /**
     * Thiết lập đơn vị tính giá cho định giá.
     *
     * @param priceUnit Đơn vị tính giá (PriceUnit).
     */
    public void setPriceUnit(PriceUnit priceUnit) {
        this.priceUnit = priceUnit;
    }

    /**
     * Trả về mức giá.
     *
     * @return Mức giá.
     */


    public double getPrice() {
        return price;
    }

    /**
     * Thiết lập mức giá cho định giá, đảm bảo mức giá không âm.
     *
     * @param price Mức giá cần thiết lập.
     */
    public void setPrice(double price) {
        if (price < 0)
            throw new IllegalArgumentException(ErrorMessages.PRICING_INVALID_PRICE);
        this.price = price;
    }

    public RoomCategory getRoomCategory() {
        return roomCategory;
    }

    public void setRoomCategory(RoomCategory roomCategory) {
        this.roomCategory = roomCategory;
    }

    @Override
    public String toString() {
        return "Pricing{" +
                "pricingID='" + pricingID + '\'' +
                ", priceUnit=" + priceUnit +
                ", price=" + price +
                ", roomCategory=" + roomCategory +
                '}';
    }
}
