package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.BusinessException;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.DigestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerDefaultsRoleNicknameAndEncryptsPassword() {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("student1");
        dto.setPassword("secret123");

        when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);

        userService.register(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).insert(captor.capture());
        User inserted = captor.getValue();

        assertThat(inserted.getUsername()).isEqualTo("student1");
        assertThat(inserted.getRole()).isEqualTo("STUDENT");
        assertThat(inserted.getNickname()).isEqualTo("student1");
        assertThat(inserted.getPassword())
                .isEqualTo(DigestUtils.md5DigestAsHex("PartTimeGosecret123".getBytes()))
                .isNotEqualTo("secret123");
    }

    @Test
    void registerRejectsDuplicateUsername() {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("student1");
        dto.setPassword("secret123");

        when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(BusinessException.class);
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void loginReturnsGeneratedTokenForActiveUserWithMatchingPassword() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("student1");
        dto.setPassword("secret123");

        User user = new User();
        user.setId(7L);
        user.setRole("STUDENT");
        user.setStatus(1);
        user.setPassword(DigestUtils.md5DigestAsHex("PartTimeGosecret123".getBytes()));

        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(user);
        when(jwtUtil.generateToken(7L, "STUDENT")).thenReturn("jwt-token");

        assertThat(userService.login(dto)).isEqualTo("jwt-token");
    }

    @Test
    void loginRejectsDisabledUserBeforeGeneratingToken() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("student1");
        dto.setPassword("secret123");

        User user = new User();
        user.setStatus(0);

        when(userMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(user);

        assertThatThrownBy(() -> userService.login(dto))
                .isInstanceOf(BusinessException.class);
        verify(jwtUtil, never()).generateToken(any(), any());
    }
}
