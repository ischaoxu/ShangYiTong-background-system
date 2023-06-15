package com.atguigu.syt.user.service;


import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.vo.user.UserAuthVo;
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

    /**
     *  更新用户实现实名信息存储
     * @author liuzhaoxu
     * @date 2023/6/14 20:00
     * @param userAuthVo
     * @param userId
     */

    void updateUserInfo(UserAuthVo userAuthVo, Long userId);

    /**
     *  根据用户id获取用户信息
     * @author liuzhaoxu
     * @date 2023/6/14 20:09
     * @param userId
     * @return com.atguigu.syt.model.user.UserInfo
     */

    UserInfo getUserInfoById(Long userId);
}
