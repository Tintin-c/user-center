package com.tintin.usercenter.service;
import java.util.Date;

import com.tintin.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Resource
    UserService userService;

    @Test
    public void addUser(){
        User user = new User();
        user.setId(0L);
        user.setUsername("Tintin");
        user.setUserAccount("123");
        user.setAvatarUrl("https://edu-guli-0626.oss-cn-guangzhou.aliyuncs.com/2022/07/08/3c8a232c1d9d4c1f90d0a423d0cb3842e7e14b1ddf50af24f791114766dc749.jpg");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("123");

        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }

    @Test
    void userRegist() {
        // 非空测试
        long result = userService.userRegist(" ", " ", "123");
        Assertions.assertEquals(-1, result);

        // 用户名>4
        result = userService.userRegist("123", "12345678", "12345678");
        Assertions.assertEquals(-1, result);

        // 密码>8
        result = userService.userRegist("1234", "1234567", "1234567");
        Assertions.assertEquals(-1, result);

        // 名字不包含特殊字符
        result = userService.userRegist("@!#@$as", "1234567", "1234567");
        Assertions.assertEquals(-1, result);

        // 密码校验
        result = userService.userRegist("1234", "12345678", "12312312");
        Assertions.assertEquals(-1, result);

        // 用户名不存在
        result = userService.userRegist("tintin", "12345678", "12345678");
        Assertions.assertEquals(-1, result);

        // 成功
        result = userService.userRegist("wrrrr", "12345678", "12345678");
    }
}