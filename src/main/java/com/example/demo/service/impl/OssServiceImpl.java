package com.example.demo.service.impl;

import com.aliyun.oss.OSS;
import com.example.demo.common.BusinessException;
import com.example.demo.entity.FileRecord;
import com.example.demo.mapper.FileRecordMapper;
import com.example.demo.service.OssService;
import com.example.demo.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OssServiceImpl implements OssService {

    private final OSS ossClient;
    private final FileRecordMapper fileRecordMapper;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Override
    public String uploadAvatar(Long userId, MultipartFile file) {
        return uploadFile(userId, file, "avatar", "jpg,png,gif,jpeg,webp", 2 * 1024 * 1024);
    }

    @Override
    public String uploadResume(Long userId, MultipartFile file) {
        return uploadFile(userId, file, "resume", "pdf,doc,docx", 10 * 1024 * 1024);
    }

    @Override
    public String uploadChatFile(Long userId, MultipartFile file) {
        return uploadFile(userId, file, "chat", "jpg,png,gif,jpeg,pdf,doc,docx,xls,xlsx,zip,rar", 10 * 1024 * 1024);
    }

    private String uploadFile(Long userId, MultipartFile file, String prefix, String allowedTypes, long maxSize) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("文件不能为空");
        }

        if (file.getSize() > maxSize) {
            throw new BusinessException("文件大小超出限制");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException("文件名不能为空");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        if (!allowedTypes.contains(extension)) {
            throw new BusinessException("不支持的文件格式");
        }

        String objectName = prefix + "/" + UUID.randomUUID() + "." + extension;

        try {
            InputStream inputStream = file.getInputStream();
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (Exception e) {
            throw new BusinessException("文件上传失败");
        }

        String fileUrl = "https://" + bucketName + "." + endpoint.replace("https://", "") + "/" + objectName;

        FileRecord record = new FileRecord();
        record.setUserId(userId);
        record.setFileName(originalFilename);
        record.setFileUrl(fileUrl);
        record.setFileType(prefix.toUpperCase());
        record.setFileSize(file.getSize());
        fileRecordMapper.insert(record);

        return fileUrl;
    }

    @Override
    public void deleteFile(Long fileId) {
        FileRecord record = fileRecordMapper.selectById(fileId);
        if (record == null) {
            throw new BusinessException("文件不存在");
        }
        if (!record.getUserId().equals(UserContext.getUserId())) {
            throw new BusinessException("无权删除此文件");
        }

        String fileUrl = record.getFileUrl();
        String objectName = fileUrl.substring(fileUrl.indexOf("/", 8) + 1);
        ossClient.deleteObject(bucketName, objectName);

        fileRecordMapper.deleteById(fileId);
    }
}
