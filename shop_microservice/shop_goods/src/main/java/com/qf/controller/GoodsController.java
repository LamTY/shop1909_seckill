package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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


    /**
     * 查询商品信息
     * @return
     */
    @RequestMapping("/list")
    public List<Goods> goodsList(){

        return goodsService.goodsList();
    }


    /**
     * 查询秒杀商品信息
     * @return
     */
    public List<Goods> queryKillList(@RequestBody Date time){

        return goodsService.queryKillList(time);
    }

}
