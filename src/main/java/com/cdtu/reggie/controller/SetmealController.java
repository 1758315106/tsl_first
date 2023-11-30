package com.cdtu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdtu.reggie.common.R;
import com.cdtu.reggie.entity.*;
import com.cdtu.reggie.service.CategoryService;
import com.cdtu.reggie.service.SetmealDishService;
import com.cdtu.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Resource
    private SetmealService setmealService;
    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private CategoryService categoryService;


    @PostMapping
    public R save(@RequestBody SetmealDao setmealDao){
        setmealService.saveWithDish(setmealDao);
        return R.success("新增套餐完成");
    }
    @GetMapping("/page")
    public R page(Integer page, Integer pageSize,String name){
        Page<Setmeal> pageInfo = new Page(page,pageSize);
        Page<SetmealDao> daoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<Setmeal>();
        lqw.like(name != null,Setmeal::getName,name);
        lqw.orderByDesc(Setmeal::getUpdateTime);
        //执行分页查询（分页器，查询条件）
        setmealService.page(pageInfo,lqw);

        BeanUtils.copyProperties(pageInfo,daoPage,"records");
        //定义一个集合存放套餐名称
        List<Setmeal> records = pageInfo.getRecords();
        //套餐集合根据菜品名称存放
        List<SetmealDao> list = records.stream().map((item)->{
            SetmealDao setmealDao = new SetmealDao();
            //将菜品名称集合存放到setmealdao实例中
            BeanUtils.copyProperties(item,setmealDao);
            //菜品id分类
            Long categoryId = item.getCategoryId();
            //根据菜品id查询套餐
            Category id = categoryService.getById(categoryId);
            if (id != null){
                String categoryname = id.getName();
                setmealDao.setCategoryName(categoryname);
            }

            return setmealDao;
        }).collect(Collectors.toList());
        //返回套餐名称集合
        daoPage.setRecords(list);
        return R.success(daoPage);
    }

    //批量删除套餐
    @DeleteMapping
    public R delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }

@PostMapping("/status/{st}")
    public R updatebyid(@PathVariable int st, String ids){

    String[] split = ids.split(",");
    List<Long> idList = Arrays.stream(split).map
            (s -> Long.parseLong(s.trim())
    ).collect(Collectors.toList());

    //将每个id new出来一个Dish对象，并设置状态
    List<Setmeal> setmeals = idList.stream().map((item) -> {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(item);
        setmeal.setStatus(st);
        return setmeal;
    }).collect(Collectors.toList()); //Dish集合


    setmealService.updateBatchById(setmeals);//批量操作

    return R.success("操作成功");
}

@GetMapping("/{id}")
    public R getDate(@PathVariable Long id){
    SetmealDao setmealDao = setmealService.getDate(id);
    return R.success(setmealDao);
}

  @PutMapping
    public R edit(@RequestBody SetmealDao setmealDao){
    //先判断是否接收到数据
    if (setmealDao == null ){
        R.error("请求异常");
    }
    //判断套餐下面是否还有关联菜品
    if (setmealDao.getSetmealDishes() == null){
        R.error("套餐内没有菜品，请添加");
    }
    //获取到关联的菜品列表，注意关联菜品的列表是我们提交过来的
    List<SetmealDish> setmealDishes = setmealDao.getSetmealDishes();
    //获取到套餐的id
    Long setmealDaoId = setmealDao.getId();
    //构造关联菜品的条件查询
    LambdaQueryWrapper<SetmealDish> lqw = new LambdaQueryWrapper<>();
    //根据套餐id在关联菜品中查询数据，注意这里所做的查询是在数据库中进行查询的
    lqw.eq(SetmealDish::getSetmealId,setmealDaoId);
    //关联菜品先移除掉原始数据库中的数据
    setmealDishService.remove(lqw);
    //为setmeal_dish表填充相关的属性
    //这里我们需要为关联菜品的表前面的字段填充套餐的id
    for(SetmealDish setmealDish:setmealDishes){
        setmealDish.setSetmealId(setmealDaoId);//填充属性值
    }
    //批量把setmealDish保存到setmeal_dish表
    //这里我们保存了我们提交过来的关联菜品数据
    setmealDishService.saveBatch(setmealDishes);
    //保存套餐关联菜品
    setmealService.updateById(setmealDao);
    //这里我们正常更新套餐
        return R.success("套餐修改成功");
}

@GetMapping("/list")
    public R list(Setmeal setmeal){
    LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
    lqw.eq(setmeal.getCategoryId() != null , Setmeal::getCategoryId,setmeal.getCategoryId());
    lqw.eq(setmeal.getStatus() != null , Setmeal::getStatus,setmeal.getStatus());
    lqw.orderByDesc(Setmeal::getUpdateTime);
    List<Setmeal> setmeals = setmealService.list(lqw);
    return R.success(setmeal);
}

}
