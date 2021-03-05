package archex;

import java.util.ArrayList;
import java.util.HashMap;

public class Path {

    public String path;
    public String method;
    public String description;
    public ArrayList<Parameters> parameters;
    public HashMap<String, ResponseItem> responses;

    public static class Parameters {
        /**
         * body
         */
        public String in;
        public String name;
        public String description;
        public String type;
        public HashMap<String, String> items;
        /**
         * in=body时
         * "$ref": "#/definitions/AccountLogin"
         */
        public HashMap<String, String> schema;

        public String getSchema() {
            String ref = schema.get("$ref");
            return ref.replace("#/definitions/", "").trim();
        }

    }

    public static class ResponseItem {
        public HashMap<String, String> schema;
    }

    public String getResponseDef() {
        ResponseItem responseItem = responses.get("200");
        String ref = responseItem.schema.get("$ref");
        return ref.replace("#/definitions/", "").trim();
    }

}
