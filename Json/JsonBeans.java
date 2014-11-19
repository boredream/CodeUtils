public class JsonBeans {
	private String ret;
	private String ret_msg;
	private ArrayList<Order_infos> order_infos;

	/*sub class*/
	public class Z_list {
		private int IS_INSURANCE;
		private String CONTACT_NAME;
		private String IDCARDS;
		private int PAY_STATUS;
		private int SALE_PRICE;
	}

	/*sub class*/
	public class Order_infos {
		private int RECEIPT_FLAG;
		private String CLASS_NAME;
		private String DEPARTURE_TIME;
		private String ARRIVAL_DATE;
		private String PHONENUM;
		private int PAY_STATUS;
		private String GOODS_NAME;
		private String PLANE_TYPE;
		private ArrayList<Z_list> z_list;
		private int SALE_PRICES;
		private String ORDER_ID;
		private String OP_TIME;
		private String AIRPLANE_NUM;
		private String DEPARTURE_DATE;
		private String ARRIVAL_TIME;
		private int TICKET_NUM;
		private String TO_AIRPORT;
		private String POLICY_ID;
		private String FROM_AIRPORT;
	}
}