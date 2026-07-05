package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;
    private String username;
    private String role;
    private String nickname;
    private String avatar;
    private String phone;
    private String email;
    private Integer status;
    private LocalDateTime createTime;
}
