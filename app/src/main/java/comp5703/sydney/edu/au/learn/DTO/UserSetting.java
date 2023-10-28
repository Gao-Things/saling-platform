package comp5703.sydney.edu.au.learn.DTO;

public class UserSetting {

    private Integer userId;


    private Integer notificationOpen;


    private Integer messageReceived;


    private Integer messageToneOpen;


    private String chooseTone;


    private Integer userSettingId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getNotificationOpen() {
        return notificationOpen;
    }

    public void setNotificationOpen(Integer notificationOpen) {
        this.notificationOpen = notificationOpen;
    }

    public Integer getMessageReceived() {
        return messageReceived;
    }

    public void setMessageReceived(Integer messageReceived) {
        this.messageReceived = messageReceived;
    }

    public Integer getMessageToneOpen() {
        return messageToneOpen;
    }

    public void setMessageToneOpen(Integer messageToneOpen) {
        this.messageToneOpen = messageToneOpen;
    }

    public String getChooseTone() {
        return chooseTone;
    }

    public void setChooseTone(String chooseTone) {
        this.chooseTone = chooseTone;
    }

    public Integer getUserSettingId() {
        return userSettingId;
    }

    public void setUserSettingId(Integer userSettingId) {
        this.userSettingId = userSettingId;
    }
}
