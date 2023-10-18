package com.usyd.capstone.service;

import com.usyd.capstone.CapstoneApplication;
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
        int count;  // count the number of offers
        count = ((List<Offer>)offerService.getOfferListBySellerId(1).getData()).size();
        assertEquals(2, count);

    }
}