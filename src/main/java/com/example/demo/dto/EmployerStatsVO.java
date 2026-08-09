package com.example.demo.dto;

import lombok.Data;

@Data
public class EmployerStatsVO {

    private long jobCount;

    private long applicationCount;

    private long interviewCount;

    private double passRate;
}
