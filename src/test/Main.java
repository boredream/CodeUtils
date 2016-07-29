package test;

import utils.AndroidUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {

    public static void main(String[] args) throws Exception {

        String str = "2015年";
        Pattern pattern = Pattern.compile(".*?([\\d]{4})[\\D]+?([\\d]{1,2})?([\\D]+)?([\\d]{1,2})?[\\s\\S]*");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            System.out.println(matcher.group());
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
            System.out.println(matcher.group(3));
        }
    }

}
