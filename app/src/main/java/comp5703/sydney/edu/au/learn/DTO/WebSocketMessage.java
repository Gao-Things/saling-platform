package comp5703.sydney.edu.au.learn.DTO;

public class WebSocketMessage {

    private Integer notificationId;

    private Integer messageType;
    private String sendUserType;

    private Offer offer;

    private Product product;

    private User remoteUser;

    private String notificationContent;

    public User getRemoteUser() {
        return remoteUser;
    }

    public void setRemoteUser(User remoteUser) {
        this.remoteUser = remoteUser;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public String getSendUserType() {
        return sendUserType;
    }

    public void setSendUserType(String sendUserType) {
        this.sendUserType = sendUserType;
    }
}
