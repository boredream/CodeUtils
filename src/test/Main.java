package test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import utils.FileUtils;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        String json = FileUtils.readToString(new File("temp/ocr.json"));

        // 瓶罐：上 日期年月日 + 下 时分 4个字母数字
        // 袋装：左 日期年月日 + 右 4个字母数字
        // 4个字母数字的规则：
        // 酱油：产线（1位）+班组（1位）+工厂代码（2位）
        // 酱&醋：产线（1位）+工厂代码（2位）+班组（1位）

        String regexDate = "[1-2]\\d{7}";
        String regexTime = "(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]";
        String regex4Code = "[0-9A-Z]{4}";
        String regexSpace = "\\s*";

        // 可能匹配到的情况：
        // 1.瓶罐 [上]年月日 + [下]时分4字母数字
        // 2.袋装 年月日空格4字母数字
        // 3.袋装 [左]年月日 + [右]4字母数字（左右可能被识别为两组）
        JsonObject date = null;
        String finalRegexDate = "^" + regexDate + "$"; // TODO: 如果匹配到干扰的日期字符串怎么办？
        JsonObject time4Code = null;
        String finalRegexTime4Code = "^" + regexTime + regexSpace + regex4Code + "$";
        JsonObject date4Code = null;
        String finalRegexDate4Code = "^" + regexDate + regexSpace + regex4Code + "$";
        JsonObject code4 = null;
        String finalRegex4Code = "^" + regex4Code + "$";

        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonObject()
                .get("Response").getAsJsonObject()
                .get("TextDetections").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject joWord = jsonArray.get(i).getAsJsonObject();
            String words = joWord.get("DetectedText").getAsString();

            if (words.matches(finalRegexDate)) {
                date = joWord;
            }
            if (words.matches(finalRegexTime4Code)) {
                time4Code = joWord;
            }
            if (words.matches(finalRegexDate4Code)) {
                date4Code = joWord;
            }
            if (words.matches(finalRegex4Code)) {
                code4 = joWord;
            }
        }

        String result = null;
        if (date != null && time4Code != null) {
            // 日期+时间4字符：可能是瓶罐。继续判断位置
            JsonObject joDateLocation = date.get("ItemPolygon").getAsJsonObject();
            JsonObject joTime4CodeLocation = time4Code.get("ItemPolygon").getAsJsonObject();
            // TODO：规则待优化
            // 如果时间在日期下面；且二者的left相近，则确认命中
            int dateTop = joDateLocation.get("Y").getAsInt();
            int dateHeight = joDateLocation.get("Height").getAsInt();
            int dateLeft = joDateLocation.get("X").getAsInt();
            int time4codeTop = joTime4CodeLocation.get("Y").getAsInt();
            int time4codeLeft = joTime4CodeLocation.get("X").getAsInt();
            // 因为倾斜所以添加一些容差
            int heightOffset = dateHeight / 2; // 理想状态下为0
            int leftAlignOffset = joDateLocation.get("Width").getAsInt() / 3; // 理想状态下为0
            if (dateTop + dateHeight - heightOffset < time4codeTop && Math.abs(dateLeft - time4codeLeft) < leftAlignOffset) {
                // 去掉时分，拼接成批次
                result = date.get("DetectedText").getAsString() + time4Code.get("DetectedText").getAsString().substring(5);
            }
        } else if (date4Code != null) {
            // 日期4字符：确认是袋装
            result = date4Code.get("DetectedText").getAsString();
        } else if (date != null && code4 != null) {
            // [低概率] 日期+4字符：可能是袋装。继续判断位置
            JsonObject joDateLocation = date.get("ItemPolygon").getAsJsonObject();
            JsonObject joCode4Location = code4.get("ItemPolygon").getAsJsonObject();
            // TODO：规则待优化
            // 如果4字符在日期的右边；且两者top相近，则确认命中
            int dateLeft = joDateLocation.get("X").getAsInt();
            int dateWidth = joDateLocation.get("Width").getAsInt();
            int dateTop = joDateLocation.get("Y").getAsInt();
            int time4codeLeft = joCode4Location.get("X").getAsInt();
            int time4codeTop = joCode4Location.get("Y").getAsInt();
            // 因为倾斜所以添加一些容差
            int topAlignOffset = joDateLocation.get("Height").getAsInt() / 3; // 理想状态下为0
            int horSpaceOffset = dateWidth / 3;
            if (Math.abs(dateTop - time4codeTop) < topAlignOffset && dateLeft + dateWidth < time4codeLeft + horSpaceOffset) {
                // 去掉时分，拼接成批次
                result = date.get("DetectedText").getAsString() + code4.get("DetectedText").getAsString();
            }
        }

        if (result != null) {
            result = result.replace(" ", "");
        }

        System.out.println("date=" + date);
        System.out.println("time4Code=" + time4Code);
        System.out.println("date4Code=" + date4Code);
        System.out.println("code4=" + code4);
        System.out.println("[result]=" + result);
    }

}
