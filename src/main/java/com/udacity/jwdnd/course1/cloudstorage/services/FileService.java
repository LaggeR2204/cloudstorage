package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void addNewFile(File file) {
        fileMapper.insert(new File(null, file.getFileName(), file.getContentType(), file.getFileSize(), file.getUserId(), file.getFileData()));
    }

    public List<File> getFilesByUserId(Integer userId) {
        return fileMapper.getAllFilesByUserId(userId);
    }

    public void deleteFileByFileId(Integer fileId, Integer userId) {
        fileMapper.delete(fileId, userId);
    }

    public File getFileByFileId(Integer fileId, Integer userId) {
        return fileMapper.getFileByFileId(fileId, userId);
    }

    public File getFileByFileName(String fileName, Integer userId) {
        return fileMapper.getFileByFileName(fileName, userId);
    }
}
