package com.cdtu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.reggie.common.CustomException;
import com.cdtu.reggie.entity.Setmeal;
import com.cdtu.reggie.entity.SetmealDao;
import com.cdtu.reggie.entity.SetmealDish;
import com.cdtu.reggie.mapper.SetmealMapper;
import com.cdtu.reggie.service.SetmealDishService;
import com.cdtu.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Resource
    private SetmealDishService setmealDishService;
    @Override
    public void saveWithDish(SetmealDao setmealDao) {
        this.save(setmealDao);
        List<SetmealDish> setmealDishes = setmealDao.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDao.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void deleteWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.in(Setmeal::getId,ids);
        lqw.eq(Setmeal::getStatus,1);
        int count = this.count(lqw);
        if (count > 0 ){
            throw new CustomException("套餐正在售卖中，无法删除");
        }

        //如果可以删除 ， 先删除setmeal表中的数据
        this.removeByIds(ids);
        //再删除setmeanldish表中的数据
        LambdaQueryWrapper<SetmealDish> dishlqw = new LambdaQueryWrapper<>();
        dishlqw.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(dishlqw);
    }

    @Override
    public SetmealDao getDate(Long id) {

        Setmeal setmeal = this.getById(id);
        SetmealDao setmealDao = new SetmealDao();
        LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(id != null ,SetmealDish::getSetmealId,id);
        if (setmeal != null){
            BeanUtils.copyProperties(setmeal,setmealDao);
            List list = setmealDishService.list(lqw);
            setmealDao.setSetmealDishes(list);
            return setmealDao;
        }
        return  null;
    }


}
