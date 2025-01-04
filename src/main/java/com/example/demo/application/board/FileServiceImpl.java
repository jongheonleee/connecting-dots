package com.example.demo.application.board;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl {

    public String uploadFile(String updloadPath, String originalFileName,
            byte[] fileData) throws Exception {
        var uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName
                .lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFileUrl = updloadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFileUrl);

        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        var deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
        }

    }
}
