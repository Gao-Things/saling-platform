package com.usyd.capstone.common.DTO;

public class MessageFormat {

    /**
     * 1. text, 2. card
     */
    private Integer messageType;

    private String messageText;

    private Integer productId;

    private String cardTitle;

    private String cardImageUrl;

    private String cardDescription;

    public MessageFormat() {
    }

    public MessageFormat(Integer messageType, String messageText) {
        this.messageType = messageType;
        this.messageText = messageText;
    }

    public MessageFormat(Integer messageType,
                         String cardTitle,
                         String cardImageUrl,
                         String cardDescription,
                         Integer productId) {

        this.messageType = messageType;

        this.cardTitle = cardTitle;
        this.cardImageUrl = cardImageUrl;
        this.cardDescription = cardDescription;
        this.productId = productId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }
}
