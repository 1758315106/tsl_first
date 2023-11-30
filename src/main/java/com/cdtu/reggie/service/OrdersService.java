package com.cdtu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdtu.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
