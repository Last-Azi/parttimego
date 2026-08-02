package com.example.demo.service;

import java.util.Map;

public interface ResumeParseService {

    /**
     * 解析简历文件，提取关键信息
     * @param fileUrl 文件URL
     * @param fileName 文件名
     * @return 解析出的简历信息
     */
    Map<String, String> parseResume(String fileUrl, String fileName);
}
