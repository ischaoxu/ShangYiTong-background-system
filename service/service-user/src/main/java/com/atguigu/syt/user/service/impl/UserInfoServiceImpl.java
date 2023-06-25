package com.atguigu.syt.user.service.impl;


import com.atguigu.common.service.exception.GuiguException;
import com.atguigu.common.util.result.ResultCodeEnum;
import com.atguigu.syt.cmn.client.DictFeignClient;
import com.atguigu.syt.enums.AuthStatusEnum;
import com.atguigu.syt.enums.DictTypeEnum;
import com.atguigu.syt.enums.UserStatusEnum;
import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.oss.client.OssFeignClient;
import com.atguigu.syt.user.mapper.UserInfoMapper;
import com.atguigu.syt.user.service.PatientService;
import com.atguigu.syt.user.service.UserInfoService;
import com.atguigu.syt.vo.user.UserAuthVo;
import com.atguigu.syt.vo.user.UserInfoQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private PatientService patientService;

    @Autowired
    private RedisTemplate redisTemplate;
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

    @Override
    public IPage<UserInfo> lisetUserPage(Integer page, Integer limit, UserInfoQueryVo userInfoQueryVo) {
        String keyword = userInfoQueryVo.getKeyword();
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin();
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd();

        Page<UserInfo> infoPage = new Page<>(page, limit);

        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(!StringUtils.isEmpty(keyword),
                        w -> w.like(UserInfo::getName, keyword)
                                .or().like(UserInfo::getPhone, keyword))
                .ge(!StringUtils.isEmpty(createTimeBegin), UserInfo::getCreateTime, createTimeBegin)
                .le(!StringUtils.isEmpty(createTimeEnd), UserInfo::getCreateTime, createTimeEnd);

        Page<UserInfo> userInfoPage = baseMapper.selectPage(infoPage, wrapper);

        ArrayList<UserInfo> userInfoList = new ArrayList<>();

        for (UserInfo userInfo : userInfoPage.getRecords()) {
            packUserInfo(userInfo);
        }


        return userInfoPage;
    }

    @Override
    public boolean lock(Long userId, Integer status) {
        if (status == 0 || status == 1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            if (userInfo == null) {
                throw new GuiguException(ResultCodeEnum.FAIL);
            }
            userInfo.setStatus(status);
           return this.updateById(userInfo);
        }
        return false;
    }

    @Override
    public Map<String, Object> show(Long userId) {
        UserInfo userInfo = this.getUserInfoById(userId);
        List<Patient> patientList = patientService.ListPatientByUserId(userId);
        patientList.forEach(patient -> {
            patientService.packPatient(patient);
        });
        HashMap<String, Object> map = new HashMap<>();
        map.put("userInfo",userInfo);
        map.put("patientList",patientList);
        return map;
    }

    @Override
    public boolean approval(Long userId, Integer authStatus) {
        if(authStatus == AuthStatusEnum.AUTH_SUCCESS.getStatus().intValue()
                || authStatus == AuthStatusEnum.AUTH_FAIL.getStatus().intValue()){
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userId);
            userInfo.setAuthStatus(authStatus);
            return this.updateById(userInfo);
        }
        return false;
    }

    @Override
    public void bindPhone(Long userId, String phone, String code) {
        //校验参数
        if(StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new GuiguException(ResultCodeEnum.PARAM_ERROR);
        }

        //校验验证码
        String phoneCode = (String)redisTemplate.opsForValue().get("code:" + phone);
        if (phoneCode == null) {
            throw new GuiguException(ResultCodeEnum.CODE_ERROR.getCode(),"验证码过期");
        }

        if(!code.equals(phoneCode)) {
            throw new GuiguException(ResultCodeEnum.CODE_ERROR);
        }

        //根据手机号查找会员
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        //手机号没有被其他人绑定过
        queryWrapper.eq(UserInfo::getPhone, phone).ne(UserInfo::getId, userId);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);

        //手机号已存在
        if(userInfo != null) {
            throw new GuiguException(ResultCodeEnum.REGISTER_MOBILE_ERROR);
        }
        //设置绑定手机号
        userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setPhone(phone);
        baseMapper.updateById(userInfo);
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
