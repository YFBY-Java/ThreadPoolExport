package com.yygx.threadpoolexporttest.controller;

import com.yygx.threadpoolexporttest.service.ExcelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 注释：
 * </p>
 * Since: 2024/9/23 <p>
 * Author: <a href="https://github.com/YFBY-Java" target="_blank">吟风抱月</a>
 */
@RestController
@Tag(name = "线程池导出Excel")  // swagger文档注解
@RequiredArgsConstructor  // 构造函数注入
public class ExcelController {

    private static final Logger log = LoggerFactory.getLogger(ExcelController.class);
    private final ExcelService excelService;


    @Operation(summary = "多线程报表导出")
    @GetMapping("/downloadExcel")
    public ResponseEntity<ByteArrayResource> downloadExcel() {
        // 模拟一下志愿表ID列表
        List<Long> volunteerTableIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L, 13L, 14L, 15L);

        // 使用 try-with-resources 自动关闭流
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            excelService.downloadExcel(volunteerTableIds, outputStream);
            byte[] excelData = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            String encodeAndFilename = URLEncoder.encode("志愿表.xlsx", StandardCharsets.UTF_8);
            headers.add("Content-Disposition", "attachment; filename=" + encodeAndFilename);
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            ByteArrayResource resource = new ByteArrayResource(excelData);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            log.error("导出Excel失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
