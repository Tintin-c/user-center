package com.tintin.usercenter.service;

import com.tintin.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
* @author tintin
* @description 针对表【user】的数据库操作Service
* @createDate 2023-02-22 20:58:24
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param checkPassword 校验密码
     * @return 返回新建账户ID
     */
    long userRegist(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount 用户账号
     * @param userPassword 用户密码
     * @param request servlet
     * @return 返回脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User saftyUser(User user);

    Integer userLogout(HttpServletRequest request);
}
