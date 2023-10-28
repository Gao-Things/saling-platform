package comp5703.sydney.edu.au.learn.DTO;

public class ProductUser {
    private Integer id;

    private String productName;

    private double productPrice;

    private String productImage;

    private long productCreateTime;

    private long productUpdateTime;

    private String productDescription;

    private Double productWeight;

    private String purity;

    //    private String no;
    private String name;

    private String email;

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

    public long getProductCreateTime() {
        return productCreateTime;
    }

    public void setProductCreateTime(long productCreateTime) {
        this.productCreateTime = productCreateTime;
    }

    public long getProductUpdateTime() {
        return productUpdateTime;
    }

    public void setProductUpdateTime(long productUpdateTime) {
        this.productUpdateTime = productUpdateTime;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(Double productWeight) {
        this.productWeight = productWeight;
    }

    public String getPurity() {
        return purity;
    }

    public void setPurity(String purity) {
        this.purity = purity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
