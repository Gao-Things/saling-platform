package comp5703.sydney.edu.au.learn.DTO;

public class Message {
    public enum MessageType {
        SENT, RECEIVED
    }

    String content;
    String avatarUrl;
    String postTime;
    MessageType type;


    public Message(String content, String avatarUrl, MessageType type, String postTime) {
        this.content = content;
        this.avatarUrl = avatarUrl;
        this.type = type;
        this.postTime = postTime;
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
}
