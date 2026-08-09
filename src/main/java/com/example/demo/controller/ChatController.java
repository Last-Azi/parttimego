package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.dto.ChatMessageVO;
import com.example.demo.dto.ChatSessionVO;
import com.example.demo.entity.ChatSession;
import com.example.demo.mapper.ChatSessionMapper;
import com.example.demo.service.ChatService;
import com.example.demo.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "聊天模块")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatSessionMapper sessionMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Operation(summary = "获取聊天会话列表")
    @GetMapping("/sessions")
    public R<List<ChatSessionVO>> getSessions() {
        Long userId = UserContext.getUserId();
        return R.ok(chatService.getSessions(userId));
    }

    @Operation(summary = "获取或创建与指定用户的会话")
    @PostMapping("/session/{targetUserId}")
    public R<Long> getOrCreateSession(@PathVariable Long targetUserId) {
        Long userId = UserContext.getUserId();
        return R.ok(chatService.getOrCreateSession(userId, targetUserId));
    }

    @Operation(summary = "获取会话的历史消息")
    @GetMapping("/messages/{sessionId}")
    public R<List<ChatMessageVO>> getMessages(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "50") int pageSize) {
        Long userId = UserContext.getUserId();
        return R.ok(chatService.getMessages(userId, sessionId, pageNum, pageSize));
    }

    @Operation(summary = "发送消息")
    @PostMapping("/send")
    public R<ChatMessageVO> sendMessage(@RequestBody Map<String, Object> params) {
        Long userId = UserContext.getUserId();
        Long sessionId = Long.valueOf(params.get("sessionId").toString());
        String content = params.get("content").toString();
        String msgType = params.containsKey("msgType") ? params.get("msgType").toString() : "TEXT";

        ChatMessageVO message = chatService.sendMessage(userId, sessionId, content, msgType);

        // 通过WebSocket推送给对方
        Long receiverId = getReceiverId(sessionId, userId);
        messagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/messages",
                message);

        return R.ok(message);
    }

    @Operation(summary = "标记消息已读")
    @PutMapping("/read/{sessionId}")
    public R<Void> markAsRead(@PathVariable Long sessionId) {
        Long userId = UserContext.getUserId();
        chatService.markAsRead(userId, sessionId);
        return R.ok();
    }

    @Operation(summary = "获取未读消息总数")
    @GetMapping("/unread/count")
    public R<Long> getUnreadCount() {
        Long userId = UserContext.getUserId();
        return R.ok(chatService.getUnreadCount(userId));
    }

    /**
     * WebSocket发送消息
     */
    @MessageMapping("/chat.send")
    public void sendWebSocketMessage(@Payload ChatMessageVO message) {
        ChatMessageVO savedMessage = chatService.sendMessage(
                message.getSenderId(),
                message.getSessionId(),
                message.getContent());

        // 推送给接收者
        messagingTemplate.convertAndSendToUser(
                message.getSenderId().toString(),
                "/queue/messages",
                savedMessage);
    }

    private Long getReceiverId(Long sessionId, Long senderId) {
        ChatSession session = sessionMapper.selectById(sessionId);
        if (session == null) return null;
        return session.getUser1Id().equals(senderId) ? session.getUser2Id() : session.getUser1Id();
    }
}
