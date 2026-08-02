package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ResumeParseService {

    /**
     * 解析简历文件，提取关键信息
     * @param fileUrl 文件URL
     * @param fileName 文件名
     * @return 解析出的简历信息
     */
    Map<String, String> parseResume(String fileUrl, String fileName);

    /**
     * 直接解析上传的简历文件，避免依赖文件URL可公开访问
     * @param file 上传的简历文件
     * @return 解析出的简历信息
     */
    Map<String, String> parseResume(MultipartFile file);
}
