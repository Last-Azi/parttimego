package com.example.demo.controller;

import com.example.demo.annotation.OperLog;
import com.example.demo.common.PageResult;
import com.example.demo.common.R;
import com.example.demo.dto.ApplicationVO;
import com.example.demo.service.ApplicationService;
import com.example.demo.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "投递模块")
@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @Operation(summary = "投递岗位（学生）")
    @PostMapping("/{jobId}")
    @OperLog(module = "投递模块", operation = "投递岗位")
    public R<Void> apply(@PathVariable Long jobId) {
        applicationService.apply(UserContext.getUserId(), jobId);
        return R.ok();
    }

    @Operation(summary = "撤回投递（学生）")
    @DeleteMapping("/{id}")
    public R<Void> withdraw(@PathVariable Long id) {
        applicationService.withdraw(UserContext.getUserId(), id);
        return R.ok();
    }

    @Operation(summary = "更新投递状态（招聘方）")
    @PutMapping("/{id}/status")
    public R<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status,
            @RequestParam(required = false) String remark) {
        applicationService.updateStatus(UserContext.getUserId(), id, status, remark);
        return R.ok();
    }

    @Operation(summary = "我的投递记录（学生）")
    @GetMapping("/mine")
    public R<PageResult<ApplicationVO>> mine(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(applicationService.myApplications(UserContext.getUserId(), status, pageNum, pageSize));
    }

    @Operation(summary = "岗位投递列表（招聘方）")
    @GetMapping("/job/{jobId}")
    public R<PageResult<ApplicationVO>> jobApplications(
            @PathVariable Long jobId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(applicationService.jobApplications(UserContext.getUserId(), jobId, status, pageNum, pageSize));
    }
}
