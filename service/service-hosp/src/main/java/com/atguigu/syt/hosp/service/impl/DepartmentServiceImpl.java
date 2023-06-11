package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.hosp.mapper.DepartmentRepository;
import com.atguigu.syt.hosp.service.DepartmentService;
import com.atguigu.syt.model.hosp.Department;
import com.atguigu.syt.vo.hosp.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Override
    public void save(Map<String, Object> paramMap, String hoscode, String depcode) {
        Department newDepartment = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Department.class);
        Department oldDepartment=departmentRepository.findByHoscodeAndDepcode(hoscode,depcode);
        if (oldDepartment == null) {
            departmentRepository.save(newDepartment);
        } else {
            newDepartment.setId(oldDepartment.getId());
            departmentRepository.save(newDepartment);
        }
    }

    @Override
    public Page<Department> getDepartmentList(int page, int limit, String hoscode) {
        Department department = new Department();
        department.setHoscode(hoscode);
        Example<Department> example = Example.of(department);
        Sort sort = Sort.by(Sort.Direction.ASC,"depcode");
        Pageable pageable = PageRequest.of(page - 1, limit,sort);
        Page<Department> departmentPage = departmentRepository.findAll(example, pageable);
//        Page<Department> departmentPage = departmentRepository.findAll(pageable);
        return departmentPage;
    }

    @Override
    public void deleteDepartment(Map<String, Object> paramMap) {
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        Department department = departmentRepository.findByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            departmentRepository.deleteById(department.getId());
        }
    }

    @Override
    public List<DepartmentVo> getDepartmentByHoscode(String hoscode) {
        //创建返回集合
        List<DepartmentVo> departmentVoList = new ArrayList<>();
        //1,获取医院所有的排班信息
        List<Department> departmentList = departmentRepository.getByHoscode(hoscode);
        //2,将排班信息按照大科室分组
        Map<String, List<Department>> depMap = departmentList.stream().collect(Collectors.groupingBy(Department::getBigcode));

        //3,将类型转换为DepartmentVo
        for (Map.Entry<String, List<Department>> entry : depMap.entrySet()) {
            DepartmentVo departmentVo = new DepartmentVo();
            departmentVo.setDepcode(entry.getKey());
            departmentVo.setDepname(entry.getValue().get(0).getBigname());
            List<Department> dep = entry.getValue();
            //4,并且封装小科室集合
            List<DepartmentVo> children = new ArrayList<>();
            if (dep.size() > 1) {
                dep.forEach(item ->{
                    DepartmentVo childrenVo = new DepartmentVo();
                    childrenVo.setDepname(item.getDepname());
                    childrenVo.setDepcode(item.getDepcode());
                    children.add(childrenVo);
                });
            }
            //5,小科室集合放入大科室
            departmentVo.setChildren(children);
            //6,将封装好的DepartmentVo放入返回集合中
            departmentVoList.add(departmentVo);
        }
        return departmentVoList;
    }


}
