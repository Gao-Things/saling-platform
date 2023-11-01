package com.usyd.capstone.common.VO;

import lombok.Data;

@Data
public class UpdateUserInfo {
    private Long id;

    private String name;

    private String email;

    private String avatarUrl;


}
