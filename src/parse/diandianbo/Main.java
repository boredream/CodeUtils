package parse.diandianbo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import parse.LeanCloudHttpUtils;
import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws Exception {
        File dir = new File("books");
        System.out.println(dir.getAbsolutePath());
        File[] files = dir.listFiles();
        if (files == null) {
            return;
        }

        System.out.println("--------------- Book 数量为: " + files.length + "---------------");
        System.out.println();
        System.out.println();

        for (File file : files) {
            saveBook(file);
        }

        System.out.println("--------------- 上传完成 ---------------");
        Thread.sleep(5 * 1000);
    }

    private static void saveBook(File file) {
        if (!file.isDirectory()) {
            return;
        }

        File[] bookFiles = file.listFiles();
        if (bookFiles == null) {
            return;
        }

        // TODO 考虑只有一种语言的情况

        // TODO: 2016/12/21 检测重复数据和重复文件
        String[] names = file.getName().split("_");

        System.out.println("--------------- 开始处理 Book " + names[0] + " ---------------");

        ArrayList<LeanFile> oldFiles = null;
        try {
            // 先获取本书相关的所有文件
            String where = "{\"name\":{\"$regex\":\"" + names[0] + "_.*\"}}";
            Type type = new TypeToken<ListResponse<LeanFile>>(){}.getType();
            ListResponse<LeanFile> response = new Gson().fromJson(
                    LeanCloudHttpUtils.getString("https://api.leancloud.cn/1.1/files?where=" + where), type);
            oldFiles = response.results;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(oldFiles == null) {
            System.out.println("获取已有文件失败");
            return;
        }

        String nameChn = names[0];
        String nameEng = names[1];
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

            if (matcherMp3Chn.find()) {
                mp3ChnFiles.add(bookFile);
                continue;
            }

            Pattern patternMp3Eng = Pattern.compile("^E[0-9]{1,3}.mp3");
            Matcher matcherMp3Eng = patternMp3Eng.matcher(bookFile.getName());

            if (matcherMp3Eng.find()) {
                mp3EngFiles.add(bookFile);
                continue;
            }

            if (bookFile.getName().endsWith(".txt")) {
                bookUnits = parseBookUnit(bookFile);
                BookUnit bookUnitFirst = new BookUnit();
                bookUnitFirst.index = 0;
                bookUnitFirst.contentChn = nameChn;
                bookUnitFirst.contentEng = nameEng;
                assert bookUnits != null;
                bookUnits.add(0, bookUnitFirst);
                continue;
            }

            if (bookFile.getName().endsWith(".mp3") && bookFile.getName().contains(nameChn)) {
                mp3File = bookFile;
                continue;
            }

            if (bookFile.getName().endsWith(".mp3") && bookFile.getName().contains(nameEng)) {
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

        if (bookUnits != null) {
            Comparator<File> comparator = (o1, o2) -> {
                Integer o1Int = Integer.parseInt(o1.getName().substring(1, o1.getName().indexOf(".")));
                Integer o2Int = Integer.parseInt(o2.getName().substring(1, o2.getName().indexOf(".")));
                return o1Int.compareTo(o2Int);
            };

            Collections.sort(picFiles, comparator);
            Collections.sort(mp3ChnFiles, comparator);
            Collections.sort(mp3EngFiles, comparator);

            if(bookUnits.size() != picFiles.size() ||
                    bookUnits.size() != mp3ChnFiles.size() ||
                    bookUnits.size() != mp3EngFiles.size()) {
                System.out.println("文字段落数量=" + bookUnits.size());
                System.out.println("图片数量=" + picFiles.size());
                System.out.println("中文音乐数量=" + mp3ChnFiles.size());
                System.out.println("英文音乐数量=" + mp3EngFiles.size());
                System.out.println("Book文字、音乐、英文音乐、封面数量不匹配");
                return;
            }

            if (surfaceSleep == null) {
                System.out.println("睡眠封面文件不存在, 文件夹内必须包含带[睡眠封面]字样的图片文件");
                return;
            }

            if(surface == null) {
                System.out.println("封面文件不存在, 文件夹内必须包含带[封面]字样的图片文件");
                return;
            }

            if(mp3File == null) {
                System.out.println("中文音乐文件不存在, 文件夹内必须包含中文名[" + nameChn + "]的mp3文件");
                return;
            }

            if(mp3FileEng == null) {
                System.out.println("英文音乐文件不存在, 文件夹内必须包含英文名[" + nameEng + "]的mp3文件");
                return;
            }

            Book book = new Book();
            book.name = nameChn;
            book.engName = nameEng;

            String surfaceName = book.name + "_封面";
            book.surfaceImg = getUrl(oldFiles, surface, surfaceName);
            if(StringUtils.isEmpty(book.surfaceImg)) {
                return;
            }

            String surfaceSleepName = book.name + "_睡眠封面";
            book.surfaceSleepImg = getUrl(oldFiles, surfaceSleep, surfaceSleepName);
            if(StringUtils.isEmpty(book.surfaceSleepImg)) {
                return;
            }

            String musicName = book.name + "_音乐";
            book.musicUrl = getUrl(oldFiles, mp3File, musicName);
            if(StringUtils.isEmpty(book.musicUrl)) {
                return;
            }

            String musicEngName = book.name + "_英文音乐";
            book.musicUrlEng = getUrl(oldFiles, mp3FileEng, musicEngName);
            if(StringUtils.isEmpty(book.musicUrlEng)) {
                return;
            }

            try {
                String bookId;

                // 根据名字获取Book
                String where = "{\"name\":\"" + book.name + "\"}";
                String responseStr = LeanCloudHttpUtils.getString("https://api.leancloud.cn/1.1/classes/Book?where=" + where);
                Type type = new TypeToken<ListResponse<Book>>(){}.getType();
                ListResponse<Book> response = new Gson().fromJson(responseStr, type);
                if(response == null || response.results == null || response.results.size() == 0) {
                    System.out.println(book.name + " Book ... 不存在,新建");

                    book.languageStatus = 2;
                    book.musicType = 2;
                    book.tagType = 2;
                    book.bookSetType = 1;
                    String postBookResponse = LeanCloudHttpUtils.postBean(book);
                    bookId = new Gson().fromJson(postBookResponse, Pointer.class).objectId;

                    System.out.println(book.name + " Book ... 新建成功");
                } else {
                    // 已存在的, 进行更新
                    // 更新操作只修改4个文件信息
                    Book newBook = response.results.get(0);
                    newBook.surfaceImg = book.surfaceImg;
                    newBook.surfaceSleepImg = book.surfaceSleepImg;
                    newBook.musicUrl = book.musicUrl;
                    newBook.musicUrlEng = book.musicUrlEng;

                    bookId = newBook.objectId;
                    String postBookResponse = LeanCloudHttpUtils.updateBean(newBook, bookId);
                    new Gson().fromJson(postBookResponse, Pointer.class);

                    System.out.println(book.name + " Book ... 已存在, 更新成功");
                }

                Pointer bookPointer = new Pointer("Book", bookId);
                String whereUnits = "{\"book\":" + new Gson().toJson(bookPointer) + "}";
                String unitResponseStr = LeanCloudHttpUtils.getString("https://api.leancloud.cn/1.1/classes/BookUnit?include=book&where=" + whereUnits);
                ListResponse<BookUnit> unitResponse = new Gson().fromJson(unitResponseStr, new TypeToken<ListResponse<BookUnit>>(){}.getType());
                if(unitResponse != null && unitResponse.results != null) {
                    System.out.println(book.name + " Book ... 已有故事段 " + unitResponse.results.size() + " 个");
                }

                System.out.println();
                // 处理Book下unit
                for (int i = 0; i < bookUnits.size(); i++) {
                    System.out.println();
                    BookUnit bookUnit = bookUnits.get(i);

                    bookUnit.imgUrl = getUrl(oldFiles, picFiles.get(i), book.name + "_" + picFiles.get(i).getName());
                    bookUnit.musicUrl = getUrl(oldFiles, mp3ChnFiles.get(i), book.name + "_" + mp3ChnFiles.get(i).getName());
                    bookUnit.musicUrlEng = getUrl(oldFiles, mp3EngFiles.get(i), book.name + "_" + mp3EngFiles.get(i).getName());

                    bookUnit.book = new Pointer("Book", bookId);

                    BookUnit oldUnit = hasUnit(unitResponse, bookUnit);
                    if (oldUnit != null) {
                        // 如果unit已经存在,更新之,注意整个更新unit
                        String unitUpdateResponse = LeanCloudHttpUtils.updateBean(bookUnit, oldUnit.objectId);
                        new Gson().fromJson(unitUpdateResponse, Pointer.class);
                        System.out.println(book.name + "_" + bookUnit.index + " BookUnit ... 已存在, 更新成功");
                    } else {
                        // 不存在新建之
                        String unitAddResponse = LeanCloudHttpUtils.postBean(bookUnit);
                        new Gson().fromJson(unitAddResponse, Pointer.class);
                        System.out.println(book.name + "_" + bookUnit.index + " BookUnit ... 新建成功");
                    }
                }
            } catch (Exception e) {
                System.out.println("Book上传失败 : " + book.name);
            }
        }
    }

    private static String getUrl(ArrayList<LeanFile> oldFiles, File uploadFile, String filename) {
        String url = null;
        try {
            // 如果文件已存在, 直接使用url, 否则新上传
            LeanFile leanFile = hasFile(oldFiles, filename);
            if(leanFile == null) {
                url = new Gson().fromJson(uploadFileWithFileName(filename, uploadFile), LeanFile.class).url;
                System.out.println(filename + " ... 文件不存在,上传成功");
            } else {
                url = leanFile.url;
                System.out.println(filename + " ... 文件已存在,使用已有url");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(filename + " ... 文件不存在,上传失败");
        }
        return url;
    }

    private static BookUnit hasUnit(ListResponse<BookUnit> unitResponse, BookUnit bookUnit) {
        if(unitResponse != null && unitResponse.results != null) {
            for (BookUnit unit : unitResponse.results) {
                if(unit.book.objectId.equals(bookUnit.book.objectId) && unit.index == bookUnit.index) {
                    return unit;
                }
            }
        }
        return null;
    }

    private static LeanFile hasFile(ArrayList<LeanFile> files, String fileName) {
        if(files == null) {
            return null;
        }

        for (LeanFile file : files) {
            if(file.name.equals(fileName)) {
                return file;
            }
        }
        return null;
    }

    private static String uploadFileWithFileName(String fileName, File file) throws Exception {
        String url = "https://api.leancloud.cn/1.1/files/" + fileName;
        return LeanCloudHttpUtils.postFile(url, file);
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
            } else if (bookUnit != null) {
                if (!StringUtils.hasChinese(line)) {
                    bookUnit.contentEng = line;
                } else {
                    bookUnit.contentChn = line;
                }
            }
        }

        if (bookUnit != null) {
            bookUnits.add(bookUnit);
        }

        return bookUnits;
    }

    private static class Book extends Pointer {
        public int musicType;
        public String surfaceImg;
        public String surfaceSleepImg;
        public int tagType;
        public String musicUrl;
        public String musicUrlEng;
        public int bookSetType;
        public int languageStatus;
        public String name;
        public String engName;
    }

    private static class BookUnit extends Pointer {
        public Pointer book;
        public int index;
        public String imgUrl;
        public String contentChn;
        public String contentEng;
        public String musicUrl;
        public String musicUrlEng;
    }

    private static class ListResponse<T> {
        public ArrayList<T> results;
    }

    private static class LeanFile {
        public String name;
        public String url;
    }

    public static class Pointer implements Serializable {
        public static final String TYPE = "Pointer";

        public String __type;
        public String className;
        public String objectId;

        public Pointer() {}

        public Pointer(String className, String objectId) {
            this.__type = TYPE;
            this.className = className;
            this.objectId = objectId;
        }
    }

}
