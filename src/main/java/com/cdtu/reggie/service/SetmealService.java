package com.cdtu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdtu.reggie.entity.Setmeal;
import com.cdtu.reggie.entity.SetmealDao;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDao setmealDao);
    public void deleteWithDish(List<Long> ids);
    public SetmealDao getDate(Long id);
}
