package comp5703.sydney.edu.au.learn.DTO;


import java.util.List;

public class Product {
    private Integer id;
    private String productName;
    private double productPrice;
    private String productImage;
    private Long productCreateTime;
    private Long productUpdateTime;
    private String productDescription;
    private double productExchangePrice;
    private Integer currentTurnOfRecord;
    private Integer priceStatus;

    private boolean inResettingProcess;

    private double productWeight;

    private String purity;

    private Long ownerId;

    private Integer productStatus;

    private Integer category;

    private int searchCount;

    private List<Offer> offers;

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getPurity() {
        return purity;
    }

    public void setPurity(String purity) {
        this.purity = purity;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Product() {
    }

    public double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(double productWeight) {
        this.productWeight = productWeight;
    }

    public Integer getPriceStatus() {
        return priceStatus;
    }

    public void setPriceStatus(Integer priceStatus) {
        this.priceStatus = priceStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Long getProductCreateTime() {
        return productCreateTime;
    }

    public void setProductCreateTime(Long productCreateTime) {
        this.productCreateTime = productCreateTime;
    }

    public Long getProductUpdateTime() {
        return productUpdateTime;
    }

    public void setProductUpdateTime(Long productUpdateTime) {
        this.productUpdateTime = productUpdateTime;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductExchangePrice() {
        return productExchangePrice;
    }

    public void setProductExchangePrice(double productExchangePrice) {
        this.productExchangePrice = productExchangePrice;
    }

    public Integer getCurrentTurnOfRecord() {
        return currentTurnOfRecord;
    }

    public void setCurrentTurnOfRecord(Integer currentTurnOfRecord) {
        this.currentTurnOfRecord = currentTurnOfRecord;
    }

    public boolean isInResettingProcess() {
        return inResettingProcess;
    }

    public void setInResettingProcess(boolean inResettingProcess) {
        this.inResettingProcess = inResettingProcess;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productImage='" + productImage + '\'' +
                ", productCreateTime=" + productCreateTime +
                ", productUpdateTime=" + productUpdateTime +
                ", productDescription='" + productDescription + '\'' +
                ", productExchangePrice=" + productExchangePrice +
                ", currentTurnOfRecord=" + currentTurnOfRecord +
                ", priceStatus=" + priceStatus +
                ", inResettingProcess=" + inResettingProcess +
                '}';
    }
}
