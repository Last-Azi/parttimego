package com.example.demo.controller;

import com.example.demo.common.R;
import com.example.demo.dto.ResumeDTO;
import com.example.demo.dto.ResumeVO;
import com.example.demo.mq.ResumeParseProducer;
import com.example.demo.service.ResumeParseService;
import com.example.demo.service.ResumeService;
import com.example.demo.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Tag(name = "简历模块")
@RestController
@RequestMapping("/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeParseService resumeParseService;
    private final ResumeParseProducer resumeParseProducer;

    public ResumeController(ResumeService resumeService, ResumeParseService resumeParseService,
                           ResumeParseProducer resumeParseProducer) {
        this.resumeService = resumeService;
        this.resumeParseService = resumeParseService;
        this.resumeParseProducer = resumeParseProducer;
    }

    @Operation(summary = "保存/更新我的简历")
    @PostMapping
    public R<Void> save(@RequestBody ResumeDTO dto) {
        resumeService.saveOrUpdate(UserContext.getUserId(), dto);
        return R.ok();
    }

    @Operation(summary = "查看我的简历")
    @GetMapping("/mine")
    public R<ResumeVO> mine() {
        return R.ok(resumeService.getMyResume(UserContext.getUserId()));
    }

    @Operation(summary = "查看简历详情（招聘方查看投递者简历）")
    @GetMapping("/{id}")
    public R<ResumeVO> detail(@PathVariable Long id) {
        return R.ok(resumeService.getById(id));
    }

    @Operation(summary = "同步解析简历文件")
    @GetMapping("/parse")
    public R<Map<String, String>> parse(@RequestParam String fileUrl, @RequestParam String fileName) {
        Map<String, String> result = resumeParseService.parseResume(fileUrl, fileName);
        log.info("Resume parse completed, fileName={}, filledFields={}, keys={}",
                fileName, result.size(), result.keySet());
        return R.ok(result);
    }

    @Operation(summary = "异步解析简历文件（MQ）")
    @PostMapping("/parse/async")
    public R<Void> parseAsync(@RequestParam String fileUrl, @RequestParam String fileName) {
        resumeParseProducer.sendParseMessage(UserContext.getUserId(), fileUrl, fileName);
        return R.ok();
    }
}
