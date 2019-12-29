package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.feign.GoodsFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Autowired
    private GoodsFeign goodsFeign;

    @RequestMapping("/list")
    public String list(Model model){
        List<Goods> goodsList = goodsFeign.goodsList();
        model.addAttribute("goodsList", goodsList);
        return "goodslist";
    }

}
