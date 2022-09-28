package archex;

import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.FileUtils;
import utils.OfficeUtils;
import utils.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CompetChance {

    static class Config implements Serializable {
        String key;
        String type1;
        String type2;
        String type3;
        List<Input> inputList;

        @Override
        public String toString() {
            return key + ":" + inputList.size();
        }
    }

    static class Input implements Serializable {
        String title;
        String type;
        String necessary;
        String groupTitle;
        int groupNumber;
        Boolean isGroupFirst;
        Integer groupMaxCount;
        Integer groupMinNecessaryCount;
        List<Input> groupInputList;

        @Override
        public String toString() {
            return "Input{" +
                    "title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", necessary='" + necessary + '\'' +
                    ", groupTitle='" + groupTitle + '\'' +
                    ", groupNumber=" + groupNumber +
                    ", isGroupFirst=" + isGroupFirst +
                    ", groupMaxCount=" + groupMaxCount +
                    ", groupMinNecessaryCount=" + groupMinNecessaryCount +
                    '}';
        }
    }

    public static void main(String[] args) {
        // xlsx解析成list<list>
        List<List<String>> list = new ArrayList<>();
        XSSFWorkbook xlsx = OfficeUtils.openXlsx(new File("temp/archex/compet_chance/compet_chance.xlsx"));
        XSSFSheet sheet = xlsx.getSheet("0921竞品活动");
        Iterator<Row> rowIterator = sheet.iterator();
        int rowCount = 0;
        int columnCount = 0;
        for(;rowIterator.hasNext();) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            List<String> rowList = new ArrayList<>();
            columnCount = 0;
            for(;cellIterator.hasNext();) {
                Cell cell = cellIterator.next();
                rowList.add(cell.getStringCellValue());
                columnCount ++;
            }
            rowCount++;
            list.add(rowList);
        }
        System.out.println("rowCount " + rowCount + " columnCount " + columnCount);

        // 配置，每列一个配置
        List<Config> configList = new ArrayList<>();

        // 前三行 待规划活动类型 - 待规划活动形式 - 待规划活动形式类别
        List<String> type1List = list.get(0);
        int type1Index = 0;
        List<String> type2List = list.get(1);
        int type2Index = 1;
        List<String> type3List = list.get(2);

        // 记录所有字段3个类型
        for (int i = 1; i < columnCount - 1; i++) {
            // type1、2是多column合并，如果当前为空，则取前一位
            if(type1List.get(i).trim().length() > 0) {
                type1Index = i;
            }
            String type1 = type1List.get(type1Index);

            if(type2List.get(i).trim().length() > 0) {
                type2Index = i;
            }
            String type2 = type2List.get(type2Index);

            String type3 = type3List.get(i);

            Config config = new Config();
            config.type1 = StringUtils.isEmpty(type1) ? "无" : type1;
            config.type2 = StringUtils.isEmpty(type2) ? "无" : type2;
            config.type3 = StringUtils.isEmpty(type3) ? "无" : type3;
            config.key = config.type1 + "_" +config.type2 + "_" +config.type3;
            configList.add(config);
        }

        // 1~3行是类型，其它的才是字段
        int startIndex = 3;
        List<String> inputTitleList = new ArrayList<>();
        List<String> inputTypeList = new ArrayList<>();
        for (int row = startIndex; row < rowCount; row++) {
            // 第一列 字段名
            inputTitleList.add(list.get(row).get(0));

            // 最后一列 字段类型
            inputTypeList.add(list.get(row).get(columnCount - 1));
        }

        // 为所有配置添加字段，排除第一列字段和最后一列字段类型
        for (int column = 1; column < columnCount - 1; column++) {
            Config config = configList.get(column - 1);
            List<Input> inputList = new ArrayList<>();
            config.inputList = inputList;

            // 字段从第四行开始
            for (int row = startIndex; row < rowCount; row++) {
                Input input = new Input();
                // 字段名
                input.title = inputTitleList.get(row - startIndex);
                // 字段类型
                input.type = inputTypeList.get(row - startIndex);
                // 必填情况
                input.necessary = list.get(row).get(column);

                if(!StringUtils.isEmpty(input.necessary)) {
                    inputList.add(input);
                }
            }
        }

        // 解析集合组件的情况
        combineGroup(configList);

        // 打印
        print(configList);

        File file = new File("temp/archex/compet_chance/compet_chance_config.json");
        FileUtils.writeString2File(new Gson().toJson(configList), file, "utf-8");
    }

    // 解析集合组件的情况
    private static void combineGroup(List<Config> configList) {
        // 先记录组合字段前缀名称
        // TODO: chunyang 2022/9/28 前缀可能错乱？
        List<String> groupInputTitlePre = Arrays.asList(
                "产品", "赠送产品", "赠送物品", "研发菜品", "月均推广TOP", "赞助品牌");
//        for (int i = 0; i < inputTitleList.size(); i++) {
//             TODO: chunyang 2022/9/28 自动解析
//        }

        // 标记字段所属组信息
        for (Config config : configList) {
            String lastPre = null;
            for (int i = 0; i < config.inputList.size(); i++) {
                Input input = config.inputList.get(i);
                Integer number = StringUtils.getNumber(input.title);
                if(number == null) {
                    // 不包含数字，一定不是group
                    continue;
                }

                for (String pre : groupInputTitlePre) {
                    if(input.title.startsWith(pre)) {
                        input.groupTitle = pre;
                        input.groupNumber = number;
                        input.isGroupFirst = lastPre == null || !lastPre.equals(pre);
                        lastPre = pre;
                        break;
                    }
                }
            }
        }

        // 开始处理所有配置字段
        for (Config config : configList) {
            // 记录同组合需要合并的字段
            List<Input> tempGroupInputList = new ArrayList<>();
            // 挨个遍历配置下的输入字段
            List<Input> inputList = config.inputList;
            for (int i = inputList.size() - 1; i >= 0; i--) {
                Input input = inputList.get(i);
                // 倒序，挨个判断如果是支持集合
                Integer number = StringUtils.getNumber(input.title);
                if(number == null) {
                    // 不带数字的肯定不是组合
                    continue;
                }

                // 未匹配前缀的也不是组合
                String titlePre = null;
                for (String pre : groupInputTitlePre) {
                    if(input.title.startsWith(pre)) {
                        titlePre = pre;
                        break;
                    }
                }
                if(titlePre == null) {
                    continue;
                }

                // 如果是组合字段
                Input groupInput = new Input();
                groupInput.title = input.title;
                groupInput.type = input.type;
                groupInput.necessary = input.necessary;

                // 先记录到临时集合
                tempGroupInputList.add(0, groupInput);

                // 遇到字段组合第一个字段时，保存临时集合并处理总的字段
                if (input.isGroupFirst) {
                    input.groupInputList = new ArrayList<>(tempGroupInputList);
                    if (input.groupInputList.get(input.groupInputList.size() - 1).necessary.contains("选填")) {
                        // 最后一个是选填，则最大数量无限
                        input.groupMaxCount = -1;
                        // 继续向前找到必填位置
                        for (int j = input.groupInputList.size() - 2; j >= 0; j--) {
                            if (input.groupInputList.get(j).necessary.contains("必填")) {
                                input.groupMinNecessaryCount = j;
                            }
                        }
                    } else {
                        // 最后一个是必填，则最大数量限定
                        input.groupMaxCount = input.groupMinNecessaryCount = input.groupInputList.size();
                    }

                    input.title = input.title.split(String.valueOf(number))[0];
                    input.type = "group";
                    input.necessary = null;
                    input.isGroupFirst = null;

                    tempGroupInputList.clear();
                } else {
                    // 冗余字段的title置为null，用于后面删除
                    input.title = null;
                }
            }

            // 清理组合字段多余内容
            Iterator<Input> inputIterator = inputList.iterator();
            for(; inputIterator.hasNext();) {
                Input input = inputIterator.next();
                if(input.title == null) {
                    inputIterator.remove();
                }
            }
        }
    }

    private static void print(List<Config> configList) {
        for (Config config : configList) {
            System.out.println(config);
            for (int i = 0; i < config.inputList.size(); i++) {
                Input input = config.inputList.get(i);
                System.out.println(input);
                if(input.groupInputList != null) {
                    for (Input groupInput : input.groupInputList) {
                        System.out.println("    " + groupInput);
                    }
                }
            }
            System.out.println();
        }
    }

}
