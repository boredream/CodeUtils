package entity;

import utils.FileUtils;
import utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ResultInfo {
    private String className;
    private String jsonStr;
    private HashMap<String, String> keyDesc;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    public HashMap<String, String> getKeyDesc() {
        return keyDesc;
    }

    public void setKeyDesc(HashMap<String, String> keyDesc) {
        this.keyDesc = keyDesc;
    }

    // 生成对象
    public String build(String dirPath) throws IOException {
        File file = new File(dirPath, className);
        String json = JsonUtils.getJavaFromJson(jsonStr);
        FileUtils.writeString2File(json, file);
        return className;
    }
}
