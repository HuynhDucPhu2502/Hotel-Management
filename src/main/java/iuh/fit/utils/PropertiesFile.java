package iuh.fit.utils;

import java.io.*;
import java.util.Properties;

public class PropertiesFile {

    public static void writeFile(String filePath, String key, String value, String comment){
        Properties properties = new Properties();
        properties.setProperty(key, value);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            properties.store(fos, comment);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFile(String filePath, String key){
        Properties properties = new Properties();
        try(FileInputStream fis = new FileInputStream(filePath)){
            properties.load(fis);
            return properties.getProperty(key);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
