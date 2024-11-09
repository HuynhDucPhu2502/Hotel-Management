package iuh.fit.models;

import iuh.fit.models.enums.ObjectStatus;
import iuh.fit.utils.ErrorMessages;
import iuh.fit.utils.GlobalConstants;
import iuh.fit.utils.RegexChecker;

import java.util.Objects;

public class ServiceCategory {
    private String serviceCategoryID;
    private String serviceCategoryName;
    private String icon;
    private ObjectStatus objectStatus;

    public ServiceCategory() {
    }

    public ServiceCategory(String serviceCategoryID, String serviceCategoryName, String icon, ObjectStatus objectStatus) {
        this.setServiceCategoryID(serviceCategoryID);
        this.setServiceCategoryName(serviceCategoryName);
        this.setIcon(icon);
        this.setObjectStatus(objectStatus);
    }

    public ServiceCategory(String serviceCategoryID) {
        this.setServiceCategoryID(serviceCategoryID);
    }

    public String getServiceCategoryID() {
        return serviceCategoryID;
    }

    public void setServiceCategoryID(String serviceCategoryID) {
        if (serviceCategoryID.trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessages.SERVICE_CATEGORY_INVALID_ID_ISNULL);
        }
        if(!RegexChecker.isValidIDFormat(GlobalConstants.SERVICECATEGORY_PREFIX, serviceCategoryID)){
            throw new IllegalArgumentException(ErrorMessages.SERVICE_CATEGORY_INVALID_ID_FORMAT);
        }
        this.serviceCategoryID = serviceCategoryID;
    }

    public String getServiceCategoryName() {
        return serviceCategoryName;
    }

    public void setServiceCategoryName(String serviceCategoryName) {
        serviceCategoryName = serviceCategoryName.trim().replaceAll("\\s+", " ");
        if (!RegexChecker.isValidName(serviceCategoryName, 3, 30)) {
            throw new IllegalArgumentException(ErrorMessages.SERVICE_CATEGORY_INVALID_NAME);
        }
        this.serviceCategoryName = serviceCategoryName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ObjectStatus getObjectStatus() {
        return objectStatus;
    }

    public void setObjectStatus(ObjectStatus objectStatus) {
        this.objectStatus = objectStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceCategory that = (ServiceCategory) o;
        return Objects.equals(serviceCategoryID, that.serviceCategoryID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceCategoryID);
    }

    @Override
    public String toString() {
        return "ServiceCategory{" +
                "serviceCategoryID='" + serviceCategoryID + '\'' +
                ", serviceCategoryName='" + serviceCategoryName + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
