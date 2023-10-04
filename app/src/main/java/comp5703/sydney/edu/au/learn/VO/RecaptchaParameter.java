package comp5703.sydney.edu.au.learn.VO;

public class RecaptchaParameter {
    private String token;
    private String  expectedAction;

    public RecaptchaParameter() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpectedAction() {
        return expectedAction;
    }

    public void setExpectedAction(String expectedAction) {
        this.expectedAction = expectedAction;
    }
}
