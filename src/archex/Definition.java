package archex;

import java.util.ArrayList;

public class Definition {

    public ArrayList<Prop> properties;

    public static class Prop {
        /**
         * string / array / integer
         */
        public String type;
        /**
         * type=array时
         */
        public String items;
        public String description;
        /**
         * date 或其他
         */
        public String format;
    }
}
