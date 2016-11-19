package entity;

import utils.StringUtils;

import java.util.List;

public class ClassInfo {
	public String annotation;
	public String className;
	public List<ClassField> fields;

	public static class ClassField {
		public String annotation;
		public String scope;
		public String type;
		public String name;
		public boolean isWhere;
		public boolean isPrimaryKey;

		public String getGetMethod() {
			if(name.startsWith("is")) {
				return name + "()";
			} else {
				return "get" + StringUtils.firstToUpperCase(name) + "()";
			}
		}

		public String getSetMethod() {
			if(name.startsWith("is")) {
				return name.replaceFirst("is", "set") + "(%s)";
			} else {
				return "set" + StringUtils.firstToUpperCase(name) + "(%s)";
			}
		}

		@Override
		public String toString() {
			return "ClassField [name=" + name + "]";
		}

	}

}
