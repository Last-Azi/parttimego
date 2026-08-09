package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("interview")
public class Interview {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long applicationId;

    private Long jobId;

    private Long employerId;

    private Long studentId;

    private LocalDateTime interviewTime;

    private String interviewPlace;

    private String interviewType;

    private String interviewContent;

    private Integer status;

    private String studentRemark;

    private String employerRemark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
