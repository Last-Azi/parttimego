package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.service.OssService;
import com.example.demo.service.UserService;
import com.example.demo.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "文件管理")
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final OssService ossService;
    private final UserService userService;

    @Operation(summary = "上传头像")
    @PostMapping("/upload/avatar")
    public R<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Long userId = UserContext.getUserId();
        String url = ossService.uploadAvatar(userId, file);
        userService.updateAvatar(userId, url);
        return R.ok(url);
    }

    @Operation(summary = "上传简历附件")
    @PostMapping("/upload/resume")
    public R<String> uploadResume(@RequestParam("file") MultipartFile file) {
        String url = ossService.uploadResume(UserContext.getUserId(), file);
        return R.ok(url);
    }

    @Operation(summary = "上传聊天文件")
    @PostMapping("/upload/chat")
    public R<String> uploadChatFile(@RequestParam("file") MultipartFile file) {
        String url = ossService.uploadChatFile(UserContext.getUserId(), file);
        return R.ok(url);
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/{id}")
    public R<Void> deleteFile(@PathVariable Long id) {
        ossService.deleteFile(id);
        return R.ok();
    }
}
