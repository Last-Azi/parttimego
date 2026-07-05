package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewVO {

    private Long id;
    private Long applicationId;
    private Long fromUserId;
    private String fromUserName;
    private Long toUserId;
    private String toUserName;
    private Long jobId;
    private String jobTitle;
    private Integer rating;
    private String content;
    private LocalDateTime createTime;
}
