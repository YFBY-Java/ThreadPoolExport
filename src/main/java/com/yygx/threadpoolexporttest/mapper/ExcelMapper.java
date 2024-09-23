package com.yygx.threadpoolexporttest.mapper;

import com.yygx.threadpoolexporttest.entity.VolunteerInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 注释：
 * </p>
 * Since: 2024/9/23 <p>
 * Author: <a href="https://github.com/YFBY-Java" target="_blank">吟风抱月</a>
 */
@Mapper
public interface ExcelMapper {

    // 根据志愿表ID查询对应的志愿信息
    @Select("SELECT * FROM volunteer_info WHERE volunteer_table_id = #{volunteerTableId}")
    List<VolunteerInfo> getVolunteerInfoByTableId(Long volunteerTableId);

}
