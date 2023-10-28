package com.usyd.capstone.service;

import com.usyd.capstone.CapstoneApplication;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.Offer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfferServiceTest {
    @Autowired
    private OfferService offerService;

    @Test
    public void getOfferListBySellerId() {
        Result list = offerService.getOfferListBySellerId(5);
        List<Offer> data = (List<Offer>)list.getData();
        long count = data.stream().count();//count
        if(list.getCode()==200 ){
            System.out.println("success");
        }else {
            System.out.println("fail");
        }
    }
}