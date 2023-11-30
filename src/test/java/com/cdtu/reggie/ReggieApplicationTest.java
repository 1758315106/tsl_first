package com.cdtu.reggie;

import com.cdtu.reggie.entity.Employee;
import com.cdtu.reggie.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReggieApplicationTest {
    @Autowired
    private EmployeeMapper employeeMapper;
    @Test
    void Demo(){
        List<Employee> list = employeeMapper.selectList(null);
        System.out.println(list);
    }
}
