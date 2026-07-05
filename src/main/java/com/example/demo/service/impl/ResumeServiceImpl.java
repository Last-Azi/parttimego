package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.dto.ResumeDTO;
import com.example.demo.dto.ResumeVO;
import com.example.demo.entity.Resume;
import com.example.demo.mapper.ResumeMapper;
import com.example.demo.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeMapper resumeMapper;

    @Override
    public void saveOrUpdate(Long userId, ResumeDTO dto) {
        Resume resume = resumeMapper.selectOne(
                new LambdaQueryWrapper<Resume>().eq(Resume::getUserId, userId));
        if (resume == null) {
            resume = new Resume();
            BeanUtil.copyProperties(dto, resume);
            resume.setUserId(userId);
            resumeMapper.insert(resume);
        } else {
            BeanUtil.copyProperties(dto, resume);
            resumeMapper.updateById(resume);
        }
    }

    @Override
    public ResumeVO getMyResume(Long userId) {
        Resume resume = resumeMapper.selectOne(
                new LambdaQueryWrapper<Resume>().eq(Resume::getUserId, userId));
        return toVO(resume);
    }

    @Override
    public ResumeVO getById(Long resumeId) {
        Resume resume = resumeMapper.selectById(resumeId);
        return toVO(resume);
    }

    private ResumeVO toVO(Resume resume) {
        if (resume == null) return null;
        ResumeVO vo = new ResumeVO();
        BeanUtil.copyProperties(resume, vo);
        return vo;
    }
}
