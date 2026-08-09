package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageVO {

    private Long id;

    private Long sessionId;

    private Long senderId;

    private String senderName;

    private String senderAvatar;

    private String content;

    private String msgType;

    private Integer isRead;

    private LocalDateTime createTime;
}
