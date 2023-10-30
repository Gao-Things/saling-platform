package com.usyd.capstone.controller;

import com.usyd.capstone.CapstoneApplication;
import com.usyd.capstone.common.DTO.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @Autowired
    private ProductController productController;

    @Test
    public void getProductsWithPrices() {
        Result productsWithPrices = productController.getProductsWithPrices();
        if (productsWithPrices.getCode() == 200) {
            System.out.println("success");
        }else{
            System.out.println("fail");
        }


    }

    @Test
    public void uploadImage() throws IOException {
        String directoryPath = "/Users/cuiliuying/Desktop";
        String fileName = "test.png";
        String filePath = Paths.get(directoryPath, fileName).toString();

// 1. 读取文件内容
        byte[] fileContent = new byte[0];
        fileContent = Files.readAllBytes(Paths.get(filePath));
        // 2. 使用字节数组创建一个 MockMultipartFile
        MockMultipartFile multipartFile = new MockMultipartFile("file",
                fileName, "image/png", fileContent);
        Result result = productController.uploadImage(multipartFile);
        if (result.getCode() == 200) {
            System.out.println("success");
        }else{
            System.out.println("fail");
        }
    }

    @Test
    public void uploadProduct() {
        productVO productVO = new productVO();
        productVO.setProductId(29);
        productVO.setImageUrl("abcd");
        productVO.setItemDescription("test");
        productVO.setUserId(3);
        productVO.setItemTitle("apple");
        productVO.setItemWeight(20.22);
        productVO.setHiddenPrice(20);
        productVO.setItemCategory(1);
        productVO.setItemPurity("test");
        productVO.setItemPrice(20.22);
        Result result = productController.uploadProduct(productVO);
        if(result.getCode() == 200){
            System.out.println("success");
        }else{
            System.out.println("fail");
        }


    }

    @Test
    public void getProductDetail() {
        Result productDetail = productController.getProductDetail(29);
        if(productDetail.getCode() == 200) {
            System.out.println("success");
        }else{
            System.out.println("fail");
        }
    }
}