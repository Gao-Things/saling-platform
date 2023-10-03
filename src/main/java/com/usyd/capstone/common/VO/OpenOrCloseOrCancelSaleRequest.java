package com.usyd.capstone.common.VO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenOrCloseOrCancelSaleRequest {
    private String token;
    private Long productId;
    private int productStatusNew;
}
