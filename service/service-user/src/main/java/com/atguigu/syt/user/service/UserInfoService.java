package com.atguigu.syt.user.service;


import com.atguigu.syt.model.user.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-12
 */
public interface UserInfoService extends IService<UserInfo> {
    /**
     * 根据openid查询用户信息
     * @param openid
     * @return
     */
    UserInfo getByOpenId(String openid);
}
