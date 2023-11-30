package com.cdtu.reggie.controller;

import com.cdtu.reggie.common.R;
import com.cdtu.reggie.entity.Orders;
import com.cdtu.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {
    @Resource
    private OrdersService ordersService;

    @PostMapping("/submit")
    public R submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }
}
