package com.tintin.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tintin.usercenter.common.BaseResponse;
import com.tintin.usercenter.common.ErrorCode;
import com.tintin.usercenter.common.ResultUtils;
import com.tintin.usercenter.exception.BusinessException;
import com.tintin.usercenter.model.domain.User;
import com.tintin.usercenter.model.domain.request.UserLoginRequest;
import com.tintin.usercenter.model.domain.request.UserRegisterRequest;
import com.tintin.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tintin.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.tintin.usercenter.constant.UserConstant.USER_LOGIN_STATUS;

/**
 * 用户接口
 *
 * @author tintin
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;


    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.userAccount;
        String userPassword = userRegisterRequest.userPassword;
        String checkPassword = userRegisterRequest.checkPassword;

        // 判断字段是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long result = userService.userRegist(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 判断是否为空
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> logout(HttpServletRequest request){
        if (request == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Integer result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User currentUser = (User) userObj;
        if (currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Long userId = currentUser.getId();
        User user = userService.getById(userId);
        User saftyUser = userService.saftyUser(user);
        return ResultUtils.success(saftyUser);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username, HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(username)){
            wrapper.like("username", username);
        }

        List<User> userList = userService.list(wrapper);
        return ResultUtils.success(userList.stream().map(user -> {
            return userService.saftyUser(user);
        }).collect(Collectors.toList()));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestParam Long userId, HttpServletRequest request){
        if (!isAdmin(request)){
            return null;
        }

        if (userId < 0){
            return null;
        }
        return ResultUtils.success(userService.removeById(userId));
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    public Boolean isAdmin(HttpServletRequest request){
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATUS);
        User user = (User) userObject;

        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
