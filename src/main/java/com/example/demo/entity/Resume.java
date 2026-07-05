package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("resume")
public class Resume {

    @TableId(type = IdType.AUTO)
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

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
