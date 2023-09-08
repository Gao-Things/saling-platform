package com.usyd.capstone.common.DTO;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class MetalApiResponse {
    private boolean success;
    private Long timestamp;
    private Date date;
    private String base;
    private JSONObject rates;


}