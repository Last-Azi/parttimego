package com.example.demo.service;

import com.example.demo.dto.ResumeDTO;
import com.example.demo.dto.ResumeVO;

public interface ResumeService {

    void saveOrUpdate(Long userId, ResumeDTO dto);

    ResumeVO getMyResume(Long userId);

    ResumeVO getById(Long resumeId);
}
