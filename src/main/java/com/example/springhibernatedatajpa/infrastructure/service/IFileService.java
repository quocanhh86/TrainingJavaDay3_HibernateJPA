package com.example.springhibernatedatajpa.infrastructure.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    String uploadFile(MultipartFile file, String folderType);

    Resource exportFile(String fileName, String folderType);

    Resource exportFile(String fileName);

    String getContentType(String fileName);
}
