package com.yygx.threadpoolexporttest.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


/**
 * <p>
 * 注释：
 * </p>
 * Since: 2024/9/20 <p>
 * Author: <a href="https://github.com/YFBY-Java" target="_blank">吟风抱月</a>
 */
@Data
@Entity
@Table(name = "volunteer_info")
public class VolunteerInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "volunteer_table_id")
    private Long volunteerTableId;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "gb_code", length = 20)
    private String gbCode;

    @Column(name = "school", length = 100)
    private String school;

    @Column(name = "ranking")
    private Integer ranking;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "is_211")
    private Boolean is211;

    @Column(name = "is_985")
    private Boolean is985;

    @Column(name = "is_double_first_class")
    private Boolean isDoubleFirstClass;

    @Column(name = "province", length = 50)
    private String province;

    @Column(name = "admission_category", length = 20)
    private String admissionCategory;

    @Column(name = "major", length = 100)
    private String major;

    @Column(name = "major_code", length = 20)
    private String majorCode;

    @Column(name = "major_group", length = 50)
    private String majorGroup;

    @Column(name = "education_years")
    private Integer educationYears;

    @Column(name = "probability", precision = 5, scale = 2)
    private BigDecimal probability;

    @Column(name = "min_score_2023")
    private Integer minScore2023;

    @Column(name = "min_rank_2023")
    private Integer minRank2023;

    @Column(name = "min_score_2022")
    private Integer minScore2022;

    @Column(name = "min_rank_2022")
    private Integer minRank2022;

    @Column(name = "min_score_2021")
    private Integer minScore2021;

    @Column(name = "min_rank_2021")
    private Integer minRank2021;
}
