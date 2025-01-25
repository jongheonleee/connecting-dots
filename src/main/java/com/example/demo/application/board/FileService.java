package com.example.demo.application.board;

public interface FileService {

    String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception;

    String createSavedFileName(String originalFileName);

    String getExtension(String originalFileName);

    String createFileUploadUrl(String uploadPath, String savedFileName);

    void write(byte[] fileData, String filePath) throws Exception;

    void deleteFile(String filePath);
}
