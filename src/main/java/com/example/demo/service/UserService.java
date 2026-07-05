package com.example.demo.service;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.UserVO;

public interface UserService {

    void register(RegisterDTO dto);

    String login(LoginDTO dto);

    UserVO getCurrentUser(Long userId);

    void updateNickname(Long userId, String nickname);

    void updateAvatar(Long userId, String avatarUrl);
}
