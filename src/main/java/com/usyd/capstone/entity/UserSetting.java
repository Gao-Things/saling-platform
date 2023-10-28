package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author yyf
 * @since 2023年10月28日
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@TableName("user_setting")
public class UserSetting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("userId")
    private Integer userId;

    @TableField("notification_open")
    private Integer notificationOpen;

    @TableField("message_received")
    private Integer messageReceived;

    @TableField("message_tone_open")
    private Integer messageToneOpen;

    @TableField("choose_tone")
    private String chooseTone;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_setting_id", nullable = false)
    @TableId(value = "user_setting_id")
    private Integer userSettingId;


}
