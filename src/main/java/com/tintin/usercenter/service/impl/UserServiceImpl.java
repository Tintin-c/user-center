package com.tintin.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tintin.usercenter.common.ErrorCode;
import com.tintin.usercenter.exception.BusinessException;
import com.tintin.usercenter.model.domain.User;
import com.tintin.usercenter.service.UserService;
import com.tintin.usercenter.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tintin.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * @author tintin
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-02-22 20:58:24
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    UserMapper userMapper;

    /**
     * 盐
     */
    public static final String SALT = "tintin"; 

    /**
     * 登录状态
     */
    @Override
    public long userRegist(String userAccount, String userPassword, String checkPassword) {
        // 非空校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 用户账号于4
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名小于4个字符");
        }

        // 用户密码大于8
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码小于8个字符");
        }

        // 用户账号不包含特殊字符
        String regEx="[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*()——+|{}【】‘；：”“'。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名小于4个字符");
        }

        // 密码校验
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名小于4个字符");
        }

        // 账号是否被注册
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        boolean exists = userMapper.exists(wrapper);
        if (exists){
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户已存在");
        }

        // 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        User user = new User();
        user.setUsername(userAccount);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setAvatarUrl("https://edu-guli-0626.oss-cn-guangzhou.aliyuncs.com/2022/07/08/3c8a232c1d9d4c1f90d0a423d0cb3842e7e14b1ddf50af24f791114766dc749.jpg");
        int insert = userMapper.insert(user);

        // 插入是否成功
        if (insert < 0){
            throw new BusinessException(ErrorCode.NULL_ERROR, "新用户插入失败");
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 非空校验
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 用户账号于4
        if(userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 用户密码大于8
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 用户账号不包含特殊字符
        String regEx="[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*()——+|{}【】‘；：”“'。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号包含特殊字符");
        }

        // 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 查找用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        wrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(wrapper);
        if (user == null){
            log.info("User login failed, userAccount cannot macth userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码有误");
        }

        // 脱敏
        User saftyUser = saftyUser(user);

        // 设置登录态
        request.getSession().setAttribute(USER_LOGIN_STATUS, saftyUser);

        return saftyUser;
    }

    /**
     * 脱敏
     * @param user
     * @return
     */
    @Override
    public User saftyUser(User user){
        if (user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        User saftyUser = new User();
        saftyUser.setId(user.getId());
        saftyUser.setUsername(user.getUsername());
        saftyUser.setUserAccount(user.getUserAccount());
        saftyUser.setAvatarUrl(user.getAvatarUrl());
        saftyUser.setGender(user.getGender());
        saftyUser.setPhone(user.getPhone());
        saftyUser.setEmail(user.getEmail());
        saftyUser.setUserStatus(user.getUserStatus());
        saftyUser.setUserRole(user.getUserRole());
        saftyUser.setCreateTime(user.getCreateTime());
        saftyUser.setIsDelete(user.getIsDelete());

        return saftyUser;
    }

    @Override
    public Integer userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATUS);
        return 1;
    }
}




