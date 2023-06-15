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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-12
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    private DictFeignClient dictFeignClient;
    @Autowired
    private OssFeignClient ossFeignClient;
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
        if (userInfo.getStatus().intValue()==0) {
            throw new GuiguException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        userInfo.setName(userAuthVo.getName());
        System.out.println(userAuthVo.getName()+"=====================================");
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        baseMapper.updateById(userInfo);
    }

    @Override
    public UserInfo getUserInfoById(Long userId) {
        UserInfo userInfo = baseMapper.selectById(userId);
        return packUserInfo(userInfo);
    }
    private UserInfo packUserInfo(UserInfo userInfo) {
        if (userInfo.getAuthStatus() == null) {
            userInfo.setAuthStatus(AuthStatusEnum.NO_AUTH.getStatus());
        }
//        获取图片临时访问地址
        if (userInfo.getCertificatesUrl() != null) {
            String previewUrl = ossFeignClient.getPreviewUrl(userInfo.getCertificatesUrl());
            userInfo.getParam().put("previewUrl", previewUrl);
        }
//        字典翻译证件类型
        String certificatesTypeString = dictFeignClient.getName(
                DictTypeEnum.CERTIFICATES_TYPE.getDictTypeId(),
                userInfo.getCertificatesType()
        );
        userInfo.getParam().put("certificatesTypeString", certificatesTypeString);
        userInfo.getParam().put(
                "authStatusString", AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus())
        );
        userInfo.getParam().put(
                "statusString", UserStatusEnum.getStatusNameByStatus(userInfo.getStatus())
        );
        return userInfo;
    }
}
