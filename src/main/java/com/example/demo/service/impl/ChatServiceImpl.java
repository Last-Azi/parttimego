package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.dto.ChatMessageVO;
import com.example.demo.dto.ChatSessionVO;
import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.ChatSession;
import com.example.demo.entity.User;
import com.example.demo.mapper.ChatMessageMapper;
import com.example.demo.mapper.ChatSessionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatSessionMapper sessionMapper;
    private final ChatMessageMapper messageMapper;
    private final UserMapper userMapper;

    @Override
    public List<ChatSessionVO> getSessions(Long userId) {
        // 查询用户参与的所有会话
        LambdaQueryWrapper<ChatSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(ChatSession::getUser1Id, userId)
                .or().eq(ChatSession::getUser2Id, userId))
                .orderByDesc(ChatSession::getLastTime);

        List<ChatSession> sessions = sessionMapper.selectList(wrapper);

        return sessions.stream().map(session -> {
            ChatSessionVO vo = new ChatSessionVO();
            vo.setId(session.getId());
            vo.setLastMessage(session.getLastMessage());
            vo.setLastTime(session.getLastTime());

            // 确定对方用户
            Long otherUserId = session.getUser1Id().equals(userId)
                    ? session.getUser2Id() : session.getUser1Id();
            vo.setOtherUserId(otherUserId);

            // 获取对方用户信息
            User otherUser = userMapper.selectById(otherUserId);
            if (otherUser != null) {
                vo.setOtherUserName(otherUser.getNickname());
                vo.setOtherUserAvatar(otherUser.getAvatar());
            }

            // 获取未读消息数
            Long unreadCount = messageMapper.selectCount(
                    new LambdaQueryWrapper<ChatMessage>()
                            .eq(ChatMessage::getSessionId, session.getId())
                            .eq(ChatMessage::getSenderId, otherUserId)
                            .eq(ChatMessage::getIsRead, 0));
            vo.setUnreadCount(unreadCount.intValue());

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long getOrCreateSession(Long userId, Long targetUserId) {
        // 查找现有会话
        LambdaQueryWrapper<ChatSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.and(inner -> inner.eq(ChatSession::getUser1Id, userId)
                        .eq(ChatSession::getUser2Id, targetUserId))
                .or(inner -> inner.eq(ChatSession::getUser1Id, targetUserId)
                        .eq(ChatSession::getUser2Id, userId)));

        ChatSession session = sessionMapper.selectOne(wrapper);

        if (session != null) {
            return session.getId();
        }

        // 创建新会话
        session = new ChatSession();
        session.setUser1Id(userId);
        session.setUser2Id(targetUserId);
        session.setCreateTime(LocalDateTime.now());
        sessionMapper.insert(session);

        return session.getId();
    }

    @Override
    public List<ChatMessageVO> getMessages(Long userId, Long sessionId, int pageNum, int pageSize) {
        // 验证用户是否是会话参与者
        ChatSession session = sessionMapper.selectById(sessionId);
        if (session == null || (!session.getUser1Id().equals(userId) && !session.getUser2Id().equals(userId))) {
            return new ArrayList<>();
        }

        // 查询消息
        Page<ChatMessage> page = messageMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<ChatMessage>()
                        .eq(ChatMessage::getSessionId, sessionId)
                        .orderByAsc(ChatMessage::getCreateTime));

        return page.getRecords().stream().map(msg -> {
            ChatMessageVO vo = new ChatMessageVO();
            vo.setId(msg.getId());
            vo.setSessionId(msg.getSessionId());
            vo.setSenderId(msg.getSenderId());
            vo.setContent(msg.getContent());
            vo.setMsgType(msg.getMsgType());
            vo.setIsRead(msg.getIsRead());
            vo.setCreateTime(msg.getCreateTime());

            // 获取发送者信息
            User sender = userMapper.selectById(msg.getSenderId());
            if (sender != null) {
                vo.setSenderName(sender.getNickname());
                vo.setSenderAvatar(sender.getAvatar());
            }

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChatMessageVO sendMessage(Long senderId, Long sessionId, String content) {
        return sendMessage(senderId, sessionId, content, "TEXT");
    }

    @Override
    @Transactional
    public ChatMessageVO sendMessage(Long senderId, Long sessionId, String content, String msgType) {
        // 验证会话
        ChatSession session = sessionMapper.selectById(sessionId);
        if (session == null) {
            throw new RuntimeException("会话不存在");
        }

        // 保存消息
        ChatMessage message = new ChatMessage();
        message.setSessionId(sessionId);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setMsgType(msgType);
        message.setIsRead(0);
        message.setCreateTime(LocalDateTime.now());
        messageMapper.insert(message);

        // 更新会话最后消息
        String lastMsg = switch (msgType) {
            case "IMAGE" -> "[图片]";
            case "FILE" -> "[文件]";
            default -> content.length() > 50 ? content.substring(0, 50) + "..." : content;
        };
        session.setLastMessage(lastMsg);
        session.setLastTime(LocalDateTime.now());
        sessionMapper.updateById(session);

        // 构建返回值
        ChatMessageVO vo = new ChatMessageVO();
        vo.setId(message.getId());
        vo.setSessionId(sessionId);
        vo.setSenderId(senderId);
        vo.setContent(content);
        vo.setMsgType(msgType);
        vo.setIsRead(0);
        vo.setCreateTime(message.getCreateTime());

        User sender = userMapper.selectById(senderId);
        if (sender != null) {
            vo.setSenderName(sender.getNickname());
            vo.setSenderAvatar(sender.getAvatar());
        }

        return vo;
    }

    @Override
    @Transactional
    public void markAsRead(Long userId, Long sessionId) {
        // 获取对方用户ID
        ChatSession session = sessionMapper.selectById(sessionId);
        if (session == null) return;

        Long senderId = session.getUser1Id().equals(userId)
                ? session.getUser2Id() : session.getUser1Id();

        // 标记对方发送的消息为已读
        messageMapper.update(null,
                new LambdaUpdateWrapper<ChatMessage>()
                        .eq(ChatMessage::getSessionId, sessionId)
                        .eq(ChatMessage::getSenderId, senderId)
                        .eq(ChatMessage::getIsRead, 0)
                        .set(ChatMessage::getIsRead, 1));
    }

    @Override
    public Long getUnreadCount(Long userId) {
        // 获取用户的所有会话
        LambdaQueryWrapper<ChatSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w.eq(ChatSession::getUser1Id, userId)
                .or().eq(ChatSession::getUser2Id, userId));
        List<ChatSession> sessions = sessionMapper.selectList(wrapper);

        if (sessions.isEmpty()) return 0L;

        // 统计所有会话的未读消息数
        long totalUnread = 0;
        for (ChatSession session : sessions) {
            Long senderId = session.getUser1Id().equals(userId)
                    ? session.getUser2Id() : session.getUser1Id();

            Long count = messageMapper.selectCount(
                    new LambdaQueryWrapper<ChatMessage>()
                            .eq(ChatMessage::getSessionId, session.getId())
                            .eq(ChatMessage::getSenderId, senderId)
                            .eq(ChatMessage::getIsRead, 0));
            totalUnread += count;
        }

        return totalUnread;
    }
}
