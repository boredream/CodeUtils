public class JsonBeans {
	private String desc;
	private long status;
	private Emptyobj emptyobj;
	private Object nulldata;
	private ArrayList<ArrayList<String>> llist;
	private Data data;

	/*sub class*/
	public class Emptyobj {
	}

	/*sub class*/
	public class Data {
		private String wendu;
		private String ganmao;
		private ArrayList<Forecast> forecast;
		private Yesterday yesterday;
		private String aqi;
		private String city;
	}

	/*sub class*/
	public class Forecast {
		private String fengxiang;
		private String fengli;
		private String high;
		private String type;
		private String low;
		private String date;
	}

	/*sub class*/
	public class Yesterday {
		private String fl;
		private String fx;
		private String high;
		private String type;
		private String low;
		private String date;
	}
}