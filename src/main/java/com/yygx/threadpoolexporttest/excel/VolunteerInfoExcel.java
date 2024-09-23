package com.yygx.threadpoolexporttest.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class VolunteerInfoExcel {

    @ExcelProperty(index = 0) // 类型
    private String type; // 类型字段，假设这是一个 String 类型的字段

    @ExcelProperty(index = 1) // 国标码
    private String gbCode; // 修改为与 SQL 结构一致

    @ExcelProperty(index = 2) // 学校
    private String school;

    @ExcelProperty(index = 3) // 排名
    private Integer ranking;

    @ExcelProperty(index = 4) // 公办
    private String isPublic; // 修改为与 SQL 结构一致

    @ExcelProperty(index = 5) // 211
    private String is211;

    @ExcelProperty(index = 6) // 985
    private String is985;

    @ExcelProperty(index = 7) // 双一流
    private String isDoubleFirstClass; // 修改为与 SQL 结构一致

    @ExcelProperty(index = 8) // 省份
    private String province;

    @ExcelProperty(index = 9) // 1冲2保3稳
    private String admissionCategory; // 修改为与 SQL 结构一致

    @ExcelProperty(index = 10) // 专业
    private String major;

    @ExcelProperty(index = 11) // 专业代码
    private String majorCode;

    @ExcelProperty(index = 12) // 专业组
    private String majorGroup;

    @ExcelProperty(index = 13) // 学制
    private String educationYears; // 修改为与 SQL 结构一致

    @ExcelProperty(index = 14) // 概率
    private String probability;

    @ExcelProperty(index = 15) // 2023年最低分
    private Integer minScore2023;

    @ExcelProperty(index = 16) // 2023年最低位次
    private String minRank2023;

    @ExcelProperty(index = 17) // 2022年最低分
    private Integer minScore2022;

    @ExcelProperty(index = 18) // 2022年最低位次
    private String minRank2022;

    @ExcelProperty(index = 19) // 2021年最低分
    private Integer minScore2021;

    @ExcelProperty(index = 20) // 2021年最低位次
    private String minRank2021;

    @ExcelProperty(index = 21) // 所属志愿表
    private Long volunteerTableId; // 修改为与 SQL 结构一致
}
