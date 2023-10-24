package comp5703.sydney.edu.au.learn.DTO;

public class TopSearch {

    private String content;

    private Integer searchCount;
    

    public TopSearch() {
    }

    public TopSearch(String content, Integer searchCount) {
        this.content = content;
        this.searchCount = searchCount;
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
