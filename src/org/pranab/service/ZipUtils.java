package org.pranab.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
    final byte[] BUFFER = new byte[4096];

    public JSONObject unzip(File zipFile, String destDirectory) {
        JSONObject response = new JSONObject();
        JSONArray files;
        File destFolder;
        FileInputStream fis;
        ZipInputStream zis;
        ZipEntry zipEntry;
        try {
            fis = new FileInputStream(zipFile);
            destFolder = new File(destDirectory);
            zis = new ZipInputStream(fis);
            zipEntry = zis.getNextEntry();
            files = new JSONArray();
            if (!zipFile.exists()) {
                response.put("message", "ZipFile is not available");
            }
            if (!destFolder.exists()) {
                destFolder.mkdir();
                response.put("message", "Creating dest folder");
            }
            File newFile;
            FileOutputStream fos;
            int len;
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                newFile = new File(destFolder + File.separator + fileName);
                fos = new FileOutputStream(newFile);
                files.add(zipEntry.getName());
                while ((len = zis.read(BUFFER)) > 0) {
                    fos.write(BUFFER, 0, len);
                }
                fos.close();
                zis.closeEntry();
                zipEntry = zis.getNextEntry();
            }
            response.put("files", files);
            response.put("destDirectory", destDirectory);
            response.put("message", "Unzipping is done");
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (Exception ex) {
            response.put("message", ex.getMessage());
        }
        return response;
    }

    public String removeExtension(String s) {
        String separator = System.getProperty("file.separator");
        String filename;
        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }
        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return filename;
        return filename.substring(0, extensionIndex);
    }

    public Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public JSONObject startWith(String directory, String startWith) {
        JSONObject response = new JSONObject();
        File destDirectory;
        try {
            destDirectory = new File(directory);
            File[] files = destDirectory.listFiles((dir, name) -> name.startsWith(startWith));
            if (files.length < 1) {
                response.put("files", "No file present with "+ startWith);
            }
            response.put("result",true);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", e.getMessage());
        }
        return response;
    }

    public JSONObject endWith(String directory, String endWith) {
        JSONObject response = new JSONObject();
        File destDirectory;
        try {
            destDirectory = new File(directory);
            File[] files = destDirectory.listFiles((dir, name) -> name.endsWith(endWith));
            if (files.length < 1) {
                response.put("files", "No file present with "+ endWith);
            }
            response.put("result",true);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", e.getMessage());
        }
        return response;
    }
}
