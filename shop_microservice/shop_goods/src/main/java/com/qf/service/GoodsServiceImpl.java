package com.qf.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.GoodsImagesMapper;
import com.qf.dao.GoodsMapper;
import com.qf.dao.GoodsSeckillMapper;
import com.qf.entity.Goods;
import com.qf.entity.GoodsImages;
import com.qf.entity.GoodsSeckill;
import com.qf.util.DateUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@CacheConfig(cacheNames = "goods")
public class GoodsServiceImpl implements IGoodsService{

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsImagesMapper goodsImagesMapper;

    @Autowired
    private GoodsSeckillMapper goodsSeckillMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //添加商品
    @Override
    @Transactional
    @CacheEvict(key = "'kill_' + #goods.goodsSeckill.startTime.time", condition = "#goods.type == 2")
    public int insertGoods(Goods goods) {

        System.out.println("good-service的添加商品");
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

            //将秒杀商品id放入redis集合中
            String timeSuffix = DateUtil.get2Date(goodsSeckill.getStartTime(), "yyMMddHH");
            stringRedisTemplate.opsForSet().add("killgoods_" + timeSuffix, goods.getId() + "");
        }


        // 将商品信息放入rabbitmq， 同步到索引库中,以及生成静态页面

        rabbitTemplate.convertAndSend("goods_exchange","",goods);

        return 1;
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
    @Cacheable(key = "'kill_' + #time.time")
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
