package com.usyd.capstone.common.compents;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.Enums.CATEGORY;
import com.usyd.capstone.entity.Product;
import com.usyd.capstone.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public abstract class BaseTask {

    @Autowired
    protected ProductMapper productMapper;

    protected HashMap<String, Product> productMap = new HashMap<>();

    protected CATEGORY category;
    public void initProductMap()
    {
        System.out.println(category);
        List<Product> tempList = productMapper.selectList(new QueryWrapper<Product>()
                .eq("category", category.getValue()));
        if(tempList != null) {
            for (Product temp : tempList)
                productMap.put(temp.getProductName().toLowerCase(), temp);
        }
    }
    public void modifyProductMap(int caseNum, Product product)
    {
        switch (caseNum){
            case 1:
                product.getProductName().toLowerCase();
        }
    }
}
