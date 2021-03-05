package entity;

public class EnumData {

    /**
     * 角色
     */
    private String role;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 父栏位类型（父和子栏位类型相同时，代表无父类型）
     */
    private String parentCol;

    /**
     * 父栏位选项 选中的条目
     */
    private String parentColEnumCode;

    /**
     * 栏位类型
     */
    private String col;

    /**
     * 栏位下条目值
     */
    private String enumValue;

    /**
     * 栏位下条目码
     */
    private String enumCode;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getParentCol() {
        return parentCol;
    }

    public void setParentCol(String parentCol) {
        this.parentCol = parentCol;
    }

    public String getParentColEnumCode() {
        return parentColEnumCode;
    }

    public void setParentColEnumCode(String parentColEnumCode) {
        this.parentColEnumCode = parentColEnumCode;
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    public String getEnumCode() {
        return enumCode;
    }

    public void setEnumCode(String enumCode) {
        this.enumCode = enumCode;
    }

}
