package com.example.demo.mq;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.dto.ResumeParseMessage;
import com.example.demo.entity.Resume;
import com.example.demo.mapper.ResumeMapper;
import com.example.demo.service.ResumeParseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResumeParseConsumer {

    private final ResumeParseService resumeParseService;
    private final ResumeMapper resumeMapper;

    @RabbitListener(queues = RabbitMQConfig.RESUME_PARSE_QUEUE)
    public void handleParseMessage(ResumeParseMessage message) {
        log.info("收到简历解析消息: userId={}, fileName={}", message.getUserId(), message.getFileName());

        try {
            // 1. 解析简历
            Map<String, String> result = resumeParseService.parseResume(
                    message.getFileUrl(), message.getFileName());

            // 打印解析结果
            log.info("解析结果: {}", result);

            // 2. 更新简历表
            Resume resume = resumeMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Resume>()
                            .eq(Resume::getUserId, message.getUserId()));

            if (resume == null) {
                // 如果没有简历记录，创建一个新的
                resume = new Resume();
                resume.setUserId(message.getUserId());
            }

            // 3. 填充解析结果
            if (result.get("realName") != null) resume.setRealName(result.get("realName"));
            if (result.get("gender") != null) resume.setGender(result.get("gender"));
            if (result.get("phone") != null) resume.setPhone(result.get("phone"));
            if (result.get("email") != null) resume.setEmail(result.get("email"));
            if (result.get("school") != null) resume.setSchool(result.get("school"));
            if (result.get("major") != null) resume.setMajor(result.get("major"));
            if (result.get("grade") != null) resume.setGrade(result.get("grade"));
            if (result.get("skills") != null) resume.setSkills(result.get("skills"));
            if (result.get("experience") != null) resume.setExperience(result.get("experience"));
            if (result.get("projectExperience") != null) resume.setProjectExperience(result.get("projectExperience"));

            // 4. 设置附件URL
            resume.setAttachmentUrl(message.getFileUrl());

            // 5. 保存或更新
            if (resume.getId() == null) {
                resumeMapper.insert(resume);
            } else {
                resumeMapper.updateById(resume);
            }

            log.info("简历解析完成: userId={}", message.getUserId());

        } catch (Exception e) {
            log.error("简历解析失败: userId={}, error={}", message.getUserId(), e.getMessage());
        }
    }
}
