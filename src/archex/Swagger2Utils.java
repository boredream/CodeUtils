package archex;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Swagger2Utils {

    private static ArrayList<Def> defList = new ArrayList<>();
    private static ArrayList<Api> apiList = new ArrayList<>();

    public static void main(String[] args) {

        String jsonStr = FileUtils.readToString(new File("temp/apidoc/swagger2.json"), "utf-8");

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonStr);
        JsonObject jsonObject = element.getAsJsonObject();

        JsonObject jbPathList = jsonObject.getAsJsonObject("paths");
        genApis(jbPathList);
        genApiCode();

        JsonObject jbDefList = jsonObject.getAsJsonObject("definitions");
        genDefs(jbDefList);
        genDefFiles();

    }

    private static String getType(String type) {
        switch (type) {
            case "string":
                return "String";
            case "integer":
                return "int";
        }
        return type;
    }

    private static void genDefFiles() {
        File dir = new File("temp/bean");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        for (File file : dir.listFiles()) {
            file.delete();
        }

        for (Def def : defList) {
            if (def.name.endsWith("Result") && !def.name.equals("surveyResult")) continue;

            StringBuilder sbField = new StringBuilder();
            StringBuilder sbGetAndSet = new StringBuilder();
            for (DefProp prop : def.props) {
                StringBuilder sbNote = new StringBuilder();
                if (!StringUtils.isEmpty(prop.des) || (prop.enums != null && prop.enums.size() > 0)) {
                    sbNote.append("    /**\n");
                    if (!StringUtils.isEmpty(prop.des)) {
                        sbNote.append("     * ")
                                .append(prop.des);
                    }

                    if ((prop.enums != null && prop.enums.size() > 0)) {
                        sbNote.append("\n").append("     * <br>");
                        for (String e : prop.enums.keySet()) {
                            sbNote.append("\n")
                                    .append("     * <li> ")
                                    .append(e)
                                    .append(" </li>");
                        }
                    }
                    sbNote.append("\n     */\n");
                }

                String type = prop.type;
                if (type.equalsIgnoreCase("string")) {
                    type = "String";
                } else if (type.equalsIgnoreCase("integer")) {
                    type = "int";
                } else if (type.equalsIgnoreCase("array")) {
                    type = "ArrayList<String>";
                } else if (type.equalsIgnoreCase("number")) {
                    type = "int";
                }

                if (prop.name == null || prop.name.length() < 1) {
                    prop.name = "type";
                }

                String name = StringUtils.firstToLowerCase(prop.name);
                String field = "    private %s %s;\n\n";
                field = String.format(field, type, name);

                sbField.append(sbNote).append(field);


                String getMethod = "    public %s %s() {\n" +
                        "        return %s;\n" +
                        "    }\n\n";
                getMethod = String.format(getMethod, type, getGetMethod(name), name);
                sbGetAndSet.append(getMethod);

                String setMethod = "    public void %s(%s %s) {\n" +
                        "        this.%s = %s;\n" +
                        "    }\n\n";
                setMethod = String.format(setMethod, getSetMethod(name), type, name, name, name);
                sbGetAndSet.append(setMethod);
            }

            sbField.append(sbGetAndSet);
            String classStr = "package com.archex.core.entity;\n" +
                    "\n" +
                    "import java.io.Serializable;\n" +
                    "\n" +
                    "public class %s implements Serializable {\n\n" +
                    "%s" +
                    "    \n" +
                    "}\n";

            String className = StringUtils.firstToUpperCase(def.name);
            String code = String.format(classStr, className, sbField.toString());

            String name = className + ".java";
            File file = new File(dir, name);
            FileUtils.writeString2File(code, file, "utf-8");
        }
    }

    private static String getGetMethod(String name) {
        if (name.startsWith("is")) {
            return name;
        } else {
            return "get" + StringUtils.firstToUpperCase(name);
        }
    }

    public static String getSetMethod(String name) {
        if (name.startsWith("is")) {
            return name.replaceFirst("is", "set");
        } else {
            return "set" + StringUtils.firstToUpperCase(name);
        }
    }

    private static void genApiCode() {
        System.out.println("--------------");
        System.out.println("     " + apiList.size());
        System.out.println("--------------");
        System.out.println();

        for (Api api : apiList) {
            String apiStr = "    /**\n" +
                    "     * %s\n" +
                    "     */\n" +
                    "    @%s(\"%s\")\n" +
                    "    Observable<%s> %s(%s);\n";

            StringBuilder noteSb = new StringBuilder();
            noteSb.append(api.summary);

            String response = api.response;

            String method = api.path.substring(api.path.lastIndexOf("/") + 1);

            StringBuilder paramsSb = new StringBuilder();

            if (api.params.size() > 0) {
                noteSb.append("\n     * ");

                List<ApiParam> falseReqParams = new ArrayList<>();
                for (int i = 0; i < api.params.size(); i++) {
                    ApiParam param = api.params.get(i);
                    if (param.inMap) {
                        falseReqParams.add(param);
                        continue;
                    }

                    String name = StringUtils.firstToLowerCase(param.name);

                    noteSb.append("\n     * @param ")
                            .append(name);

                    if (!StringUtils.isEmpty(param.des)) {
                        noteSb.append(" ")
                                .append(param.des);
                    }

                    if (i > 0) {
                        paramsSb.append(",");
                    }
                    if (param.type.equals("schema")) {
                        paramsSb.append("\n\t\t@Body ")
                                .append(StringUtils.firstToUpperCase(param.schema))
                                .append(" ")
                                .append(name);
                    } else {
                        String type = param.type;
                        if (type.equalsIgnoreCase("string")) {
                            type = "String";
                        }

                        paramsSb.append("\n\t\t@Query(\"")
                                .append(param.name)
                                .append("\") ")
                                .append(type)
                                .append(" ")
                                .append(name);
                    }
                }

                if (falseReqParams.size() > 0) {
                    noteSb.append("\n     * @param map 非必填选参数");
                    for (ApiParam falseReqParam : falseReqParams) {
                        noteSb.append("\n")
                                .append("     *            <li>")
                                .append(falseReqParam.name);
                        if(!StringUtils.isEmpty(falseReqParam.des)) {
                            noteSb.append(" ").append(falseReqParam.des);
                        }
                        noteSb.append("</li>");
                    }

                    paramsSb.append("\n\t\t@QueryMap HashMap<String, String> map");
                }
            }

            apiStr = String.format(apiStr, noteSb.toString(), api.method, api.path, response, method, paramsSb.toString());
            System.out.println(apiStr);
        }
    }

    private static void genApis(JsonObject paths) {
        for (Map.Entry<String, JsonElement> entry : paths.entrySet()) {
            String pathName = entry.getKey();
            JsonObject jbPaths = entry.getValue().getAsJsonObject();

            for (Map.Entry<String, JsonElement> methodPath : jbPaths.entrySet()) {
                String method = methodPath.getKey().toUpperCase();
                JsonObject jbPath = methodPath.getValue().getAsJsonObject();

                Api api = new Api();
                api.path = pathName;
                api.method = method;

                JsonElement jeDes = jbPath.get("summary");
                if (jeDes != null) {
                    api.summary = jeDes.getAsString();
                }

                List<ApiParam> params = new ArrayList<>();
                JsonElement jeParams = jbPath.get("parameters");
                if (jeParams == null) continue;
                for (JsonElement jeParam : jeParams.getAsJsonArray()) {
                    JsonObject jbParam = jeParam.getAsJsonObject();

                    ApiParam param = new ApiParam();

                    String name = jbParam.get("name").getAsString();
                    if (!StringUtils.isEmpty(name)) {
                        param.name = name;
                    }

                    JsonElement jeParamDes = jbParam.get("description");
                    if (jeParamDes != null) {
                        param.des = jeParamDes.getAsString();
                    }

                    String in = jbParam.get("in").getAsString();
                    if (in.equals("header")) {
                        continue;
                    }

                    if (in.equals("body")) {
                        JsonElement jeRef = jbParam.getAsJsonObject("schema").get("$ref");
                        if (jeRef == null) {
                            continue;
                        }
                        param.schema = jeRef.getAsString().replace("#/definitions/", "").trim();
                        param.type = "schema";
                    } else {
                        String type = jbParam.get("type").getAsString();
                        if (type.equals("array")) {
                            JsonElement jeType = jbParam.getAsJsonObject("items").get("type");
                            if (jeType != null) {
                                param.type = "Array<" + getType(jeType.getAsString()) + ">";
                            } else {
                                JsonElement jeRef = jbParam.getAsJsonObject("items").get("$ref");
                                if (jeRef == null) continue;

                                param.type = "schema";
                                param.schema = jeRef.getAsString().replace("#/definitions/", "").trim();
                            }
                        } else {
                            param.type = getType(type);
                        }
                    }

                    JsonElement jeReq = jbParam.get("required");
                    if (jeReq != null && !jeReq.getAsBoolean()) {
                        param.inMap = true;
                    }
                    param.inMap = false; // TODO: 2018/3/22

                    params.add(param);
                }
                api.params = params;

                JsonElement jeRef = jbPath.getAsJsonObject("responses").getAsJsonObject("200").getAsJsonObject("schema").get("$ref");
                String response = jeRef.getAsString().replace("#/definitions/", "").trim();
                response = response.replace("RestResponse", "BaseResponse")
                        .replace("»", ">")
                        .replace("«", "<")
                        .replace("string", "String")
                        .replace("Map", "HashMap")
                        .replace("<List<", "<ArrayList<");

                api.response = response;

                apiList.add(api);
            }
        }
    }

    private static void genDefs(JsonObject defs) {
        for (Map.Entry<String, JsonElement> entry : defs.entrySet()) {
            String name = entry.getKey();
            if (name.contains("RestResponse«")) {
                continue;
            }

            JsonElement jeDefProps = entry.getValue().getAsJsonObject().get("properties");
            if (jeDefProps == null) {
                continue;
            }
            List<DefProp> props = new ArrayList<>();
            for (Map.Entry<String, JsonElement> propEntry : jeDefProps.getAsJsonObject().entrySet()) {
                DefProp prop = new DefProp();
                prop.name = propEntry.getKey();

                JsonObject joProp = propEntry.getValue().getAsJsonObject();
                JsonElement jeDef = joProp.get("description");
                if (jeDef != null) {
                    prop.des = jeDef.getAsString();
                }

                JsonElement jeRef = joProp.get("$ref");
                if (jeRef != null) {
                    prop.type = jeRef.getAsString().replace("#/definitions/", "").trim();
                } else {
                    String type = getType(joProp.get("type").getAsString());
                    if (type.equals("array")) {
                        JsonElement jeType = joProp.getAsJsonObject("items").get("type");
                        if (jeType != null) {
                            prop.type = "Array<" + getType(jeType.getAsString()) + ">";
                        } else {
                            JsonElement jePropRef = joProp.getAsJsonObject("items").get("$ref");
                            if (jePropRef == null) continue;

                            prop.type = jePropRef.getAsString().replace("#/definitions/", "").trim();
                        }
                    } else {
                        prop.type = getType(type);
                    }
                }

                props.add(prop);
            }

            Def def = new Def();
            def.name = name;
            def.props = props;
            defList.add(def);
        }

    }

    static class Api {
        public String summary;
        public String path;
        public String method;
        public List<ApiParam> params;
        public String response;
    }

    static class ApiParam {
        public String type;
        public String des;
        public String name;
        public String schema;
        public boolean inMap;
    }

    static class Def {
        public String name;
        public List<DefProp> props;
    }

    static class DefProp {
        public String type;
        public String name;
        public String des;
        public Map<String, String> enums;
    }

}
