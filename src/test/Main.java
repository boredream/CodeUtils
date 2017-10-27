package test;

import utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

    static class WdSubject {
        String name;
        String startTime;
        String errorTime;
        int diffMonth;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Integer.toHexString(Integer.MAX_VALUE));
    }

    private static void test() {
        ArrayList<String> strings = FileUtils.readToStringLines(new File("temp/wd.txt"));
        int index = 0;
        WdSubject subject = new WdSubject();
        assert strings != null;
        List<WdSubject> subjects = new ArrayList<>();
        for (String string : strings) {
            switch (index) {
                case 2:
                    subject.errorTime = string;
                    break;
                case 3:
                    subject.startTime = string;
                    subject.diffMonth = calculateDiff(subject);
                    subject = new WdSubject();
                    subjects.add(subject);
                    break;

            }
            if(index == 6) {
                index = 0;
            } else {
                index ++;
            }
        }

        int count = 0;
        for (WdSubject sub : subjects) {
            if(sub.diffMonth <= 12) {
                count ++;
            }
        }
        System.out.println(count + " / " + subjects.size());
        System.out.println(1f*count/subjects.size()*100 + "%");
    }

    private static int calculateDiff(WdSubject subject) {
        int errorYear = Integer.parseInt(subject.errorTime.split("-")[0]);
        int startYear = Integer.parseInt(subject.startTime.split("-")[0]);
        int errorMonth = Integer.parseInt(subject.errorTime.split("-")[1]);
        int startMonth = Integer.parseInt(subject.startTime.split("-")[1]);
        return (errorYear-startYear)*12+(errorMonth-startMonth);
    }

}
