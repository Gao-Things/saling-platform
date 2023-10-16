package comp5703.sydney.edu.au.learn.DTO;

public class Message {
    public enum MessageType {
        SENT, RECEIVED
    }

    String content;
    MessageType type;

    public Message(String content, MessageType type) {
        this.content = content;
        this.type = type;
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
