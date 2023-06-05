package com.atguigu.syt.cmn.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.ListUtils;
import com.atguigu.syt.cmn.easyexcel.test.DemoData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

/**
 * @author liuzhaoxu
 * @date 2023年06月05日 9:41
 */
@SpringBootTest
public class ExcelWriteTest {

    /**
     * 最简单的写
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>
     * 2. 直接写即可
     */
    @Test
    public void simpleWrite07() {
        // 注意 simpleWrite在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
        // 写法2
        String fileName = "d:/simpleWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, DemoData.class).sheet("模板").doWrite(data());
    }

    @Test
    public void simpleWrite03() {
        String fileName = "d:/simpleWrite" + System.currentTimeMillis() + ".xls";
        EasyExcel.write(fileName, DemoData.class).excelType(ExcelTypeEnum.XLS).sheet("模板").doWrite(data());
    }

    private List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();

        //算上标题，最多可写65536行(.xls)
        //算上标题，最多可写1048576行(.xlsx)
        //java.lang.IllegalArgumentException: Invalid row number (65536) outside allowable range (0..65535)
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }
}