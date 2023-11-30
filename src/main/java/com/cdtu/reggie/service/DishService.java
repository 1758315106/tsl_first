package com.cdtu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdtu.reggie.entity.Dish;
import com.cdtu.reggie.entity.DishDto;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}