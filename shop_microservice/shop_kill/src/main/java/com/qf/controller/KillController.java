package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.entity.ResultData;
import com.qf.feign.GoodsFeign;
import com.qf.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/kill")
public class KillController {

    @Autowired
    private GoodsFeign goodsFeign;

    /**
     *     查询秒杀场次
     */
    @RequestMapping("/queryKillTimes")
    public ResultData<List<Date>> queryKillTimes(){

        List<Date> dates = new ArrayList<>();

        //获得当前时间
        Date now = DateUtil.getNextNDate(0);
        //获得下一个小时的时间
        Date next1 = DateUtil.getNextNDate(1);
        //获得下下个小时的时间
        Date next2 = DateUtil.getNextNDate(2);
        dates.add(now);
        dates.add(next1);
        dates.add(next2);

        return new ResultData<List<Date>>().setCode(ResultData.ResultCodeList.OK).setData(dates);
    }


    /**
     * 查询秒杀商品
     * @param n
     * @return
     */
    @RequestMapping("/queryKillList")
    public ResultData<List<Goods>> queryKillList(Integer n){

        Date time = DateUtil.getNextNDate(n);

        List<Goods> goodsList = goodsFeign.queryKillList(time);

        return new ResultData<List<Goods>>().setCode(ResultData.ResultCodeList.OK)
                .setData(goodsList);
    }

}
