package reptile.bcgm;

public class CaoMain {
	private String type;
	private String name;
	private String href;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public String toString() {
		return "Cao [type=" + type + ", path=" + name + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CaoMain) {
			CaoMain cm = (CaoMain) obj;
			if (this.name != null && cm.name != null) {
				return this.name.equals(cm.name);
			}
		} else if (obj instanceof CaoImg) {
			CaoImg ci = (CaoImg) obj;
			if (this.name != null && ci.getName() != null) {
				return this.name.equals(ci.getName());
			}
		}
		return super.equals(obj);
	}
}