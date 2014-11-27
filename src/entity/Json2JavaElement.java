package entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Json2JavaElement {

	private int level;
	/**
	 * 是否为数组类型
	 * 
	 * <p>
	 * 如果是数组的话,数组内数据类型为customClassName对应的自定义类,或者type
	 */
	private boolean isArray;
	private JsonElement arrayItemJe;
	/**
	 * 数组深度,
	 */
	private int arrayDeep;

	public int getArrayDeep() {
		return arrayDeep;
	}

	public void setArrayDeep(int arrayDeep) {
		this.arrayDeep = arrayDeep;
	}

	/**
	 * 自定义类名
	 * 
	 * <p>
	 * 非空时代表是自定义类,此时不使用type参数(customClassName和type只能二选一,互斥关系)
	 */
	private String customClassName;
	private JsonObject souceJo;
	private Json2JavaElement parentJb;

	private String name;
	private Class<?> type;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isArray() {
		return isArray;
	}

	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}

	public JsonElement getArrayItemJe() {
		return arrayItemJe;
	}

	public void setArrayItemJe(JsonElement arrayItemJe) {
		this.arrayItemJe = arrayItemJe;
	}

	public String getCustomClassName() {
		return customClassName;
	}

	public void setCustomClassName(String customClassName) {
		this.customClassName = customClassName;
	}

	public JsonObject getSouceJo() {
		return souceJo;
	}

	public void setSouceJo(JsonObject souceJo) {
		this.souceJo = souceJo;
	}

	public Json2JavaElement getParentJb() {
		return parentJb;
	}

	public void setParentJb(Json2JavaElement parentJb) {
		this.parentJb = parentJb;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "\n"
				+ "Json2JavaElement [level=" + level + ", isArray=" + isArray
				+ ", arrayDeep=" + arrayDeep + ", name=" + name + ", type="
				+ type + "]";
	}

}
