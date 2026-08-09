package com.example.demo.controller;

import com.example.demo.common.PageResult;
import com.example.demo.common.R;
import com.example.demo.dto.MessageVO;
import com.example.demo.service.MessageService;
import com.example.demo.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "消息通知")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "获取未读消息数量")
    @GetMapping("/unread/count")
    public R<Long> getUnreadCount() {
        return R.ok(messageService.getUnreadCount(UserContext.getUserId()));
    }

    @Operation(summary = "获取消息列表")
    @GetMapping("/list")
    public R<PageResult<MessageVO>> getList(
            @RequestParam(required = false) Integer isRead,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(messageService.getList(UserContext.getUserId(), isRead, pageNum, pageSize));
    }

    @Operation(summary = "标记单条消息已读")
    @PutMapping("/{id}/read")
    public R<Void> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(UserContext.getUserId(), id);
        return R.ok();
    }

    @Operation(summary = "标记所有消息已读")
    @PutMapping("/read/all")
    public R<Void> markAllAsRead() {
        messageService.markAllAsRead(UserContext.getUserId());
        return R.ok();
    }
}
