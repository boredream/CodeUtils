package entity;

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

		@Override
		public String toString() {
			return "ClassField [name=" + name + "]";
		}

	}

}
