package com.qf.controller;

import com.qf.aop.IsLogin;
import com.qf.aop.LoginStatus;
import com.qf.entity.Goods;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.feign.GoodsFeign;
import com.qf.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/kill")
public class KillController {

    @Autowired
    private GoodsFeign goodsFeign;

    /**
     *     查询秒杀场次
     */
    @RequestMapping("/queryKillTimes")
    @ResponseBody
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
    @ResponseBody
    public ResultData<List<Goods>> queryKillList(Integer n){

        Date time = DateUtil.getNextNDate(n);

        List<Goods> goodsList = goodsFeign.queryKillList(time);

        return new ResultData<List<Goods>>().setCode(ResultData.ResultCodeList.OK)
                .setData(goodsList);
    }

    /**
     * 获取当前时间
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryNow")
    public ResultData<Date> queryNow(){
        return new ResultData<Date>().setCode(ResultData.ResultCodeList.OK).setData(new Date());
    }


    /**
     * 秒杀商品抢购
     */
    @RequestMapping("/buyNow")
    @IsLogin(mustLogin = true)
    public String buyNow(Integer gid, Integer gnumber, Model model){

        User user = LoginStatus.getUser();

        return "success";
    }

    /**
     * 验证码
     */
    @RequestMapping("/code")
    public void code(HttpServletResponse response){


    }
}
