package com.qf.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.GoodsImagesMapper;
import com.qf.dao.GoodsMapper;
import com.qf.dao.GoodsSeckillMapper;
import com.qf.entity.Goods;
import com.qf.entity.GoodsImages;
import com.qf.entity.GoodsSeckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl implements IGoodsService{

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsImagesMapper goodsImagesMapper;

    @Autowired
    private GoodsSeckillMapper goodsSeckillMapper;


    //添加商品
    @Override
    @Transactional
    public int insertGoods(Goods goods) {

        //添加商品
        goodsMapper.insert(goods);

        //添加封面信息
        GoodsImages fmImages = new GoodsImages()
                .setGid(goods.getId())
                .setInfo(goods.getSubject())
                .setIsfengmian(1)
                .setUrl(goods.getFmUrl());
        goodsImagesMapper.insert(fmImages);

        //添加非封面信息
        for (String s : goods.getOtherUrl()) {
            GoodsImages unfmImages = new GoodsImages()
                    .setGid(goods.getId())
                    .setIsfengmian(0)
                    .setUrl(s);
            goodsImagesMapper.insert(unfmImages);
        }


        //判断是否是秒杀商品，是的话添加秒杀商品信息
        //TODO 添加秒杀信息
        if(goods.getType() == 2){
            GoodsSeckill goodsSeckill = goods.getGoodsSeckill();
            goodsSeckill.setGid(goods.getId());
            goodsSeckillMapper.insert(goodsSeckill);
        }



        return 0;
    }

    //查询商品
    @Override
    @Transactional(readOnly = true)
    public List<Goods> goodsList() {

        //查询所有商品
        List<Goods> goodsList = goodsMapper.selectList(null);

        //查询商品的封面与非封面
        for (Goods goods : goodsList) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("gid", goods.getId());
            List<GoodsImages> images = goodsImagesMapper.selectList(queryWrapper);

            //判断是封面还是非封面
            for (GoodsImages image : images) {
                if(image.getIsfengmian() == 1){
                    goods.setFmUrl(image.getUrl());
                }else {
                    goods.addOtherUrl(image.getUrl());
                }
            }


            //判断是否是秒杀商品，是的话查询秒杀商品信息
            if(goods.getType() == 2){
                GoodsSeckill goodsSeckill = goodsSeckillMapper.selectOne(queryWrapper);
                goods.setGoodsSeckill(goodsSeckill);

            }

        }


        return goodsList;
    }


    /**
     * 查询秒杀商品
     * @param time
     * @return
     */
    @Override
    public List<Goods> queryKillList(Date time) {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("start_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
        List<GoodsSeckill> killList = goodsSeckillMapper.selectList(queryWrapper);
        List<Goods> goodsList = new ArrayList<>();

        for (GoodsSeckill goodsSeckill : killList) {
            Goods goods = goodsMapper.selectById(goodsSeckill.getGid());
            goods.setGoodsSeckill(goodsSeckill);

            //查询相关图片
            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("gid", goods.getId());
            List<GoodsImages> images = goodsImagesMapper.selectList(queryWrapper2);

            for (GoodsImages image : images) {
                if(image.getIsfengmian() == 1){
                    //是封面
                    goods.setFmUrl(image.getUrl());
                } else {
                    //非封面
                    goods.addOtherUrl(image.getUrl());
                }
            }
            goodsList.add(goods);
        }

        return goodsList;
    }
}
