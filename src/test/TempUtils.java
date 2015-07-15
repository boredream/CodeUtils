package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.Element;

import utils.AndroidUtils;
import utils.CharacterParser;
import utils.FileUtils;
import utils.XmlUtil;

public class TempUtils {

	public static void weiboEmoji() {
		// emoji
		StringBuilder sb = new StringBuilder();

		String names = "[羞羞哒甜馨][萌神奥莉][带着微博去旅行][爱红包][拍照][马到成功]→_→[呵呵][嘻嘻][哈哈][爱你][挖鼻屎][吃惊][晕][泪][馋嘴][抓狂][哼][可爱][怒][汗][害羞][睡觉][钱][偷笑][笑cry][doge][喵喵][酷][衰][闭嘴][鄙视][花心][鼓掌][悲伤][思考][生病][亲亲][怒骂][太开心]" +
				"[懒得理你][右哼哼][左哼哼][嘘][委屈][吐][可怜][打哈气][挤眼][失望][顶][疑问][困][感冒][拜拜][黑线][阴险][打脸][傻眼][互粉][心][伤心][猪头][熊猫][兔子]";

		String regexEmoji = "\\[([\u4e00-\u9fa5a-zA-Z0-9])+\\]";
		Pattern patternEmoji = Pattern.compile(regexEmoji);
		Matcher matcherEmoji = patternEmoji.matcher(names);

		CharacterParser parser = CharacterParser.getInstance();
		List<File> files = FileUtils.getAllFiles("temp");
		while (matcherEmoji.find()) { // 如果可以匹配到
			String key = matcherEmoji.group(); // 获取匹配到的具体字符

			String pinyinName = "d_" + parser.getSpelling(key).replace("[", "").replace("]", "");

			boolean hasName = false;
			for (File file : files) {
				String fileName = FileUtils.getName(file);
				if (fileName.equals(pinyinName)) {
					hasName = true;
					break;
				}
			}

			sb.append("emojiMap.put(\"" + key + "\", R.drawable." + pinyinName + ");\n");
			if (!hasName) {
				System.out.println(key);
			}
		}
		System.out.println(sb.toString());
	}
	
	public static void batchRename() {
		List<File> files = FileUtils.getAllFiles("C:\\Users\\root\\Documents\\QQEIM Files\\2851657065\\FileRecv\\images_7.14\\normal");
		for(File file : files) {
			File nFile = new File(file.getParent(), "ic_" + file.getName());
			file.renameTo(nFile);
		}
	}
	
	/**
	 * 复制项目代码
	 * 
	 * @param projectPath 
	 * @param subPaths
	 */
	public static void batchCopyFiles(String projectPath, String[] subPaths) {
		List<File> allFiles = FileUtils.getAllFiles("E:\\GitHub\\NoDrinkOut\\src\\com\\boredream\\nodrinkout\\fragment");
		for(File file : allFiles) {
			for(String subPath : subPaths) {
				
			}
			
			String string = FileUtils.readToString(file);
			FileUtils.writeString2File(
					string,
					new File("E:\\GitHub\\GeekSchool\\MyWeibo\\Demo4Eclipse\\src\\com\\boredream\\boreweibo\\fragment\\" + file.getName()),
					"UTF-8");
		}
	}
	
	public static void responseRetCode_Msg() {
		StringBuilder sb = new StringBuilder();
		Map<String, String> maps = new TreeMap<String, String>(
				new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						Integer parseInt1 = Integer.parseInt(o1.replace("\"", ""));
						Integer parseInt2 = Integer.parseInt(o2.replace("\"", ""));
						return parseInt1.compareTo(parseInt2);
					}
		});
		
		String regexEmoji = "\"([0-9])+\"";
		Pattern patternEmoji = Pattern.compile(regexEmoji);
		//
		File file = new File("temp\\map.txt");
		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader bufferedreader = new BufferedReader(fr);
			String line;
			while ((line=bufferedreader.readLine()) != null) {
				Matcher matcherEmoji = patternEmoji.matcher(line);
				if(matcherEmoji.find()) {
//					System.out.println(matcherEmoji.group());
//					System.out.println(line.substring(line.indexOf("//") + 2));
					
//					map.put("2", "账号不存在");
					String key = matcherEmoji.group();
					String value = "\"" + line.substring(line.indexOf("//") + 2) + "\"";
					
					maps.put(key, value);
					
					
//					sb.append("map.put(").append(key).append(", ").append(value).append(");\n");
					
				}
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(Map.Entry<String, String> entry : maps.entrySet()) {
			sb.append("map.put(").append(entry.getKey()).append(", ").append(entry.getValue()).append(");\n");
		}
		
		System.out.println(sb.toString());
	}
	
}
