package com.example.demo.controller;

import com.example.demo.common.PageResult;
import com.example.demo.common.R;
import com.example.demo.dto.InterviewDTO;
import com.example.demo.dto.InterviewVO;
import com.example.demo.service.InterviewService;
import com.example.demo.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "面试邀请")
@RestController
@RequestMapping("/interview")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @Operation(summary = "发送面试邀请")
    @PostMapping
    public R<Void> create(@Valid @RequestBody InterviewDTO dto) {
        interviewService.create(UserContext.getUserId(), dto);
        return R.ok();
    }

    @Operation(summary = "企业面试列表")
    @GetMapping("/employer/list")
    public R<PageResult<InterviewVO>> employerList(
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(interviewService.employerList(UserContext.getUserId(), jobId, status, pageNum, pageSize));
    }

    @Operation(summary = "学生面试列表")
    @GetMapping("/student/list")
    public R<PageResult<InterviewVO>> studentList(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(interviewService.studentList(UserContext.getUserId(), status, pageNum, pageSize));
    }

    @Operation(summary = "面试详情")
    @GetMapping("/{id}")
    public R<InterviewVO> getDetail(@PathVariable Long id) {
        return R.ok(interviewService.getDetail(UserContext.getUserId(), id));
    }

    @Operation(summary = "接受面试邀请")
    @PutMapping("/{id}/accept")
    public R<Void> accept(@PathVariable Long id) {
        interviewService.accept(UserContext.getUserId(), id);
        return R.ok();
    }

    @Operation(summary = "拒绝面试邀请")
    @PutMapping("/{id}/reject")
    public R<Void> reject(@PathVariable Long id, @RequestParam(required = false) String remark) {
        interviewService.reject(UserContext.getUserId(), id, remark);
        return R.ok();
    }

    @Operation(summary = "标记面试完成")
    @PutMapping("/{id}/complete")
    public R<Void> complete(@PathVariable Long id) {
        interviewService.complete(UserContext.getUserId(), id);
        return R.ok();
    }

    @Operation(summary = "取消面试邀请")
    @PutMapping("/{id}/cancel")
    public R<Void> cancel(@PathVariable Long id) {
        interviewService.cancel(UserContext.getUserId(), id);
        return R.ok();
    }
}
