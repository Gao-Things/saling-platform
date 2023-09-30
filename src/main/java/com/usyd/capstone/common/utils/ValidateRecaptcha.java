package com.usyd.capstone.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.usyd.capstone.common.Enums.SYSTEM_SECURITY_KEY;
import com.usyd.capstone.common.VO.Recaptcha;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ValidateRecaptcha {

    public boolean validate( Recaptcha recaptcha) {

        // 创建一个JSON对象
        JSONObject eventObject = new JSONObject();
        eventObject.put("token", recaptcha.getToken());
        eventObject.put("siteKey", SYSTEM_SECURITY_KEY.RECAPTCHA_SECRET_KEY.getValue());
        eventObject.put("expectedAction", recaptcha.getExpectedAction());

        // 将JSON对象添加到包含"event"字段的外部对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("event", eventObject);

        // 将JSONObject转换成JSON格式的字符串
        String jsonRequestBody = jsonObject.toJSONString();

        // API URL
        String apiUrl = "https://recaptchaenterprise.googleapis.com/v1/projects/my-project-54012-1692413464721/assessments?key=AIzaSyDM_dL6KmNoXYqXsAR8HFsYAftHpIVk4Mg";

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request entity with headers and body
        HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Send the POST request
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        // Handle the response as needed
        if (response.getStatusCode() == HttpStatus.OK) {


            String json =  response.getBody();

            JSONObject jsonObject1 = JSONObject.parseObject(json);
            JSONObject riskAnalysis = jsonObject1.getJSONObject("riskAnalysis");
            double score = riskAnalysis.getDoubleValue("score");
            System.out.println("Score: " + score);
            return !(score <= 0.2);

        } else {
            System.out.println("Request failed with status: " + response.getStatusCode());
            return false;
        }
    }
}
