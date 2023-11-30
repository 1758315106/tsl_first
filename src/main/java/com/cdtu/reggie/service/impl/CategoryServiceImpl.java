package com.cdtu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.reggie.common.CustomException;
import com.cdtu.reggie.entity.Category;
import com.cdtu.reggie.entity.Dish;
import com.cdtu.reggie.entity.Setmeal;
import com.cdtu.reggie.mapper.CategoryMapper;
import com.cdtu.reggie.service.CategoryService;
import com.cdtu.reggie.service.DishService;
import com.cdtu.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private DishService dishService;
    @Resource
    private SetmealService setmealService;


    @Override
    public void remove(Long id) {
        //添加查询条件，根据id分类查询
        LambdaQueryWrapper<Dish> dishlqw = new LambdaQueryWrapper<>();
        dishlqw.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishlqw);
        if (count > 0 ){
            throw  new CustomException("有关联菜品，删除失败");
        }

        LambdaQueryWrapper<Setmeal> setmeallqw = new LambdaQueryWrapper<>();
        setmeallqw.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(setmeallqw);
        if (count1 > 0 ){
            throw new CustomException("有关联菜品，删除失败");
        }

        super.removeById(id);
    }


}
