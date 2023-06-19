package com.atguigu.syt.order.service.impl;

import com.atguigu.syt.model.order.PaymentInfo;
import com.atguigu.syt.order.mapper.PaymentInfoMapper;
import com.atguigu.syt.order.service.PaymentInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付信息表 服务实现类
 * </p>
 *
 * @author com/atguigu
 * @since 2023-06-19
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

}
