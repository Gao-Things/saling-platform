package comp5703.sydney.edu.au.learn.VO;

public class MyProductVO {
    private Integer userId;
    private boolean isSelling;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public boolean isSelling() {
        return isSelling;
    }

    public void setSelling(boolean selling) {
        isSelling = selling;
    }

    public MyProductVO(Integer userId, boolean isSelling) {
        this.userId = userId;
        this.isSelling = isSelling;
    }
}
