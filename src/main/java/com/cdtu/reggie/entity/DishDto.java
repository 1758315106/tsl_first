package com.cdtu.reggie.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish{
    public List<DishFlavor> flavors = new ArrayList<>();
    private String categoryName;
    private Integer copies;
}