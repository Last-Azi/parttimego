package com.example.demo.dto;

import lombok.Data;

import java.util.List;

@Data
public class StatsVO {

    private long userCount;
    private long jobCount;
    private long applicationCount;
    private long reviewCount;

    private List<DayStat> dailyJobs;
    private List<DayStat> dailyApplications;
    private List<CategoryStat> categoryStats;

    @Data
    public static class DayStat {
        private String date;
        private long count;
    }

    @Data
    public static class CategoryStat {
        private String category;
        private long count;
    }
}
