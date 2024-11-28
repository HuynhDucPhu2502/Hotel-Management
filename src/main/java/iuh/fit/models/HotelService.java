package iuh.fit.models;

import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;

public class HotelService {
    private String serviceId;
    private String serviceName;
    private String description;
    private double servicePrice;
    private ServiceCategory serviceCategory;
    private ObjectStatus objectStatus;

    public HotelService() {
    }

    public HotelService(String serviceId, String serviceName, double servicePrice, String description, ServiceCategory serviceCategory, ObjectStatus objectStatus) {
        this.setServiceId(serviceId);
        this.setServiceName(serviceName);
        this.setServicePrice(servicePrice);
        this.setDescription(description);
        this.setServiceCategory(serviceCategory);
        this.setObjectStatus(objectStatus);
    }

    public HotelService(String serviceId) {
        this.setServiceId(serviceId);
    }

    public void setServiceId(String serviceId) {
        if (serviceId == null || serviceId.trim().isEmpty())
            throw new IllegalArgumentException(ErrorMessages.HOTEL_SERVICE_INVALID_ID_ISNULL);

        if (!RegexChecker.isValidIDFormat(GlobalConstants.HOTELSERVICE_PREFIX, serviceId))
            throw new IllegalArgumentException(ErrorMessages.HOTEL_SERVICE_INVALID_ID_FORMAT);

        this.serviceId = serviceId;
    }

    public void setServiceName(String serviceName) {
        if (serviceName == null || serviceName.trim().isEmpty())
            throw new IllegalArgumentException(ErrorMessages.HOTEL_SERVICE_INVALID_NAME_ISNULL);
        this.serviceName = serviceName;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException(ErrorMessages.HOTEL_SERVICE_DESCRIPTION_ISNULL);
        this.description = description;
    }

    public void setServicePrice(double servicePrice) {
        if (servicePrice < 0) throw new IllegalArgumentException(ErrorMessages.HOTEL_SERVICE_INVALID_PRICE);
        this.servicePrice = servicePrice;
    }

    public void setServiceCategory(ServiceCategory serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public void setObjectStatus(ObjectStatus objectStatus) {
        this.objectStatus = objectStatus;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDescription() {
        return description;
    }

    public double getServicePrice() {
        return servicePrice;
    }

    public ServiceCategory getServiceCategory() {
        return serviceCategory;
    }

    public ObjectStatus getObjectStatus() {
        return objectStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelService that = (HotelService) o;
        return Objects.equals(serviceId, that.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId);
    }

}
