package com.yygx.threadpoolexporttest.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * <p>
 * 注释：
 * </p>
 * Since: 2024/9/23 <p>
 * Author: <a href="https://github.com/YFBY-Java" target="_blank">吟风抱月</a>
 */
public interface ExcelService {
    void downloadExcel(List<Long> volunteerTableIds, ByteArrayOutputStream outputStream);

}
