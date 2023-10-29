package comp5703.sydney.edu.au.learn.VO;

public class ItemVO {

    private Integer productId;
    private String itemTitle;
    private String itemDescription;
    private double itemWeight;

    private String imageUrl;
    private Integer userId;

    private Integer category;
    private Integer hiddenPrice;
    private String itemPurity;
    private double itemPrice;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(double itemWeight) {
        this.itemWeight = itemWeight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getHiddenPrice() {
        return hiddenPrice;
    }

    public void setHiddenPrice(Integer hiddenPrice) {
        this.hiddenPrice = hiddenPrice;
    }

    public String getItemPurity() {
        return itemPurity;
    }

    public void setItemPurity(String itemPurity) {
        this.itemPurity = itemPurity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }
}
