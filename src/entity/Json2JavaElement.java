package entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 转换数据元素
 */
public class Json2JavaElement {

	/**
	 * 是否为集合类型
	 * 
	 * <p>
	 * 如果是集合的话,集合内数据类型为customClassName对应的自定义类,或者type
	 */
	private boolean isArray;
	
	/**
	 * 集合数据
	 */
	private JsonElement arrayItemJe;
	
	/**
	 * 集合深度,如果是3则为ArrayList<ArrayList<ArrayList<>>>
	 */
	private int arrayDeep;

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
	
	/**
	 * 注释,null时不添加注释
	 */
	private String des;

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

	public int getArrayDeep() {
		return arrayDeep;
	}

	public void setArrayDeep(int arrayDeep) {
		this.arrayDeep = arrayDeep;
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
	
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	@Override
	public String toString() {
		return "\n"
				+ "Json2JavaElement [isArray=" + isArray
				+ ", arrayDeep=" + arrayDeep + ", path=" + name + ", type="
				+ type + "]";
	}

}
