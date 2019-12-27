package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class Goods extends BaseEntity{
    private String subject;
    private String info;
    private BigDecimal price;
    private Integer save;
    private Integer type = 1;

    @TableField(exist = false)
    private String fmUrl;

    @TableField(exist = false)
    private List<String> otherUrl = new ArrayList<>();

    @TableField(exist = false)
    private GoodsSeckill goodsSeckill;

    public void addOtherUrl(String url){
        otherUrl.add(url);
    }

}
