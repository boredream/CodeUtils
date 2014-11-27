package entity;

import com.google.gson.JsonObject;

public class ArrayType {
	/**
	 * 类型为数组中泛型类型
	 */
	private Class<?> type;
	/**
	 * 如果数组泛型为自定义类型,用此参数保存数据
	 */
	private JsonObject jo;
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

	public JsonObject getJo() {
		return jo;
	}

	public void setJo(JsonObject jo) {
		this.jo = jo;
	}

	public int getArrayDeep() {
		return arrayDeep;
	}

	public void setArrayDeep(int arrayDeep) {
		this.arrayDeep = arrayDeep;
	}

}
