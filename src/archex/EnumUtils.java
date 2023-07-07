package archex;

import com.google.gson.Gson;
import entity.EnumData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.FileUtils;
import utils.OfficeUtils;
import utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EnumUtils {

    private static ArrayList<String> moduleNames;

    public static void main(String[] args) {

        List<List<String>> list = parseXlsx("temp/archex/renum.xlsx", "菜品商机-食材");

        int columnCount = 41;

        List<String> type1List = new ArrayList<>();
        List<String> type2List = new ArrayList<>();
        String type1 = null;
        for (int i = 1; i < columnCount; i++) {
            String cell = list.get(0).get(i);
            if (StringUtils.isEmpty(cell)) {
                // 如果当前cell为空，用上一个
                cell = type1;
            } else {
                // 如果当前cell非空，更新type1
                type1 = cell;
            }
            type1List.add(cell);
            type2List.add(list.get(2).get(i));
        }
//        System.out.println(type1List);
//        System.out.println(type2List);


        List<List<String>> allEnumList = new ArrayList<>();
        for (int columnIndex = 1; columnIndex < columnCount; columnIndex++) {
            String type = type1List.get(columnIndex - 1) + "/" + type2List.get(columnIndex - 1) + "/";
            List<String> enumList = new ArrayList<>();
            for (int rowIndex = 3; ; rowIndex++) {
                String cell;
                try {
                    if(columnIndex == 40) {
                        System.out.println("");
                    }
                    cell = list.get(rowIndex).get(columnIndex);
                } catch (IndexOutOfBoundsException e) {
                    cell = null;
                }
                if(StringUtils.isEmpty(cell)) {
                    break;
                }
                enumList.add(type + cell);
            }
            allEnumList.add(enumList);
        }

        int totalCount = 0;
        for (int i = 0; i < allEnumList.size(); i++) {
            totalCount += allEnumList.get(i).size();
        }

        String json = new Gson().toJson(allEnumList);
        FileUtils.writeString2File(json, new File("temp/archex/renum.json"), "utf-8");
        // System.out.println(new Gson().toJson(allEnumList));
        System.out.println(totalCount);
    }

    // xlsx解析成list<list>
    private static List<List<String>> parseXlsx(String filepath, String sheetName) {
        List<List<String>> list = new ArrayList<>();
        XSSFWorkbook xlsx = OfficeUtils.openXlsx(new File(filepath));
        if (xlsx == null) {
            return list;
        }
        XSSFSheet sheet = xlsx.getSheet(sheetName);
        Iterator<Row> rowIterator = sheet.iterator();
        for (; rowIterator.hasNext(); ) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            List<String> rowList = new ArrayList<>();
            for (; cellIterator.hasNext(); ) {
                Cell cell = cellIterator.next();
                String value = cell.getStringCellValue();
                value = value.trim();
                rowList.add(value);
            }
            list.add(rowList);
        }
        return list;
    }

    private static void getModuleMap(ArrayList<String> lines) {
        moduleNames = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (StringUtils.isEmpty(line)) {
                continue;
            }

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
                if (!moduleName.equals(mName)) continue;

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

            String str = "        // %s\n" +
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
            if (items.length < 8) continue;

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
