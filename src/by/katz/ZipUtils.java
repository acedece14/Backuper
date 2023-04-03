package by.katz;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    private static void processDirectory(File dir, ArrayList<String> filesListInDir) throws IOException {
        var files = dir.listFiles();
        if (files == null) {
            System.out.println("cant list dir :" + dir.getAbsolutePath());
            System.exit(1);
        }
        for (var file : files) {
            if (file.isFile()) filesListInDir.add(file.getAbsolutePath());
            else processDirectory(file, filesListInDir);
        }
    }

    static void zipDirectory(File targetDir, File zipOutFilename) {
        var filesListInDir = new ArrayList<String>();
        try (var outputStream = new FileOutputStream(zipOutFilename);
             var zipOutputStream = new ZipOutputStream(outputStream)) {
            processDirectory(targetDir, filesListInDir);
            for (var filePath : filesListInDir) {
                System.out.println("Zipping " + filePath);
                var ze = new ZipEntry(filePath.substring(targetDir.getAbsolutePath().length() + 1, filePath.length()));
                zipOutputStream.putNextEntry(ze);
                try (var inputStream = new FileInputStream(filePath);) {
                    var buffer = new byte[1024];
                    for (int len; (len = inputStream.read(buffer)) > 0; )
                        zipOutputStream.write(buffer, 0, len);
                    zipOutputStream.closeEntry();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);}
    }
}
