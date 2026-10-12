package com.example.springhibernatedatajpa.common.service.Impl;

import com.example.springhibernatedatajpa.common.GlobalConfig;
import com.example.springhibernatedatajpa.common.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements IFileService {

    private final Path storageDirectory;

    public FileServiceImpl() {
        this.storageDirectory = Paths.get(GlobalConfig.FOLDER_PATH).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.storageDirectory);
        } catch (IOException e) {
            log.error("Không thể khởi tạo thư mục lưu trữ gốc: {}", storageDirectory, e);
            throw new RuntimeException("Không thể khởi tạo thư mục lưu trữ gốc", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file, String folderType) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File tải lên không được để trống");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"
        );

        String subFolder = (folderType != null && !folderType.isBlank()) ? folderType.trim() : "others";
        validateFileType(originalFilename, subFolder);

        Path folderPath = this.storageDirectory.resolve(subFolder).normalize();
        try {
            Files.createDirectories(folderPath);
        } catch (IOException e) {
            log.error("Không thể tạo thư mục phân loại: {}", folderPath, e);
            throw new RuntimeException("Không thể tạo thư mục: " + subFolder, e);
        }

        String savedFileName = UUID.randomUUID() + "_" + originalFilename;
        Path targetLocation = folderPath.resolve(savedFileName);
        try (InputStream in = new BufferedInputStream(file.getInputStream());
             OutputStream out = new BufferedOutputStream(
                     Files.newOutputStream(targetLocation, StandardOpenOption.CREATE, StandardOpenOption.WRITE)
             )) {

            byte[] buffer = new byte[GlobalConfig.BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();

            log.info("Lưu file thành công vào folder [{}]: {}", subFolder, targetLocation);
            return savedFileName;
        } catch (IOException e) {
            try {
                Files.deleteIfExists(targetLocation);
            } catch (IOException ignored) {}

            log.error("Lỗi khi lưu file vào folder [{}]: {}", subFolder, originalFilename, e);
            throw new RuntimeException("Không thể lưu file lên server", e);
        }
    }

    @Override
    public Resource exportFile(String fileName, String folderType) {
        try {
            Path folderPath = (folderType != null && !folderType.isBlank())
                    ? this.storageDirectory.resolve(folderType.trim()).normalize()
                    : this.storageDirectory;

            Path filePath = folderPath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Không tìm thấy file: " + fileName + " trong thư mục: " + folderType);
            }
        } catch (MalformedURLException | FileNotFoundException e) {
            log.error("Lỗi khi tìm kiếm file để xuất: folder=[{}], file=[{}]", folderType, fileName, e);
            throw new RuntimeException("File không tồn tại hoặc không thể đọc được: " + fileName, e);
        }
    }

    @Override
    public Resource exportFile(String fileName) {
        return exportFile(fileName, null);
    }

    @Override
    public String getContentType(String fileName) {
        if (fileName == null) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith(".pdf")) {
            return MediaType.APPLICATION_PDF_VALUE;
        } else if (lowerName.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (lowerName.endsWith(".doc")) {
            return "application/msword";
        } else if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (lowerName.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (lowerName.endsWith(".webp")) {
            return "image/webp";
        } else if (lowerName.endsWith(".gif")) {
            return MediaType.IMAGE_GIF_VALUE;
        }
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }

    private void validateFileType(String filename, String folderType) {
        String lowerName = filename.toLowerCase();
        if ("img".equalsIgnoreCase(folderType) || "image".equalsIgnoreCase(folderType) || "avatar".equalsIgnoreCase(folderType)) {
            if (!lowerName.endsWith(".jpg") && !lowerName.endsWith(".jpeg")
                    && !lowerName.endsWith(".png") && !lowerName.endsWith(".webp") && !lowerName.endsWith(".gif")) {
                throw new IllegalArgumentException("Thư mục ảnh chỉ hỗ trợ các định dạng: .jpg, .jpeg, .png, .webp, .gif");
            }
        } else if ("contract".equalsIgnoreCase(folderType) || "contracts".equalsIgnoreCase(folderType) || "doc".equalsIgnoreCase(folderType)) {
            if (!lowerName.endsWith(".pdf") && !lowerName.endsWith(".docx") && !lowerName.endsWith(".doc")) {
                throw new IllegalArgumentException("Hợp đồng/Tài liệu chỉ hỗ trợ các định dạng: .pdf, .docx, .doc");
            }
        }
    }
}
