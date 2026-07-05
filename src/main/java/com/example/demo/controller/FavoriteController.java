package com.example.demo.controller;

import com.example.demo.common.PageResult;
import com.example.demo.common.R;
import com.example.demo.dto.JobVO;
import com.example.demo.service.FavoriteService;
import com.example.demo.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "收藏模块")
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "收藏岗位")
    @PostMapping("/{jobId}")
    public R<Void> add(@PathVariable Long jobId) {
        favoriteService.add(UserContext.getUserId(), jobId);
        return R.ok();
    }

    @Operation(summary = "取消收藏")
    @DeleteMapping("/{jobId}")
    public R<Void> remove(@PathVariable Long jobId) {
        favoriteService.remove(UserContext.getUserId(), jobId);
        return R.ok();
    }

    @Operation(summary = "检查是否已收藏")
    @GetMapping("/{jobId}/check")
    public R<Boolean> check(@PathVariable Long jobId) {
        return R.ok(favoriteService.isFavorite(UserContext.getUserId(), jobId));
    }

    @Operation(summary = "我的收藏列表")
    @GetMapping("/mine")
    public R<PageResult<JobVO>> mine(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(favoriteService.myFavorites(UserContext.getUserId(), pageNum, pageSize));
    }
}
