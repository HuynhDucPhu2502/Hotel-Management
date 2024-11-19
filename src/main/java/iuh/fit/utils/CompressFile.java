package iuh.fit.utils;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import java.io.File;

public class CompressFile {
    public static void compressWithPassword(String sourceFile, String zipFile, String password) {
        try {
            ZipFile zip = new ZipFile(zipFile);

            if (password != null && !password.isEmpty()) {
                zip.setPassword(password.toCharArray());
            }

            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(CompressionMethod.DEFLATE);
            if (password != null && !password.isEmpty()) {
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(EncryptionMethod.AES);
            }

            zip.addFile(new File(sourceFile), parameters);

            System.out.println("Nén file thành công: " + zipFile);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
