package comp5703.sydney.edu.au.learn.DTO;

public class TopSearch {

    private Integer productId;

    private String content;

    private Integer searchCount;
    

    public TopSearch() {
    }

    public TopSearch(Integer productId, String content, Integer searchCount) {
        this.productId = productId;
        this.content = content;
        this.searchCount = searchCount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Integer searchCount) {
        this.searchCount = searchCount;
    }
}
