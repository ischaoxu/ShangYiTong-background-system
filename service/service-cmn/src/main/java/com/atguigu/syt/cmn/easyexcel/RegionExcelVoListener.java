package com.atguigu.syt.cmn.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.cmn.service.RegionService;
import com.atguigu.syt.cmn.service.impl.RegionServiceImpl;
import com.atguigu.syt.vo.cmn.RegionExcelVo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @author liuzhaoxu
 * @date 2023年06月05日 11:12
 */
@Slf4j
public class RegionExcelVoListener implements ReadListener<RegionExcelVo> {


    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<RegionExcelVo> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private RegionService regionService;

    public RegionExcelVoListener() {
        // 这里是demo，所以随便new一个。实际使用如果到了spring,请使用下面的有参构造函数
        regionService = new RegionServiceImpl();
    }

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param regionService
     */
    public RegionExcelVoListener(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(RegionExcelVo data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSONObject.toJSONString(data));
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        regionService.saveRegionExcelVoBatch(cachedDataList);
        log.info("存储数据库成功！");
    }
}
