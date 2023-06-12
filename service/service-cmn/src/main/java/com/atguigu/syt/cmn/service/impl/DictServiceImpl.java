package com.atguigu.syt.cmn.service.impl;

import com.atguigu.syt.cmn.mapper.DictMapper;
import com.atguigu.syt.cmn.mapper.DictTypeMapper;
import com.atguigu.syt.cmn.service.DictService;
import com.atguigu.syt.model.cmn.Dict;
import com.atguigu.syt.model.cmn.DictType;
import com.atguigu.syt.vo.cmn.DictTypeVo;
import com.atguigu.syt.vo.cmn.DictVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2023-06-04
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    @Autowired
    private DictTypeMapper dictTypeMapper;
    @Override
    @Cacheable(value = "DictTypeVos", unless="#result.size() == 0")
    public List<DictTypeVo> findAllDictList() {
//        1,获取字典类型信息
        List<DictType> dictTypeList = dictTypeMapper.selectList(null);
//        2,获取字典内容信息
        List<Dict> dictList = baseMapper.selectList(null);
//        3,遍历字典类型,根据类型id与dict_type_id封装到DictTypeVo对象集合
        ArrayList<DictTypeVo> DictTypeVos = new ArrayList<>();
        dictTypeList.forEach(dictType -> {
            DictTypeVo typeVo = new DictTypeVo();
            typeVo.setId("parent-"+dictType.getId());
            typeVo.setName(dictType.getName());

            List<Dict> list=
            dictList.stream().filter(dict -> dict.getDictTypeId().longValue() == dictType.getId().longValue()).collect(Collectors.toList());
            ArrayList<DictVo> dictVoList= new ArrayList<>();
            list.forEach(dict -> {
                DictVo dictVo = new DictVo();
                dictVo.setId("children-"+dict.getId());
                dictVo.setName(dict.getName());
                dictVo.setValue(dict.getValue());
                dictVoList.add(dictVo);
            });
            typeVo.setChildren(dictVoList);
            DictTypeVos.add(typeVo);
        });
        return DictTypeVos;
    }

    @Override
    public String getNameByDictTypeIdAndValue(Long dictTypeId, String value) {
        LambdaQueryWrapper<Dict> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dict::getDictTypeId, dictTypeId);
        wrapper.eq(Dict::getValue, value);
        Dict dict = baseMapper.selectOne(wrapper);
        if (dict == null) {
            return "";
        }
        return dict.getName();
    }

    @Override
    public List<Dict> listDictByDictTypeId(Long dictTypeId) {
        LambdaQueryWrapper<Dict> eq = new LambdaQueryWrapper<Dict>();
        eq.eq(Dict::getDictTypeId, dictTypeId);
        return baseMapper.selectList(eq);
    }
}
