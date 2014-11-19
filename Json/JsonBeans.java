public class JsonBeans {
	private String desc;
	private long status;
	private Data data;

	/*sub class*/class Data {
		private String wendu;
		private String ganmao;
		private Forecast forecast;
	}

	/*sub class*/class Forecast {
		private String fengxiang;
		private String fengli;
		private String high;
		private String type;
		private String low;
		private String date;
	}
}