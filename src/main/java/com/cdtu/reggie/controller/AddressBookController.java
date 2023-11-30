package com.cdtu.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cdtu.reggie.common.BaseContext;
import com.cdtu.reggie.common.R;
import com.cdtu.reggie.entity.AddressBook;
import com.cdtu.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

//    新增地址
    @PostMapping
    public R inserter(@RequestBody AddressBook addressBook){

        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    //设置默认地址
    @PutMapping("default")
    public R setDefault(@RequestBody AddressBook addressBook){

        LambdaUpdateWrapper<AddressBook> lqw = new LambdaUpdateWrapper<>();
        lqw.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        lqw.set(AddressBook::getIsDefault, 0);

        addressBookService.update(lqw);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }

    //根据id查询地址
    @GetMapping("/{id}")
    public R selectById(@PathVariable Long id){

        AddressBook address = addressBookService.getById(id);
        if (address != null){
        return R.success(address);
        }
        return R.error("未找到该对象");
    }

    //查询默认地址
    @GetMapping("default")
    public R getDefault(){

        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        lqw.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = addressBookService.getOne(lqw);
        if (addressBook ==null){
            R.error("未找到该对象");
        }
        return R.success(addressBook);
    }

    //查询全部地址
    @GetMapping("/list")
    public R list(AddressBook addressBook){
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        addressBook.setUserId(BaseContext.getCurrentId());
        lqw.eq(null != addressBook.getUserId(),AddressBook::getUserId,addressBook.getUserId());
        lqw.orderByDesc(AddressBook::getUpdateTime);

        return R.success(addressBookService.list(lqw));
    }
}
