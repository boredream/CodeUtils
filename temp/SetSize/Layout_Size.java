

	private TextView tv_set_size1;
	private TextView tv_money;
	private TextView tv_set_size2;
	private TextView tv_confirm_good_name;
	private TextView tv_confirm_good_description;
	private TextView tv_phone;
	private TextView tv_set_size3;

	private void initView() {
		tv_set_size1 = (TextView) findViewById(R.id.tv_set_size1);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_set_size2 = (TextView) findViewById(R.id.tv_set_size2);
		tv_confirm_good_name = (TextView) findViewById(R.id.tv_confirm_good_name);
		tv_confirm_good_description = (TextView) findViewById(R.id.tv_confirm_good_description);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_set_size3 = (TextView) findViewById(R.id.tv_set_size3);

	}


	@Override
	protected void onResume() {
		super.onResume();
		tv_set_size1.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
		tv_money.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
		tv_set_size2.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
		tv_confirm_good_name.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
		tv_confirm_good_description.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
		tv_phone.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
		tv_set_size3.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
	}
