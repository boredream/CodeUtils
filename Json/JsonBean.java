<<<<<<< Updated upstream

	private int errNum;
	private String errMsg;
	private RetData retData;

	public static class RetData {
		private String city;
		private String pinyin;
		private String citycode;
		private String date;
		private String time;
		private String postCode;
		private float longitude;
		private float latitude;
		private String altitude;
		private String weather;
		private String temp;
		private String l_tmp;
		private String h_tmp;
		private String WD;
		private String WS;
		private String sunrise;
		private String sunset;

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getPinyin() {
			return pinyin;
		}

		public void setPinyin(String pinyin) {
			this.pinyin = pinyin;
		}

		public String getCitycode() {
			return citycode;
		}

		public void setCitycode(String citycode) {
			this.citycode = citycode;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getPostCode() {
			return postCode;
		}

		public void setPostCode(String postCode) {
			this.postCode = postCode;
		}

		public float getLongitude() {
			return longitude;
		}

		public void setLongitude(float longitude) {
			this.longitude = longitude;
		}

		public float getLatitude() {
			return latitude;
		}

		public void setLatitude(float latitude) {
			this.latitude = latitude;
		}

		public String getAltitude() {
			return altitude;
		}

		public void setAltitude(String altitude) {
			this.altitude = altitude;
		}

		public String getWeather() {
			return weather;
		}

		public void setWeather(String weather) {
			this.weather = weather;
		}

		public String getTemp() {
			return temp;
		}

		public void setTemp(String temp) {
			this.temp = temp;
		}

		public String getL_tmp() {
			return l_tmp;
		}

		public void setL_tmp(String l_tmp) {
			this.l_tmp = l_tmp;
		}

		public String getH_tmp() {
			return h_tmp;
		}

		public void setH_tmp(String h_tmp) {
			this.h_tmp = h_tmp;
		}

		public String getWD() {
			return WD;
		}

		public void setWD(String WD) {
			this.WD = WD;
		}

		public String getWS() {
			return WS;
		}

		public void setWS(String WS) {
			this.WS = WS;
		}

		public String getSunrise() {
			return sunrise;
		}

		public void setSunrise(String sunrise) {
			this.sunrise = sunrise;
		}

		public String getSunset() {
			return sunset;
		}

		public void setSunset(String sunset) {
			this.sunset = sunset;
		}
	}

	public int getErrNum() {
		return errNum;
=======
public class JsonBeans {
	private ArrayList<Content> content;
	private Object sort;
	private int numberOfElements;
	private int totalElements;
	private boolean firstPage;
	private boolean lastPage;
	private int totalPages;
	private int size;
	private int number;

	/*sub class*/
	public class Content {
		private long id;
		private long sharingId;
		private String sharingContent;
		private Object sharingImage;
		private Object sharingImageUrl;
		private boolean sharingDeleted;
		private String commentUserName;
		private String commentPortraitPic;
		private String commentPortraitPicUrl;
		private String commentToUserName;
		private String commentType;
		private boolean commentDeleted;
		private String content;
		private boolean looked;
		private String createTime;
>>>>>>> Stashed changes
	}

	public void setErrNum(int errNum) {
		this.errNum = errNum;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public RetData getRetData() {
		return retData;
	}

	public void setRetData(RetData retData) {
		this.retData = retData;
	}

