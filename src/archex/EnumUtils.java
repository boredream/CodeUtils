package archex;

import com.google.gson.Gson;
import entity.EnumData;
import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EnumUtils {

    private static ArrayList<String> moduleNames;

    public static void main(String[] args) {
        ArrayList<String> lines = FileUtils.readToStringLines(new File("temp/office/enums.txt"));
        getModuleMap(lines);
        getModuleItemMap(lines);
    }

    private static void getModuleMap(ArrayList<String> lines) {
        moduleNames = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (StringUtils.isEmpty(line)) continue;

            String moduleDes = line.split("\t")[1];
            String moduleName = line.split("\t")[2];
            if (!moduleNames.contains(moduleName)) {
                String str = "moduleMap.put(\"TODO\", \"%s\"); // %s";
                str = String.format(str, moduleName, moduleDes);
                sb.append(str).append("\n");
                moduleNames.add(moduleName);
            }
        }
        System.out.println(sb.toString());
    }

    private static void getModuleItemMap(ArrayList<String> lines) {
//        // 菜品收集
//        moduleItemMap.put("dish_collection", new HashMap<String, String>() {
//            {
//                put("xx", "");
//            }
//        });

        StringBuilder sb = new StringBuilder();
        for (String mName : moduleNames) {
            List<String> names = new ArrayList<>();
            StringBuilder sbPut = new StringBuilder();
            String desc = "";
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (StringUtils.isEmpty(line)) continue;
                String moduleName = line.split("\t")[2];
                if(!moduleName.equals(mName)) continue;

                desc = line.split("\t")[1];
                String name = line.split("\t")[6];
                String item = line.split("\t")[3];

                if (!names.contains(name)) {
                    String str = "                put(\"%s\", \"%s\");";
                    str = String.format(str, name, item);
                    sbPut.append(str).append("\n");
                    names.add(name);
                }
            }

            String str =    "        // %s\n" +
                    "        moduleItemMap.put(\"%s\", new HashMap<String, String>() {\n" +
                    "            {\n" +
                    "%s" +
                    "            }\n" +
                    "        });";
            str = String.format(str, desc, mName, sbPut.toString());
            sb.append(str).append("\n\n");
        }
        System.out.println(sb.toString());
    }

    private static void getItemMap(ArrayList<String> lines) {
        List<String> names = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (StringUtils.isEmpty(line)) continue;

            String name = line.split("\t")[6];
            String item = line.split("\t")[3];
            if (!names.contains(name)) {
                String str = "itemMap.put(\"%s\", \"%s\");";
                str = String.format(str, name, item);
                sb.append(str).append("\n");
                names.add(name);
            }
        }
        System.out.println(sb.toString());
    }

    private static void getJson(ArrayList<String> lines) {
        List<EnumData> datas = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (StringUtils.isEmpty(line)) continue;

            String[] items = line.split("\t");
            if(items.length < 8) continue;

            EnumData data = new EnumData();
            data.setRole(items[0]);
            data.setModule(items[2]);
            data.setParentCol(items[4]);
            data.setCol(items[3]);
            if (!data.getCol().equals(data.getParentCol())) {
                data.setParentColEnumCode(items[5]);
            }
            data.setEnumValue(items[6]);
            data.setEnumCode(i + "");

            datas.add(data);
        }
        System.out.println(new Gson().toJson(datas));
    }

}
