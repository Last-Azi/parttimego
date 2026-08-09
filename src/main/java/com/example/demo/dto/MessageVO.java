package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageVO {

    private Long id;
    private String type;
    private String title;
    private String content;
    private Long relatedId;
    private Integer isRead;
    private String isReadText;
    private LocalDateTime createTime;
}
