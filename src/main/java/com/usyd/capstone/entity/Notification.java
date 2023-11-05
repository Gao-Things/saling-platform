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
 * @since 2023年10月10日
 */
@Getter
@Setter
@Entity
@Accessors(chain = true)
@TableName("notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    @TableId(value = "notification_id")
    private Integer notificationId;

    /**
     * 9 为系统消息
     */
    @TableField("notification_type")
    private Integer notificationType;

    /**
     * 1:seller 2:buyer
     */
    @TableField("send_user_type")
    private Integer sendUserType;

    @TableField("send_user_id")
    private Integer sendUserId;

    @TableField("notification_content")
    private String notificationContent;

    @TableField("notification_timestamp")
    private Long notificationTimestamp;

    @TableField("user_is_read")
    private Integer userIsRead;


}
