package com.example.demo.dto;

import lombok.Data;

@Data
public class JobQuery {

    private String keyword;
    private String city;
    private String category;
    private String salaryType;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
