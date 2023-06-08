package com.atguigu.syt.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.syt.hosp.mapper.DepartmentRepository;
import com.atguigu.syt.hosp.service.DepartmentService;
import com.atguigu.syt.model.hosp.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Map;

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


}
