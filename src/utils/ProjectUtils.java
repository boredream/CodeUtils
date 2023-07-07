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

    private static List<String> ignoreFileNames = Arrays.asList("strings.xml", "ApiService.java", "OcrHelper.java");

    public static void main(String[] args) {
//        String projectPath = "/Users/lcy/Documents/mobile-android/app/src/main";
//        String projectPath = "/Users/lcy/Documents/mobile-android/core/src/main";
//        extractAllString(projectPath);

        // 去重后提取成表格
        parseStringsToXml();
    }

    static class StringKV {
        String name;
        String value;

        public StringKV(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    public static void parseStringsToXml() {
        List<Element> elements1 = XmlUtil
                .read(new File("/Users/lcy/Documents/mobile-android/app/src/main" + "/res/values/strings.xml"))
                .getRootElement()
                .elements();
        List<Element> elements2 = XmlUtil
                .read(new File("/Users/lcy/Documents/mobile-android/core/src/main" + "/res/values/strings.xml"))
                .getRootElement()
                .elements();
        List<Element> elements = new ArrayList<>();
        elements.addAll(elements1);
        elements.addAll(elements2);

        // 先记录所有string的键
        HashSet<String> valueSet = new HashSet<>();
        List<StringKV> dataList = new ArrayList<>();
        for (Element element : elements) {
            String value = element.getText();
            if(valueSet.contains(value)) {
                continue;
            }
            valueSet.add(value);

            String name = element.attribute("name").getValue();
            dataList.add(new StringKV(name, value));
        }
        File targetCsvFile = new File("temp" + File.separator + "office" + File.separator + "strings.csv");
        OfficeUtils.saveCVS(dataList, targetCsvFile);
    }

    public static void extractAllString(String projectPath) {
        // 先读取映射表 strings.xml
        // project = "/Users/lcy/Documents/mobile-android/app/src/main"
        String stringXmlPath = projectPath + "/res/values/strings.xml";
        File stringXmlFile = new File(stringXmlPath);
        Document valuesDoc = XmlUtil.read(stringXmlFile);
        Element rootElement = valuesDoc.getRootElement();
        List<Element> elements = rootElement.elements();

        // 先记录所有string的键
        HashSet<String> stringKeySet = new HashSet<>();
        for (Element element : elements) {
            Attribute nameAtt = element.attribute("name");
            stringKeySet.add(nameAtt.getValue());
        }

        int totalCount = 0;
        List<File> files = FileUtils.getAllFiles(projectPath);
        Map<String, String> stringIdValueMap = new TreeMap<>();
        for (File file : files) {
            // 只检测java和xml文件
            if (!file.getName().endsWith(".java") && !file.getName().endsWith(".xml")) {
                continue;
            }
            // 过滤不检测的文件
            if (ignoreFileNames.contains(file.getName())) {
                continue;
            }
            FileReader fr;
            StringBuilder newFileContent = new StringBuilder();
            // 是否有替换string操作
            boolean isReplace = false;
            // string抽取的id后缀
            int stringIdEndIndex;
            // 先尝试生成当前文件的name前缀
            String stringNamePre = FileUtils.getName(file.getName().toLowerCase(Locale.CHINA)) + "_";
            // 如果当前文件之前提取过文字，则 stringIdEndIndex 需要计算开始的index
            int startIndex = 0;
            String stringName;
            do {
                stringName = stringNamePre + startIndex++;
            } while (stringKeySet.contains(stringName));
            stringIdEndIndex = startIndex - 1;

            try {
                fr = new FileReader(file);
                BufferedReader bufferedreader = new BufferedReader(fr);

                String line;
                // 是否属于多行注释
                boolean multiLineAnno = false;
                // 按行遍历文件内容
                while ((line = bufferedreader.readLine()) != null) {
                    // 空行跳过
                    if ("".equals(line.trim())) {
                        newFileContent.append(line).append("\n");
                        continue;
                    }

                    // 单行注释,//开头，跳过
                    if (line.trim().startsWith("//")) {
                        newFileContent.append(line).append("\n");
                        continue;
                    }

                    // 如果还是在多行注释中，跳过
                    if (multiLineAnno) {
                        // 如果本行包含多行注释结束符,结束
                        if (line.contains("*/")) {
                            multiLineAnno = false;
                        }
                        newFileContent.append(line).append("\n");
                        continue;
                    }

                    // 多行注释开始(包括/*和/**)，跳过
                    if (line.contains("/*")) {
                        multiLineAnno = true;
                        newFileContent.append(line).append("\n");
                        continue;
                    }

                    // 至此为有效代码

                    String regexEmoji = "\"([\\s\\S]+?)\"";

                    Pattern pattern = Pattern.compile(regexEmoji);
                    Matcher matcher = pattern.matcher(line);

                    String regexChinese = "[\u4e00-\u9fa5]+";
                    Pattern patternChiese = Pattern.compile(regexChinese);

                    // 正则获取到所有引号"括起来的内容
                    while (matcher.find()) {
                        String chinese = matcher.group(1);

                        Matcher matcherChinese = patternChiese.matcher(chinese);
                        // 如果引号包含的内容中没有中文,跳过
                        if (!matcherChinese.find()) {
                            continue;
                        }

                        // 如果是日志打印,跳过
                        if (line.trim().startsWith("Log") || line.trim().startsWith("showLog") || line.trim().startsWith("logger.log")) {
                            continue;
                        }

                        // 至此代表该行包含待替换中文
                        if (file.getName().endsWith(".java")) {
                            // 给字符串取名，规则为：文件名小写_递增数字
                            String stringId = FileUtils.getName(file.getName().toLowerCase(Locale.CHINA)) + "_" + stringIdEndIndex++;

                            // 将中文替换为映射的key
                            line = line.replace("\"" + chinese + "\"", "AppKeeper.getApp().getString(R.string." + stringId + ")");
                            stringIdValueMap.put(stringId, chinese);
                            isReplace = true;
                        } else if (file.getName().endsWith(".xml")) {
                            // tools:text 的说明类文字跳过
                            if (line.contains("tools:text=\"")) {
                                continue;
                            }

                            // 给字符串取名，规则为：文件名小写_递增数字
                            String stringId = FileUtils.getName(file.getName().toLowerCase(Locale.CHINA)) + "_" + stringIdEndIndex++;
                            // xml用@string/blabla 替换中文。注意,不替换空字符""
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
                // 如果有替换操作
                totalCount++;
//                if(totalCount >= 10) {
//                    // FIXME: 2022/2/16 控制一次替换文件数量，测试脚本时使用
//                    break;
//                }

                // java文件需要判断添加import
                if (file.getName().endsWith(".java")) {
                    String importAppKeeper = "import com.archex.core.constants.AppKeeper;\n";

                    String importR;
                    if(file.getAbsolutePath().contains("/core/")) {
                        importR = "import com.archex.core.R;\n";
                    } else {
                        importR = "import com.archex.fsfa.R;\n";
                    }
                    int index = newFileContent.indexOf("\n\n");
                    if (newFileContent.indexOf(importAppKeeper) == -1) {
                        newFileContent.insert(index + 2, importAppKeeper);
                    }
                    if (newFileContent.indexOf(importR) == -1) {
                        newFileContent.insert(index + 2, importR);
                    }
                }

                // 写入回文件，完成替换操作
                FileUtils.writeString2File(newFileContent.toString(), file, "UTF-8");
            }
        }

        System.out.println(totalCount);
        System.out.println(stringIdValueMap);

        // 处理是否在values/xx.xml对应文件下下已有某个抽取过的值，保证key不重复
        for (Map.Entry<String, String> entry : stringIdValueMap.entrySet()) {
            boolean hasElement = false;

            for (Element element : elements) {
                Attribute nameAtt = element.attribute("name");
                if (nameAtt.getValue().equals(entry.getKey())) {
                    hasElement = true;
                    break;
                }
            }

            if (!hasElement) {
                // 插入映射表中一条数据
                Element element = rootElement.addElement("string");
                element.addAttribute("name", entry.getKey());
                element.setText(entry.getValue());
            }
        }
        // 保存写入映射表文件
        XmlUtil.write2xml(stringXmlFile, valuesDoc);
    }

    // 暂时无用   移除同一个文件下的重复文字
    public static void removeSameFileDuplicate(String projectPath) {
        String stringXmlPath = projectPath + "/res/values/strings.xml";
        File stringFile = new File(stringXmlPath);
        Document valuesDoc = XmlUtil.read(stringFile);
        Element rootElement = valuesDoc.getRootElement();

        HashMap<String, HashMap<String, String>> fileNamesMap = new HashMap<>();
        HashMap<String, HashMap<String, String>> fileReplaceMap = new HashMap<>();
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            Attribute nameAtt = element.attribute("name");
            String name = nameAtt.getValue();
            int index = name.lastIndexOf("_");
            if (index == -1) {
                continue;
            }
            String filename = name.substring(0, index); // 去掉最后的数字
            HashMap<String, String> strings = fileNamesMap.computeIfAbsent(filename, k -> new HashMap<>());
            String text = element.getText();
            if(strings.containsKey(text)) {
                // 单个文件内有重复的值，进行替换操作
                HashMap<String, String> replaceMap = fileReplaceMap.computeIfAbsent(filename, k -> new HashMap<>());
                replaceMap.put(name, strings.get(text));

                // 删除重复的字符
                rootElement.remove(element);
            } else {
                // 无重复的值，记录
                strings.put(text, name);
            }
        }
        // save string文件
        XmlUtil.write2xml(stringFile, valuesDoc);

        // 同步替换其它strings
        // TODO: chunyang 2022/2/18 代码有误
        Document valuesDocFT = XmlUtil.read(new File(stringXmlPath.replace("/values/", "/values-zh-rTW/")));
        Element rootElementFT = valuesDocFT.getRootElement();
        for (Object obj : rootElementFT.elements()) {
            if(obj instanceof Element) {
                Element element = (Element) obj;
                for (Map.Entry<String, HashMap<String, String>> entry : fileReplaceMap.entrySet()) {
                    HashMap<String, String> names = entry.getValue();
                    if(names.containsKey(element.attribute("name").getValue())) {
                        rootElementFT.remove(element);
                    }
                }
            }
        }

        if (1==1) {
            return;
        }

        // 先遍历所有文件
        List<File> files = FileUtils.getAllFiles(projectPath);
        for (File file : files) {
            if (!file.getName().endsWith(".java") && !file.getName().endsWith(".xml")) {
                continue;
            }
            if (ignoreFileNames.contains(file.getName())) {
                continue;
            }

            // 找到需要替换字符串的文件
            String filename = FileUtils.getName(file.getName().toLowerCase(Locale.CHINA));
            if(fileReplaceMap.containsKey(filename)) {
                System.out.println(filename);
                for (Map.Entry<String, String> stringEntry : fileReplaceMap.get(filename).entrySet()) {
                    // 需要把oldName替换为newName
                    String oldName = stringEntry.getKey();
                    String newName = stringEntry.getValue();

                    String fileContent = FileUtils.readToString(file, "utf-8");
                    fileContent = fileContent.replace("getString(R.string." + oldName + ")", "getString(R.string." + newName + ")")
                            .replace("\"@string/" + oldName + "\"", "\"@string/" + newName + "\"");
                    FileUtils.writeString2File(fileContent, file, "utf-8");
                }
            }
        }
    }

    // 暂时无用
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
