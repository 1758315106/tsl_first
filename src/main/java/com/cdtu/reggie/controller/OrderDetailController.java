package com.cdtu.reggie.controller;

import com.cdtu.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RequestMapping("/orderDetail")
@RestController
@Slf4j
public class OrderDetailController {
    @Resource
    private OrderDetailService orderDetailService;
}
