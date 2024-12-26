package work;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.FileUtils;
import utils.HttpUtils;
import utils.OfficeUtils;
import utils.ShUser;

import java.io.File;
import java.util.*;

public class WorkMain {

    static class TaskInfo {
        String story;
        String storyName;
        String taskName;
        String person;
        String personNumber;
        int sp;

        @Override
        public String toString() {
            return "TaskInfo{" +
                    "story='" + story + '\'' +
                    ", taskName='" + taskName + '\'' +
                    ", person='" + person + '\'' +
                    ", personNumber='" + personNumber + '\'' +
                    ", sp=" + sp +
                    '}';
        }
    }

    public static void main(String[] args) {
        String json = null;
        try {
            Map<String, String> header = new HashMap<>();
            header.put("Cookie", "cookiesession1=678A3E18475293BE2CD76A556399C382; pgv_pvi=5455938860; gr_user_id=d9ce2817-29f8-4643-8f43-0f6930518e36; pgv_info=ssi=s6685903712; mywork.tab.tasks=false; MPC_DEV_USER_TOKEN=b3fb31a46f3d4209a484199e8341296f; MPC_DEV_USER_INFO={%22account%22:%2218010089%22%2C%22anotherName%22:%22%E6%9D%8E%E6%98%A5%E9%98%B3%22%2C%22avater%22:%22http://jira.shinho.net.cn/secure/useravatar?ownerId=chunyang.li&avatarId=10805%22%2C%22department%22:0%2C%22email%22:%22lichunyang@shinhofood.com%22%2C%22employeeSexCd%22:%22999999%22%2C%22employeeTypeCd%22:%22Employee%22%2C%22id%22:1665%2C%22job%22:1%2C%22jobName%22:%22%E5%89%8D%E7%AB%AF%E5%BC%80%E5%8F%91%E5%B7%A5%E7%A8%8B%E5%B8%88%22%2C%22name%22:%22%E6%9D%8E%E6%98%A5%E9%98%B3%22%2C%22orgCd%22:%2201002374%22%2C%22parEmployeeId%22:%2217070057%22%2C%22parEmployeeName%22:%22%E5%BC%A0%E5%87%8C%22%2C%22parPostnCd%22:%2210005873%22%2C%22positionCd%22:%22SF01006625018810%22%2C%22postnName%22:%22%E9%AB%98%E7%BA%A7%E5%89%8D%E7%AB%AF%E5%BC%80%E5%8F%91%E5%B7%A5%E7%A8%8B%E5%B8%8804%22%2C%22postnType%22:%22%E9%AB%98%E7%BA%A7%E5%89%8D%E7%AB%AF%E5%BC%80%E5%8F%91%E5%B7%A5%E7%A8%8B%E5%B8%88%22%2C%22salesBuLevel%22:%225%22%2C%22salesBuStName%22:%22%E6%95%B0%E5%AD%97%E5%8C%96%E4%BA%A7%E5%93%81%E5%BC%80%E5%8F%91%E7%BB%84%EF%BC%88%E5%AF%B9%E5%A4%96%E4%B8%8A%E6%B5%B7%EF%BC%89%22%2C%22status%22:%22ACTIVE%22%2C%22title%22:2%2C%22token%22:%22b3fb31a46f3d4209a484199e8341296f%22%2C%22updatedAt%22:1720692002%2C%22password%22:%22Bore123321%22}; seraph.confluence=118980633%3Ab5f664b8897b2a5c4dba153cfaf3d48bb8e8bed5; MPC_TEST_USER_TOKEN=e89f9d57749b41e997448ef892b9888f; MPC_TEST_USER_INFO={%22account%22:%2218010089%22%2C%22anotherName%22:%22%E6%9D%8E%E6%98%A5%E9%98%B3%22%2C%22avater%22:%22http://jira.shinho.net.cn/secure/useravatar?ownerId=chunyang.li&avatarId=10805%22%2C%22department%22:0%2C%22email%22:%22lichunyang@shinhofood.com%22%2C%22employeeSexCd%22:%22999999%22%2C%22employeeTypeCd%22:%22Employee%22%2C%22id%22:1665%2C%22job%22:1%2C%22jobName%22:%22%E5%89%8D%E7%AB%AF%E5%BC%80%E5%8F%91%E5%B7%A5%E7%A8%8B%E5%B8%88%22%2C%22name%22:%22%E6%9D%8E%E6%98%A5%E9%98%B3%22%2C%22orgCd%22:%2201002818%22%2C%22parEmployeeId%22:%2221090069%22%2C%22parEmployeeName%22:%22%E9%BB%84%E9%9B%AA%E5%B3%B0%22%2C%22parPostnCd%22:%2210009572%22%2C%22positionCd%22:%2210009773%22%2C%22postnName%22:%22%E9%AB%98%E7%BA%A7%E5%89%8D%E7%AB%AF%E5%BC%80%E5%8F%91%E5%B7%A5%E7%A8%8B%E5%B8%88-04%22%2C%22postnType%22:%22%E9%AB%98%E7%BA%A7%E5%89%8D%E7%AB%AF%E5%BC%80%E5%8F%91%E5%B7%A5%E7%A8%8B%E5%B8%88%22%2C%22salesBuLevel%22:%224%22%2C%22salesBuStName%22:%22%E5%89%8D%E7%AB%AF%E7%BB%84%22%2C%22status%22:%22ACTIVE%22%2C%22title%22:2%2C%22token%22:%22e89f9d57749b41e997448ef892b9888f%22%2C%22updatedAt%22:1720692002%2C%22password%22:%22Bore123321%22}; MPC_PRD_USER_TOKEN=95d4866ac1ca4963aa7b0acfd51e5814; MPC_PRD_USER_INFO={%22account%22:%2218010089%22%2C%22anotherName%22:%22%E6%9D%8E%E6%98%A5%E9%98%B3%22%2C%22avater%22:%22http://jira.shinho.net.cn/secure/useravatar?ownerId=chunyang.li&avatarId=10805%22%2C%22department%22:1%2C%22email%22:%22lichunyang@shinhofood.com%22%2C%22employeeSexCd%22:%22999999%22%2C%22employeeTypeCd%22:%22Employee%22%2C%22id%22:1665%2C%22job%22:7%2C%22jobName%22:%22%E6%95%8F%E6%8D%B7%E6%95%99%E7%BB%83%22%2C%22name%22:%22%E6%9D%8E%E6%98%A5%E9%98%B3%22%2C%22orgCd%22:%2201002818%22%2C%22parEmployeeId%22:%2221090069%22%2C%22parEmployeeName%22:%22%E9%BB%84%E9%9B%AA%E5%B3%B0%22%2C%22parPostnCd%22:%2210013215%22%2C%22positionCd%22:%2210009773%22%2C%22postnName%22:%22%E9%AB%98%E7%BA%A7%E5%89%8D%E7%AB%AF%E5%BC%80%E5%8F%91%E5%B7%A5%E7%A8%8B%E5%B8%88-04%22%2C%22postnType%22:%22%E9%AB%98%E7%BA%A7%E5%89%8D%E7%AB%AF%E5%BC%80%E5%8F%91%E5%B7%A5%E7%A8%8B%E5%B8%88%22%2C%22salesBuLevel%22:%224%22%2C%22salesBuStName%22:%22%E5%89%8D%E7%AB%AF%E7%BB%84%22%2C%22status%22:%22ACTIVE%22%2C%22title%22:2%2C%22token%22:%2295d4866ac1ca4963aa7b0acfd51e5814%22%2C%22updatedAt%22:0%2C%22password%22:%22Bore123321%22}; JSESSIONID=F43208AD467D696893E8C8081664BCFE");
            json = HttpUtils.getString("http://confluence.shinho.net.cn/pages/viewpage.action?pageId=117943117", header);
            System.out.println(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (1==1) {
            return;
        }

        XSSFWorkbook xlsx = OfficeUtils.openXlsx(new File("temp/shinho/confluence.xlsx"));
        XSSFSheet sheet = xlsx.getSheet("SheetJS");
        Iterator<Row> rowIterator = sheet.iterator();

        // 先记录所有行列
        List<List<Object>> list = new ArrayList<>();
        for (; rowIterator.hasNext(); ) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            List<Object> rowValueList = new ArrayList<>();
            for (; cellIterator.hasNext(); ) {
                Cell cell = cellIterator.next();
                Object value = getCellValue(cell);
                rowValueList.add(value);
            }
            // 空的行跳过
            if (rowValueList.isEmpty()) {
                continue;
            }

            if ("任务名称（页面+功能）".equals(rowValueList.get(0))) {
                continue;
            }

            // 因为有回车的存在，所以copy的时候会出现拆分单元格情况。拆分到下一行的行就只会有一个列
            String userName = isSingleNameRow(rowValueList);
            if (userName != null) {
                // 名字信息加入到上一行的最后一个名字里
                List<Object> lastRow = list.get(list.size() - 1);
                lastRow.set(rowValueList.size() - 1, lastRow.get(rowValueList.size() - 1) + "," + userName);
                continue;
            }

            if (rowValueList.get(0) == null) {
                // 可能是技术方案里的回车，也可能是名字的回车。只有名字有意义
                continue;
            }

            list.add(rowValueList);
        }

        // 技术方案数据转为jira数据
        List<TaskInfo> taskList = new ArrayList<>();
        String story = null;
        String storyName = null;
        String storyUser = null;
        for (List<Object> row : list) {
            String firstValue = row.get(0).toString();
            if (firstValue.startsWith("A2-")) {
                // 故事数据
                story = firstValue.split(" - ")[0].trim();
                storyName = firstValue.split(" - ")[1].replace(" 待办", "").trim();
                // 有可能故事里指定了用户，代表这个故事下未填写人名的都分配到他
                Object user = row.get(row.size() - 1);
                if (user != null) {
                    storyUser = user.toString();
                }
                continue;
            }

            // 任务行
            Object user = row.get(row.size() - 1);
            String taskUser = user == null ? storyUser : user.toString();
            if (taskUser == null) {
                // 异常数据
                System.out.println("异常数据，缺失用户信息 " + row);
                continue;
            }

            Object sp = row.get(row.size() - 2);
            int spCount = 0;
            try {
                spCount = Integer.parseInt(sp.toString().replace(".0", ""));
            } catch (Exception e) {
                System.out.println("异常数据，SP不是数字 " + row);
                continue;
            }

            for (String singleUser : taskUser.split("[, ]")) {
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.story = story;
                taskInfo.storyName = storyName;
                taskInfo.taskName = firstValue;
                taskInfo.sp = spCount;
                taskInfo.person = singleUser;
                taskInfo.personNumber = ShUser.getUserNumber(singleUser);
                taskList.add(taskInfo);
            }
        }

        HashMap<String, HashSet<String>> userStoryMap = new HashMap<>();
        for (TaskInfo taskInfo : taskList) {
            HashSet<String> stories = userStoryMap.getOrDefault(taskInfo.person, new HashSet<>());
            stories.add(taskInfo.story);
            userStoryMap.put(taskInfo.person, stories);
        }

        HashMap<String, Integer> userTotalSpMap = new HashMap<>();
        for (TaskInfo taskInfo : taskList) {
            if(taskInfo.person.equals("胡翔")) {
                System.out.println(taskInfo.taskName +
                        "\t" + taskInfo.story +
                        "\t" + taskInfo.storyName +
                        "\t" + taskInfo.person +
                        "\t" + taskInfo.personNumber +
                        "\t" + taskInfo.sp +
                        "\t" + // 开始时间
                        "\t" + // 结束时间
                        "\t" + "前端"
                );
            }
            userTotalSpMap.put(taskInfo.person, userTotalSpMap.getOrDefault(taskInfo.person, 0) + taskInfo.sp);
        }

        userTotalSpMap.forEach((name, sp) -> {
            System.out.println("姓名：" + name);
            System.out.println("SP：" + sp);
            System.out.println("故事：" + userStoryMap.get(name).size());
            System.out.println();
        });
    }

    private static String isSingleNameRow(List<Object> row) {
        // 只有最后一列有值，且是用户信息
        for (int i = 0; i < row.size(); i++) {
            if (i != row.size() - 1 && row.get(i) != null) {
                return null;
            } else {
                String userName = String.valueOf(row.get(i));
                if (ShUser.getUserNumber(userName) != null) {
                    return userName;
                }
            }
        }
        return null;
    }

    private static Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                // 处理字符串类型的单元格
                return cell.getStringCellValue().trim();
            case Cell.CELL_TYPE_NUMERIC:
                // 处理数值类型的单元格
                return cell.getNumericCellValue();
        }
        return null;
    }

    static class DeviceCount {
        String name;
        int count;

        public DeviceCount(String name, int count) {
            this.name = name;
            this.count = count;
        }
    }

    private static void method(String path) {
        String content = FileUtils.readToString(new File(path));
        HashSet<String> userIdSet = new HashSet<>();
        int huaweiCount = 0;
        int deviceCount = 0;
        int androidDeviceCount = 0;
        int iOSDeviceCount = 0;
        ArrayList<DeviceCount> brandCounts = new ArrayList<>();
        ArrayList<DeviceCount> deviceCounts = new ArrayList<>();
        JsonArray array = new JsonParser().parse(content).getAsJsonObject()
                .get("aggregations").getAsJsonObject()
                .get("GROUP_PHONE").getAsJsonObject()
                .get("buckets").getAsJsonArray();
        for (JsonElement element : array) {
            String device = element.getAsJsonObject().get("key").getAsString();
            JsonArray userList = element.getAsJsonObject()
                    .get("GROUP_SALE").getAsJsonObject()
                    .get("buckets").getAsJsonArray();
            if (device.contains("HUAWEI")) {
                huaweiCount += userList.size();
            }
            deviceCount += userList.size();

            if (device.contains("iPhone") || device.contains("iPad")) {
                iOSDeviceCount += userList.size();
            } else {
                androidDeviceCount += userList.size();
                deviceCounts.add(new DeviceCount(device, userList.size()));

                DeviceCount curCount = null;
                String brand = device.split(" - ")[0].toLowerCase();
                for (DeviceCount count : brandCounts) {
                    if (count.name.equals(brand)) {
                        curCount = count;
                        break;
                    }
                }
                if (curCount == null) {
                    brandCounts.add(new DeviceCount(brand, userList.size()));
                } else {
                    curCount.count += userList.size();
                }
            }
            for (JsonElement je : userList) {
                String userId = je.getAsJsonObject().get("key").getAsString();
                userIdSet.add(userId);
            }
        }
        System.out.println(path + ": 华为用户=" + huaweiCount +
                " / 总用户=" + userIdSet.size() +
                " / 安卓设备=" + androidDeviceCount +
                " / 苹果设备=" + iOSDeviceCount +
                " / 总设备数=" + deviceCount);

        brandCounts.sort((o1, o2) -> Integer.compare(o2.count, o1.count));
        for (int i = 0; i < 10; i++) {
            System.out.println(brandCounts.get(i).name
                    + "=" + brandCounts.get(i).count);
        }
        System.out.println("----");
    }

}
