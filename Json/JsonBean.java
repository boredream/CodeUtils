public class JsonBeans {
	private String status;
	private Return return;

	/*sub class*/
	public class Return {
		private M1 m1;
		private M2 m2;
	}

	/*sub class*/
	public class M1 {
		private int age;
		private Date date;
		private int id;
		private String name;
	}

	/*sub class*/
	public class Date {
		private int date;
		private int day;
		private int hours;
		private int minutes;
		private int month;
		private int seconds;
		private long time;
		private int timezoneOffset;
		private int year;
		private int date;
		private int day;
		private int hours;
		private int minutes;
		private int month;
		private int seconds;
		private long time;
		private int timezoneOffset;
		private int year;
	}

	/*sub class*/
	public class M2 {
		private int age;
		private Date date;
		private int id;
		private String name;
	}
}