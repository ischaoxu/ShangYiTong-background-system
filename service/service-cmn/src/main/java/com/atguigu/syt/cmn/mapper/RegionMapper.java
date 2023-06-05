package com.atguigu.syt.cmn.mapper;

import com.atguigu.syt.model.cmn.Region;
import com.atguigu.syt.vo.cmn.RegionExcelVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author atguigu
 * @since 2023-06-04
 */
@Repository
public interface RegionMapper extends BaseMapper<Region> {
    /** 
     *  保存上传的数据字典
     * @author liuzhaoxu 
     * @date 2023/6/5 11:35
     * @param cachedDataList  
     */
    void insertRegionExcelVoList(@Param("cachedDataList") List<RegionExcelVo> cachedDataList);
    
}
