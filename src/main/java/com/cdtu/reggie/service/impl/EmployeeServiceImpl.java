package com.cdtu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.reggie.entity.Employee;
import com.cdtu.reggie.mapper.EmployeeMapper;
import com.cdtu.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl
        <EmployeeMapper, Employee> implements EmployeeService {

}
