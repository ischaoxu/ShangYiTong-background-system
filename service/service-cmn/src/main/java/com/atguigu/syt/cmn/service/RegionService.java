package com.atguigu.syt.cmn.service;

import com.atguigu.syt.model.cmn.Region;
import com.atguigu.syt.vo.cmn.RegionExcelVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  地区管理服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-04
 */
public interface RegionService extends IService<Region> {

    /**
     *  根据上级节点获取下一级别地区信息
     * @author liuzhaoxu
     * @date 2023/6/4 19:56
     * @param parentCode
     * @return java.util.List<com.atguigu.syt.model.cmn.Region>
     */

    List<Region> findRegionListByParentCode(String parentCode);
    /**
     *  获取导出的地区数据字典
     * @author liuzhaoxu
     * @date 2023/6/5 10:32
     * @return java.util.List<com.atguigu.syt.vo.cmn.RegionExcelVo>
     */

    List<RegionExcelVo> findRegionExcelVoList();

    /**
     *  导入数据字典
     * @author liuzhaoxu
     * @date 2023/6/5 11:33
     * @param cachedDataList
     */

    void saveRegionExcelVoBatch(List<RegionExcelVo> cachedDataList);
    /**
     *  导入excel
     * @author liuzhaoxu
     * @date 2023/6/5 11:55
     * @param file
     */

    void importData(MultipartFile file);
}
