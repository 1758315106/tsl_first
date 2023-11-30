package com.cdtu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdtu.reggie.common.R;
import com.cdtu.reggie.entity.Category;
import com.cdtu.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){
            log.info("category{}",category);
            categoryService.save(category);
            return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //构建分页器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        //条件排序
        lqw.orderByAsc(Category::getSort);
        //分页查询
        categoryService.page(pageInfo,lqw);
        return R.success(pageInfo);
    }

    //根据id删除菜品分类
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类id{}",id);
//        categoryService.removeById(id);
        categoryService.remove(id);
        return R.success("成功删除");
    }

    //根据id修改信息
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("分类信息修改成功");
    }

    //菜品分类查询
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();

        //添加条件
        lqw.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(lqw);
        return R.success(list);
    }

}
