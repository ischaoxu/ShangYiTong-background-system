package com.atguigu.syt.user.service.impl;


import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.cmn.client.DictFeignClient;
import com.atguigu.syt.enums.AuthStatusEnum;
import com.atguigu.syt.enums.DictTypeEnum;
import com.atguigu.syt.enums.UserStatusEnum;
import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.oss.client.OssFeignClient;
import com.atguigu.syt.user.mapper.UserInfoMapper;
import com.atguigu.syt.user.service.UserInfoService;
import com.atguigu.syt.vo.user.UserAuthVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-12
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private DictFeignClient dictFeignClient;
    @Autowired
    private OssFeignClient ossFeignClient;
    @Autowired
    @Qualifier("taskExecutor")
    private Executor executor;

    @Override
    public UserInfo getByOpenId(String openid) {

        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getOpenid, openid);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void updateUserInfo(UserAuthVo userAuthVo, Long userId) {
        UserInfo userInfo = baseMapper.selectById(userId);
        if (userInfo == null) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
        if (userInfo.getStatus().intValue() == 0) {
            throw new GuiguException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        userInfo.setName(userAuthVo.getName());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        baseMapper.updateById(userInfo);
    }

    @Override
    public UserInfo getUserInfoById(Long userId) {
        UserInfo userInfo = baseMapper.selectById(userId);
        Long a = System.currentTimeMillis();
        log.info("开始记录：" + a);
        UserInfo userInfo1 = packUserInfo(userInfo);
        Long b = System.currentTimeMillis();
        log.info("结束记录：" + b);
        log.info("获取用户信息用时：" + (b - a) + "ms");
        return userInfo1;
    }

    private UserInfo packUserInfo(UserInfo userInfo) {
        //获取图片临时访问地址
//        if (userInfo.getAuthStatus() == null) {
//            userInfo.setAuthStatus(AuthStatusEnum.NO_AUTH.getStatus());
//        }
//                if (userInfo.getCertificatesUrl() != null) {
//            String previewUrl = ossFeignClient.getPreviewUrl(userInfo.getCertificatesUrl());
//            userInfo.getParam().put("previewUrl", previewUrl);
//        }
//
//
//        //字典翻译证件类型
//        String certificatesTypeString = dictFeignClient.getName(
//                DictTypeEnum.CERTIFICATES_TYPE.getDictTypeId(),
//                userInfo.getCertificatesType()
//        );
//        userInfo.getParam().put("certificatesTypeString", certificatesTypeString);


        //获取图片临时访问地址
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            if (userInfo.getCertificatesUrl() != null) {
                String previewUrl = ossFeignClient.getPreviewUrl(userInfo.getCertificatesUrl());
                log.info("================future================================");
                return previewUrl;
            }
            return null;
        }, executor).whenComplete((previewUrl, error) -> {
            userInfo.getParam().put("previewUrl", previewUrl);
            if (error != null) {
                log.warn("获取图片临时访问地址出错：" + error.getLocalizedMessage());
            }
        }).exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });
        //字典翻译证件类型
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            String certificatesTypeString = dictFeignClient.getName(
                    DictTypeEnum.CERTIFICATES_TYPE.getDictTypeId(),
                    userInfo.getCertificatesType()
            );

            log.info("================future2================================");
            return certificatesTypeString;
        }, executor).whenComplete((certificatesTypeString, error) -> {
            userInfo.getParam().put("certificatesTypeString", certificatesTypeString);
            if (error != null) {
                log.warn("字典映射出错：" + error.getLocalizedMessage());
            }
        }).exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });

        log.info("================main================================");
        userInfo.getParam().put(
                "authStatusString",
                AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus())
        );
        userInfo.getParam().put(
                "statusString",
                UserStatusEnum.getStatusNameByStatus(userInfo.getStatus())
        );

        future.join();
        future2.join();

        return userInfo;
    }
}
