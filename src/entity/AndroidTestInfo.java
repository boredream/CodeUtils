package entity;

import java.util.List;

public class AndroidTestInfo {

    /**
     * 编号
     */
    private String number;

    /**
     * 测试项
     */
    private String title;

    /**
     * 操作步骤
     */
    private List<AndroidTestAction> actions;

    /**
     * 预期结果
     */
    private AndroidTestAssertion assertion;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AndroidTestAction> getActions() {
        return actions;
    }

    public void setActions(List<AndroidTestAction> actions) {
        this.actions = actions;
    }

    public AndroidTestAssertion getAssertion() {
        return assertion;
    }

    public void setAssertion(AndroidTestAssertion assertion) {
        this.assertion = assertion;
    }
}
