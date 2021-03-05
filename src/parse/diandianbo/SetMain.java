package parse.diandianbo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import parse.LeanCloudHttpUtils;
import parse.Pointer;
import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetMain {

    static boolean isTest = false;

    public static void main(String[] args) throws Exception {
        File dir = new File("booksets");
        System.out.println(dir.getAbsolutePath());
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        System.out.println("--------------- Book集 数量为: " + files.length + "---------------");
        System.out.println();
        System.out.println();

        for (File file : files) {
            Book parentBook = saveBookSet(file);

            System.out.println();
            for (File childFile : file.listFiles()) {
                if(childFile.isFile()) {
                    continue;
                }

                saveChildBook(parentBook, childFile);
            }
        }

        System.out.println("--------------- 结束 ---------------");
        Thread.sleep(60 * 1000);
    }

    private static Book saveBookSet(File file) {
        if (!file.isDirectory()) {
            return null;
        }

        File[] bookFiles = file.listFiles();
        if (bookFiles == null) {
            return null;
        }

        System.out.println("--------------- 开始处理 Book集 " + file.getName() + " ---------------");

        // 读取配置文件
        File configFile = new File(file, "set_config.txt");
        if (!configFile.exists()) {
            System.out.println("处理失败, 绘本集文件夹内必须包含 set_config 父绘本配置文件!");
            return null;
        }

        int language_type = 1;
        int tag_type = 0;
        int age_range = 0;
        float price = 0;
        int book_category_id = 0;
        String bookSetDesc = null;
        String nameChn = null;
        String nameEng = null;

        for (String configInfo : FileUtils.readToStringLines(configFile)) {
            if (configInfo.startsWith("#")) {
                continue;
            }

            String[] kv = configInfo.split("=");

            switch (kv[0].trim()) {
                case "language_type":
                    try {
                        if (kv.length < 2 || kv[1].trim().equals("")) {
                            System.out.println("配置信息错误: language_type 为必填项");
                            return null;
                        }

                        language_type = Integer.parseInt(kv[1].trim());
                        if (language_type < 1 || language_type > 3) {
                            System.out.println("配置信息错误: language_type 必须在1~3范围内");
                            return null;
                        }
                    } catch (Exception e) {
                        System.out.println("配置信息错误: language_type 必须为数字");
                        return null;
                    }
                    break;
                case "tag_type":
                    try {
                        if (kv.length < 2 || kv[1].trim().equals("")) {
                            tag_type = 0;
                        } else {
                            tag_type = Integer.parseInt(kv[1].trim());
                        }
                    } catch (Exception e) {
                        System.out.println("配置信息错误: tag_type 必须为数字");
                        return null;
                    }
                    break;
                case "age_range":
                    try {
                        if (kv.length < 2 || kv[1].trim().equals("")) {
                            System.out.println("配置信息错误: age_range 为必填项");
                            return null;
                        }

                        age_range = Integer.parseInt(kv[1].trim());
                        if (age_range < 0 || age_range > 3) {
                            System.out.println("配置信息错误: age_range 必须在0~3范围内");
                            return null;
                        }
                    } catch (Exception e) {
                        System.out.println("配置信息错误: age_range 必须为数字");
                        return null;
                    }
                    break;
                case "price":
                    if (kv.length < 2 || kv[1].trim().equals("")) {
                        // 没填价格
                        price = 0;
                    } else {
                        try {
                            String priceStr = kv[1].trim();
                            price = Float.parseFloat(priceStr);
                            if (price < 0) {
                                System.out.println("配置信息错误: price 必须大于0");
                                return null;
                            }

                            int index = priceStr.indexOf(".");
                            if (index != -1 && priceStr.split("\\.")[1].length() > 2) {
                                System.out.println("配置信息错误: price 小数点最多支持2位");
                                return null;
                            }

                        } catch (Exception e) {
                            System.out.println("配置信息错误: price 必须为数字, 可以是2位以内的小数");
                            return null;
                        }
                    }
                    break;
                case "book_category_id":
                    try {
                        if (kv.length == 2 && !kv[1].trim().equals("")) {
                            book_category_id = Integer.parseInt(kv[1].trim());
                        }
                    } catch (Exception e) {
                        System.out.println("配置信息错误: book_category_id 必须为数字");
                        return null;
                    }
                    break;
                case "book_desc":
                    if (kv.length == 2) {
                        bookSetDesc = kv[1];
                    }
                    break;
                case "book_name_chn":
                    if (language_type <= 2) {
                        if ( kv.length < 2 || kv[1].trim().equals("")) {
                            System.out.println("配置信息错误: book_name_chn 不能为空");
                            return null;
                        }
                    }
                    nameChn = kv[1].trim();
                    break;
                case "book_name_eng":
                    if (language_type >= 2) {
                        if ( kv.length < 2 || kv[1].trim().equals("")) {
                            System.out.println("配置信息错误: book_name_eng 不能为空");
                            return null;
                        }
                    }
                    nameEng = kv[1].trim();
                    break;
            }
        }

//        ArrayList<LeanFile> oldFiles = new ArrayList<>();
//        try {
//            // 先获取本书相关的所有文件
//            String whereName = language_type <= 2 ? nameChn : nameEng;
//            // 文件名不能有空格
//            whereName = getEncodeWithoutPerName(whereName);
//            String where = "{\"path\":{\"$regex\":\"" + whereName + "_.*\"}}";
//            Type type = new TypeToken<ListResponse<LeanFile>>() {}.getType();
//            String fileResponse = LeanCloudHttpUtils.getString("https://api.leancloud.cn/1.1/files?where=" + where);
//            ListResponse<LeanFile> response = new Gson().fromJson(fileResponse, type);
//
//            if(response.results != null) {
//                oldFiles.addAll(response.results);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (oldFiles == null) {
//            System.out.println("获取已有文件失败");
//            return null;
//        }

        File surface = null;

        for (File bookFile : bookFiles) {
            if (bookFile.getName().contains("封面") && bookFile.isFile()) {
                surface = bookFile;
                continue;
            }
        }

        if (surface == null) {
            System.out.println("封面图片不存在, 文件夹内必须包含 封面.jpg");
            return null;
        }

        /////////////////////////   验证通过    ////////////////////////////
        System.out.println("--------------- 验证通过,开始上传 ---------------");
        if(isTest) {
            return null;
        }

        Book book = new Book();
        book.name = nameChn;
        book.engName = nameEng;
        book.bookSetDesc = bookSetDesc;

        String surfaceName = (language_type <= 2 ? book.name : book.engName) + "_封面";
        book.surfaceImg = getUrl(surface, surfaceName);
        if (StringUtils.isEmpty(book.surfaceImg)) {
            System.out.println("封面图片文件上传失败");
            return null;
        }

        try {
            String bookId;

            // 根据名字获取已有Book
            String where;
            if(language_type <= 2) {
                where = "{\"path\":\"" + book.name + "\"}";
            } else {
                where = "{\"engName\":\"" + book.engName + "\"}";
            }
            where = URLEncoder.encode(where, "utf-8");
            String responseStr = LeanCloudHttpUtils.getString("https://api.leancloud.cn/1.1/classes/Book?where=" + where);
            Type type = new TypeToken<ListResponse<Book>>() {}.getType();
            ListResponse<Book> response = new Gson().fromJson(responseStr, type);
            if (response == null || response.results == null || response.results.size() == 0) {
                System.out.println((language_type <= 2 ? book.name : book.engName) + " Book ... 不存在,新建");

                book.languageStatus = language_type;
                book.musicType = 2;
                book.tagType = tag_type;
                book.bookSetType = 2;
                book.ageRange = age_range;
                book.price = price;
                book.categoryId = book_category_id;

                String postBookResponse = LeanCloudHttpUtils.postBean(book);
                bookId = new Gson().fromJson(postBookResponse, Pointer.class).objectId;
                book.objectId = bookId;

                System.out.println((language_type <= 2 ? book.name : book.engName) + " Book ... 新建成功");
            } else {
                // 已存在的, 进行更新
                bookId = response.results.get(0).objectId;

                book.languageStatus = language_type;
                book.musicType = 2;
                book.tagType = tag_type;
                book.bookSetType = 2;
                book.ageRange = age_range;
                book.price = price;
                book.categoryId = book_category_id;
                book.objectId = bookId;

                String postBookResponse = LeanCloudHttpUtils.updateBean(book, bookId);
                new Gson().fromJson(postBookResponse, Pointer.class);

                System.out.println((language_type <= 2 ? book.name : book.engName) + " Book ... 已存在, 更新成功");
            }

            return book;
        } catch (Exception e) {
            System.out.println("Book集上传失败 : " + (language_type <= 2 ? book.name : book.engName));
        }

        return null;
    }

    private static String encodeString(String whereName) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(whereName, "utf-8");
        encode = encode.replace("%", "");
        return encode;
    }

    private static void saveChildBook(Book parentBook, File file) {
        if (!file.isDirectory()) {
            return;
        }

        File[] bookFiles = file.listFiles();
        if (bookFiles == null) {
            return;
        }


        System.out.println("-------- 开始处理绘本集[" + parentBook.getDisplayName() + "]中的子Book " + file.getName() + " --------");

        // 读取配置文件
        File configFile = new File(file, "book_config.txt");
        if (!configFile.exists()) {
            System.out.println("处理失败, 文件夹内必须包含book_config.txt配置文件!");
            return;
        }

        int language_type = 1;
        int music_type = 0;
        int tag_type = parentBook.tagType;
        int age_range = parentBook.ageRange;
        float price = 0;
        int book_unit_size = 0;
        int book_category_id = 0;
        String nameChn = null;
        String nameEng = null;

        for (String configInfo : FileUtils.readToStringLines(configFile)) {
            if (configInfo.startsWith("#")) {
                continue;
            }

            String[] kv = configInfo.split("=");

            switch (kv[0].trim()) {
                case "language_type":
                    try {
                        if ( kv.length < 2 || kv[1].trim().equals("")) {
                            System.out.println("配置信息错误: language_type 为必填项");
                            return;
                        }

                        language_type = Integer.parseInt(kv[1].trim());
                        if (language_type < 1 || language_type > 3) {
                            System.out.println("配置信息错误: language_type 必须在1~3范围内");
                            return;
                        }
                    } catch (Exception e) {
                        System.out.println("配置信息错误: language_type 必须为数字");
                        return;
                    }
                    break;
                case "music_type":
                    try {
                        if ( kv.length < 2 || kv[1].trim().equals("")) {
                            System.out.println("配置信息错误: music_type 为必填项");
                            return;
                        }

                        music_type = Integer.parseInt(kv[1].trim());
                        if (music_type < 1 || music_type > 3) {
                            System.out.println("配置信息错误: music_type 必须在1~3范围内");
                            return;
                        }
                    } catch (Exception e) {
                        System.out.println("配置信息错误: music_type 必须为数字");
                        return;
                    }
                    break;
                case "book_unit_size":
                    if (music_type >= 2) {
                        try {
                            if ( kv.length < 2 || kv[1].trim().equals("")) {
                                System.out.println("配置信息错误: 分段音乐类型时 book_unit_size 为必填项");
                                return;
                            }

                            book_unit_size = Integer.parseInt(kv[1].trim());
                            if (book_unit_size <= 0) {
                                System.out.println("配置信息错误: book_unit_size 必须大于0");
                                return;
                            }
                        } catch (Exception e) {
                            System.out.println("配置信息错误: book_unit_size 必须为数字");
                            return;
                        }
                    }
                    break;
                case "book_name_chn":
                    if (language_type <= 2) {
                        if ( kv.length < 2 || kv[1].trim().equals("")) {
                            System.out.println("配置信息错误: book_name_chn 不能为空");
                            return;
                        }
                    }
                    nameChn = kv[1].trim();
                    break;
                case "book_name_eng":
                    if (language_type >= 2) {
                        if ( kv.length < 2 || kv[1].trim().equals("")) {
                            System.out.println("配置信息错误: book_name_eng 不能为空");
                            return;
                        }
                    }
                    nameEng = kv[1].trim();
                    break;
            }
        }

//        ArrayList<LeanFile> oldFiles = new ArrayList<>();
//        try {
//            // 先获取本书相关的所有文件
//            String whereName = language_type <= 2 ? nameChn : nameEng;
//            // 文件名不能有空格
//            whereName = getEncodeWithoutPerName(whereName);
//            String where = "{\"path\":{\"$regex\":\"" + whereName + "_.*\"}}";
//            Type type = new TypeToken<ListResponse<LeanFile>>() {
//            }.getType();
//            String fileResponse = LeanCloudHttpUtils.getString("https://api.leancloud.cn/1.1/files?where=" + where);
//            ListResponse<LeanFile> response = new Gson().fromJson(fileResponse, type);
//
//            if(response.results != null) {
//                oldFiles.addAll(response.results);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (oldFiles == null) {
//            System.out.println("获取已有文件失败");
//            return;
//        }

        File surface = null;
        File surfaceSleep = null;
        File mp3File = null;
        File mp3FileEng = null;
        ArrayList<BookUnit> bookUnits = null;
        ArrayList<File> picFiles = new ArrayList<>();
        ArrayList<File> mp3ChnFiles = new ArrayList<>();
        ArrayList<File> mp3EngFiles = new ArrayList<>();

        for (File bookFile : bookFiles) {
            Pattern patternPic = Pattern.compile("^P[0-9]{1,3}.jpg");
            Matcher matcherPic = patternPic.matcher(bookFile.getName());

            if (matcherPic.find()) {
                picFiles.add(bookFile);
                continue;
            }

            Pattern patternMp3Chn = Pattern.compile("^C[0-9]{1,3}.mp3");
            Matcher matcherMp3Chn = patternMp3Chn.matcher(bookFile.getName());

            if (language_type <= 2 && matcherMp3Chn.find()) {
                mp3ChnFiles.add(bookFile);
                continue;
            }

            Pattern patternMp3Eng = Pattern.compile("^E[0-9]{1,3}.mp3");
            Matcher matcherMp3Eng = patternMp3Eng.matcher(bookFile.getName());

            if (language_type >= 2 && matcherMp3Eng.find()) {
                mp3EngFiles.add(bookFile);
                continue;
            }

            if (bookFile.getName().endsWith(".txt") && !bookFile.getName().equals("book_config.txt")) {
                bookUnits = parseBookUnit(bookFile);
                BookUnit bookUnitFirst = new BookUnit();
                bookUnitFirst.index = 0;
                bookUnitFirst.contentChn = nameChn;
                bookUnitFirst.contentEng = nameEng;
                bookUnits.add(0, bookUnitFirst);
                continue;
            }

            if (language_type <= 2 && bookFile.getName().endsWith(".mp3") && bookFile.getName().contains(nameChn)) {
                mp3File = bookFile;
                continue;
            }

            if (language_type >= 2 && bookFile.getName().endsWith(".mp3") && bookFile.getName().contains(nameEng)) {
                mp3FileEng = bookFile;
                continue;
            }

            if (bookFile.getName().contains("睡眠封面")) {
                surfaceSleep = bookFile;
                continue;
            }

            if (bookFile.getName().contains("封面")) {
                surface = bookFile;
                continue;
            }
        }

        Comparator<File> comparator = (o1, o2) -> {
            Integer o1Int = Integer.parseInt(o1.getName().substring(1, o1.getName().indexOf(".")));
            Integer o2Int = Integer.parseInt(o2.getName().substring(1, o2.getName().indexOf(".")));
            return o1Int.compareTo(o2Int);
        };

        if (music_type >= 2) {
            if (bookUnits == null || bookUnits.size() == 0) {
                System.out.println("txt中解析文字段落数量必须>0, 请确定您的txt文件存在, 并且其中内容格式正确");
                return;
            }

            if (bookUnits.size() != book_unit_size) {
                System.out.println("txt中解析的文字段落数量为:" + bookUnits.size() + " 和 book_unit_size:" + book_unit_size + "  不匹配");
                System.out.println("请确定文字数量是否正确, 以及txt文件编码格式是否为utf-8");
                return;
            }

            if (picFiles == null || picFiles.size() == 0) {
                System.out.println("图片文件数量为0, 请确定您的文件夹中包含 P+数字.jpg 的图片文件");
                return;
            }

            if (picFiles.size() != book_unit_size) {
                System.out.println("图片数量为:" + picFiles.size() + " 和 book_unit_size:" + book_unit_size + "  不匹配");
                return;
            }

            Collections.sort(picFiles, comparator);

            if(language_type <= 2) {
                if (mp3ChnFiles == null || mp3ChnFiles.size() == 0) {
                    System.out.println("中文音乐文件数量为0, 请确定您的文件夹中包含 C+数字.mp3 的音乐文件");
                    return;
                }

                if (mp3ChnFiles.size() != book_unit_size) {
                    System.out.println("中文音乐数量为:" + mp3ChnFiles.size() + " 和 book_unit_size:" + book_unit_size + "  不匹配");
                    return;
                }

                Collections.sort(mp3ChnFiles, comparator);
            }

            if(language_type >= 2) {
                if (mp3EngFiles == null || mp3EngFiles.size() == 0) {
                    System.out.println("英文音乐文件数量为0, 请确定您的文件夹中包含 E+数字.mp3 的音乐文件");
                    return;
                }

                if (mp3EngFiles.size() != book_unit_size) {
                    System.out.println("英文音乐数量为:" + mp3EngFiles.size() + " 和 book_unit_size:" + book_unit_size + "  不匹配");
                    return;
                }

                Collections.sort(mp3EngFiles, comparator);
            }
        }

        if (surface == null) {
            System.out.println("封面图片不存在, 文件夹内必须包含 封面.jpg");
            return;
        }

        if (music_type <= 2 && surfaceSleep == null) {
            System.out.println("睡眠封面图片不存在, 文件夹内必须包含 睡眠封面.jpg");
            return;
        }

        if (music_type <= 2 && mp3File == null && language_type <= 2) {
            System.out.println("中文音乐文件不存在, 文件夹内必须包含 " + nameChn + ".mp3");
            return;
        }

        if (music_type <= 2 && mp3FileEng == null && language_type >= 2) {
            System.out.println("英文音乐文件不存在, 文件夹内必须包含 " + nameEng + ".mp3");
            return;
        }

        /////////////////////////   验证通过    ////////////////////////////
        System.out.println("--------------- 验证通过,开始上传 ---------------");
        if(isTest) {
            return;
        }

        Book book = new Book();
        book.name = nameChn;
        book.engName = nameEng;

        String surfaceName = (language_type <= 2 ? book.name : book.engName) + "_封面";
        book.surfaceImg = getUrl(surface, surfaceName);
        if (StringUtils.isEmpty(book.surfaceImg)) {
            System.out.println("封面图片文件上传失败");
            return;
        }

        if(music_type <= 2) {
            String surfaceSleepName = (language_type <= 2 ? book.name : book.engName) + "_睡眠封面";
            book.surfaceSleepImg = getUrl(surfaceSleep, surfaceSleepName);
            if (StringUtils.isEmpty(book.surfaceSleepImg)) {
                System.out.println("睡眠封面图片文件上传失败");
                return;
            }
        }

        if (music_type <= 2 && language_type <= 2) {
            String musicName = book.name + "_音乐";
            book.musicUrl = getUrl(mp3File, musicName);
            if (StringUtils.isEmpty(book.musicUrl)) {
                System.out.println("中文音乐文件上传失败");
                return;
            }
        }

        if (music_type <= 2 && language_type >= 2) {
            String musicEngName = (language_type <= 2 ? book.name : book.engName)  + "_英文音乐";
            book.musicUrlEng = getUrl(mp3FileEng, musicEngName);
            if (StringUtils.isEmpty(book.musicUrlEng)) {
                System.out.println("英文音乐文件上传失败");
                return;
            }
        }

        try {
            String bookId;

            // 根据名字获取已有Book
            String where;
            if(language_type <= 2) {
                where = "{\"path\":\"" + book.name + "\"}";
            } else {
                where = "{\"engName\":\"" + book.engName + "\"}";
            }
            where = URLEncoder.encode(where, "utf-8");
            String responseStr = LeanCloudHttpUtils.getString("https://api.leancloud.cn/1.1/classes/Book?where=" + where);
            Type type = new TypeToken<ListResponse<Book>>() {}.getType();
            ListResponse<Book> response = new Gson().fromJson(responseStr, type);
            if (response == null || response.results == null || response.results.size() == 0) {
                System.out.println((language_type <= 2 ? book.name : book.engName) + " Book ... 不存在,新建");

                book.languageStatus = language_type;
                book.musicType = music_type;
                book.tagType = tag_type;
                book.bookSetType = 3;
                book.ageRange = age_range;
                book.price = price;
                book.categoryId = book_category_id;

                book.parentBookSet = new Pointer("Book", parentBook.getObjectId());

                String postBookResponse = LeanCloudHttpUtils.postBean(book);
                bookId = new Gson().fromJson(postBookResponse, Pointer.class).objectId;

                System.out.println((language_type <= 2 ? book.name : book.engName) + " Book ... 新建成功");
            } else {
                // 已存在的, 进行更新
                bookId = response.results.get(0).objectId;

                book.languageStatus = language_type;
                book.musicType = music_type;
                book.tagType = tag_type;
                book.bookSetType = 3;
                book.ageRange = age_range;
                book.price = price;
                book.categoryId = book_category_id;

                book.parentBookSet = new Pointer("Book", parentBook.getObjectId());

                String postBookResponse = LeanCloudHttpUtils.updateBean(book, bookId);
                new Gson().fromJson(postBookResponse, Pointer.class);

                System.out.println((language_type <= 2 ? book.name : book.engName) + " Book ... 已存在, 更新成功");
            }

            if(music_type >= 2) {
                Pointer bookPointer = new Pointer("Book", bookId);
                String whereUnits = "{\"book\":" + new Gson().toJson(bookPointer) + "}";
                whereUnits = encodeString(whereUnits);
                String unitResponseStr = LeanCloudHttpUtils.getString("https://api.leancloud.cn/1.1/classes/BookUnit?include=book&where=" + whereUnits);
                ListResponse<BookUnit> unitResponse = new Gson().fromJson(unitResponseStr, new TypeToken<ListResponse<BookUnit>>() {}.getType());
                if (unitResponse != null && unitResponse.results != null) {
                    System.out.println((language_type <= 2 ? book.name : book.engName) + " Book ... 已有故事段 " + unitResponse.results.size() + " 个");
                }

                System.out.println();
                // 处理Book下unit
                for (int i = 0; i < bookUnits.size(); i++) {
                    System.out.println();
                    BookUnit bookUnit = bookUnits.get(i);

                    bookUnit.imgUrl = getUrl(picFiles.get(i), (language_type <= 2 ? book.name : book.engName) + "_" + picFiles.get(i).getName());
                    if(language_type <= 2) {
                        bookUnit.musicUrl = getUrl(mp3ChnFiles.get(i), (language_type <= 2 ? book.name : book.engName) + "_" + mp3ChnFiles.get(i).getName());
                    }
                    if(language_type >= 2) {
                        bookUnit.musicUrlEng = getUrl(mp3EngFiles.get(i), (language_type <= 2 ? book.name : book.engName) + "_" + mp3EngFiles.get(i).getName());
                    }

                    bookUnit.book = new Pointer("Book", bookId);

                    BookUnit oldUnit = hasUnit(unitResponse, bookUnit);
                    if (oldUnit != null) {
                        // 如果unit已经存在,更新之,注意整个更新unit
                        String unitUpdateResponse = LeanCloudHttpUtils.updateBean(bookUnit, oldUnit.objectId);
                        new Gson().fromJson(unitUpdateResponse, Pointer.class);
                        System.out.println((language_type <= 2 ? book.name : book.engName) + "_" + bookUnit.index + " BookUnit ... 已存在, 更新成功");
                    } else {
                        // 不存在新建之
                        String unitAddResponse = LeanCloudHttpUtils.postBean(bookUnit);
                        new Gson().fromJson(unitAddResponse, Pointer.class);
                        System.out.println((language_type <= 2 ? book.name : book.engName) + "_" + bookUnit.index + " BookUnit ... 新建成功");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Book上传失败 : " + (language_type <= 2 ? book.name : book.engName));
        }
    }

    private static String getUrl(File uploadFile, String filename) {
        String url = null;
        try {
//            // 如果文件已存在, 直接使用url, 否则新上传
//            LeanFile leanFile = hasFile(oldFiles, filename);
//            if (leanFile == null) {
                url = new Gson().fromJson(uploadFileWithFileName(filename, uploadFile), LeanFile.class).url;
                System.out.println(filename + " ... 文件上传成功");
//            } else {
//                url = leanFile.url;
//                System.out.println(filename + " ... 文件已存在,使用已有url");
//            }
        } catch (Exception e) {
            System.out.println(filename + " ... 文件上传失败");
        }
        return url;
    }

    private static BookUnit hasUnit(ListResponse<BookUnit> unitResponse, BookUnit bookUnit) {
        if (unitResponse != null && unitResponse.results != null) {
            for (BookUnit unit : unitResponse.results) {
                if (unit.book.objectId.equals(bookUnit.book.objectId) && unit.index == bookUnit.index) {
                    return unit;
                }
            }
        }
        return null;
    }

//    private static LeanFile hasFile(ArrayList<LeanFile> files, String fileName) {
//        if (files == null) {
//            return null;
//        }
//
//        for (LeanFile file : files) {
//            try {
//                if (file.path.equals(getEncodeWithoutPerName(fileName))) {
//                    return file;
//                }
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    private static String uploadFileWithFileName(String fileName, File file) throws Exception {
        fileName = getEncodeWithoutPerName(fileName);
        String url = "https://api.leancloud.cn/1.1/files/" + fileName;
        return LeanCloudHttpUtils.postFile(url, file);
    }

    private static String getEncodeWithoutPerName(String fileName) throws UnsupportedEncodingException {
        fileName = fileName.replace(" ", "");
        fileName = URLEncoder.encode(fileName, "utf-8");
        fileName = fileName.replace("%", "");
        return fileName;
    }

    private static ArrayList<BookUnit> parseBookUnit(File file) {
        ArrayList<BookUnit> bookUnits = new ArrayList<>();
        ArrayList<String> lines = FileUtils.readToStringLines(file);

        if (lines == null) {
            return null;
        }

        BookUnit bookUnit = null;
        for (String line : lines) {
            if (line.length() == 0) {
                continue;
            }

            Pattern pattern = Pattern.compile("^([0-9]{1,3})、(.+)");
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                if (bookUnit != null) {
                    bookUnits.add(bookUnit);
                }

                bookUnit = new BookUnit();
                bookUnit.index = Integer.parseInt(matcher.group(1));

                if (!StringUtils.hasChinese(line)) {
                    bookUnit.contentEng = matcher.group(2);
                } else {
                    bookUnit.contentChn = matcher.group(2);
                }
            } else {
                if (bookUnit != null) {
                    if (!StringUtils.hasChinese(line)) {
                        if (bookUnit.contentEng == null) {
                            bookUnit.contentEng = line;
                        } else {
                            bookUnit.contentEng += ("\n" + line);
                        }
                    } else {
                        if (bookUnit.contentChn == null) {
                            bookUnit.contentChn = line;
                        } else {
                            bookUnit.contentChn += ("\n" + line);
                        }
                    }
                }
            }
        }

        if (bookUnit != null) {
            bookUnits.add(bookUnit);
        }

        return bookUnits;
    }

}
