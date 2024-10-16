package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

@Service
@Log
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
            log.info("파일을 삭제했습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }

    }
}
