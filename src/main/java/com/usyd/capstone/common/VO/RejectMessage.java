package com.usyd.capstone.common.VO;

import lombok.Data;

@Data
public class RejectMessage {
    private String cardTitle;
    private String cardDescription;
    private Integer offerId;
    private Integer userId;
}
