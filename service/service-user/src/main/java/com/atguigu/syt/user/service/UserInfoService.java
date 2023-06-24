package com.atguigu.syt.user.service;


import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.vo.user.UserAuthVo;
import com.atguigu.syt.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

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

    /**
     *  获取用户分页列表
     * @author liuzhaoxu
     * @date 2023/6/18 22:08
     * @param page
     * @param limit
     * @param userInfoQueryVo
     * @return java.util.List<com.atguigu.syt.model.user.UserInfo>
     */

    IPage<UserInfo> lisetUserPage(Integer page, Integer limit, UserInfoQueryVo userInfoQueryVo);

    /**
     *  修改用户状态
     * @author liuzhaoxu
     * @date 2023/6/18 23:06
     * @param userId
     * @param status
     * @return boolean
     */

    boolean lock(Long userId, Integer status);

    /**
     *   获取用户详情，就诊人信息
     * @author liuzhaoxu
     * @date 2023/6/18 23:13
     * @param userId
     * @return java.util.Map<java.lang.String,java.lang.Object>
     */

    Map<String, Object> show(Long userId);

    /**
     *  审核用户
     * @author liuzhaoxu
     * @date 2023/6/18 23:28
     * @param userId
     * @param authStatus
     * @return boolean
     */

    boolean approval(Long userId, Integer authStatus);

    /**
     *  绑定手机号
     * @author liuzhaoxu
     * @date 2023/6/21 18:17
     * @param userId
     * @param phone
     * @param code
     */

    void bindPhone(Long userId, String phone, String code);
}
