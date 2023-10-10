package com.usyd.capstone.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@TableName("notification")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "notification_id", type = IdType.AUTO)
    private Integer notificationId;

    @TableField("notification_type")
    private Integer notificationType;

    /**
     * 1:seler 2:buyer
     */
    @TableField("send_user_type")
    private Integer sendUserType;

    @TableField("send_user_id")
    private Integer sendUserId;

    @TableField("notification_content")
    private String notificationContent;

    @TableField("notification_timet_stamp")
    private Long notificationTimetStamp;

    @TableField("user_is_read")
    private Integer userIsRead;


}
