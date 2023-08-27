package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.usyd.capstone.entity.abstractEntities.NotSuperUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@TableName("admin_user")
public class AdminUser extends NotSuperUser implements Serializable {

    @OneToMany(mappedBy = "admin_user")
    private Set<AdminUserProduct> studentCourses = new HashSet<>();

}
