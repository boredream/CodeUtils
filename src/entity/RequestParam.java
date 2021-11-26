package entity;

import java.util.ArrayList;

public class RequestParam {
    private String name;
    private String fieldType;
    private String paramType = "Query";
    private String des;
    // 参数值为枚举型时
    private ArrayList<String> selectOptions;

    public RequestParam() {
    }

    public RequestParam(String name, String type, String des,
                        ArrayList<String> selectOptions) {
        this.name = name;
        this.fieldType = type;
        this.des = des;
        this.selectOptions = selectOptions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFieldType() {
        // TODO string -> String
        if(fieldType.equals("string")) {
            return "String";
        }
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public ArrayList<String> getSelectOptions() {
        return selectOptions;
    }

    public void setSelectOptions(ArrayList<String> selectOptions) {
        this.selectOptions = selectOptions;
    }

    @Override
    public String toString() {
        return "RequestParam [path=" + name + ", type=" + fieldType + ", des="
                + des + "]";
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof RequestParam) {
            RequestParam oParam = (RequestParam) o;
            return this.name.equals(oParam.name);
        }
        return super.equals(o);
    }
}