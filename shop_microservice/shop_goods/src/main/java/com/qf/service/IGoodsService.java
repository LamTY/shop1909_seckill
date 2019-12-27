package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface IGoodsService {

    //添加商品
    int insertGoods(Goods goods);

    //查询商品
    List<Goods> goodsList();
}
