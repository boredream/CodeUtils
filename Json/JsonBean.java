public class JsonBeans {
	private Data data;
	private String message;
	private int status;

	/*sub class*/
	public class Data {
		private String order_number;
		private int status;
		private String pay_time;
	}
}