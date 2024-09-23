package com.yygx.threadpoolexporttest.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

@Data
@HeadRowHeight(25)
public class MajorItemExcel {

    @ExcelProperty(value = {"院校信息", "类型"})
    private String type;
    /**
     * 学校国标码
     */

    @ExcelProperty({"院校信息", "国标码"})
    private String code;

    private String schoolCode;
    /**
     * 学校名称
     */
    @ColumnWidth(20)
    @ExcelProperty({"院校信息", "学校"})
    private String title;
    /**
     * 全国排名
     */

    @ExcelProperty({"院校信息", "排名"})
    private Integer zrRank;
    /**
     * 是否为公办 0否 1是
     */

    @ExcelProperty(value = {"院校信息", "公办"})
    private String isPublic;


    @ExcelProperty(value = {"院校信息", "211"})
    private String f211;

    @ExcelProperty(value = {"院校信息", "985"})
    private String f985;


    @ExcelProperty(value = {"院校信息", "双一流"})
    private String dualClass;

    @ExcelProperty({"院校信息", "省份"})
    private String provinceName;

    @ExcelProperty({"院校信息","1冲2保3稳"})
    private String typeSign;


    @ExcelProperty({"专业信息", "ID"})
    private Integer id;

    @ExcelProperty({"专业信息", "专业"})
    private String majorTitle;

    @ExcelProperty({"专业信息", "专业代码"})
    private String majorCode;

    @ExcelProperty(value = {"专业信息", "专业组"})
    private String majorGroup;

    @ExcelProperty({"专业信息", "学制"})
    private String system = "4年";


    @NumberFormat("#.##%")
    @ExcelProperty({"专业信息", "概率"})
    private String probability;

    @ExcelProperty({"2023", "最低分"})
    private Integer lowestScore2023;

    @ExcelProperty({"2023", "最低位次"})
    private String lowestOrder2023;

    @ExcelProperty({"2022", "最低分"})
    private Integer lowestScore2022;

    @ExcelProperty({"2022", "最低位次"})
    private String lowestOrder2022;

    @ExcelProperty({"2021", "最低分"})
    private Integer lowestScore2021;

    @ExcelProperty({"2021", "最低位次"})
    private String lowestOrder2021;

}
