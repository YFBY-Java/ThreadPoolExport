package com.yygx.threadpoolexporttest.service.impl;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.yygx.threadpoolexporttest.config.ThreadPoolConfig;
import com.yygx.threadpoolexporttest.entity.MajorItemExcel;
import com.yygx.threadpoolexporttest.entity.VolunteerInfo;
import com.yygx.threadpoolexporttest.excel.VolunteerInfoExcel;
import com.yygx.threadpoolexporttest.mapper.ExcelMapper;
import com.yygx.threadpoolexporttest.service.ExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 注释：
 * </p>
 * Since: 2024/9/23 <p>
 * Author: <a href="https://github.com/YFBY-Java" target="_blank">吟风抱月</a>
 */
@Service
@RequiredArgsConstructor  // 构造函数注入
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    private final ExcelMapper excelMapper;
    private final ExecutorService executorService;  // 注入自定义的线程池


    @Override
    public void downloadExcel(List<Long> volunteerTableIds, ByteArrayOutputStream outputStream) {
        // 1.单线程导出
//        downloadExcelSingle(volunteerTableIds, outputStream);

        // 2.多线程导出，在运行时创建线程
//        downloadExcelMulti(volunteerTableIds, outputStream);

        // 3.线程池导出
        downloadExcelThreadPool(volunteerTableIds, outputStream);

    }



    // 线程池导出
    public void downloadExcelThreadPool(List<Long> volunteerTableIds, ByteArrayOutputStream outputStream) {
        try (ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream()) {
            // 创建 ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(outputStream1, MajorItemExcel.class).build();

            // 使用map存储每个志愿表对应的Excel数据（保持志愿表id和数据的对应关系）
            Map<Long,List<MajorItemExcel>> volunteerDataMap = new ConcurrentHashMap<>();

            // 创建并发任务，负责查询数据，并将结果放入 volunteerDataMap
            List<Callable<Void>> tasks = volunteerTableIds.stream().map(volunteerTableId -> (Callable<Void>) () -> {
                try {
                    List<VolunteerInfo> volunteerInfoByTableId = excelMapper.getVolunteerInfoByTableId(volunteerTableId);
                    // 转换
                    List<MajorItemExcel> excelDataList = volunteerInfoByTableId.stream()
                            .map(this::convertToExcelData)
                            .toList();
                    // 存储到map
                    volunteerDataMap.put(volunteerTableId, excelDataList);
                } catch (Exception e) {
                    log.error("导出Excel失败", e);
                }
                return null;

            }).toList();

            // 执行并等待所有任务完成
            List<Future<Void>> futures = executorService.invokeAll(tasks);
            for (Future<Void> future : futures) {
                future.get();
            }

            // 所有数据查询完毕后，按顺序写入到 Excel 的sheet
            for (Long volunteerTableId : volunteerTableIds) {
                // 创建新的sheet
                WriteSheet writeSheet = EasyExcel.writerSheet("志愿表" + volunteerTableId)
                        .head(MajorItemExcel.class)
                        .build();
                // 获取对应的Excel数据写入
                List<MajorItemExcel> excels = volunteerDataMap.get(volunteerTableId);
                if (excels != null) {
                    excelWriter.write(excels, writeSheet);
                }
            }

            // 关闭资源
            excelWriter.finish();
            // 将所有数据写入到输出流
            outputStream.write(outputStream1.toByteArray());

        } catch (IOException | InterruptedException | ExecutionException e) {
            log.error("导出Excel失败", e);
        }

    }



    // 多线程导出，在运行时创建线程
    public void downloadExcelMulti(List<Long> volunteerTableIds, ByteArrayOutputStream outputStream) {
        try (ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream()) {
            // 主线程创建 ExcelWriter
            ExcelWriter excelWriter = EasyExcel.write(outputStream1, MajorItemExcel.class).build();
            // 使用map存储每个志愿表对应的Excel数据（保持志愿表和数据的相应关系）
            Map<Long, List<MajorItemExcel>> volunteerDataMap = new ConcurrentHashMap<>();
            // 创建线程队列，逐个处理志愿表
            List<Thread> threads = new ArrayList<>();
            for (Long volunteerTableId : volunteerTableIds) {
                Thread thread = new Thread(() -> {
                    try {
                        List<VolunteerInfo> volunteerInfos = excelMapper.getVolunteerInfoByTableId(volunteerTableId);
                        //转换
                        List<MajorItemExcel> excelDataList = volunteerInfos.stream()
                                .map(this::convertToExcelData)
                                .toList();
                        volunteerDataMap.put(volunteerTableId, excelDataList);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

                threads.add(thread);
                thread.start();  // 启动线程
            }

            // 等待所有线程执行完毕
            for (Thread thread : threads) {
                thread.join();
            }
            // 所有线程执行完之后，按顺序写入到 Excel的sheet
            for (Long volunteerTableId : volunteerTableIds) {
                // 创建 sheet
                WriteSheet writeSheet = EasyExcel.writerSheet("志愿表" + volunteerTableId)
                        .head(MajorItemExcel.class)
                        .build();
                List<MajorItemExcel> excels = volunteerDataMap.get(volunteerTableId);
                if (excels != null) {
                    excelWriter.write(excels, writeSheet);
                }
            }
            excelWriter.finish();
            // 将所有数据写入到输出流
            outputStream.write(outputStream1.toByteArray());
        } catch (IOException | InterruptedException e) {
            log.error("导出Excel失败", e);
        }
    }


    // 单线程的导出
    public void downloadExcelSingle(List<Long> volunteerTableIds, ByteArrayOutputStream outputStream) {
        ExcelWriter excelWriter = null;
        try (ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream()) {
            // 创建 ExcelWriter
            excelWriter = EasyExcel.write(outputStream1, MajorItemExcel.class).build();
            // 遍历每个志愿表ID，查询并写入Excel
            for (Long volunteerTableId : volunteerTableIds) {
                try {
                    List<VolunteerInfo> volunteerInfoByTableId = excelMapper.getVolunteerInfoByTableId(volunteerTableId);
                    // 将 VolunteerInfo 转换为 MajorItemExcel
                    List<MajorItemExcel> excelDataList = volunteerInfoByTableId.stream()
                            .map(this::convertToExcelData)
                            .collect(Collectors.toList());
                    WriteSheet writeSheet = EasyExcel.writerSheet("志愿表" + volunteerTableId)
                            .head(MajorItemExcel.class)
                            .build();
                    excelWriter.write(excelDataList, writeSheet);
                } catch (Exception e) {
                    log.error("导出Excel失败", e);
                }
            }
            excelWriter.finish();
            outputStream.write(outputStream1.toByteArray());
        } catch (IOException e) {
            log.error("导出Excel失败", e);
        }
    }


    // 转换
    private MajorItemExcel convertToExcelData(VolunteerInfo volunteerInfo) {
        MajorItemExcel excelData = new MajorItemExcel();

        // ----- 院校信息 -----
        // 填充 ID（根据实际需求，这里填充为专业的 ID）
        excelData.setId(volunteerInfo.getId().intValue());

        // 填充学校名称
        excelData.setTitle(volunteerInfo.getSchool());

        // 填充全国排名
        excelData.setZrRank(volunteerInfo.getRanking());

        // 填充学校类型（type，根据逻辑设置，假设type = 5000 为综合性院校，其他类型可根据业务需求动态设置）
        excelData.setType(volunteerInfo.getType()); // 举例为综合性院校类型

        // 填充学校国标码
        excelData.setCode(volunteerInfo.getGbCode());

        // 是否为公办学校，0为否，1为是
        excelData.setIsPublic(volunteerInfo.getIsPublic() ? "是" : "否");

        // 是否为 211，1为是，2为否
        excelData.setF211(volunteerInfo.getIs211() ? "是" : "否");

        // 是否为 985，1为是，2为否
        excelData.setF985(volunteerInfo.getIs985() ? "是" : "否");

        // 是否为双一流，1为是，0为否
        excelData.setDualClass(volunteerInfo.getIsDoubleFirstClass() ? "是" : "否");

        // 填充省份名称
        excelData.setProvinceName(volunteerInfo.getProvince());

        excelData.setTypeSign(volunteerInfo.getAdmissionCategory());

        // ----- 专业信息 -----
        // 填充专业名称
        excelData.setMajorTitle(volunteerInfo.getMajor());

        // 填充专业代码
        excelData.setMajorCode(volunteerInfo.getMajorCode());

        // 填充专业组
        excelData.setMajorGroup(volunteerInfo.getMajorGroup());

        // 填充学制，默认设置为 4年
        excelData.setSystem("4年");

        // 填充概率
        excelData.setProbability(volunteerInfo.getProbability() + "%");

        // ----- 历年分数信息 -----
        // 2023年最低分
        excelData.setLowestScore2023(volunteerInfo.getMinScore2023());

        // 2023年最低位次（转换为字符串形式）
        excelData.setLowestOrder2023(volunteerInfo.getMinRank2023() != null ? volunteerInfo.getMinRank2023().toString() : "");

        // 2022年最低分
        excelData.setLowestScore2022(volunteerInfo.getMinScore2022());

        // 2022年最低位次
        excelData.setLowestOrder2022(volunteerInfo.getMinRank2022() != null ? volunteerInfo.getMinRank2022().toString() : "");

        // 2021年最低分
        excelData.setLowestScore2021(volunteerInfo.getMinScore2021());

        // 2021年最低位次
        excelData.setLowestOrder2021(volunteerInfo.getMinRank2021() != null ? volunteerInfo.getMinRank2021().toString() : "");

        // ----- 其他信息 -----
        // 填充招生人数等其他可能需要的字段（如有）
        // 示例：excelData.setProEnr(volunteerInfo.getProEnr());

        // 返回转换后的 Excel 数据对象
        return excelData;
    }
}
