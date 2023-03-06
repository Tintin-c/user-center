package com.tintin.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author tintin
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -7542137974635128589L;

    public String userAccount;
    public String userPassword;
}
