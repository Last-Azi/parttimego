package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewDTO {

    @NotNull(message = "投递记录ID不能为空")
    private Long applicationId;

    @NotNull(message = "面试时间不能为空")
    private LocalDateTime interviewTime;

    private String interviewPlace;

    @NotBlank(message = "请选择面试方式")
    private String interviewType;

    private String interviewContent;
}
