package com.usyd.capstone.entity;

import com.usyd.capstone.entity.abstractEntities.User;

import javax.persistence.Entity;

@Entity
public class SuperUser extends User {
    private String unnamedAttr;
}
