package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import entity.ArrayType;
import entity.Json2JavaElement;

public class JsonUtils {
	
	/**
	 * 将json字符串拷贝至 /Json/JsonString.txt 文件中去,然后调用该方法,<br>
	 * 就会在/Json/JsonString.txt中生成一个对应的JavaBean类<br><br>
	 * 注意: 如果json字符串中有null或者空数组[]这种无法判断类型的,会统一使用Object类型
	 */
	public static void parseJson2Java() {
		String string = FileUtils.readToString(new File("Json\\JsonString.txt"), "UTF-8");
		// 解析获取整个json结构
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(string);
		JsonObject jo = element.getAsJsonObject();
		List<Json2JavaElement> jsonBeanTree = getJsonBeanTree(jo);
		// 利用获取到的json结构创建对应的javabean文件内容
		String javaBeanStr = createJavaBean(jsonBeanTree);
		// 将生成的内容写入到文件中去
		FileUtils.writeString2File(javaBeanStr, new File("Json\\JsonBean.java"));
	}
	
	private static String createJavaBean(List<Json2JavaElement> jsonBeanTree) {
		StringBuilder sb = new StringBuilder();
		boolean hasCustomeClass = false;
		List<String> customClassNames = new ArrayList<String>();
		
		sb.append("public class JsonBeans {\n");
		
		Iterator<Json2JavaElement> iterator = jsonBeanTree.iterator();
		while(iterator.hasNext()) {
			Json2JavaElement jb = iterator.next();
			
			if(jb.getCustomClassName() != null && !customClassNames.contains(jb.getCustomClassName())) {
				customClassNames.add(jb.getCustomClassName());
			}
			
			if(jb.getParentJb() != null) {
				hasCustomeClass = true;
				continue;
			}
			
			// 第一轮只设置根变量
			sb.append("\tprivate ")
				.append(getTypeName(jb))
				.append(" ")
				.append(jb.getName())
				.append(";\n");
			// 移除根变量,方便后续循环设置自定义类,可以减少后续循环次数
			iterator.remove();
		}
		
		// 设置所有自定义类
		if(hasCustomeClass) {
			for(String customClassName : customClassNames) {
				sb.append("\n\t/*sub class*/\n");
				sb.append("\tpublic class ")
					.append(customClassName)
					.append(" {\n");
				Iterator<Json2JavaElement> customIterator = jsonBeanTree.iterator();
				while(customIterator.hasNext()) {
					Json2JavaElement jb = customIterator.next();
					
					if(!StringUtils.firstToUpperCase(jb.getParentJb().getName()).equals(customClassName)) {
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
		return sb.toString();
	}
	
	private static List<Json2JavaElement> getJsonBeanTree(JsonObject rootJo) {
		jsonBeans = new ArrayList<Json2JavaElement>();
		recursionJson(rootJo, null);
		return jsonBeans;
	}
	
	private static List<Json2JavaElement> jsonBeans = new ArrayList<Json2JavaElement>(); 
	private static void recursionJson(JsonObject jo, Json2JavaElement parentJb) {
		for (Entry<String, JsonElement> entry : jo.entrySet()) {
			String name = entry.getKey();
			JsonElement je = entry.getValue();
			
			Json2JavaElement jb = new Json2JavaElement();
			Class<?> type = getJsonType(je);
			
			jb.setName(name);
			if(parentJb != null) {
				jb.setParentJb(parentJb);
			}
			
			// 自定义类
			if(type == null) {
				jb.setCustomClassName(StringUtils.firstToUpperCase(name));
				jb.setSouceJo(je.getAsJsonObject());
				jsonBeans.add(jb);
				// 自定义类, 递归
				recursionJson(je.getAsJsonObject(), jb);
			} else if(type.equals(JsonArray.class)) {
				deepLevel = 0;
				arrayType = new ArrayType();
				getJsonArrayType(je.getAsJsonArray());
				
				jb.setArray(true);
				jb.setArrayDeep(deepLevel);
				
				if(arrayType.getJo() != null) {
					jb.setCustomClassName(StringUtils.firstToUpperCase(name));
					// 数组内的末点元素类型为自定义类, 递归
					recursionJson(arrayType.getJo(), jb);
				} else {
					jb.setType(arrayType.getType());
				}
				jsonBeans.add(jb);
			} else {
				jb.setType(type);
				jsonBeans.add(jb);
			}
		}
	}
	
	private static int deepLevel = 0;
	private static ArrayType arrayType = new ArrayType();
	private static void getJsonArrayType(JsonArray jsonArray) {
		deepLevel ++;
		// 如果数组为空,则数组内元素类型直接设为Object
		if (jsonArray.size() == 0) {
			arrayType.setArrayDeep(deepLevel);
			arrayType.setType(Object.class);
		} else {
			// 非空则取出第一个元素
			JsonElement childJe = jsonArray.get(0);
			Class<?> type = getJsonType(childJe);
			
			if(type == null) {
				arrayType.setJo(childJe.getAsJsonObject());
				arrayType.setArrayDeep(deepLevel);
			} else if (type.equals(JsonArray.class)) {
				// recursion 如果数组里面还是数组,则递归本方法
				getJsonArrayType(childJe.getAsJsonArray());
				return;
			} else {
				arrayType.setArrayDeep(deepLevel);
				arrayType.setType(type);
			}
		}
	}
	
	private static Class<?> getJsonType(JsonElement je) {
		Class<?> clazz = null;
		
		if(je.isJsonNull()) {
			// type视为object类型
			clazz = Object.class;
		} else if(je.isJsonPrimitive()) {
			// primitive类型可能是数字或者是布尔型
			clazz = getJsonPrimitiveType(je);
		} else if(je.isJsonObject()) {
			// 自定义类型参数,返回null
			clazz = null;
		} else if(je.isJsonArray()) {
			clazz = JsonArray.class;
		}
		return clazz;
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

		StringBuilder sb = new StringBuilder();
		for(int i=0; i<jb.getArrayDeep(); i++) {
			sb.append("ArrayList<");
		}
		sb.append(name);
		for(int i=0; i<jb.getArrayDeep(); i++) {
			sb.append(">");
		}
		return sb.toString();
	}
}
