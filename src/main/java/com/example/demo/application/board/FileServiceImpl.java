package com.example.demo.application.board;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        String savedFileName = createSavedFileName(originalFileName);
        String fileUploadUrl = createFileUploadUrl(uploadPath, savedFileName);
        write(fileData, fileUploadUrl);
        return savedFileName;
    }

    public String createSavedFileName(String originalFileName) {
        var uuid = UUID.randomUUID();
        return uuid.toString() + getExtension(originalFileName);
    }

    public String getExtension(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }


    public String createFileUploadUrl(String uploadPath, String savedFileName) {
        return uploadPath + "/" + savedFileName;
    }

    public void write(byte[] fileData, String filePath) throws Exception {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(fileData);
        fos.close();
    }

    public void deleteFile(String filePath) {
        var deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
        }
    }
}
