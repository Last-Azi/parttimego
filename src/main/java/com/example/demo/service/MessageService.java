package com.example.demo.service;

import com.example.demo.common.PageResult;
import com.example.demo.dto.MessageVO;

public interface MessageService {

    void send(Long userId, String type, String title, String content, Long relatedId);

    long getUnreadCount(Long userId);

    PageResult<MessageVO> getList(Long userId, Integer isRead, int pageNum, int pageSize);

    void markAsRead(Long userId, Long messageId);

    void markAllAsRead(Long userId);
}
