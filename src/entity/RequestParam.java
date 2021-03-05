package entity;

import java.util.ArrayList;

public class RequestParam {
    private String name;
    private String type;
    private String restType = "Query";
    private String des;
    // 参数值为枚举型时
    private ArrayList<String> selectOptions;

    public RequestParam(String name, String type, String des,
                        ArrayList<String> selectOptions) {
        this.name = name;
        this.type = type;
        this.des = des;
        this.selectOptions = selectOptions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        // TODO string -> String
        if(type.equals("string")) {
            return "String";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRestType() {
        return restType;
    }

    public void setRestType(String restType) {
        this.restType = restType;
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
        return "RequestParam [path=" + name + ", type=" + type + ", des="
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