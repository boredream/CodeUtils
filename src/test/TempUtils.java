package test;

import entity.IdNamingBean;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.xslf.usermodel.*;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import utils.AndroidUtils;
import utils.FileUtils;
import utils.StringUtils;
import utils.XmlUtil;

import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TempUtils {

	public static void main(String[] args) throws Exception {

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
	 *    是否自动给没有id的TextView添加id
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
			sbSetTextSize.append(StringUtils.formatSingleLine(2, setSizeLine));
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
				StringUtils.formatSingleLine(2, "super.onResume();"));
		sbSetTextSize
				.insert(0, StringUtils.formatSingleLine(1,
						"protected void onResume() {"));
		sbSetTextSize.insert(0, StringUtils.formatSingleLine(1, "@Override"));
		sbSetTextSize.append(StringUtils.formatSingleLine(1, "}"));

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
		for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
			newStr = newStr.replace(entry.getKey(), entry.getValue());
		}
		return newStr;
	}

	public static void replacePPT() {
		Map<String, String> replaceMap = new HashMap<String, String>();
		// replaceMap.put("实战名称", "微博实战：Android 视频播放");
		// replaceMap.put("知识点1", "我页面的实现");

		replaceMap.put("我页面的实现", "知识点1");
		replaceMap.put("个人中心页面的基本实现", "知识点2");
		replaceMap.put("个人中心页面菜单栏相关效果", "知识点3");
		replaceMap.put("个人中心页面背景图变化效果", "");
		// replaceMap.put("知识点1", "我页面的实现");
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
							// // 替换图片
							// byte[] pictureData = IOUtils.toByteArray(
							// new FileInputStream("E:\\33.png"));
							// int idx = ppt.addPicture(pictureData,
							// XSLFPictureData.PICTURE_TYPE_PNG);
							// XSLFPictureShape pic = slide.createPicture(idx);
							// // 设置XSLFPictureShape的位置信息
							// pic.setAnchor(anchor);
							// // 移除XSLFTextShape
							// slide.removeShape(txShape);
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

							// if (text.contains("{picture}")) {
							// // 替换图片
							// byte[] pictureData = IOUtils
							// .toByteArray(new FileInputStream(
							// "E:\\33.png"));
							// int idx = ppt.addPicture(pictureData,
							// XSLFPictureData.PICTURE_TYPE_PNG);
							// XSLFPictureShape pic = slide.createPicture(idx);
							// slide.removeShape(txSunShape);
							// pic.setAnchor(anchor);
							// }
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
			if (!file.exists()) {
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
				"D:\\workproject\\businessCMT\\src\\main\\res\\values\\strings.xml");
		Document valuesDoc = XmlUtil.read(file);
		Element rootElement = valuesDoc.getRootElement();

		String regexChinese = "[\u4e00-\u9fa5]+";
		Pattern patternChiese = Pattern.compile(regexChinese);

		List<Element> elements = rootElement.elements();
		List<String> values = new ArrayList<String>();
		for (Element e : elements) {
			String text = e.getText();
			Matcher matcher = patternChiese.matcher(text);
			if (matcher.find() && !values.contains(text)) {
				values.add(text);
				System.out.println(text);
			}
		}
	}

	public static void desParams() {
		// String beanPath1 =
		// "D:\\work\\BusinessCMT2.0\\src\\com\\imohoo\\BusinessCMT\\model";
		String beanPath = "D:\\work\\BusinessCMT2.0\\src\\com\\imohoo\\BusinessCMT\\db\\table";

		List<File> allFiles = FileUtils.getAllFiles(beanPath);
		for (File file : allFiles) {

			String content = FileUtils.readToString(file, "UTF-8");
			if (!content.contains("DatabaseTable")) {
				continue;
			}

			List<String> paramList = new ArrayList<String>();

			// System.out.println(file.getName() +
			// " ----------------------------");
			FileReader fr;

			try {
				fr = new FileReader(file);
				BufferedReader bufferedreader = new BufferedReader(fr);
				String line;
				while ((line = bufferedreader.readLine()) != null) {
					if (line.contains("public void set") && line.contains("String")) {
						int lastIndex = line.lastIndexOf(")");
						int beforeIndex = line.indexOf("(String") + 8;
						String param = line.substring(beforeIndex, lastIndex);
						// System.out.println(param);
						paramList.add(param);
					}
				}
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// System.out.println(paramList);
			// System.out.println();

			if (paramList.size() > 0) {
				for (String param : paramList) {
					// setter
					// this.path = path;
					String oldSetter = "this." + param + " = " + param + ";";
					// this.path = Util.encryptDES(path);
					String newSetter = "this." + param + " = " + "Util.encryptDES(" + param + ");";
					if (content.contains(oldSetter)) {
						content = content.replace(oldSetter, newSetter);
					} else {
						System.out.println(param + " 的setter方法格式不对");
					}

					// getter
					// return path;
					String oldGetter = "return " + param + ";";
					// return Util.decryptDES(path);
					String newGetter = "return " + "Util.decryptDES(" + param + ");";
					if (content.contains(oldGetter)) {
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
