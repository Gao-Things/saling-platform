package comp5703.sydney.edu.au.learn.VO;

public class modifyProductStatusParameter {
    private String token;
    private Integer productId;
    private Integer productStatusNew;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductStatusNew() {
        return productStatusNew;
    }

    public void setProductStatusNew(Integer productStatusNew) {
        this.productStatusNew = productStatusNew;
    }
}
