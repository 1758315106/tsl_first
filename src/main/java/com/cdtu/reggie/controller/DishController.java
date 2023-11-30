package com.cdtu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdtu.reggie.common.R;
import com.cdtu.reggie.entity.Category;
import com.cdtu.reggie.entity.Dish;
import com.cdtu.reggie.entity.DishDto;
import com.cdtu.reggie.entity.DishFlavor;
import com.cdtu.reggie.service.CategoryService;
import com.cdtu.reggie.service.DishFlavorService;
import com.cdtu.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Resource
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> insert(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R select(int page,int pageSize, String name){
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //构建查询及排序条件
        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        //添加过滤条件
        lqw.like(name != null,Dish::getName,name);
        //添加排序条件
        lqw.orderByDesc(Dish::getUpdateTime);
        //遍历分页查询列表数据，根据分类ID查询分类信息，从而获取该菜品的分类名称
        dishService.page(pageInfo,lqw);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            //根据id分类查询菜品
            Category category = categoryService.getById(categoryId);

            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;

        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);

        //封装数据并返回
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品修改成功");
    }

    //分类查询菜品
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        LambdaQueryWrapper<Dish> lqw = new LambdaQueryWrapper<>();
        lqw.eq(dish.getCategoryId() != null,Dish::getCategoryId, dish.getCategoryId());
        lqw.eq(Dish::getStatus,1);
        lqw.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(lqw);
        List<DishDto> dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishid = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId,dishid);
            List<DishFlavor> dishFlavorslist = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavorslist);

            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }

    @PostMapping("/status/{st}")
    public R updatestatus(@PathVariable int st, String ids) {
        String[] split = ids.split(",");
        List<Long> idList = Arrays.stream(split).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());

        //将每个id new出来一个Dish对象，并设置状态
        List<Dish> dishes = idList.stream().map((item) -> {
            Dish dish = new Dish();
            dish.setId(item);
            dish.setStatus(st);
            return dish;
        }).collect(Collectors.toList()); //Dish集合


        dishService.updateBatchById(dishes);//批量操作

        return R.success("操作成功");

    }
 }
