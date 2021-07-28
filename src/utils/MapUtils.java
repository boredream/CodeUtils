package utils;

import java.io.File;
import java.util.ArrayList;

public class MapUtils {

    public static void main(String[] args) {
        Integer distance1 = getDistance(106.580932, 29.517611, 106.450148, 29.609481);
        Integer distance2 = getDistance(106.580932, 29.517611, 106.469165, 29.563992);
        System.out.println(distance1);
        System.out.println(distance2);
    }

    private static void method1() {
        ArrayList<String> lines = FileUtils.readToStringLines(new File("temp/locations.txt"));
        StringBuilder sb = new StringBuilder();
        int start = 70000;
        for (int i = start; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] tabs = line.split("\t");
            if (tabs.length != 11) continue;
            double gd_ln =  Double.parseDouble(tabs[3]);
            double gd_lat = Double.parseDouble(tabs[4]);
            double bd_ln =  Double.parseDouble(tabs[5]);
            double bd_lat = Double.parseDouble(tabs[6]);
            double c_ln =   Double.parseDouble(tabs[7]);
            double c_lat =  Double.parseDouble(tabs[8]);

            Integer gd_dis = getDistance(c_ln, c_lat, gd_ln, gd_lat);
            Integer bd_dis = getDistance(c_ln, c_lat, bd_ln, bd_lat);

            StringBuilder sbLine = new StringBuilder();
            for (int j = 0; j < 9; j++) {
                sbLine.append(tabs[j]).append("\t");
            }
            sbLine.append(gd_dis).append("\t");
            sbLine.append(tabs[10]);

            if (gd_dis<bd_dis) {
                // 源数据计算距离（与百度签到地址）更近 => 源数据是百度 => 需要转换成高德
                sbLine.append("\t是");
            }
            sbLine.append("\n");
            sb.append(sbLine.toString());
            System.out.print(sbLine.toString());
        }
        FileUtils.writeString2File(sb.toString(), new File("temp/locations_new.txt"), "utf-8");
    }

    /**
     * 地图计算两个坐标点的距离
     *
     * @param lng1 经度1
     * @param lat1 纬度1
     * @param lng2 经度2
     * @param lat2 纬度2
     * @return 距离（米）
     */
    public static Integer getDistance(Double lng1, Double lat1, Double lng2, Double lat2) {
        if (null == lng1 || null == lat1 || null == lng2 || null == lat2) {
            return null;
        }
        try {
            double radLat1 = Math.toRadians(lat1);
            double radLat2 = Math.toRadians(lat2);
            double a = radLat1 - radLat2;
            double b = Math.toRadians(lng1) - Math.toRadians(lng2);
            double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                    Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
            s = s * 6378.137; // EARTH_RADIUS
            return (int) Math.round(s * 1000);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
