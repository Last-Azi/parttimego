package com.example.demo.controller;

import com.aliyun.oss.OSS;
import com.example.demo.common.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

@Tag(name = "OSS测试")
@RestController
@RequestMapping("/test/oss")
@RequiredArgsConstructor
public class TestOssController {

    private final OSS ossClient;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Operation(summary = "测试OSS连接")
    @GetMapping("/connect")
    public R<String> testConnect() {
        try {
            boolean exists = ossClient.doesBucketExist(bucketName);
            if (exists) {
                return R.ok("OSS连接成功，Bucket存在: " + bucketName);
            } else {
                return R.fail("Bucket不存在: " + bucketName);
            }
        } catch (Exception e) {
            return R.fail("OSS连接失败: " + e.getMessage());
        }
    }

    @Operation(summary = "测试上传文件")
    @PostMapping("/upload")
    public R<String> testUpload(@RequestParam("file") MultipartFile file) {
        try {
            String objectName = "test/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            ossClient.putObject(bucketName, objectName, file.getInputStream());

            String url = "https://" + bucketName + ".oss-cn-hangzhou.aliyuncs.com/" + objectName;
            return R.ok("上传成功: " + url);
        } catch (Exception e) {
            return R.fail("上传失败: " + e.getMessage());
        }
    }

    @Operation(summary = "测试上传文本")
    @GetMapping("/upload-text")
    public R<String> testUploadText() {
        try {
            String objectName = "test/hello_" + System.currentTimeMillis() + ".txt";
            String content = "Hello PartTimeGo OSS Test!";
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));

            String url = "https://" + bucketName + ".oss-cn-hangzhou.aliyuncs.com/" + objectName;
            return R.ok("文本上传成功: " + url);
        } catch (Exception e) {
            return R.fail("上传失败: " + e.getMessage());
        }
    }
}
