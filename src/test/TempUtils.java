package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import utils.AndroidUtils;
import utils.CharacterParser;
import utils.FileUtils;
import utils.XmlUtil;
import entity.IdNamingBean;

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
	
	/**
	 * 自动生成TextView size设置代码
	 * 
	 * @param isAutoSetId 是否自动给没有id的TextView添加id
	 */
	public static void autoCreateSizeSet(boolean isAutoSetId) {
		// TODO 自动设置文字id的前缀
		String tvId4SetSizePre = "@+id/tv_set_size";
		// 自动设置文字id的后缀
		int tvId4SetSizeEnd = 1;
		
		// TODO 项目中的文字大小数组,项目中如果有变化需要修改此处
		Integer[] TEXT_SIZE = { 20, 18, 16, 14, 12, 28 };
		// 数组转成集合,方便indexOf操作
		List<Integer> TEXT_SIZE_LIST = Arrays.asList(TEXT_SIZE);
		// TODO 如果size不在集合中,则默认获取的索引
		int defaultIndex = 3;
		
		Document document = XmlUtil.read("temp\\SetSize\\layout_size.xml");
		List<Element> docElements = XmlUtil.getAllElements(document);
		
		List<IdNamingBean> tvIdNameBeans = new ArrayList<IdNamingBean>();
		StringBuilder sbSetTextSize = new StringBuilder();
		
		// 遍历layout中全部元素
		for (int i=0; i<docElements.size(); i++) {
			Element element = docElements.get(i);
			
			// 如果不是TextView,跳过
			if(!element.getName().equals("TextView")) {
				continue;
			}
			
			//////////////	 处理textSize	 ////////////////
			Attribute attrTextSize = element.attribute("textSize");

			// TODO 另一种处理,没有textSize参数时不做setSize操作
//			if(attrTextSize == null) {
//				System.out.println("布局内第" + i + "个控件TextView未设置textSize");
//				continue;
//			}

			int indexOf = -1;
			if(attrTextSize == null) {
				// 没有textSize参数时,取默认的size
				indexOf = defaultIndex;
			} else {
				// 有textSize再去数据中获取索引
				
				int textSizeNum = -1;
				// 获取文字大小
				String textSize = attrTextSize.getValue()
						// 排除单位
						.replace("dp", "")
						.replace("dip", "")
						.replace("px", "")
						.replace("sp", "");
				try {
					textSizeNum = Integer.parseInt(textSize);
				} catch (Exception e) {
					textSizeNum = -1;
				}
				
				if(textSizeNum == -1) {
					System.out.println("布局内第" + i + "个控件TextView的textSize格式不对");
					continue;
				}
				
				indexOf = TEXT_SIZE_LIST.indexOf(textSizeNum);
				if(indexOf == -1) {
					// TODO 另一种处理,获取不到时不做setSize操作
//					System.out.println("布局内第" + i + "个TextView的textSize不在TEXT_SIZE数组中");
//					continue;
					
					// 获取不到时,去默认索引位置的size
					indexOf = defaultIndex;
				}
			}
			
			//////////////	处理id	 ////////////////
			Attribute attrID = element.attribute("id");
				
			String id = null;
			if(attrID == null) {
				// 如果没有id,且需要自动设置一个
				if(isAutoSetId) {
					id = tvId4SetSizePre + (tvId4SetSizeEnd++);
					element.addAttribute("android:id", id);
				}
			} else {
				// 如果已有id
				id = attrID.getValue();
			}
			
			if(id == null) {
				System.out.println("布局内第" + i + "个控件TextView没有id的不做处理");
				continue;
			}
			
			// 去除"@+id/"后的id名称
			String idName = id.replace("@+id/", "");

			tvIdNameBeans.add(new IdNamingBean(element.getName(), idName, element));
			
			// 例 tv_set_size1.setTextSize(Constant.TEXT_SIZE[0] * TxtManager.getInstance().getTxtSize(context));
			String setSizeLine = idName + ".setTextSize(Constant.TEXT_SIZE[" + indexOf + 
					"] * TxtManager.getInstance().getTxtSize(context));";
			sbSetTextSize.append(AndroidUtils.formatSingleLine(2, setSizeLine));
		}
		
		// 如果要自动setId则将设置好id的xml写回布局layout_size_new.xml中
		if(isAutoSetId) {
			XmlUtil.write2xml(new File("temp\\SetSize\\layout_size_new.xml"), document);
		}
		
		AndroidUtils.idNamingBeans = tvIdNameBeans;
		// 解析idNamingBeans集合中的信息,生成页面文本信息
		String activityContent = AndroidUtils.createActivityContent();
		
		// 封装到onResume里
//		@Override
//		protected void onResume() {
//			super.onResume();
//			tv_set_size1.setTextSize(Constant.TEXT_SIZE[0] * TxtManager.getInstance().getTxtSize(context))
//		}
		sbSetTextSize.insert(0, AndroidUtils.formatSingleLine(2, "super.onResume();"));
		sbSetTextSize.insert(0, AndroidUtils.formatSingleLine(1, "protected void onResume() {"));
		sbSetTextSize.insert(0, AndroidUtils.formatSingleLine(1, "@Override"));
		sbSetTextSize.append(AndroidUtils.formatSingleLine(1, "}"));
		
		activityContent += sbSetTextSize.toString();
		
		FileUtils.writeString2File(activityContent, new File("temp\\SetSize\\Layout_Size.java"));
		
		System.out.println("--------------");
		System.out.println("代码生成正确,请在SetSize/Layout_Size.java中查看");
	}
	
}
