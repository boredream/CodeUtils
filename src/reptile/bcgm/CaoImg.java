package reptile.bcgm;


public class CaoImg {
	private String name;
	private String img;
	private String otherName;
	private String intro;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	@Override
	public String toString() {
		return "CaoImg [path=" + name + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CaoImg) {
			CaoImg ci = (CaoImg) obj;
			if (this.name != null && ci.name != null) {
				return this.name.equals(ci.name);
			}
		}
		return super.equals(obj);
	}
}