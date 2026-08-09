package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatSessionVO {

    private Long id;

    private Long otherUserId;

    private String otherUserName;

    private String otherUserAvatar;

    private String lastMessage;

    private LocalDateTime lastTime;

    private Integer unreadCount;
}
