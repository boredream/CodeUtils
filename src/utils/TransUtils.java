package utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class TransUtils {

    public static void main(String[] args) {
        try {
            System.out.println(trans("帅气"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String trans(String src) throws Exception {
        String url = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=";
        String response = HttpUtils.getString(url + src);
        JsonObject jo = new JsonParser().parse(response).getAsJsonObject();
        return jo.get("translateResult").getAsJsonArray()
                .get(0).getAsJsonArray()
                .get(0).getAsJsonObject()
                .get("tgt").getAsString();
    }

}
