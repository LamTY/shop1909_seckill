package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {


    @Autowired
    private IGoodsService goodsService;

    /**
     * 添加商品
     * @param goods
     * @return
     */
    @RequestMapping("/insert")
    public int insertGoods(@RequestBody Goods goods){

        System.out.println("要添加的商品信息为："+goods);

        return goodsService.insertGoods(goods);
    }


    @RequestMapping("/list")
    public List<Goods> goodsList(){

        return goodsService.goodsList();
    }

}
