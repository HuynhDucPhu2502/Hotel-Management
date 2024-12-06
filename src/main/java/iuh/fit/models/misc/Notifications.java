package iuh.fit.models.misc;

import java.time.LocalDateTime;

/**
 * @author Le Tran Gia Huy
 * @created 05/12/2024 - 9:50 AM
 * @project HotelManagement
 * @package iuh.fit.models.misc
 */
public class Notifications {
    private int notificationID;
    private String entityID;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private boolean isRead;

    public Notifications() {
    }

    public Notifications(String content, LocalDateTime createdAt, String entityID, boolean isRead, int notificationID, String title) {
        this.content = content;
        this.createdAt = createdAt;
        this.entityID = entityID;
        this.isRead = isRead;
        this.notificationID = notificationID;
        this.title = title;
    }

    @Override
    public int hashCode() {
        return notificationID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Notifications{" +
                "content='" + content + '\'' +
                ", notificationID=" + notificationID +
                ", entityID='" + entityID + '\'' +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                ", isRead=" + isRead +
                '}';
    }
}
