package comp5703.sydney.edu.au.learn.DTO;

public class Message {
    public enum MessageType {
        SENT, RECEIVED
    }

    String content;
    String avatarUrl;
    MessageType type;


    public Message(String content, String avatarUrl, MessageType type) {
        this.content = content;
        this.avatarUrl = avatarUrl;
        this.type = type;
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
}
