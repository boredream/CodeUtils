package archex;


import utils.CharacterParser;
import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UiUtils {

    public static final HashMap<String, String> nameFieldMap = new HashMap<>();

    static {
        // 主货架
        nameFieldMap.put("主货架总节数", "totalCount");
        nameFieldMap.put("单节货架层数", "storeyCount");
        nameFieldMap.put("单层排面数", "rowCount");
        nameFieldMap.put("单个品牌可抢占的最大节数", "maxCount");
        nameFieldMap.put("货架高度", "shelfHeight");
        nameFieldMap.put("货架宽度", "shelfWidth");
        nameFieldMap.put("更新频率", "updateRate");
        nameFieldMap.put("拍照", "photo");

        nameFieldMap.put("付费主货架节数", "shelfCount");
        nameFieldMap.put("陈列总排面数", "totalRowCount");
        nameFieldMap.put("单价 - 常规时间（前台购买）", "frontNormalUnitPrice");
        nameFieldMap.put("单价 - 节假日时间（前台购买）", "frontHolidayUnitPrice");
        nameFieldMap.put("单价 - 常规时间（后台购买）", "backendNormalUnitPrice");
        nameFieldMap.put("单价 - 节假日时间（后台购买）", "backendHolidayUnitPrice");

        nameFieldMap.put("免费主货架节数", "shelfCount");

        nameFieldMap.put("酱油类主货架节数", "shelfCount");

        nameFieldMap.put("我品酱油类主货架节数", "shelfCount");
        nameFieldMap.put("陈列评级", "shelfLevel");
        nameFieldMap.put("投放时间", "useDuration");
        nameFieldMap.put("安装时间", "setupDuration");

        nameFieldMap.put("竞品品牌", "productBrand");
        nameFieldMap.put("竞品酱油类主货架节数", "shelfCount");

        // 平面图
        nameFieldMap.put("消防疏散图", "fireEvacuationPhoto");
        nameFieldMap.put("门店布局平面图", "storeLayoutPhoto");


        // 其它陈列

        // TODO: chunyang 2021/3/9 添加非重复字段


    }

    static class FieldInfo {
        String module;
        String subModule;
        String name;
        String type;
        boolean necessary;
        transient String note;
        String field;
        String tip;
        String unit;

        // 获取组件名前缀 sib/iib/eib
        String getViewNamePre() {
            String idPre;
            switch (type) {
                case "下拉选择":
                    idPre = "sib";
                    break;
                case "照片上传":
                    idPre = "iib";
                    break;
                case "数字输入":
                default:
                    idPre = "eib";
                    break;
            }
            return idPre;
        }

        // 获取组件view名称
        public String getInputBarName() {
            String inputBar;
            switch (type) {
                case "下拉选择":
                    inputBar = "com.archex.core.view.inputbar.SpinnerInputBar";
                    break;
                case "照片上传":
                    inputBar = "com.archex.core.view.inputbar.ImageInputBar";
                    break;
                case "数字输入":
                default:
                    inputBar = "com.archex.core.view.inputbar.EditInputBar";
                    break;
            }
            return inputBar;
        }

        // 获取组件名id subModule_eib_fieldName
        String getViewId() {
            String fieldName = !StringUtils.isEmpty(field) ? field : name;
            String modulePre = "";
            if (subModule != null) {
                // 为了防止名字重复，前面加个sub module拼音前缀
                // 去括弧，转拼音
                modulePre = subModule.split("（")[0].trim();
                modulePre = CharacterParser.getInstance().getAllFirst(modulePre) + "_";
            }
            return modulePre + getViewNamePre() + "_" + StringUtils.camelTo_(fieldName);
        }

        // 获取组件字段名 subModuleEibFieldName
        String getViewFieldName() {
            return StringUtils._ToCamel(getViewId());
        }
    }

    public static void main(String[] args) {
        ArrayList<FieldInfo> infoList = parseFields("");
        genLayoutCode(infoList);
        genJavaCode(infoList);
    }

    private static ArrayList<FieldInfo> parseFields(String moduleName) {
        String str = FileUtils.readToString(new File("temp/fields.html"), "utf-8");
        // 分大模块
        String[] modules = str.split("<h3 id=\"id-模块字段-GT/MT渠道-");
        String content = null;
        for (int i = 1; i < modules.length; i++) {
            if (modules[i].startsWith(moduleName)) {
                content = modules[i];
                break;
            }
        }
        ArrayList<FieldInfo> infoList = new ArrayList<>();
        if (content == null) return infoList;

        String module = null;
        String subModule = null;
        for (String s : content.split("<tr>")) {
            if (s.contains("是否必填") && s.contains("子模块")) {
                continue;
            }

            String[] tds = s.split("</td>");
            if (tds[0].contains("id-模块字段-GT/MT渠道-") && tds[0].contains("<h4 ")) {
                // h4 对应子模块
                module = StringUtils.regexGet("<h4 id=\"id-模块字段-GT/MT渠道-(.*?)\">", tds[0]);
            }

            for (String td : tds) {
                if (td.contains("<td colspan=\"4\" class=\"confluenceTd\">")) {
                    // <td colspan="4" class="confluenceTd"><strong>付费地堆</strong></td>
                    subModule = td.split("<td colspan=\"4\" class=\"confluenceTd\">")[1]
                            .replaceAll("<.*?>", "").trim();
                    break;
                }
            }

            if (tds.length == 5) {
                FieldInfo info = new FieldInfo();
                String[] td0s = tds[0].split("<td .*?>");
                info.module = module;
                info.subModule = subModule;
                info.name = td0s[td0s.length - 1].replace("\n", "").replaceAll("<.*?>", "").trim();
                info.type = tds[1].replace("\n", "").replaceAll("<.*?>", "").trim();
                info.necessary = tds[2].replace("\n", "").replaceAll("<.*?>", "").trim().length() > 0;
                info.note = tds[3].replaceAll("<.*?>", "").trim();
                info.tip = null;
                info.field = nameFieldMap.get(info.name);
                if (info.note.contains("提示：")) {
                    for (String line : info.note.split("\n")) {
                        if (line.trim().startsWith("提示：")) {
                            info.tip = line.replaceFirst("提示：", "").trim();
                        }
                    }
                }
                if (info.note.contains("单位")) {
                    for (String line : info.note.split("\n")) {
                        if (line.trim().startsWith("单位：")) {
                            info.unit = line.replaceFirst("单位：", "").trim();
                        } else if (line.trim().startsWith("单位 ")) {
                            info.unit = line.replaceFirst("单位 ", "").trim();
                        }
                    }
                }
                infoList.add(info);
            }
        }
        return infoList;
    }

    private static void genIosCode(ArrayList<FieldInfo> infoList) {
        StringBuilder sbLayout = new StringBuilder();
        StringBuilder sbCode = new StringBuilder();

        String module = null;
        for (FieldInfo info : infoList) {
            String inputBar;
            String style = null;
            switch (info.type) {
                case "下拉选择":
                    inputBar = "GeneralInputView2";
                    style = "picker";
                    break;
                case "照片上传":
                    inputBar = "TakePhotoView";
                    break;
                case "数字输入":
                default:
                    inputBar = "GeneralInputView2";
                    break;
            }

            String fieldName;
            if (!StringUtils.isEmpty(info.field)) {
                fieldName = info.field;
            } else {
                fieldName = CharacterParser.getInstance().getAllFirst(info.name);
                fieldName = fieldName.replace("-", "")
                        .replace("（", "")
                        .replace("）", "")
                        .replace(" ", "");
            }
            String id = fieldName + "View";
            String title = info.name;
            if (info.unit != null) {
                title += "（" + info.unit + "）";
            }

            if (!info.module.equals(module)) {
                sbLayout.append("\n");
                sbLayout.append("模块：").append(info.module).append("\n");

                sbCode.append("\n");
                sbCode.append("模块：").append(info.module).append("\n");
            }
            module = info.module;

            sbLayout.append("lazy private var ").append(id).append(" = ")
                    .append(inputBar).append("(title: \"").append(title)
                    .append("\", isRequired: ").append(info.necessary).append(")");
            if (info.note.contains("小数")) {
                sbLayout.append(".keyboardType(.decimalPad)");
            } else if (info.note.contains("整数")) {
                sbLayout.append(".keyboardType(.numberPad)");
            }
            if (style != null) {
                sbLayout.replace(sbLayout.length() - 1, sbLayout.length(), ", style: ." + style + ")");
            }
            sbLayout.append("\n");

            if (!StringUtils.isEmpty(info.tip)) {
                sbCode.append(id).append(".showTip(.none, content: \"").append(info.tip).append("\")\n");
            }
        }

        FileUtils.writeString2File(sbLayout.toString(), new File("temp/layoutIos.txt"), "utf-8");
        FileUtils.writeString2File(sbCode.toString(), new File("temp/codeIos.txt"), "utf-8");
    }

    private static void genLayoutCode(ArrayList<FieldInfo> infoList) {
        HashMap<String, ArrayList<FieldInfo>> moduleFieldMap = new HashMap<>();
        for (FieldInfo info : infoList) {
            moduleFieldMap.computeIfAbsent(info.module, k -> new ArrayList<>()).add(info);
        }

        for (Map.Entry<String, ArrayList<FieldInfo>> entry : moduleFieldMap.entrySet()) {
            StringBuilder sb = new StringBuilder();
            for (FieldInfo info : entry.getValue()) {
                sb.append("<").append(info.getInputBarName()).append("\n");
                sb.append("\tandroid:id=\"@+id/").append(info.getViewId()).append("\"\n");
                sb.append("\tandroid:layout_width=\"match_parent\"\n");
                sb.append("\tandroid:layout_height=\"wrap_content\"\n");
                if (info.note.contains("小数")) {
                    sb.append("\tapp:filter=\"decimalString\"\n");
                } else if (info.note.contains("整数")) {
                    sb.append("\tapp:filter=\"num\"\n");
                }
                if (info.tip != null) {
                    sb.append("\tapp:tip=\"").append(info.tip).append("\"\n");
                }
                String title = info.name;
                if (info.unit != null) {
                    title += "（" + info.unit + "）";
                }
                sb.append("\tapp:header=\"").append(title).append("\"\n");
                sb.append("\tapp:isNecessary=\"").append(info.necessary).append("\" />\n");
                sb.append("\n");
            }

            FileUtils.writeString2File(sb.toString(), new File("temp/ui/layout" + entry.getKey() + ".xml"), "utf-8");
        }
    }

    private static void genJavaCode(ArrayList<FieldInfo> infoList) {
        HashMap<String, ArrayList<FieldInfo>> moduleFieldMap = new HashMap<>();
        for (FieldInfo info : infoList) {
            moduleFieldMap.computeIfAbsent(info.module, k -> new ArrayList<>()).add(info);
        }

        for (Map.Entry<String, ArrayList<FieldInfo>> entry : moduleFieldMap.entrySet()) {
            StringBuilder sb = new StringBuilder();

            // private static final Integer REQ_CODE_PHOTO = 20001;
            int baseReqCode = 20000;
            for (FieldInfo info : entry.getValue()) {
                if (!"照片上传".equals(info.type)) continue;

                String cst = "REQ_CODE_" + info.getViewId().toUpperCase();
                sb.append("private static final int ").append(cst).append(" = ").append(baseReqCode++).append(";\n");
            }
            sb.append("\n\n");

            // init iib
            for (FieldInfo info : entry.getValue()) {
                String viewFieldName = info.getViewFieldName();
                String cst = "REQ_CODE_" + info.getViewId().toUpperCase();

                sb.append(viewFieldName).append(".init(5, new OnPickImageListener() {\n");
                sb.append("\t@Override\n");
                sb.append("\tpublic void onCamera() {\n");
                sb.append("\t\ttakeCameraWithReqCode(").append(cst).append(");\n");
                sb.append("\t}\n");
                sb.append("\n");
                sb.append("\t@Override\n");
                sb.append("\tpublic void onAlbum() {\n");
                sb.append("\t}\n");
                sb.append("});\n");
            }
            sb.append("\n\n");

            // onPickImageResultWithRequestCode
            sb.append("@Override\n");
            sb.append("protected void onPickImageResultWithRequestCode(int requestCode, @NonNull String path) {\n");
            sb.append("\tswitch (requestCode) {\n");
            for (FieldInfo info : entry.getValue()) {
                String viewFieldName = info.getViewFieldName();
                String cst = "REQ_CODE_" + info.getViewId().toUpperCase();

                sb.append("\t\tcase ").append(cst).append(":\n");
                sb.append("\t\t\t").append(viewFieldName).append(".addLocalImage(path);\n");
                sb.append("\t\t\tbreak;\n");
            }
            sb.append("\t}\n");
            sb.append("}\n");
            sb.append("\n\n");

            // get
            for (FieldInfo info : entry.getValue()) {
                // subModule.setPhoto(subModuleIibPhoto.getImageStr());
                String bean = info.subModule == null ? "info" : info.getViewId().split("_")[0];
                String fieldName = !StringUtils.isEmpty(info.field) ? info.field : info.name;
                String setMethod = ".set" + StringUtils.firstToUpperCase(fieldName);
                String viewFieldName = info.getViewFieldName();

                sb.append(bean).append(setMethod).append("(").append(viewFieldName);
                switch (info.type) {
                    case "下拉选择":
                        sb.append(".getEnumSystemValue());");
                        break;
                    case "照片上传":
                        sb.append(".getImageStr());");
                        break;
                    case "数字输入":
                    default:
                        sb.append(".getText());");
                        break;
                }
                sb.append("\n");
            }
            sb.append("\n\n");

            // set
            for (FieldInfo info : entry.getValue()) {
                // subModule.setPhoto(subModuleIibPhoto.getImageStr());
                String bean = info.subModule == null ? "info" : info.getViewId().split("_")[0];
                String fieldName = !StringUtils.isEmpty(info.field) ? info.field : info.name;
                String getMethod = ".get" + StringUtils.firstToUpperCase(fieldName);
                String viewFieldName = info.getViewFieldName();

                sb.append(viewFieldName);
                switch (info.type) {
                    case "下拉选择":
                        sb.append(".setEnumBySystemValue(");
                        break;
                    case "照片上传":
                        sb.append(".setImageUrls(");
                        break;
                    case "数字输入":
                    default:
                        sb.append(".setText(");
                        break;
                }
                sb.append(bean).append(getMethod).append("());\n");
            }

            FileUtils.writeString2File(sb.toString(), new File("temp/ui/activity" + entry.getKey() + ".xml"), "utf-8");
        }
    }

}