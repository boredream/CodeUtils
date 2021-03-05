package entity;

public class AndroidTestAssertion {

    public static final int TYPE_SHOW_TOAST = 1;
    public static final int TYPE_SHOW_ACTIVITY = 2;
    public static final int TYPE_SHOW_DIALOG = 3;
    public static final int TYPE_SHOW_VIEW = 3;

    private int assertionType;
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAssertionType() {
        return assertionType;
    }

    public void setAssertionType(int assertionType) {
        this.assertionType = assertionType;
    }
}
