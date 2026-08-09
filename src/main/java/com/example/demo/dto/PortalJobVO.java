package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PortalJobVO {

    private Long id;

    private String title;

    private String publisherName;

    private String publisherAvatar;

    private BigDecimal salaryMin;

    private BigDecimal salaryMax;

    private String salaryType;

    private String city;

    private String category;

    private String workTime;

    private LocalDateTime createTime;
}
