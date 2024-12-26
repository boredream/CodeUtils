package work;

import org.locationtech.jts.util.CollectionUtil;
import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SH {

    static class UserPoint implements Comparable<UserPoint> {

        String info;
        Integer point;

        @Override
        public int compareTo(UserPoint o) {
            return o.point.compareTo(point);
        }

        @Override
        public String toString() {
            return info + "=" + point;
        }
    }

    public static void main(String[] args) {
        ArrayList<String> lines = FileUtils.readToStringLines(new File("temp/jixiao/star.csv"));
        List<UserPoint> list = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] split = line.split(",");
            UserPoint user = new UserPoint();
            StringBuilder sb = new StringBuilder();
            int total = 0;
            for (int j = 3; j < split.length; j++) {
                total += Integer.parseInt(split[j]);
            }
            sb.append(split[1]).append("-").append(split[2]);
            user.info = sb.toString();
            user.point = total;
            list.add(user);
        }
        Collections.sort(list);
        System.out.println(list);
    }

}
