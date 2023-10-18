package com.usyd.capstone.service;
import com.usyd.capstone.common.VO.MakeOrUpdateAnOfferRequest;
import com.usyd.capstone.common.VO.AcceptOrCancelOrRejectAnOfferRequest;
import com.usyd.capstone.common.VO.OpenOrCloseOrCancelSaleRequest;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.CapstoneApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NormalUserServiceTest {
    @Autowired
    private NormalUserService normalUserService; // 注入服务

    @Test
    public void makeOrUpdateAnOffer() {

        MakeOrUpdateAnOfferRequest request = new MakeOrUpdateAnOfferRequest(); // 请确保类名正确
        request.setToken("sampleToken");
        request.setProductId(1L);
        request.setNote("sampleNote");
        request.setPrice(100.0);
        Result result = normalUserService.makeOrUpdateAnOffer(
                request.getToken(),
                request.getProductId(),
                request.getNote(),
                request.getPrice()
        );

        // 对结果进行验证
        assertNotNull(result);
//        assertEquals("Cannot parse the token!", result.getData());
//
//        // Add any additional assertions based on your expected results.
//        assertEquals("Cannot find the user account or product!", result.getData());
//        assertEquals("The user account isn't active!", result.getData());
//        assertEquals("You cannot buy your own items!", result.getData());
//        assertEquals("The product isn't for sale!", result.getData());



    }

    @Test
    public void acceptAnOffer() {
        AcceptOrCancelOrRejectAnOfferRequest request = new AcceptOrCancelOrRejectAnOfferRequest();
        request.setToken("sampleToken");
        request.setOfferId(1L);

        Result result = normalUserService.acceptAnOffer(request.getToken(), request.getOfferId());

        assertNotNull(result);
        // Add any additional assertions based on your expected results.

    }

    @Test
    public void cancelAnOffer() {
        AcceptOrCancelOrRejectAnOfferRequest request = new AcceptOrCancelOrRejectAnOfferRequest();
        request.setToken("sampleToken");
        request.setOfferId(1L);

        Result result = normalUserService.cancelAnOffer(request.getToken(), request.getOfferId());

        assertNotNull(result);

        // Add any additional assertions based on your expected results.
    }

    @Test
    public void rejectAnOffer() {
        AcceptOrCancelOrRejectAnOfferRequest request = new AcceptOrCancelOrRejectAnOfferRequest();
        request.setToken("sampleToken");
        request.setOfferId(1L);

        Result result = normalUserService.rejectAnOffer(request.getToken(), request.getOfferId());

        assertNotNull(result);
    }

    @Test
    public void openOrCloseOrCancelSale() {
        OpenOrCloseOrCancelSaleRequest request = new OpenOrCloseOrCancelSaleRequest();
        request.setToken("sampleToken");
        request.setProductId(1L);
        request.setProductStatusNew(1); // Assuming 1 represents some valid status.

        Result result = normalUserService.openOrCloseOrCancelSale(request.getToken(), request.getProductId(), request.getProductStatusNew());


    }
}