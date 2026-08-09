package com.example.demo.service;

import com.example.demo.dto.ChatMessageVO;
import com.example.demo.dto.ChatSessionVO;

import java.util.List;

public interface ChatService {

    /**
     * 获取用户的聊天会话列表
     */
    List<ChatSessionVO> getSessions(Long userId);

    /**
     * 获取或创建与指定用户的会话
     */
    Long getOrCreateSession(Long userId, Long targetUserId);

    /**
     * 获取会话的历史消息
     */
    List<ChatMessageVO> getMessages(Long userId, Long sessionId, int pageNum, int pageSize);

    /**
     * 发送消息
     */
    ChatMessageVO sendMessage(Long senderId, Long sessionId, String content);

    /**
     * 发送消息（指定类型：TEXT/IMAGE/FILE）
     */
    ChatMessageVO sendMessage(Long senderId, Long sessionId, String content, String msgType);

    /**
     * 标记消息已读
     */
    void markAsRead(Long userId, Long sessionId);

    /**
     * 获取未读消息总数
     */
    Long getUnreadCount(Long userId);
}
