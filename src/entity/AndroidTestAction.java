package entity;

public class AndroidTestAction {

    public static final int TYPE_CLICK = 1;
    public static final int TYPE_TYPE_TEXT = 2;

    private int step;
    private boolean isNotAction;
    private int actionType;
    private String view;
    private String text;

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public boolean isNotAction() {
        return isNotAction;
    }

    public void setNotAction(boolean notAction) {
        isNotAction = notAction;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
