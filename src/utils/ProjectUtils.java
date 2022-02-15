package utils;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectUtils {

   public static void extractAllString() {
      List<File> files = FileUtils.getAllFiles("D:\\work\\BusinessCMT2.0");
      // List<File> files =
      // FileUtils.getAllFiles("D:\\work\\BusinessCMT2.0\\src\\com\\imohoo\\BusinessCMT\\view\\activity\\pay");

      Map<String, String> stringIdValueMap = new TreeMap<String, String>();
      for (File file : files) {
         if (file.getName().endsWith(".java")
                 || file.getName().endsWith(".xml")) {
            FileReader fr;
            StringBuilder newFileContent = new StringBuilder();
            /**
             * 是否是页面类
             */
            boolean isActivity = false;
            /**
             * 是否有替换string操作
             */
            boolean isReplace = false;
            /**
             * 代码行数
             */
            int totoalLine = 0;
            /**
             * string抽取的id后缀
             */
            int stringIdEnd = 0;
            try {
               fr = new FileReader(file);
               BufferedReader bufferedreader = new BufferedReader(fr);

               String line;
               // 是否属于多行注释
               boolean multiLineAnno = false;
               while ((line = bufferedreader.readLine()) != null) {

                  totoalLine++;

                  // 空行
                  if (line.trim().equals("")) {
                     newFileContent.append(line + "\n");
                     continue;
                  }

                  // 单行注释,//开头
                  if (line.trim().startsWith("//")) {
                     newFileContent.append(line + "\n");
                     continue;
                  }

                  // 如果还是在多行注释中
                  if (multiLineAnno) {
                     // 如果本行包含多行注释结束符,结束
                     if (line.contains("*/")) {
                        multiLineAnno = false;
                     }
                     newFileContent.append(line + "\n");
                     continue;
                  }

                  // 多行注释开始(包括/*和/**)
                  if (line.contains("/*")) {
                     multiLineAnno = true;
                     newFileContent.append(line + "\n");
                     continue;
                  }

                  // 有效代码

                  // 判断是否为页面类
                  if (line.contains("extends") && line.contains("Activity")) {
                     isActivity = true;
                  }

                  // 中文字符"blablabla"
                  String regexEmoji = "\"([\\s\\S]+?)\"";

                  Pattern pattern = Pattern.compile(regexEmoji);
                  Matcher matcher = pattern.matcher(line);

                  String regexChinese = "[\u4e00-\u9fa5]+";
                  Pattern patternChiese = Pattern.compile(regexChinese);

                  // 如果该行包含字符串"blablabla"
                  while (matcher.find()) {
                     String chinese = matcher.group(1);

                     Matcher matcherChinese = patternChiese.matcher(chinese);
                     // TODO 如果包含的内容中没有中文,跳过
                     if (!matcherChinese.find()) {
                        continue;
                     }

                     // TODO 如果是日志内容,跳过
                     if (line.trim().startsWith("Log") || line.trim().startsWith("showLog")) {
                        continue;
                     }

                     if (file.getName().endsWith(".java")) {
                        // id规则,文件名小写_递增数字
                        String stringId = FileUtils.getName(file.getName().toLowerCase(Locale.CHINA)) + "_" + stringIdEnd++;

                        if (isActivity) {
                           // 如果是页面类,直接getResource
                           line = line.replace("\"" + chinese + "\"", "getResources().getString(R.string." + stringId + ")");
                        } else {
                           // 非页面类,用application去getResource
                           line = line.replace("\"" + chinese + "\"", "BusinessCMTApplication.getInstance().getResources().getString(R.string." + stringId + ")");
                        }
                        stringIdValueMap.put(stringId, chinese);
                        isReplace = true;
                     } else if (file.getName().endsWith(".xml")) {
                        // id规则,文件名小写_递增数字
                        String stringId = FileUtils.getName(file.getName().toLowerCase(Locale.CHINA)) + "_" + stringIdEnd++;
                        // xml用@string/blabla替换,注意,不替换""
                        line = line.replace(chinese, "@string/" + stringId);
                        stringIdValueMap.put(stringId, chinese);
                        isReplace = true;
                     }
                  }
                  newFileContent.append(line + "\n");

               }
               fr.close();
            } catch (IOException e) {
               e.printStackTrace();
            }

            if (isReplace) {
               // TODO 写入回文件,如果有替换操作
               // FileUtils.writeString2File(newFileContent.toString(),
               // file, "UTF-8");
            }
         }

      }

      System.out.println(stringIdValueMap);

      // save to
      File file = new File("D:\\work\\BusinessCMT2.0\\res\\values\\string.xml");
      Document valuesDoc = XmlUtil.read(file);
      Element rootElement = valuesDoc.getRootElement();

      List<Element> elements = rootElement.elements();
      for (Map.Entry<String, String> entry : stringIdValueMap.entrySet()) {
         // 是否在values/xx.xml对应文件下下已有某个抽取过的值
         boolean hasElement = false;

         for (Element element : elements) {
            Attribute nameAtt = element.attribute("path");
            if (nameAtt.getValue().equals(entry.getKey())) {
               hasElement = true;
               break;
            }
         }

         if (!hasElement) {
            // <string path="app_name">Stone Chat</string>
            Element element = rootElement.addElement("string");
            element.addAttribute("path", entry.getKey());
            element.setText(entry.getValue());
         }
      }
      // TODO save string文件
      // XmlUtil.write2xml(file, valuesDoc);
   }

   public static void compareStrings() {

      List<String> hasStrName = new ArrayList<String>();

      // save to
      File file = new File("D:\\work\\BusinessCMT2.0\\res\\values-en\\strings.xml");
      Document valuesDoc = XmlUtil.read(file);
      Element rootElement = valuesDoc.getRootElement();

      List<Element> elements = rootElement.elements();
      for (Element element : elements) {
         Attribute nameAtt = element.attribute("path");
         String name = nameAtt.getValue();
         hasStrName.add(name);
      }

      // save to
      File fileChn = new File("D:\\work\\BusinessCMT2.0\\res\\values\\strings.xml");
      Document valuesDocChn = XmlUtil.read(fileChn);
      Element rootElementChn = valuesDocChn.getRootElement();

      List<Element> elementsChn = rootElementChn.elements();
      for (Element element : elementsChn) {
         Attribute nameAtt = element.attribute("path");
         String name = nameAtt.getValue();
         if (!hasStrName.contains(name)) {
            System.out.println(element.getText());
         }
      }

   }
}
