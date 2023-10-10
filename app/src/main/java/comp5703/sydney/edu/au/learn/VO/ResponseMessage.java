package comp5703.sydney.edu.au.learn.VO;

public class ResponseMessage {
    private int ok;

    private Integer notificationId;


    public ResponseMessage(int ok, Integer notificationId) {
        this.ok = ok;
        this.notificationId = notificationId;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }
    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }
}

