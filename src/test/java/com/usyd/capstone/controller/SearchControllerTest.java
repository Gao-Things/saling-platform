package com.usyd.capstone.controller;

import com.usyd.capstone.CapstoneApplication;
import com.usyd.capstone.common.DTO.Result;
import com.usyd.capstone.entity.Search;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchControllerTest {

@Autowired
    private SearchController searchController;
    @Test
    public void getSearchResultByInput() {
        Result result = searchController.getSearchResultByInput("apple");
        if (result.getCode() == 200) {
            System.out.println("success");
        }else{
            System.out.println("fail");
        }
    }

    @Test
    public void saveSearchHistory() {
        Search search = new Search(); // Construct a valid Search object
        search.setUserId(1); // Set a valid user ID
        search.setSearchContent("apple");
        Result result = searchController.saveSearchHistory(search);
        if (result.getCode() == 200) {
            System.out.println("success");
        }else{ // If the result code is not 200, then the test fails
            System.out.println("fail");
        }


    }

    @Test
    public void getSearchHistoryByUserId() {
        Result result = searchController.getSearchHistoryByUserId(1);
        if (result.getCode() == 200) {
            System.out.println("success");
        }else{
            System.out.println("fail");
        }
    }

    @Test
    public void deleteHistoryByUserId() {
        Result result = searchController.deleteHistoryByUserId(1);
        if (result.getCode() == 200) {
            System.out.println("success");
        }else{
            System.out.println("fail");
        }
    }
}