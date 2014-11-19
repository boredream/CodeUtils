package test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Level;

import utils.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;



public class Main {

//	private static String proPath = "E:\\workspace\\ConvenientPos";
	private static String activityPath = "E:\\workspace\\PosTest\\src\\com\\example\\postest\\TestActivity.java";
	private static String layoutPath = "E:\\workspace\\PosTest\\res\\layout\\test.xml";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		List<File> allFiles = FileUtils.getAllFiles(proPath);
//		for(File file : allFiles) {
//			if(file.getName().endsWith(".xml")) {
//				Document document = XmlUtil.read(file);
//				boolean replace = XmlUtil.replacePartAttrValue(document, ":", "：", "textLeft");
//				if(replace) {
//					XmlUtil.write2xml(file, document);
//				}
//			}
//		}
		
//		AndroidUtils.delNoUseSrcFile(proPath);
//		
//		List<File> allFiles = FileUtils.getAllFiles(proPath);
//		for(File file : allFiles) {
//			String string = FileUtils.readToString(file);
//			if(string.contains("croller.fling(")) {
//				System.out.println(file.getAbsoluteFile());
//			}
//		}
		
		
		ArrayList<String> string = FileUtils.readToStringLines(new File("Json\\JsonStrings.txt"));
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(string.get(2));
		JsonObject jo = element.getAsJsonObject();
		List<Json2JavaElement> jsonBeanTree = getJsonBeanTree(jo);
//		
////		System.out.println(jsonBeanTree);
//		createJavaBean(jsonBeanTree);
	}
	
	private static void createJavaBean(List<Json2JavaElement> jsonBeanTree) {
		StringBuilder sb = new StringBuilder();
		boolean hasCustomeClass = false;
		
		sb.append("public class JsonBeans {\n");
		
		Iterator<Json2JavaElement> iterator = jsonBeanTree.iterator();
		while(iterator.hasNext()) {
			Json2JavaElement jb = iterator.next();
			if(jb.getLevel() > 0) {
				hasCustomeClass = true;
				continue;
			}
			sb.append("\tprivate ")
				.append(getTypeName(jb))
				.append(" ")
				.append(jb.getName())
				.append(";\n");
			iterator.remove();
		}
		
		if(hasCustomeClass) {
			while(jsonBeanTree.size() > 0) {
				Json2JavaElement parentJb = jsonBeanTree.get(0).getParentJb();
				String customClassName = parentJb.getCustomClassName();
				if(customClassName == null) {
					throw new RuntimeException("root element didn't oprate");
				}
				
				sb.append("\n\t/*sub class*/class ")
					.append(customClassName)
					.append(" {\n");
				Iterator<Json2JavaElement> customIterator = jsonBeanTree.iterator();
				while(customIterator.hasNext()) {
					Json2JavaElement jb = customIterator.next();
					
//					System.out.println(jb.getName() + " ... " + jb.getParentJb().getName() + " = " + customClassName);
					
					if(!firstToUpperCase(jb.getParentJb().getName()).equals(customClassName)) {
						continue;
					}
					sb.append("\t\tprivate ")
						.append(getTypeName(jb))
						.append(" ")
						.append(jb.getName())
						.append(";\n");
					customIterator.remove();
				}
				sb.append("\t}\n");
			}
		}
		
		sb.append("}");
		FileUtils.writeString2File(sb.toString(), new File("Json\\JsonBeans.java"));
	}
	
	// temp
	private static HashMap<String, List<Json2JavaElement>> JsonBeanMap = new HashMap<String, List<Json2JavaElement>>();
	private static List<Json2JavaElement> getJsonBeanTree(JsonObject rootJo) {
		level = -1;
		jsonBeans = new ArrayList<Json2JavaElement>();
		recursionJson(rootJo, null);
		return jsonBeans;
	}
	
	private static int level = -1;
	private static List<Json2JavaElement> jsonBeans = new ArrayList<Json2JavaElement>(); 
	private static void recursionJson(JsonObject jo, Json2JavaElement parentJb) {
		level ++;
		for (Entry<String, JsonElement> entry : jo.entrySet()) {
			Json2JavaElement jb = getJsonType(entry);
			
			jb.setLevel(level);
			if(parentJb != null) {
				jb.setParentJb(parentJb);
			}
			
			// 如果是集合类型
			if(jb.isArray()) {
				JsonElement arrayItemJe = jb.getArrayItemJe();
				if(arrayItemJe.isJsonObject()) {
					// 集合里面是自定义类型
					recursionJson(arrayItemJe.getAsJsonObject(), jb);
					return;
				} else {
					
				}
			}
			
			// 如果jo非空,代表类型是自定义类型
			if(jb.getSouceJo() != null) {
				jsonBeans.add(jb);
				recursionJson(jb.getSouceJo(), jb);
				return;
			}
			
			jsonBeans.add(jb);
		}
	}
	
	private static Json2JavaElement getJsonType(Entry<String, JsonElement> entry) {
		Json2JavaElement jb = new Json2JavaElement();
		
		String key = entry.getKey();
		JsonElement je = entry.getValue();
		
		Class<?> clazz = null;
		if(je.isJsonNull()) {
			// type视为object类型
			clazz = Object.class;
		} else if(je.isJsonPrimitive()) {
			// primitive类型可能是数字或者是布尔型
			clazz = getJsonPrimitiveType(je);
		} else if(je.isJsonObject()) {
			// 自定义类型参数,使用key作为类名(开头字母换位大写)
			String className = firstToUpperCase(key);
			jb.setCustomClassName(className);
			
			JsonObject souceJo = je.getAsJsonObject();
			jb.setSouceJo(souceJo);
		} else if(je.isJsonArray()) {
			JsonArray ja = je.getAsJsonArray();
			jb.setArray(true);
			if(ja.size() == 0) {
				// 集合内元素的type视为object类型
				clazz = Object.class;
			} else {
				JsonElement childJe = ja.get(0);
				jb.setArrayItemJe(childJe);
			}
		}
		
		jb.setName(key);
		jb.setType(clazz);
		
		return jb;
	}

	private static String firstToUpperCase(String key) {
		return key.substring(0, 1).toUpperCase(Locale.CHINA) + key.substring(1);
	}
	
	private static Class<?> getJsonPrimitiveType(JsonElement je) {
		Class<?> clazz = Object.class;
		JsonPrimitive jp = je.getAsJsonPrimitive();
		if(jp.isNumber()) {
			String num = jp.getAsString();
			if(num.contains(".")) {
				try {
					Float.parseFloat(num);
					clazz = float.class;
				} catch(NumberFormatException e) {
					clazz = double.class;
				}
			} else {
				try {
					Integer.parseInt(num);
					clazz = int.class;
				} catch(NumberFormatException e) {
					clazz = long.class;
				}
			}
		} else if(jp.isBoolean()) {
			clazz = boolean.class;
		} else if(jp.isString()) {
			clazz = String.class;
		}
		return clazz;
	}
	
	private static String getTypeName(Json2JavaElement jb) {
		// 无法获取类型时,默认Object
		String name = "Object";
		
		Class<?> type = jb.getType();
		if(jb.getCustomClassName() != null && jb.getCustomClassName().length() > 0) {
			// 自定义类,类型名称直接用自定义的名称customClassName
			name = jb.getCustomClassName();
		} else {
			// 非自定义类即可以获取类型,则解析类型class名称
			name = type.getName();
			int lastIndexOf = name.lastIndexOf(".");
			if(lastIndexOf != -1) {
				name = name.substring(lastIndexOf + 1);
			}
		}
		return name;
	}

}