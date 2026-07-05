package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.BusinessException;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.UserVO;
import com.example.demo.entity.User;
import com.example.demo.enums.CodeEnum;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public void register(RegisterDTO dto) {
        // 检查用户名是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        BeanUtil.copyProperties(dto, user);
        user.setPassword(encryptPassword(dto.getPassword()));
        if (user.getRole() == null) {
            user.setRole("STUDENT");
        }
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        userMapper.insert(user);
    }

    @Override
    public String login(LoginDTO dto) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        if (!encryptPassword(dto.getPassword()).equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        return jwtUtil.generateToken(user.getId(), user.getRole());
    }

    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        UserVO vo = new UserVO();
        BeanUtil.copyProperties(user, vo);
        return vo;
    }

    @Override
    public void updateNickname(Long userId, String nickname) {
        User user = new User();
        user.setId(userId);
        user.setNickname(nickname);
        userMapper.updateById(user);
    }

    @Override
    public void updateAvatar(Long userId, String avatarUrl) {
        User user = new User();
        user.setId(userId);
        user.setAvatar(avatarUrl);
        userMapper.updateById(user);
    }

    private String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(("PartTimeGo" + password).getBytes());
    }
}
