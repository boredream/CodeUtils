package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.ClassInfo;
import entity.ClassInfo.ClassField;

import javax.swing.plaf.TextUI;

public class ParseUtlis {

    public static void main(String[] args) {
        genParseWithRetrofit();
    }

    private static void genParseWithRetrofit() {
        List<File> entityFiles = FileUtils.getAllFiles(
                new File("parse" + File.separator + "entities"));

        StringBuilder sbService = new StringBuilder();
        StringBuilder sbMethod = new StringBuilder();

        for (File file : entityFiles) {
            ClassInfo info = readClassInfo(file);

            // // 添加课程
            // @POST("/1/classes/Course")
            // Observable<BaseEntity> addCourse(
            // @Body Course course);

            // add
            sbService.append(StringUtils.formatSingleLine(0, "// 添加" + getApiName(info)));
            sbService.append(StringUtils.formatSingleLine(0, "@POST(\"/1/classes/" + info.className + "\")"));
            sbService.append(StringUtils.formatSingleLine(0, "Observable<BaseEntity> add" + info.className + "("));
            sbService.append(StringUtils.formatSingleLine(2, "@Body " + info.className + " entity);"));
            sbService.append("\n");

            // // 活动订单添加到活动中
            // @PUT("/1/classes/Activity/{objectId}")
            // Observable<BaseEntity> addOrder2Act(
            // @Path("objectId") String orderId,
            // @Body Map<String, Relation> relation);

            // update
            sbService.append(StringUtils.formatSingleLine(0, "// 修改" + getApiName(info)));
            sbService.append(StringUtils.formatSingleLine(0, "@PUT(\"/1/classes/" + info.className + "/{objectId}\")"));
            sbService.append(StringUtils.formatSingleLine(0, "Observable<BaseEntity> update" + info.className + "("));
            sbService.append(StringUtils.formatSingleLine(2, "@Path(\"objectId\") String objectId,"));
            sbService.append(StringUtils.formatSingleLine(2, "@Body Map<String, Object> updateInfo);"));
            sbService.append("\n");

            // // 查询课程
            // @GET("/1/classes/Course")
            // Observable<ListResponse<Course>> getCourse(
            // @Query("limit") int perPageCount,
            // @Query("skip") int page,
            // @Query("where") String where,
            // @Query("include") String include);

            // query
            sbService.append(StringUtils.formatSingleLine(0, "// 查询" + getApiName(info)));
            sbService.append(StringUtils.formatSingleLine(0, "@GET(\"/1/classes/" + info.className + "\")"));
            sbService.append(StringUtils.formatSingleLine(0, "Observable<ListResponse<" + info.className + ">> get" + info.className + "("));
            sbService.append(StringUtils.formatSingleLine(2, "@Query(\"limit\") int perPageCount,"));
            sbService.append(StringUtils.formatSingleLine(2, "@Query(\"skip\") int page,"));
            sbService.append(StringUtils.formatSingleLine(2, "@Query(\"where\") String where,"));
            sbService.append(StringUtils.formatSingleLine(2, "@Query(\"include\") String include);"));
            sbService.append("\n");

            // /**
            // * 获取活动
            // */
            // public static Observable<ListResponse<Act>> getPrivateAct(String
            // city, int page) {
            // BmobService service = getApiService();
            // String where = "{\"city\":\"" + city + "\"}";
            // return service.getAct(CommonConstants.COUNT_OF_PAGE,
            // (page - 1) * CommonConstants.COUNT_OF_PAGE, where, "coach");
            // }

            // query method
            sbMethod.append(StringUtils.formatSingleLine(0, "/**"));
            sbMethod.append(StringUtils.formatSingleLine(0, " * 查询" + getApiName(info)));
            sbMethod.append(StringUtils.formatSingleLine(0, " */"));
            sbMethod.append(StringUtils.formatSingleLine(0, "public static Observable<ListResponse<" + info.className + ">> get" + info.className + "(int page) {"));
            sbMethod.append(StringUtils.formatSingleLine(1, "BmobService service = getApiService();"));
            sbMethod.append(StringUtils.formatSingleLine(1, "String where = \"{}\";"));
            sbMethod.append(StringUtils.formatSingleLine(1, "return service.get" + info.className + "(CommonConstants.COUNT_OF_PAGE,"));
            sbMethod.append(StringUtils.formatSingleLine(2, "(page - 1) * CommonConstants.COUNT_OF_PAGE, where, null);"));
            sbMethod.append(StringUtils.formatSingleLine(0, "}"));
            sbMethod.append("\n");

            // query method by where option
            List<ClassField> whereFiles = new ArrayList<>();
            for (ClassField field : info.fields) {
                if (field.isWhere) {
                    whereFiles.add(field);
                }
            }
            if (whereFiles.size() > 0) {
                sbMethod.append(StringUtils.formatSingleLine(0, "/**"));
                sbMethod.append(StringUtils.formatSingleLine(0, " * 查询" + getApiName(info)));
                sbMethod.append(StringUtils.formatSingleLine(0, " * @param page"));
                StringBuilder sbParams = new StringBuilder();
                StringBuilder sbWheres = new StringBuilder();
                StringBuilder sbWhere1 = new StringBuilder(); // [%s, %s]
                StringBuilder sbWhere2 = new StringBuilder(); // , whereJingluo, whereFunctionType
                for (ClassField field : whereFiles) {
                    // String whereJingluo = "{}";
                    // if (!TextUtils.isEmpty(jingLuo)) {
                    //     whereJingluo = "{\"jingLuo\":\"" + jingLuo + "\"}";
                    // }
                    // String whereFunctionType = "{}";
                    // if (!TextUtils.isEmpty(functionType)) {
                    //     whereFunctionType = "{\"functionType\":\"" + functionType + "\"}";
                    // }
                    // String where = String.format("{\"$and\":[%s, %s]}", whereJingluo, whereFunctionType);
                    String fieldName = getFieldName(field);
                    String firstUpperName = StringUtils.firstToUpperCase(field.name);

                    sbMethod.append(StringUtils.formatSingleLine(0, " * @param " + field.name + " " + fieldName));

                    sbParams.append(", ").append(field.type).append(" ").append(field.name);

                    sbWheres.append(StringUtils.formatSingleLine(1, "String where" + firstUpperName + " = \"{}\";"));
                    sbWheres.append(StringUtils.formatSingleLine(1, "if (!TextUtils.isEmpty(" + field.name + ")) {"));
                    if(field.type.equals("String")) {
                        sbWheres.append(StringUtils.formatSingleLine(2, "where" + firstUpperName + " = \"{\\\"" + field.name + "\\\":\\\"\" + " + field.name + " + \"\\\"}\";"));
                    } else {
                        sbWheres.append(StringUtils.formatSingleLine(2, "where" + firstUpperName + " = \"{\\\"" + field.name + "\\\":\" + " + field.name + " + \"}\";"));
                    }
                    sbWheres.append(StringUtils.formatSingleLine(1, "}"));

                    sbWhere1.append(", %s");

                    sbWhere2.append(", where").append(firstUpperName);

                    if (whereFiles.size() == 1) {
                        sbWheres.append(StringUtils.formatSingleLine(1, "String where = where" + firstUpperName + ";"));
                    }
                }

                if (whereFiles.size() > 1) {
                    sbWheres.append(StringUtils.formatSingleLine(1, "String where = String.format(\"{\\\"$and\\\":[" + sbWhere1.substring(2) + "]}\"" + sbWhere2.toString() + ");"));
                }

                sbMethod.append(StringUtils.formatSingleLine(0, " */"));
                sbMethod.append(StringUtils.formatSingleLine(0, "public static Observable<ListResponse<" + info.className + ">> get" + info.className + "(int page" + sbParams.toString() + ") {"));
                sbMethod.append(StringUtils.formatSingleLine(1, "BmobService service = getApiService();"));
                sbMethod.append(sbWheres.toString());
                sbMethod.append(StringUtils.formatSingleLine(1, "return service.get" + info.className + "(CommonConstants.COUNT_OF_PAGE,"));
                sbMethod.append(StringUtils.formatSingleLine(2, "(page - 1) * CommonConstants.COUNT_OF_PAGE, where, null);"));
                sbMethod.append(StringUtils.formatSingleLine(0, "}"));
                sbMethod.append("\n");
            }

            // relation
            for (ClassField field : info.fields) {
                // add or remove relationEntity
                if (field.type.equals("Relation")) {
                    String owningClass = info.className;
                    String owningName = getApiName(info);

                    String relationClass = field.name;
                    String relationName = getFieldName(field);

                    sbMethod.append(StringUtils.formatSingleLine(0, "/**"));
                    sbMethod.append(StringUtils.formatSingleLine(0, " * 添加/移除 [" + owningName + "] 的 [" + relationName + "]"));
                    sbMethod.append(StringUtils.formatSingleLine(0, " */"));
                    sbMethod.append(StringUtils.formatSingleLine(0, "public static Observable<BaseEntity> update" + owningClass + "Relation(" +
                            owningClass + " ownEntity, " + relationClass + " relationEntity, boolean isAdd) {"));
                    sbMethod.append(StringUtils.formatSingleLine(1,
                            "Pointer pointer = new Pointer(\"" + (field.type.equals("User") ? "_User" : field.type) + "\", relationEntity.getObjectId());"));
                    sbMethod.append(StringUtils.formatSingleLine(1,
                            "Relation relation = new Relation();"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "if(isAdd) {"));
                    sbMethod.append(StringUtils.formatSingleLine(2, "relation.add(pointer);"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "} else {"));
                    sbMethod.append(StringUtils.formatSingleLine(2, "relation.remove(pointer);"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "}"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "Map<String, Relation> relationMap = new HashMap<>();"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "relationMap.put(\"" + field.name + "\", relation);"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "return getApiService().update" + owningClass + "(ownEntity.getObjectId(), relationMap);"));
                    sbMethod.append(StringUtils.formatSingleLine(0, "}"));
                    sbMethod.append("\n");

                    // /**
                    // * 用户收藏列表,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
                    // */
                    // public static Observable<ListResponse<Course>>
                    // getCollectCourse() {
                    // BmobService service = getApiService();
                    //
                    // User currentUser = UserInfoKeeper.getCurrentUser();
                    //
                    // Where userIdEqaulWhere = new Where();
                    // userIdEqaulWhere.setClassName("_User");
                    // HashMap<String, String> userIdMap = new HashMap<>();
                    // userIdMap.put("objectId", currentUser.getObjectId());
                    // userIdEqaulWhere.setWhere(userIdMap);
                    //
                    // Map<String, Where> inQuerymap = new HashMap<>();
                    // inQuerymap.put(Where.OP_INQUERY, userIdEqaulWhere);
                    //
                    // Map<String, Map<String, Where>> whereMap = new
                    // HashMap<>();
                    // whereMap.put("collectUsers", inQuerymap);
                    //
                    // String where = new Gson().toJson(whereMap);
                    //
                    // return service.getCollectCourse(where);
                    // }

                    // get owningEntity list of relationEntity
                    sbMethod.append(StringUtils.formatSingleLine(0, "/**"));
                    sbMethod.append(StringUtils.formatSingleLine(0, " * 获取 关联了 [" + relationName + "] 的 [" + owningName + "]列表"));
                    sbMethod.append(StringUtils.formatSingleLine(0, " */"));
                    sbMethod.append(StringUtils.formatSingleLine(0,
                            "public static Observable<ListResponse<" + owningClass + ">> get" + owningClass + "Of" + relationClass + "(int page) {"));
                    sbMethod.append(StringUtils.formatSingleLine(1,
                            "Pointer pointer = new Pointer(\"" + (field.type.equals("User") ? "_User" : field.type) + "\", relationEntity.getObjectId());"));
                    sbMethod.append(StringUtils.formatSingleLine(1,
                            "Relation relation = new Relation();"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "if(isAdd) {"));
                    sbMethod.append(StringUtils.formatSingleLine(2, "relation.add(pointer);"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "} else {"));
                    sbMethod.append(StringUtils.formatSingleLine(2, "relation.remove(pointer);"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "}"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "Map<String, Relation> relationMap = new HashMap<>();"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "relationMap.put(\"" + field.name + "\", relation);"));
                    sbMethod.append(StringUtils.formatSingleLine(1, "return getApiService().update" + owningClass + "(ownEntity.getObjectId(), relationMap);"));
                    sbMethod.append(StringUtils.formatSingleLine(0, "}"));
                    sbMethod.append("\n");
                }
            }
        }

        System.out.println(sbService.toString());
        System.out.println(sbMethod.toString());
    }

    private static ClassInfo readClassInfo(File file) {
        ClassInfo info = new ClassInfo();
        String fileContent = FileUtils.readToString(file, "UTF-8");

        Matcher matcherAnotation = Pattern.compile(
                "(/\\*\\*[\\s\\S]+?\\*/)?[\\s]*public class ([\\S]+)").matcher(fileContent);
        if (matcherAnotation.find()) {
            if (matcherAnotation.group(1) != null) {
                info.annotation = matcherAnotation.group(1)
                        .replace("/**", "")
                        .replace("*/", "")
                        .replace("*", "")
                        .trim();
                info.annotation = info.annotation.split("\\s")[0];
            } else {
                info.annotation = null;
            }
            info.className = matcherAnotation.group(2).trim();

            fileContent = fileContent.replace(matcherAnotation.group(), "");
        }

        Matcher matcherFields = Pattern.compile(
                "(/\\*\\*[\\s\\S]+?\\*/)?[\\s]*(private|public) ([a-zA-Z_0-9]+) ([a-zA-Z_0-9]+)?;").matcher(fileContent);
        List<ClassField> fields = new ArrayList<>();
        while (matcherFields.find()) {
            String str = matcherFields.group();

            ClassField cf = new ClassField();
            if (matcherFields.group(1) != null) {
                cf.annotation = matcherFields.group(1)
                        .replace("/**", "")
                        .replace("*/", "")
                        .replace("*", "")
                        .trim();
                if (cf.annotation.contains("[where]")) {
                    cf.isWhere = true;
                    cf.annotation = cf.annotation.replace("[where]", "").trim();
                }
            }
            cf.scope = matcherFields.group(2);
            cf.type = matcherFields.group(3);
            cf.name = matcherFields.group(4);

            fields.add(cf);
        }
        info.fields = fields;

        return info;
    }

    private static String getApiName(ClassInfo info) {
        String apiName = "";
        if (info.annotation != null && info.annotation.length() > 0) {
            apiName = info.annotation.replace("*", "").trim();
            Matcher matcher = Pattern.compile("[\\^%&',;=\\?\\$\\s]+").matcher(apiName);
            if (matcher.find()) {
                apiName = apiName.substring(0, matcher.start()).trim();
            }
        } else {
            apiName = info.className;
        }
        return apiName;
    }

    private static String getFieldName(ClassField field) {
        String fieldName = "";
        if (field.annotation != null && field.annotation.length() > 0) {
            fieldName = field.annotation.replace("*", "").trim();
            Matcher matcher = Pattern.compile("[\\^%&',;=\\?\\$\\s]+").matcher(fieldName);
            if (matcher.find()) {
                fieldName = fieldName.trim().substring(0, matcher.start()).trim();
            }
        } else {
            fieldName = field.name;
        }
        return fieldName;
    }
}
