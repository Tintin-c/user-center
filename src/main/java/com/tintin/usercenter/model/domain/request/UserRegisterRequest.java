package com.tintin.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author tintin
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = -9154143718389494300L;
    public String userAccount;
    public String userPassword;
    public String checkPassword;
}
