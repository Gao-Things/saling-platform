package com.usyd.capstone.common.DTO;

import com.usyd.capstone.entity.abstractEntities.User;
import lombok.Data;

@Data
public class MessageNotificationDTO {
    private Integer notificationId;

    private Integer messageType;

    private User remoteUser;

    private String notificationContent;
}
