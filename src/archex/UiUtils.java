package archex;

import utils.FileUtils;

import java.io.File;

public class UiUtils {

    public static void main(String[] args) {
        String str = FileUtils.readToString(new File("temp/fields.html"), "utf-8");
        StringBuilder sb = new StringBuilder();
        for (String s : str.split("<tr>")) {
            if (s.contains("是否必填") && s.contains("子模块")) {
                continue;
            }
            String[] tds = s.split("</td>");
            if(tds.length == 5) {
                String name = tds[0].replace("\n", "").replaceAll("<.*?>", "").trim();
                String type = tds[1].replace("\n", "").replaceAll("<.*?>", "").trim();
                boolean necessary = tds[2].replace("\n", "").replaceAll("<.*?>", "").trim().length() > 0;
                String note = tds[3].replace("\n", "").replaceAll("<.*?>", "").trim();

                String inputBar;
                String idPre;
                switch (type) {
                    case "下拉选择":
                        idPre = "sib";
                        inputBar = "com.archex.core.view.inputbar.SpinnerInputBar";
                        break;
                    case "照片上传":
                        idPre = "iib";
                        inputBar = "com.archex.core.view.inputbar.ImageInputBar";
                        break;
                    case "数字输入":
                    default:
                        idPre = "eib";
                        inputBar = "com.archex.core.view.inputbar.EditInputBar";
                        break;
                }

                String id = idPre + "_" + name;

                sb.append("<").append(inputBar).append("\n");
                sb.append("\tandroid:id=\"@+id/").append(id).append("\"\n");
                sb.append("\tandroid:layout_width=\"match_parent\"\n");
                sb.append("\tandroid:layout_height=\"wrap_content\"\n");
                if (note.contains("小数")) {
                    sb.append("\tapp:filter=\"decimalString\"\n");
                } else if (note.contains("整数")) {
                    sb.append("\tapp:filter=\"num\"\n");
                }
                sb.append("\tapp:header=\"").append(name).append("\"\n");
                sb.append("\tapp:isNecessary=\"").append(necessary).append("\" />\n");
                sb.append("\n");

            }
        }

        System.out.println(sb.toString());

    }

}
