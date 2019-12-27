package com.qf.service;

import com.qf.dao.GoodsImagesMapper;
import com.qf.dao.GoodsMapper;
import com.qf.dao.GoodsSeckillMapper;
import com.qf.entity.Goods;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GoodsServiceImpl implements IGoodsService{

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsImagesMapper goodsImagesMapper;

    @Autowired
    private GoodsSeckillMapper goodsSeckillMapper;


    //添加商品
    @Override
    public int insertGoods(Goods goods) {




        return 0;
    }

    //查询商品
    @Override
    public List<Goods> goodsList() {

        return null;
    }
}
