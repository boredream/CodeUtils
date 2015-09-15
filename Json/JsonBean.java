public class JsonBeans {
	private Data data;
	private String message;
	private int status;

	/*sub class*/
	public class Data {
		private String role;
		private int lack_exp;
		private int level;
		private boolean is_vip;
		private int integral;
	}
}