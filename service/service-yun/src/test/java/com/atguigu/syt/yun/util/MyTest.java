package com.atguigu.syt.yun.util;

import org.joda.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author liuzhaoxu
 * @date 2023年06月14日 13:03
 */
@SpringBootTest
public class MyTest {
    @Autowired
    private  OssAliyunProperties ossAliyunProperties;

    @Test
    public void test1() {
        System.out.println(new LocalDate().toString());
    }
}
