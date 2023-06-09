package com.atguigu.syt.cmn.service;

import com.atguigu.syt.model.cmn.Dict;
import com.atguigu.syt.vo.cmn.DictTypeVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-04
 */
public interface DictService extends IService<Dict> {
    /**
     *  获取所有数据字典信息
     * @author liuzhaoxu
     * @date 2023/6/4 18:59
     * @return java.util.List<com.atguigu.syt.vo.cmn.DictTypeVo>
     */

    List<DictTypeVo> findAllDictList();

    /**
     *  获取数据字典名称
     * @author liuzhaoxu
     * @date 2023/6/9 18:11
     * @param dictTypeId
     * @param value
     * @return java.lang.String
     */

    String getNameByDictTypeIdAndValue(Long dictTypeId, String value);
}
