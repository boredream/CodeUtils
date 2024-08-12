package test;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.OfficeUtils;

import java.io.File;
import java.util.*;

public class JxMain {

    public static void main(String[] args) {
        XSSFWorkbook xlsx = OfficeUtils.openXlsx(new File("temp/jixiao/jx1.xlsx"));
        XSSFSheet sheet = xlsx.getSheet("SheetJS");
        Iterator<Row> rowIterator = sheet.iterator();
        HashMap<String, Double> namePointMap1 = new HashMap<>();
        for (; rowIterator.hasNext(); ) {
            Row row = rowIterator.next();
            String name = row.getCell(0).getStringCellValue() + "-" + row.getCell(1).getStringCellValue();
            if("工号-姓名".equals(name)) {
                continue;
            }
            double add = 0.4 * (row.getCell(9).getNumericCellValue() - 100);
            namePointMap1.put(name, add);
        }

        xlsx = OfficeUtils.openXlsx(new File("temp/jixiao/jx2.xlsx"));
        sheet = xlsx.getSheet("SheetJS");
        rowIterator = sheet.iterator();
        HashMap<String, Double> namePointMap2 = new HashMap<>();
        for (; rowIterator.hasNext(); ) {
            Row row = rowIterator.next();
            String name = row.getCell(0).getStringCellValue() + "-" + row.getCell(1).getStringCellValue();
            if("工号-姓名".equals(name)) {
                continue;
            }
            double add = 0.6 * (row.getCell(9).getNumericCellValue() - 100);
            namePointMap2.put(name, add);
        }

        ArrayList<UserPoint> list = new ArrayList<>();
        for (Map.Entry<String, Double> entry : namePointMap2.entrySet()) {
            String name = entry.getKey();
            Double point1 = namePointMap1.get(name);
            Double point2 = entry.getValue();
            list.add(new UserPoint(name, point1 + point2 + 100));
        }
        Collections.sort(list);

        for (UserPoint user : list) {
            System.out.println(user);
        }
    }

    static class UserPoint implements Comparable<UserPoint> {
        public String name;
        public Double point;

        public UserPoint(String name, Double point) {
            this.name = name;
            this.point = point;
        }

        @Override
        public int compareTo(UserPoint o) {
            return o.point.compareTo(point);
        }

        @Override
        public String toString() {
            return name + " = " + point;
        }
    }

}
