package com.example.demo.controller;

import com.example.demo.common.PageResult;
import com.example.demo.common.R;
import com.example.demo.dto.EmployerStatsVO;
import com.example.demo.dto.JobDTO;
import com.example.demo.dto.JobQuery;
import com.example.demo.dto.JobVO;
import com.example.demo.service.JobService;
import com.example.demo.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "岗位模块")
@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @Operation(summary = "发布岗位（招聘方）")
    @PostMapping
    public R<Void> publish(@Valid @RequestBody JobDTO dto) {
        jobService.publish(UserContext.getUserId(), dto);
        return R.ok();
    }

    @Operation(summary = "修改岗位（招聘方）")
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable Long id, @Valid @RequestBody JobDTO dto) {
        jobService.update(UserContext.getUserId(), id, dto);
        return R.ok();
    }

    @Operation(summary = "下架岗位（招聘方）")
    @PutMapping("/{id}/offline")
    public R<Void> offline(@PathVariable Long id) {
        jobService.offline(UserContext.getUserId(), id);
        return R.ok();
    }

    @Operation(summary = "删除岗位（招聘方）")
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        jobService.delete(UserContext.getUserId(), id);
        return R.ok();
    }

    @Operation(summary = "搜索岗位（学生）")
    @GetMapping("/search")
    public R<PageResult<JobVO>> search(JobQuery query) {
        return R.ok(jobService.search(query));
    }

    @Operation(summary = "岗位详情")
    @GetMapping("/{id}")
    public R<JobVO> detail(@PathVariable Long id) {
        return R.ok(jobService.getDetail(id));
    }

    @Operation(summary = "我的岗位（招聘方）")
    @GetMapping("/mine")
    public R<PageResult<JobVO>> mine(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(jobService.myJobs(UserContext.getUserId(), status, pageNum, pageSize));
    }

    @Operation(summary = "审核通过（管理员）")
    @PutMapping("/{id}/approve")
    public R<Void> approve(@PathVariable Long id) {
        jobService.approve(id);
        return R.ok();
    }

    @Operation(summary = "审核拒绝（管理员）")
    @PutMapping("/{id}/reject")
    public R<Void> reject(@PathVariable Long id, @RequestParam String reason) {
        jobService.reject(id, reason);
        return R.ok();
    }

    @Operation(summary = "岗位列表（管理员）")
    @GetMapping("/admin/list")
    public R<PageResult<JobVO>> adminList(
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(jobService.adminList(status, pageNum, pageSize));
    }

    @Operation(summary = "企业统计数据")
    @GetMapping("/employer/stats")
    public R<EmployerStatsVO> employerStats() {
        return R.ok(jobService.getEmployerStats(UserContext.getUserId()));
    }

    @Operation(summary = "热门岗位排行榜")
    @GetMapping("/hot")
    public R<java.util.List<JobVO>> hotJobs(@RequestParam(defaultValue = "10") int top) {
        return R.ok(jobService.getHotJobs(top));
    }
}
