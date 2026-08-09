package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {

    /**
     * 上传头像图片
     */
    String uploadAvatar(Long userId, MultipartFile file);

    /**
     * 上传简历附件
     */
    String uploadResume(Long userId, MultipartFile file);

    /**
     * 上传聊天文件（图片/文件）
     */
    String uploadChatFile(Long userId, MultipartFile file);

    /**
     * 删除文件
     */
    void deleteFile(Long fileId);
}
