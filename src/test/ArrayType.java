package test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ArrayType {
	/**
	 * 类型为数组中泛型类型
	 */
	private Class<?> type;
	/**
	 * 如果数组泛型为自定义类型,则用customClassName
	 */
	public JsonObject jo;
	/**
	 * 数组深度,如果是3则为ArrayList<ArrayList<ArrayList<>>>
	 */
	private int arrayDeep;
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	
	public int getArrayDeep() {
		return arrayDeep;
	}
	public void setArrayDeep(int arrayDeep) {
		this.arrayDeep = arrayDeep;
	}
	
	
	
}
