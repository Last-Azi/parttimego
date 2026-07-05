package com.example.demo.controller;

import com.example.demo.common.PageResult;
import com.example.demo.common.R;
import com.example.demo.dto.StatsVO;
import com.example.demo.dto.UserVO;
import com.example.demo.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "管理后台")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "数据统计")
    @GetMapping("/stats")
    public R<StatsVO> stats() {
        return R.ok(adminService.getStats());
    }

    @Operation(summary = "用户列表")
    @GetMapping("/users")
    public R<PageResult<UserVO>> listUsers(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(adminService.listUsers(status, role, pageNum, pageSize));
    }

    @Operation(summary = "启用/禁用用户")
    @PutMapping("/users/{id}/toggle")
    public R<Void> toggleUser(@PathVariable Long id) {
        adminService.toggleUserStatus(id);
        return R.ok();
    }
}
