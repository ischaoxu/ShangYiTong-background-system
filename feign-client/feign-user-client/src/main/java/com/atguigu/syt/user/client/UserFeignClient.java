package com.atguigu.syt.user.client;

import com.atguigu.syt.model.user.Patient;
import com.atguigu.syt.user.client.impl.UserFeignClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-user" ,
        contextId = "userFeignClient",
        fallback = UserFeignClientImpl.class)
public interface UserFeignClient {

    @GetMapping("/inner/user/patient/get/{id}")
    Patient getPatient(@PathVariable("id") Long id);

}
