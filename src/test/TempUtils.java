package test;

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFGroupShape;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import utils.AndroidUtils;
import utils.CharacterParser;
import utils.FileUtils;
import utils.XmlUtil;
import entity.IdNamingBean;

public class TempUtils {
	
	public static void main(String[] args) {
//		extractAllString();
		exportXml();
//		autoCreateSizeSet(true);
//		desParams();
	}

	public static void weiboEmoji() {
		
		// emoji
		StringBuilder sb = new StringBuilder();

		String names = "[羞羞哒甜馨][萌神奥莉][带着微博去旅行][爱红包][拍照][马到成功]→_→[呵呵][嘻嘻][哈哈][爱你][挖鼻屎][吃惊][晕][泪][馋嘴][抓狂][哼][可爱][怒][汗][害羞][睡觉][钱][偷笑][笑cry][doge][喵喵][酷][衰][闭嘴][鄙视][花心][鼓掌][悲伤][思考][生病][亲亲][怒骂][太开心]"
				+ "[懒得理你][右哼哼][左哼哼][嘘][委屈][吐][可怜][打哈气][挤眼][失望][顶][疑问][困][感冒][拜拜][黑线][阴险][打脸][傻眼][互粉][心][伤心][猪头][熊猫][兔子]";

		String regexEmoji = "\\[([\u4e00-\u9fa5a-zA-Z0-9])+\\]";
		Pattern patternEmoji = Pattern.compile(regexEmoji);
		Matcher matcherEmoji = patternEmoji.matcher(names);

		CharacterParser parser = CharacterParser.getInstance();
		List<File> files = FileUtils.getAllFiles("temp");
		while (matcherEmoji.find()) { // 如果可以匹配到
			String key = matcherEmoji.group(); // 获取匹配到的具体字符

			String pinyinName = "d_"
					+ parser.getSpelling(key).replace("[", "").replace("]", "");

			boolean hasName = false;
			for (File file : files) {
				String fileName = FileUtils.getName(file);
				if (fileName.equals(pinyinName)) {
					hasName = true;
					break;
				}
			}

			sb.append("emojiMap.put(\"" + key + "\", R.drawable." + pinyinName
					+ ");\n");
			if (!hasName) {
				System.out.println(key);
			}
		}
		System.out.println(sb.toString());
	}

	public static void batchRename() {
		List<File> files = FileUtils.getAllFiles("C:" + File.separator
				+ "Users" + File.separator + "root" + File.separator
				+ "Documents" + File.separator + "QQEIM Files" + File.separator
				+ "2851657065" + File.separator + "FileRecv" + File.separator
				+ "images_7.14" + File.separator + "normal");
		for (File file : files) {
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
		List<File> allFiles = FileUtils.getAllFiles("E:" + File.separator
				+ "GitHub" + File.separator + "NoDrinkOut" + File.separator
				+ "src" + File.separator + "com" + File.separator + "boredream"
				+ File.separator + "nodrinkout" + File.separator + "fragment");
		for (File file : allFiles) {
			for (String subPath : subPaths) {

			}

			String string = FileUtils.readToString(file);
			FileUtils.writeString2File(string, new File("E:" + File.separator
					+ "GitHub" + File.separator + "GeekSchool" + File.separator
					+ "MyWeibo" + File.separator + "Demo4Eclipse"
					+ File.separator + "src" + File.separator + "com"
					+ File.separator + "boredream" + File.separator
					+ "boreweibo" + File.separator + "fragment"
					+ File.separator + "" + file.getName()), "UTF-8");
		}
	}

	public static void responseRetCode_Msg() {
		StringBuilder sb = new StringBuilder();
		Map<String, String> maps = new TreeMap<String, String>(
				new Comparator<String>() {

					@Override
					public int compare(String o1, String o2) {
						Integer parseInt1 = Integer.parseInt(o1.replace("\"",
								""));
						Integer parseInt2 = Integer.parseInt(o2.replace("\"",
								""));
						return parseInt1.compareTo(parseInt2);
					}
				});

		String regexEmoji = "\"([0-9])+\"";
		Pattern patternEmoji = Pattern.compile(regexEmoji);
		//
		File file = new File("temp" + File.separator + "map.txt");
		FileReader fr;
		try {
			fr = new FileReader(file);
			BufferedReader bufferedreader = new BufferedReader(fr);
			String line;
			while ((line = bufferedreader.readLine()) != null) {
				Matcher matcherEmoji = patternEmoji.matcher(line);
				if (matcherEmoji.find()) {
					// System.out.println(matcherEmoji.group());
					// System.out.println(line.substring(line.indexOf("//") +
					// 2));

					// map.put("2", "账号不存在");
					String key = matcherEmoji.group();
					String value = "\""
							+ line.substring(line.indexOf("//") + 2) + "\"";

					maps.put(key, value);

					// sb.append("map.put(").append(key).append(", ").append(value).append(");\n");

				}
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Map.Entry<String, String> entry : maps.entrySet()) {
			sb.append("map.put(").append(entry.getKey()).append(", ")
					.append(entry.getValue()).append(");\n");
		}

		System.out.println(sb.toString());
	}

	/**
	 * 自动生成TextView size设置代码
	 * 
	 * @param isAutoSetId
	 *            是否自动给没有id的TextView添加id
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
		int defaultIndex = 2;

		Document document = XmlUtil.read("temp" + File.separator + "SetSize"
				+ File.separator + "layout_size.xml");
		List<Element> docElements = XmlUtil.getAllElements(document);

		List<IdNamingBean> tvIdNameBeans = new ArrayList<IdNamingBean>();
		StringBuilder sbSetTextSize = new StringBuilder();

		// 遍历layout中全部元素
		for (int i = 0; i < docElements.size(); i++) {
			Element element = docElements.get(i);

			// 如果不是TextView,跳过
			if (!element.getName().equals("TextView")) {
				continue;
			}

			// //////////// 处理textSize ////////////////
			Attribute attrTextSize = element.attribute("textSize");

			// TODO 另一种处理,没有textSize参数时不做setSize操作
			// if(attrTextSize == null) {
			// System.out.println("布局内第" + i + "个控件TextView未设置textSize");
			// continue;
			// }

			int indexOf = -1;
			if (attrTextSize == null) {
				// 没有textSize参数时,取默认的size
				indexOf = defaultIndex;
			} else {
				// 有textSize再去数据中获取索引

				int textSizeNum = -1;
				// 获取文字大小
				String textSize = attrTextSize.getValue()
						// 排除单位
						.replace("dp", "").replace("dip", "").replace("px", "")
						.replace("sp", "");
				try {
					textSizeNum = Integer.parseInt(textSize);
				} catch (Exception e) {
					textSizeNum = -1;
				}

				if (textSizeNum == -1) {
					System.out.println("布局内第" + i + "个控件TextView的textSize格式不对");
					continue;
				}

				indexOf = TEXT_SIZE_LIST.indexOf(textSizeNum);
				if (indexOf == -1) {
					// TODO 另一种处理,获取不到时不做setSize操作
					// System.out.println("布局内第" + i +
					// "个TextView的textSize不在TEXT_SIZE数组中");
					// continue;

					// 获取不到时,去默认索引位置的size
					indexOf = defaultIndex;
				}
			}

			// //////////// 处理id ////////////////
			Attribute attrID = element.attribute("id");

			String id = null;
			if (attrID == null) {
				// 如果没有id,且需要自动设置一个
				if (isAutoSetId) {
					id = tvId4SetSizePre + (tvId4SetSizeEnd++);
					element.addAttribute("android:id", id);
				}
			} else {
				// 如果已有id
				id = attrID.getValue();
			}

			if (id == null) {
				System.out.println("布局内第" + i + "个控件TextView没有id的不做处理");
				continue;
			}

			// 去除"@+id/"后的id名称
			String idName = id.replace("@+id/", "");

			tvIdNameBeans.add(new IdNamingBean(element.getName(), idName,
					element));

			// 例 tv_set_size1.setTextSize(Constant.TEXT_SIZE[0] *
			// TxtManager.getInstance().getTxtSize(this));
			String setSizeLine = idName + ".setTextSize(Constant.TEXT_SIZE["
					+ indexOf
					+ "] * TxtManager.getInstance().getTxtSize(this));";
			sbSetTextSize.append(AndroidUtils.formatSingleLine(2, setSizeLine));
		}

		// 如果要自动setId则将设置好id的xml写回布局layout_size_new.xml中
		if (isAutoSetId) {
			XmlUtil.write2xml(new File("temp" + File.separator + "SetSize"
					+ File.separator + "layout_size_new.xml"), document);
		}

		AndroidUtils.idNamingBeans = tvIdNameBeans;
		// 解析idNamingBeans集合中的信息,生成页面文本信息
		String activityContent = AndroidUtils.createActivityContent();

		// 封装到onResume里
		// @Override
		// protected void onResume() {
		// super.onResume();
		// tv_set_size1.setTextSize(Constant.TEXT_SIZE[0] *
		// TxtManager.getInstance().getTxtSize(context))
		// }
		sbSetTextSize.insert(0,
				AndroidUtils.formatSingleLine(2, "super.onResume();"));
		sbSetTextSize
				.insert(0, AndroidUtils.formatSingleLine(1,
						"protected void onResume() {"));
		sbSetTextSize.insert(0, AndroidUtils.formatSingleLine(1, "@Override"));
		sbSetTextSize.append(AndroidUtils.formatSingleLine(1, "}"));

		activityContent += sbSetTextSize.toString();

		FileUtils.writeString2File(activityContent, new File("temp"
				+ File.separator + "SetSize" + File.separator
				+ "Layout_Size.java"));

		System.out.println("--------------");
		System.out.println("代码生成正确,请在SetSize/Layout_Size.java中查看");
	}
	
	/**
	 * 替换map中所有匹配的key，替换成value
	 * 
	 * @param oldStr
	 * @param replaceMap
	 * @return
	 */
	public static String replaceAllMap(String oldStr, Map<String, String> replaceMap) {
		String newStr = oldStr;
		for(Map.Entry<String, String> entry : replaceMap.entrySet()) {
			newStr = newStr.replace(entry.getKey(), entry.getValue());
		}
		return newStr;
	}

	public static void replacePPT() {
		Map<String, String> replaceMap = new HashMap<String, String>();
//		replaceMap.put("实战名称", "微博实战：Android 视频播放");
//		replaceMap.put("知识点1", "我页面的实现");
		
		replaceMap.put("我页面的实现", "知识点1");
		replaceMap.put("个人中心页面的基本实现", "知识点2");
		replaceMap.put("个人中心页面菜单栏相关效果", "知识点3");
		replaceMap.put("个人中心页面背景图变化效果", "");
//		replaceMap.put("知识点1", "我页面的实现");
		// TODO replaceMaps.put(被替换内容, 替换内容);
		
		try {
			// 获取ppt文件
			FileInputStream is = new FileInputStream("temp"
					+ File.separator + "office" + File.separator
					+ "ppt2007.pptx");
			XMLSlideShow ppt = new XMLSlideShow(is);
			is.close();
			// 获取幻灯片
			for (XSLFSlide slide : ppt.getSlides()) {
				// 获取每一张幻灯片中的shape
				for (XSLFShape shape : slide.getShapes()) {
					// position on the canvas
					Rectangle2D anchor = shape.getAnchor();
					if (shape instanceof XSLFTextShape) {
						XSLFTextShape txShape = (XSLFTextShape) shape;
						// 获取其中的文字
						String text = txShape.getText();
						
						// 替换文字内容
						text = replaceAllMap(text, replaceMap);
						txShape.setText(text);
						
						if (text.contains("{picture}")) {
//							// 替换图片
//							byte[] pictureData = IOUtils.toByteArray(
//								new FileInputStream("E:\\33.png"));
//							int idx = ppt.addPicture(pictureData,
//									XSLFPictureData.PICTURE_TYPE_PNG);
//							XSLFPictureShape pic = slide.createPicture(idx);
//							// 设置XSLFPictureShape的位置信息
//							pic.setAnchor(anchor);
//							// 移除XSLFTextShape
//							slide.removeShape(txShape);
							System.out.println("替换图片");
						}
					} else if (shape instanceof XSLFGroupShape) {
						System.out.println("替换group");
						for (XSLFShape sunshape : ((XSLFGroupShape) shape).getShapes()) {
							XSLFTextShape txSunShape = (XSLFTextShape) sunshape;
							
							// 获取其中的文字
							String text = txSunShape.getText();
							
							// 替换文字内容
							text = replaceAllMap(text, replaceMap);
							txSunShape.setText(text);
							
//							if (text.contains("{picture}")) {
//								// 替换图片
//								byte[] pictureData = IOUtils
//										.toByteArray(new FileInputStream(
//												"E:\\33.png"));
//								int idx = ppt.addPicture(pictureData,
//										XSLFPictureData.PICTURE_TYPE_PNG);
//								XSLFPictureShape pic = slide.createPicture(idx);
//								slide.removeShape(txSunShape);
//								pic.setAnchor(anchor);
//							}
						}
					} else if (shape instanceof XSLFPictureShape) {
						System.out.println("替换picture");
						XSLFPictureShape pShape = (XSLFPictureShape) shape;
						XSLFPictureData pData = pShape.getPictureData();
						System.out.println(pData.getFileName());
					} else {
						System.out.println("Process me: " + shape.getClass());
					}
				}
			}

			File file = new File("temp" + File.separator + 
				"office" + File.separator + "ppt2007plus_new.pptx");
			if(!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream out = new FileOutputStream(file);
			ppt.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void setPPT() {

		try {
			PowerPointExtractor ppe = new PowerPointExtractor("temp"
					+ File.separator + "office" + File.separator
					+ "ppt2007.ppt");
			DocumentSummaryInformation dsi = ppe.getDocSummaryInformation();
			DirectoryEntry root = ppe.getRoot();

			System.out.println(dsi.getSlideCount());
			System.out.println(root.getName());

		} catch (Exception e) {
			e.printStackTrace();
		}

		// SlideShow _slideShow = new SlideShow();
		// Slide slide = _slideShow.createSlide();
		//
		// // 创建并置入简单文本
		// TextBox _text = new TextBox();
		// TextRun _textRun = _text.createTextRun();
		// _textRun.setRawText("杜磊米");
		// _text.setAnchor(new Rectangle(10,10,100,100));
		//
		// // 创建并置入带有样式的文本
		// AutoShape _autoShape = new AutoShape(ShapeTypes.Rectangle); //设置形状
		// TextRun _autoText = _autoShape.createTextRun();
		// _autoText.setRawText("杜磊米");
		// _autoShape.setAnchor(new Rectangle(200,200,100,100));
		// _autoShape.setFillColor(new Color(170,215,255));
		// _autoShape.setLineWidth(5.0);
		// _autoShape.setLineStyle(Line.LINE_DOUBLE);
		//
		// // AutoShape 对象可以设置多个不同样式文本
		// TextRun _autoText2 = _autoShape.createTextRun();
		// RichTextRun _richText = _autoText2.appendText("杜");
		// _richText.setFontColor(new Color(255,255,255));
		// RichTextRun _richText2 = _autoText2.appendText("磊米");
		// _richText2.setFontColor(new Color(255,0,0));
		// _richText2.setFontSize(12);
		//
		// // 将文本对象置入幻灯片
		// slide.addShape(_text);
		// slide.addShape(_autoShape);
		//
		//
		//
		// // 输出文件
		// try {
		// _slideShow.write(new FileOutputStream("temp\\office\\test.pptx"));
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
	
	public static void exportXml() {
		// save to
		File file = new File(
				"D:\\work\\BusinessCMT2.0\\res\\values\\strings.xml");
		Document valuesDoc = XmlUtil.read(file);
		Element rootElement = valuesDoc.getRootElement();

		String regexChinese = "[\u4e00-\u9fa5]+";
		Pattern patternChiese = Pattern.compile(regexChinese);
		
		List<Element> elements = rootElement.elements();
		List<String> values = new ArrayList<String>();
		for (Element e : elements) {
			String text = e.getText();
			Matcher matcher = patternChiese.matcher(text);
			if(matcher.find() && !values.contains(text)) {
				values.add(text);
				System.out.println(text);
			}
		}
	}
	
	public static void extractAllString() {
		List<File> files = FileUtils.getAllFiles("D:\\work\\BusinessCMT2.0");
//		List<File> files = FileUtils.getAllFiles("D:\\work\\BusinessCMT2.0\\src\\com\\imohoo\\BusinessCMT\\view\\activity\\pay");
		
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

						totoalLine ++;
						
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
						if(line.contains("extends") && line.contains("Activity")) {
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
							if(!matcherChinese.find()) {
								continue;
							}
							
							// TODO 如果是日志内容,跳过
							if(line.trim().startsWith("Log") || line.trim().startsWith("showLog")) {
								continue;
							}
							
							if(file.getName().endsWith(".java")) {
								// id规则,文件名小写_递增数字
								String stringId = FileUtils.getName(file.getName().toLowerCase(Locale.CHINA)) + "_" + stringIdEnd++;
								
								if(isActivity) {
									// 如果是页面类,直接getResource
									line = line.replace("\"" + chinese + "\"", "getResources().getString(R.string."+stringId+")");
								} else {
									// 非页面类,用application去getResource
									line = line.replace("\"" + chinese + "\"", "BusinessCMTApplication.getInstance().getResources().getString(R.string."+stringId+")");
								}
								stringIdValueMap.put(stringId, chinese);
								isReplace = true;
							} else if(file.getName().endsWith(".xml")) {
								// id规则,文件名小写_递增数字
								String stringId = FileUtils.getName(file.getName().toLowerCase(Locale.CHINA)) + "_" + stringIdEnd++;
								// xml用@string/blabla替换,注意,不替换""
								line = line.replace(chinese, "@string/"+stringId);
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
				
				if(isReplace) {
					// TODO 写入回文件,如果有替换操作
//					FileUtils.writeString2File(newFileContent.toString(), file, "UTF-8");
				}
			}

		}
		
		System.out.println(stringIdValueMap);
		
		// save to
		File file = new File("D:\\work\\BusinessCMT2.0\\res\\values\\string.xml");
		Document valuesDoc = XmlUtil.read(file);
		Element rootElement = valuesDoc.getRootElement();
		
		List<Element> elements = rootElement.elements();
		for(Map.Entry<String, String> entry : stringIdValueMap.entrySet()) {
			// 是否在values/xx.xml对应文件下下已有某个抽取过的值
			boolean hasElement = false;
			
			for(Element element : elements) {
				Attribute nameAtt = element.attribute("name");
				if(nameAtt.getValue().equals(entry.getKey())) {
					hasElement = true;
					break;
				}
			}
			
			if(!hasElement) {
	//			<string name="app_name">Stone Chat</string>
				Element element = rootElement.addElement("string");
				element.addAttribute("name", entry.getKey());
				element.setText(entry.getValue());
			}
		}
		// TODO save string文件
//		XmlUtil.write2xml(file, valuesDoc);
	}
	
	
	public static void desParams() {
//		String beanPath1 = "D:\\work\\BusinessCMT2.0\\src\\com\\imohoo\\BusinessCMT\\model";
		String beanPath = "D:\\work\\BusinessCMT2.0\\src\\com\\imohoo\\BusinessCMT\\db\\table";
		
		List<File> allFiles = FileUtils.getAllFiles(beanPath);
		for(File file : allFiles) {
			
			String content = FileUtils.readToString(file, "UTF-8");
			if(!content.contains("DatabaseTable")) {
				continue;
			}

			List<String> paramList = new ArrayList<String>();
			
//			System.out.println(file.getName() + " ----------------------------");
			FileReader fr;
			
			try {
				fr = new FileReader(file);
				BufferedReader bufferedreader = new BufferedReader(fr);
				String line;
				while ((line=bufferedreader.readLine()) != null) {
					if(line.contains("public void set") && line.contains("String")) {
						int lastIndex = line.lastIndexOf(")");
						int beforeIndex = line.indexOf("(String") + 8;
						String param = line.substring(beforeIndex, lastIndex);
//						System.out.println(param);
						paramList.add(param);
					}
				}
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			System.out.println(paramList);
//			System.out.println();
			
			if(paramList.size() > 0) {
				for(String param : paramList) {
					// setter
					// this.name = name;
					String oldSetter = "this." + param + " = " + param + ";";
					// this.name = Util.encryptDES(name);
					String newSetter = "this." + param + " = " + "Util.encryptDES(" + param + ");";
					if(content.contains(oldSetter)) {
						content = content.replace(oldSetter, newSetter);
					} else {
						System.out.println(param + " 的setter方法格式不对");
					}
					
					// getter
					// return name;
					String oldGetter = "return " + param + ";";
					// return Util.decryptDES(name);
					String newGetter = "return " + "Util.decryptDES(" + param + ");";
					if(content.contains(oldGetter)) {
						content = content.replace(oldGetter, newGetter);
					} else {
						System.out.println(param + " 的getter方法格式不对");
					}
				}
				
				FileUtils.writeString2File(content, file, "UTF-8");
			}
		}
		
		
	}
}
