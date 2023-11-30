package com.cdtu.reggie.entity;

import lombok.Data;

import java.util.List;

@Data
public class SetmealDao extends Setmeal{
    private List<SetmealDish> setmealDishes;//获取菜品信息
    private String categoryName;//获取菜品名称
}
