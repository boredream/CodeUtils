package utils;

import java.util.HashMap;

public class ShUser {

   static HashMap<String, String> nameMap = new HashMap<>();
   static {
      nameMap.put("李春阳", "18010089");
      nameMap.put("胡翔", "18020013");
      nameMap.put("杨建", "18070144");
      nameMap.put("刘殷平", "18070142");
      nameMap.put("李永炳", "20030015");
      nameMap.put("皮嘉酉", "21120065");
      nameMap.put("刁望庆", "17030086");
   }

   public static String getUserNumber(String name) {
      return nameMap.get(name);
   }
}
