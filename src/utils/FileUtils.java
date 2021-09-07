package utils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtils {

    public static void main(String[] args) {

    }

    /**
     * 批量重命名
     *
     * @param formatter 重命名格式，xxx %s xxx，其中%s为原有名称
     */
    private static void batchRename(String dirPath, String formatter) {
        for (File file : getAllFiles(dirPath)) {
            File newFile = new File(file.getParentFile(), String.format(formatter, file.getName()));
            copyFileByChannel(file, newFile);
        }
    }

    /**
     * 递归获取的文件列表集合
     */
    private static List<File> allFiles = new ArrayList<>();

    /**
     * 获取指定目录下全部文件
     *
     * @param dir 根目录路径
     * @return 获取到的文件列表
     */
    public static List<File> getAllFiles(String dir) {
        return getAllFiles(new File(dir));
    }

    /**
     * 获取指定目录下全部文件
     *
     * @param rootFile 根目录文件
     * @return 获取到的文件列表
     */
    public static List<File> getAllFiles(File rootFile) {
        allFiles = new ArrayList<File>();
        try {
            getFiles(rootFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allFiles;
    }

    /**
     * 递归dir下全部文件并保存至allFiles
     *
     * @param dir 发起递归的根目录
     */
    public static void getFiles(File dir) throws Exception {
        File[] fs = dir.listFiles();
        for (int i = 0; i < fs.length; i++) {
            File file = fs[i];
            if (fs[i].isDirectory()) {
                try {
                    getFiles(fs[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                allFiles.add(file);
            }
        }
    }

    /**
     * 使用文件通道的方式复制文件
     *
     * @param srcFile 源文件
     * @param tarFile 复制到的新文件
     */
    public static void copyFileByChannel(File srcFile, File tarFile) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;

        try {
            fi = new FileInputStream(srcFile);
            fo = new FileOutputStream(tarFile);
            in = fi.getChannel();// 得到对应的文件通道
            out = fo.getChannel();// 得到对应的文件通道
            // 连接两个通道，并且从in通道读取，然后写入out通道
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 替换指定目录下全部java文件内符合自定义条件的字符串
     *
     * @param rootPath      根目录的绝对路径
     * @param replaceString key-原文字 value-需要替换的文字
     */
    public static void replaceStringOfJava(String rootPath, Map<String, String> replaceString) {
        // 获取全部文件
        List<File> files = FileUtils.getAllFiles(rootPath);

        for (File file : files) {
            // 如果不是java后缀的文件,则跳过
            if (!file.getName().endsWith(".java")) {
                continue;
            }

            // 将文件读取为一整个字符串
            String fileContent = readToString(file);

            // 是否有替换操作
            boolean hasReplace = false;
            // 遍历替换map,依次替换全部字符串
            for (Map.Entry<String, String> entry : replaceString.entrySet()) {
                if (fileContent.contains(entry.getKey())) {
                    fileContent = fileContent.replace(entry.getKey(), entry.getValue());
                    hasReplace = true;
                }
            }

            // 如果有替换操作,则将替换后的新文件内容字符串写入回文件中去
            if (hasReplace) {
                writeString2File(fileContent, file);
            }
        }
    }

    /**
     * 替换指定目录下全部java文件内符合自定义条件的字符串,支持正则
     *
     * @param rootPath      根目录的绝对路径
     * @param replaceString key-原文字 value-需要替换的文字
     */
    public static void replaceAllStringOfJava(String rootPath, Map<String, String> replaceString, String charSet) {
        // 获取全部文件
        List<File> files = FileUtils.getAllFiles(rootPath);

        for (File file : files) {
            // 如果不是java后缀的文件,则跳过
            if (!file.getName().endsWith(".java")) {
                continue;
            }

            // 将文件读取为一整个字符串
            String fileContent = readToString(file, charSet);

            // 是否有替换操作
            boolean hasReplace = false;
            // 遍历替换map,依次替换全部字符串
            for (Map.Entry<String, String> entry : replaceString.entrySet()) {
                if (fileContent.contains(entry.getKey())) {
                    fileContent = fileContent.replaceAll(entry.getKey(), entry.getValue());
                    hasReplace = true;
                }
            }

            // 如果有替换操作,则将替换后的新文件内容字符串写入回文件中去
            if (hasReplace) {
                writeString2File(fileContent, file, charSet);
            }
        }
    }


    /**
     * 删除无用java文件
     *
     * @param rootPath 根目录的绝对路径
     */
    public static void delNoUseJavaFile(String rootPath) {
        List<File> files = getAllFiles(rootPath);
        out:
        for (File file : files) {
            if (!file.getName().endsWith(".java")) {
                continue;
            }

            for (File compareFile : files) {
                // 如果包含文件名,则视为有使用
                String fileContent = readToString(compareFile);
                if (fileContent.contains(getName(file))) {
                    continue out;
                }
            }

            String absname = file.getAbsoluteFile().getName();
            boolean delete = file.delete();
            System.out.println(absname + " ... delete=" + delete);
        }
    }

    /**
     * 获取代码行数详情,包括总/空行/注释/有效代码各个行数
     *
     * @param rootPath 根目录的绝对路径
     */
    public static void getCodeLinesDetail(String rootPath) {
        // 全部文件中行数
        int allLines = 0;
        // 全部文件中空行数
        int allEmptyLines = 0;
        // 全部文件中代码行数
        int allCodeLines = 0;
        // 全部文件中注释行数
        int allAnnoLines = 0;

        List<File> files = FileUtils.getAllFiles(rootPath);
        for (File file : files) {
            // TODO 只统计java和xml代码
            if (file.getName().endsWith(".java") || file.getName().endsWith(".xml")) {
                FileReader fr;
                try {
                    fr = new FileReader(file);
                    BufferedReader bufferedreader = new BufferedReader(fr);

                    String line;
                    // 是否属于多行注释
                    boolean multiLineAnno = false;
                    while ((line = bufferedreader.readLine()) != null) {
                        allLines++;

                        // 空行
                        if (line.trim().equals("")) {
                            allEmptyLines++;
                            continue;
                        }

                        // 单行注释
                        if (line.contains("//")) {
                            allAnnoLines++;
                            continue;
                        }

                        // 如果还是在多行注释中
                        if (multiLineAnno) {
                            allAnnoLines++;
                            // 如果本行包含多行注释结束符,结束
                            if (line.contains("*/")) {
                                multiLineAnno = false;
                            }
                            continue;
                        }

                        // 多行注释开始(包括/*和/**)
                        if (line.contains("/*")) {
                            allAnnoLines++;
                            multiLineAnno = true;
                            continue;
                        }

                        // 有效代码
                        allCodeLines++;
                    }
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("文件总行数为：" + allLines);
        System.out.println("文件空行数为：" + allEmptyLines);
        System.out.println("文件注释行数为：" + allAnnoLines);
        System.out.println("文件有效代码行数为：" + allCodeLines);
        System.out.println("--------------------");
        // TODO 计算比例规则为 注释行数/有效代码数
        float percent = (float) allAnnoLines / allCodeLines * 100;
        // 格式化百分比,保留2位小数  %50.00
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("注释比例(注释行数/有效代码数): %" + df.format(percent));
    }

    /**
     * 获取代码行数,只统计java/xml后缀的文件
     *
     * @param rootPath 根目录的绝对路径
     */
    public static void getCodeLines(String rootPath) {
        int allLines = 0;
        List<File> files = getAllFiles(rootPath);
        for (File file : files) {
            if (file.getName().endsWith(".dart") || file.getName().endsWith(".xml")) {
                int lines = getLines(file);
                allLines += lines;
            }
        }
        System.out.println(allLines);
    }

    /**
     * 获取文件内文本的行数
     */
    public static int getLines(File file) {
        int lines = 0;
        FileReader fr;
        try {
            fr = new FileReader(file);
            BufferedReader bufferedreader = new BufferedReader(fr);
            while ((bufferedreader.readLine()) != null) {
                lines++;
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static File getFileByName(String proPath, String filename) {
        File tarFile = null;

        List<File> files = FileUtils.getAllFiles(proPath);

        for (File file : files) {
            String fileName = file.getName();
            if (fileName.equals(filename)) {
                tarFile = file;
                break;
            }
        }

        return tarFile;
    }

    /**
     * 获取文件名,去除后缀部分
     */
    public static String getName(File file) {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf("."));
        // 如果是.9.png结尾的,则在去除.png后缀之后还需要去除.9的后缀
        if (file.getName().endsWith(".9.png")) {
            name = name.substring(0, name.lastIndexOf("."));
        }
        return name;
    }

    /**
     * 获取文件名,去除后缀部分
     */
    public static String getName(String fileAbsPath) {
        File file = new File(fileAbsPath);
        return getName(file);
    }

    /**
     * 文件名和后缀分开
     */
    public static String[] getNameMap(File file) {
        String[] nameMap = new String[2];

        String name = file.getName();
        name = name.substring(0, name.lastIndexOf("."));
        // 如果是.9.png结尾的,则在去除.png后缀之后还需要去除.9的后缀
        if (file.getName().endsWith(".9.png")) {
            name = name.substring(0, name.lastIndexOf("."));
        }

        nameMap[0] = name;
        nameMap[1] = file.getName().replaceFirst(name, "");

        return nameMap;
    }

    /**
     * 将文件读取为字符串
     */
    public static String readToString(File file) {
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // 获取文件的编码格式,再根据编码格式生成字符串
            String charSet = getCharSet(file);
            return new String(filecontent, charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseCharset(String oldString, String oldCharset, String newCharset) {
        byte[] bytes;
        try {
            bytes = oldString.getBytes(oldCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("UnsupportedEncodingException - oldCharset is wrong");
        }
        try {
            return new String(bytes, newCharset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("UnsupportedEncodingException - newCharset is wrong");
        }
    }

    /**
     * 根据指定编码格式将文件读取为字符串
     */
    public static String readToString(File file, String charSet) {
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将文件内容以行为单位读取
     */
    public static ArrayList<String> readToStringLines(File file) {
        ArrayList<String> strs = new ArrayList<String>();

        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                strs.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignored) {
                }
            }
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException ignored) {
                }
            }
        }

        return strs;
    }

    /**
     * 搜索某目录下所有文件的文本中,是否包含某个字段,如果包含打印改文件路径
     *
     * @param path 搜索目录
     * @param key  包含字段
     */
    public static void searchFileContent(String path, String key) {
        List<File> allFiles = FileUtils.getAllFiles(path);
        for (File file : allFiles) {
            String string = FileUtils.readToString(file);
            if (string.contains(key)) {
                System.out.println(file.getAbsoluteFile());
            }
        }
    }

    /**
     * 获取文件编码格式,暂只判断gbk/utf-8
     */
    public static String getCharSet(File file) {
        String chatSet = null;
        try {
            InputStream in = new java.io.FileInputStream(file);
            byte[] b = new byte[3];
            in.read(b);
            in.close();
            if (b[0] == -17 && b[1] == -69 && b[2] == -65)
                chatSet = "UTF-8";
            else
                chatSet = "GBK";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chatSet;
    }

    /**
     * 将字符串写入文件
     */
    public static void writeString2File(String str, File file, String encoding) {
        BufferedWriter writer = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), encoding));
            writer.write(str);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                writer = null;
                e.printStackTrace();
            }
        }
    }

    public static void writeString2File(String str, File file) {
        writeString2File(str, file, getCharSet(file));
    }

    /**
     * 将字节数组写入文件
     */
    public static void writeBytes2File(byte[] bytes, File file) {
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                fos = null;
                e.printStackTrace();
            }
        }
    }

}
