package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResumeVO {

    private Long id;
    private Long userId;
    private String realName;
    private String gender;
    private String school;
    private String major;
    private String grade;
    private String phone;
    private String email;
    private String skills;
    private String experience;
    private String selfIntro;
    private String projectExperience;
    private String expectCity;
    private String expectSalary;
    private String attachmentUrl;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
