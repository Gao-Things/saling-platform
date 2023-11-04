package comp5703.sydney.edu.au.learn.DTO;

public class Message {
    public enum MessageType {
        SENT, RECEIVED, SENTCARD, RECEIVEDCARD
    }

    String content;
    String avatarUrl;
    String postTime;
    MessageType type;

    // 卡片专用字段
    private Integer productId;
    private String cardImageUrl;
    private String cardTitle;
    private String cardDescription;


    public Message() {
    }

    public Message(String content, String avatarUrl, MessageType type, String postTime) {
        this.content = content;
        this.avatarUrl = avatarUrl;
        this.type = type;
        this.postTime = postTime;
    }


    // 构造函数用于卡片消息
    public Message(String cardTitle,
                   String cardDescription,
                   String avatarUrl,
                   String cardImageUrl,
                   MessageType type,
                   String postTime,
                   Integer productId) {
        this.cardTitle = cardTitle;
        this.cardDescription = cardDescription;
        this.cardImageUrl = cardImageUrl;
        this.type = type;
        this.postTime = postTime;
        this.avatarUrl = avatarUrl;
        this.productId = productId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }
}
