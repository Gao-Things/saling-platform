package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.usyd.capstone.entity.abstractEntities.NotSuperUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@TableName("normal_user")
public class NormalUser extends NotSuperUser implements Serializable {
    private Long id;
    private int gender;

    private String phone;
}
