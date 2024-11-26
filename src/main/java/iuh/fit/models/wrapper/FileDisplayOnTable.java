package iuh.fit.models.wrapper;

import java.time.LocalDateTime;
import java.util.Objects;

public class FileDisplayOnTable {
    private String name;
    private LocalDateTime dateCreated;
    private String fileType;
    private double size;
    private String filePath;

    public FileDisplayOnTable(String name, LocalDateTime dateCreated, String fileType, double size, String filePath) {
        this.name = name;
        this.dateCreated = dateCreated;
        this.fileType = fileType;
        this.size = size;
        this.filePath = filePath;
    }

    public FileDisplayOnTable(String name) {
        this.name = name;
    }

    public FileDisplayOnTable() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileDisplayOnTable that = (FileDisplayOnTable) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "FileDisplayOnTable{" +
                "name='" + name + '\'' +
                ", dateCreated=" + dateCreated +
                ", fileType='" + fileType + '\'' +
                ", size=" + size +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
