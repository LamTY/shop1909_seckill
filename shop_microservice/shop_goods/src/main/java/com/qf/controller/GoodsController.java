package com.qf.controller;

import com.qf.entity.Goods;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    /**
     * 添加商品
     * @param goods
     * @return
     */
    @RequestMapping("/insert")
    public String insertGoods(Goods goods){


        return "";
    }


    @RequestMapping("/list")
    public String goodsList(){

        return "";
    }

}
