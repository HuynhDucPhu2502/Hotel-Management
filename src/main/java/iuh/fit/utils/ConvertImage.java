package iuh.fit.utils;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ConvertImage {
    public static String encodeImageToBase64(File imageFile) throws IOException {
        try {
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            if (imageBytes.length == 0) {
                throw new IOException("File rỗng: " + imageFile.getAbsolutePath());
            }
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            return base64String;

        } catch (IOException e) {
            System.err.println("Lỗi khi đọc/chuyển đổi file: " + e.getMessage());
            throw e;
        }
    }

    public static File decodeBase64ToImage(String base64Image) throws IOException {
        try {
            String formatName = "png";
            if (base64Image.contains(",")) {
                String header = base64Image.split(",")[0];
                if (header.contains("jpeg")) {
                    formatName = "jpg";
                } else if (header.contains("png")) {
                    formatName = "png";
                } else if (header.contains("gif")) {
                    formatName = "gif";
                } else if (header.contains("bmp")) {
                    formatName = "bmp";
                }
                base64Image = base64Image.split(",")[1];
            }

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(bis);

            if (image == null) {
                throw new IOException("Không thể đọc ảnh từ dữ liệu Base64");
            }

            File tempFile = File.createTempFile("decodedImage", "." + formatName);
            ImageIO.write(image, formatName, tempFile);

            bis.close();
            return tempFile;
        } catch (IOException e) {
            System.err.println("Lỗi khi chuyển đổi Base64 thành ảnh: " + e.getMessage());
            throw e;
        }
    }

}
