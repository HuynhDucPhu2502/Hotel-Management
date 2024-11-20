package iuh.fit.utils;

import java.util.prefs.Preferences;

public class FilePathManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(FilePathManager.class);

    // Lưu đường dẫn cho một chức năng cụ thể
    public static void savePath(String functionKey, String path) {
        prefs.put(functionKey, path);
    }

    // Lấy đường dẫn cho một chức năng cụ thể
    public static String getPath(String functionKey, String defaultPath) {
        return prefs.get(functionKey, defaultPath);
    }

    // Xóa đường dẫn của một chức năng
    public static void removePath(String functionKey) {
        prefs.remove(functionKey);
    }

    // Xóa tất cả đường dẫn
    public static void clearAllPaths() {
        try {
            prefs.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
