package com.qf.controller;

import com.qf.entity.Goods;
import com.qf.entity.GoodsSeckill;
import com.qf.entity.ResultData;
import com.qf.feign.GoodsFeign;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.UUID;

@RequestMapping("/goods")
@Controller
public class GoodsController {

    //设置上传路径
    private String uploadPath = "G:/imgs";

    @Autowired
    private GoodsFeign goodsFeign;

    /**
     * 后台展示商品列表
     * @return
     */
    @RequestMapping("/list")
    public String list(Model model){
        List<Goods> goodsList = goodsFeign.goodsList();

        model.addAttribute("goodsList", goodsList);
        return "goodslist";
    }


    /**
     * 后台上传商品图片
     * @return
     */
    @RequestMapping("/uploader")
    @ResponseBody
    public ResultData<String> uploader(MultipartFile file){


        String fileName = UUID.randomUUID().toString();
        String path = uploadPath + "/" + fileName;

        try (
                InputStream inputStream = file.getInputStream();
                OutputStream outputStream = new FileOutputStream(path);
                ){
            IOUtils.copy(inputStream, outputStream);
            return new ResultData<String>().setCode(ResultData.ResultCodeList.OK).setData(path);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new ResultData<String>().setCode(ResultData.ResultCodeList.ERROR);
    }

    /**
     * 图片回显
     */
    @RequestMapping("/showimg")
    @ResponseBody

    public void showimg(String imgPath, HttpServletResponse response ){

        try (
                InputStream inputStream = new  FileInputStream(imgPath) ;
                ServletOutputStream outputStream = response.getOutputStream();
                ){
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("insert")
    public String insertGoods(Goods goods, GoodsSeckill goodsSeckill){

        System.out.println("添加商品");
        goods.setGoodsSeckill(goodsSeckill);
        System.out.println("商品为"+goods);
        goodsFeign.insertGoods(goods);
        return "redirect:http://localhost/back/goods/list";
    }

}
