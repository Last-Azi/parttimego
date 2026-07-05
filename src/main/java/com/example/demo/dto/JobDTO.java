package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class JobDTO {

    @NotBlank(message = "岗位标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "最低薪资不能为空")
    private BigDecimal salaryMin;

    @NotNull(message = "最高薪资不能为空")
    private BigDecimal salaryMax;

    private String salaryType = "日结";

    @NotBlank(message = "工作城市不能为空")
    private String city;

    private String address;

    private String category;

    private String workTime;

    private Integer headcount = 1;
}
