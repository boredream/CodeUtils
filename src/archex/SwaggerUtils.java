//package archex;
//
//import utils.FileUtils;
//import utils.StringUtils;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//public class SwaggerUtils {
//
//    private static ArrayList<Def> defList = new ArrayList<>();
//    private static ArrayList<Api> apiList = new ArrayList<>();
//
//    public static void main(String[] args) {
//        String content = FileUtils.readToString(new File("temp/swagger.txt"), "utf-8");
//        String[] split = content.split("paths:|definitions:|externalDocs:");
//
//        String definitions = split[2];
//        genDefs(definitions);
//
//        String paths = split[1];
//        genApis(paths);
//
//        genDefFiles();
//        genApiCode();
//    }
//
//    private static void genDefFiles() {
//        File dir = new File("temp/bean");
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        for (File file : dir.listFiles()) {
//            file.delete();
//        }
//
//        for (Def def : defList) {
//            if (def.name.endsWith("Result") && !def.name.equals("surveyResult")) continue;
//
//            StringBuilder sbField = new StringBuilder();
//            StringBuilder sbGetAndSet = new StringBuilder();
//            for (DefProp prop : def.props) {
//                StringBuilder sbNote = new StringBuilder();
//                sbNote.append("    /**\n");
//                if (!StringUtils.isEmpty(prop.des) || (prop.enums != null && prop.enums.size() > 0)) {
//                    if (!StringUtils.isEmpty(prop.des)) {
//                        sbNote.append("     * ")
//                                .append(prop.des);
//                    }
//
//                    if ((prop.enums != null && prop.enums.size() > 0)) {
//                        sbNote.append("\n").append("     * <br>");
//                        for (String e : prop.enums.keySet()) {
//                            sbNote.append("\n")
//                                    .append("     * <li> ")
//                                    .append(e)
//                                    .append(" </li>");
//                        }
//                    }
//                }
//                sbNote.append("\n     */\n");
//
//                String type = prop.type;
//                if (type.equalsIgnoreCase("string")) {
//                    type = "String";
//                } else if (type.equalsIgnoreCase("integer")) {
//                    type = "int";
//                } else if (type.equalsIgnoreCase("array")) {
//                    type = "ArrayList<String>";
//                } else if (type.equalsIgnoreCase("number")) {
//                    type = "int";
//                }
//
//                if (prop.name == null || prop.name.length() < 1) {
//                    prop.name = "type";
//                }
//
//                String name = StringUtils.firstToLowerCase(prop.name);
//                String field = "    private %s %s;\n\n";
//                field = String.format(field, type, name);
//
//                sbField.append(sbNote).append(field);
//
//
//                String getMethod = "    public %s %s() {\n" +
//                        "        return %s;\n" +
//                        "    }\n\n";
//                getMethod = String.format(getMethod, type, getGetMethod(name), name);
//                sbGetAndSet.append(getMethod);
//
//                String setMethod = "    public void %s(%s %s) {\n" +
//                        "        this.%s = %s;\n" +
//                        "    }\n\n";
//                setMethod = String.format(setMethod, getSetMethod(name), type, name, name, name);
//                sbGetAndSet.append(setMethod);
//            }
//
//            sbField.append(sbGetAndSet);
//            String classStr = "package com.archex.fsfa.entity;\n" +
//                    "\n" +
//                    "import java.io.Serializable;\n" +
//                    "\n" +
//                    "public class %s implements Serializable {\n\n" +
//                    "%s" +
//                    "    \n" +
//                    "}\n";
//
//            String className = StringUtils.firstToUpperCase(def.name);
//            String code = String.format(classStr, className, sbField.toString());
//
//            String name = className + ".java";
//            File file = new File(dir, name);
//            FileUtils.writeString2File(code, file, "utf-8");
//        }
//    }
//
//    private static String getGetMethod(String name) {
//        if (name.startsWith("is")) {
//            return name;
//        } else {
//            return "get" + StringUtils.firstToUpperCase(name);
//        }
//    }
//
//    public static String getSetMethod(String name) {
//        if (name.startsWith("is")) {
//            return name.replaceFirst("is", "set");
//        } else {
//            return "set" + StringUtils.firstToUpperCase(name);
//        }
//    }
//
//    private static void genApiCode() {
//        for (Api api : apiList) {
//            String apiStr = "    /**\n" +
//                    "     * %s\n" +
//                    "     */\n" +
//                    "    @%s(\"%s\")\n" +
//                    "    Observable<%s> %s(%s);\n";
//
//            StringBuilder noteSb = new StringBuilder();
//            noteSb.append(api.summary);
//
//            String response = api.response;
//            if (response.endsWith("List")) {
//                response = String.format("BaseListResponse<%s>", response);
//            } else if (response.endsWith("Result") && !response.equals("surveyResult")) {
//                response = "BaseResponse";
//            } else {
//                response = String.format("BaseResponse<%s>", response);
//            }
//
//            String method = api.name.substring(api.name.lastIndexOf("/") + 1);
//
//            StringBuilder paramsSb = new StringBuilder();
//
//            if (api.params.size() > 0) {
//                noteSb.append("\n     * ");
//
//                for (int i = 0; i < api.params.size(); i++) {
//                    ApiParam param = api.params.get(i);
//                    String name = StringUtils.firstToLowerCase(param.name);
//
//                    noteSb.append("\n     * @param ")
//                            .append(name);
//
//                    if (!StringUtils.isEmpty(param.des)) {
//                        noteSb.append(" ")
//                                .append(param.des);
//                    }
//
//                    if (i > 0) {
//                        paramsSb.append(",");
//                    }
//                    if (param.type.equals("schema")) {
//                        paramsSb.append("\n\t\t@Body ")
//                                .append(StringUtils.firstToUpperCase(param.schema))
//                                .append(" ")
//                                .append(name);
//                    } else {
//                        String type = param.type;
//                        if (type.equalsIgnoreCase("string")) {
//                            type = "String";
//                        }
//
//                        paramsSb.append("\n\t\t@Query(\"")
//                                .append(param.name)
//                                .append("\") ")
//                                .append(type)
//                                .append(" ")
//                                .append(name);
//                    }
//
//
//                }
//            }
//
//            apiStr = String.format(apiStr, noteSb.toString(), api.method, api.name, response, method, paramsSb.toString());
//            System.out.println(apiStr);
//        }
//    }
//
//    private static void genApis(String paths) {
//        String[] pathList = paths.split("  /");
//        for (String path : pathList) {
//            String[] rests = path.split("\n    [pdg]");
//            String name = rests[0];
//
//            for (int i = 1; i < rests.length; i++) {
//                String rest = rests[i];
//                Api api = new Api();
//                api.name = "/" + name.replace(":", "");
//
//                String[] items = rest.split("tags:|summary:|consumes:|parameters:|responses:");
//                String method = "";
//                switch (items[0].trim()) {
//                    case "et:":
//                        method = "GET";
//                        break;
//                    case "ost:":
//                        method = "POST";
//                        break;
//                    case "ut:":
//                        method = "PUT";
//                        break;
//                    case "elete:":
//                        method = "DELETE";
//                        break;
//                }
//                api.method = method;
//                api.summary = items[2].trim().substring(1, items[2].trim().length() - 1);
//
//                String[] params = items[4].split("-");
//                List<ApiParam> paramList = new ArrayList<>();
//                String refPre = "$ref: \"#/definitions/";
//                for (int j = 1; j < params.length; j++) {
//                    String[] lines = params[j].split("\n");
//
//                    if (lines[0].contains("header")) continue;
//
//                    ApiParam param = new ApiParam();
//                    for (int k = 0; k < lines.length; k++) {
//                        String line = lines[k].trim();
//                        if (line.startsWith("path:")) {
//                            param.name = line.replace("path:", "")
//                                    .replace("\"", "")
//                                    .trim();
//                        } else if (line.startsWith("description:")) {
//                            param.des = line.replace("description:", "")
//                                    .replace("\"", "")
//                                    .trim();
//                        } else if (line.startsWith("type:")) {
//                            param.type = line.replace("type:", "")
//                                    .replace("\"", "")
//                                    .trim();
//                        } else if (line.startsWith("schema:")) {
//                            String schema = lines[k + 1].replace(refPre, "")
//                                    .replace("\"", "")
//                                    .trim();
//                            param.type = "schema";
//                            param.schema = schema;
//                        }
//                    }
//
//                    paramList.add(param);
//                }
//                api.params = paramList;
//
//                String response = items[5].trim();
//                int index = response.indexOf(refPre);
//                api.response = response.substring(index + refPre.length(), response.length() - 1).trim();
//                api.response = StringUtils.firstToUpperCase(api.response);
//
//                apiList.add(api);
//            }
//        }
//    }
//
//    private static void genDefs(String definitions) {
//        StringBuilder sb = new StringBuilder();
//        String[] defLines = definitions.split("\n");
//        List<String> defStrList = new ArrayList<>();
//        for (int i = 0; i < defLines.length; i++) {
//            String line = defLines[i];
//            Pattern compile = Pattern.compile("^  [a-zA-Z]+:");
//            if (compile.matcher(line).find()) {
//                if (sb.toString().trim().length() > 0) {
//                    defStrList.add(sb.toString().trim());
//                    sb = new StringBuilder();
//                    sb.append(line).append("\n");
//                    continue;
//                }
//            }
//
//            sb.append(line).append("\n");
//        }
//
//        defList = new ArrayList<>();
//        for (String defStr : defStrList) {
//            String[] strs = defStr.split("    properties:");
//            String name = strs[0].split("type: \"")[0]
//                    .replace(":", "").trim();
//            String[] propLines = strs[1].trim().split("\n      ");
//
//            Def def = new Def();
//            def.name = name;
//            List<String> propStrList = new ArrayList<>();
//            StringBuilder sbProp = new StringBuilder();
//            for (String propLine : propLines) {
//                if (!propLine.startsWith("  ")) {
//                    if (sbProp.toString().trim().length() > 0) {
//                        propStrList.add(sbProp.toString());
//                        sbProp = new StringBuilder();
//                        sbProp.append(propLine.trim()).append("\n");
//                        continue;
//                    }
//                }
//
//                sbProp.append(propLine.trim()).append("\n");
//            }
//
//            List<DefProp> propList = new ArrayList<>();
//            for (String propStr : propStrList) {
//                DefProp prop = new DefProp();
//                String[] items = propStr.split("type:|description:|enum:");
//                if (items.length < 3) {
//                    continue;
//                }
//                prop.name = items[0].replace("\"", "")
//                        .replace(":", "")
//                        .trim();
//                prop.type = items[1].replace("\"", "").trim();
//                prop.des = items[2].trim().substring(1, items[2].trim().length() - 1);
//
//                if (items.length > 3) {
//                    Map<String, String> enumMap = new HashMap<>();
//                    String[] enums = items[3].split("- \"");
//                    for (String e : enums) {
//                        String[] kv = e.split(" : ");
//                        if (kv.length < 2) continue;
//                        String key = kv[0].replace("\"", "").trim();
//                        String value = kv[1].replace("\"", "").trim();
//                        enumMap.put(key, value);
//                    }
//                    prop.enums = enumMap;
//                }
//
//                propList.add(prop);
//            }
//
//            def.props = propList;
//            defList.add(def);
//        }
//    }
//
//    static class Api {
//        public String summary;
//        public String name;
//        public String method;
//        public List<ApiParam> params;
//        public String response;
//    }
//
//    static class ApiParam {
//        public String type;
//        public String des;
//        public String name;
//        public String schema;
//    }
//
//    static class Def {
//        public String name;
//        public List<DefProp> props;
//    }
//
//    static class DefProp {
//        public String type;
//        public String name;
//        public String des;
//        public Map<String, String> enums;
//    }
//
//}
