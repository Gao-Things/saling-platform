package com.usyd.capstone.service;
import com.usyd.capstone.common.DTO.LoginResponse;
import com.usyd.capstone.common.Enums.ROLE;
import com.usyd.capstone.common.VO.MakeOrUpdateAnOfferRequest;
import com.usyd.capstone.common.VO.AcceptOrCancelOrRejectAnOfferRequest;
import com.usyd.capstone.common.VO.OpenOrCloseOrCancelSaleRequest;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.CapstoneApplication;
import com.usyd.capstone.common.compents.JwtToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NormalUserServiceTest {
    @Resource
    private NormalUserService normalUserService; // 注入服务

    @Test
    public void makeOrUpdateAnOffer() {
        ROLE role;
        role = ROLE.ROLE_SUPER;
        String token = JwtToken.generateToken(3L, "123456", role);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(3L);
        loginResponse.setRole(3);
        MakeOrUpdateAnOfferRequest request = new MakeOrUpdateAnOfferRequest(); // 请确保类名正确
        request.setToken(token);
        request.setProductId(38L);
        request.setNote("sampleNote");
        request.setPrice(99);
        Result result = normalUserService.makeOrUpdateAnOffer(
                request.getToken(),
                request.getProductId(),
                request.getNote(),
                request.getPrice()
        );


        // 对结果进行验证
        assertNotNull(result);
//        assertEquals("Cannot parse the token!", result.getData());
    }

    @Test
    public void acceptAnOffer() {
        ROLE role;
        role = ROLE.ROLE_SUPER;
        String token = JwtToken.generateToken(2L, "123456", role);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(2L);
        loginResponse.setRole(3);
        AcceptOrCancelOrRejectAnOfferRequest request = new AcceptOrCancelOrRejectAnOfferRequest();
        request.setToken(token);
        request.setOfferId(12L);

        Result result = normalUserService.acceptAnOffer(request.getToken(), request.getOfferId());

        assertNotNull(result);
        // Add any additional assertions based on your expected results.

    }

    @Test
    public void cancelAnOffer() {
        ROLE role;
        role = ROLE.ROLE_SUPER;
        String token = JwtToken.generateToken(2L, "123456", role);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(2L);
        loginResponse.setRole(3);
        AcceptOrCancelOrRejectAnOfferRequest request = new AcceptOrCancelOrRejectAnOfferRequest();
        request.setToken(token);
        request.setOfferId(12L);

        Result result = normalUserService.cancelAnOffer(request.getToken(), request.getOfferId());

        assertNotNull(result);

        // Add any additional assertions based on your expected results.
    }

    @Test
    public void rejectAnOffer() {
        ROLE role;
        role = ROLE.ROLE_SUPER;
        String token = JwtToken.generateToken(2L, "123456", role);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(2L);
        loginResponse.setRole(3);
        AcceptOrCancelOrRejectAnOfferRequest request = new AcceptOrCancelOrRejectAnOfferRequest();
        request.setToken(token);
        request.setOfferId(12L);

        Result result = normalUserService.rejectAnOffer(request.getToken(), request.getOfferId());

        assertNotNull(result);
    }

    @Test
    public void openOrCloseOrCancelSale() {
        ROLE role;
        role = ROLE.ROLE_SUPER;
        String token = JwtToken.generateToken(2L, "123456", role);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(2L);
        loginResponse.setRole(3);
        OpenOrCloseOrCancelSaleRequest request = new OpenOrCloseOrCancelSaleRequest();
        request.setToken(token);
        request.setProductId(38L);
        request.setProductStatusNew(1); // Assuming 1 represents some valid status.

        Result result = normalUserService.openOrCloseOrCancelSale(request.getToken(), request.getProductId(), request.getProductStatusNew());
        assertNotNull(result);

    }
}