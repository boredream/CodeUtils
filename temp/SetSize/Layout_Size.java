

	private TextView tv_name;
	private TextView tv_set_size1;
	private TextView tv_phone;
	private TextView tv_contact;
	private TextView tv_set_size2;
	private TextView tv_address;

	private void initView() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_set_size1 = (TextView) findViewById(R.id.tv_set_size1);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_contact = (TextView) findViewById(R.id.tv_contact);
		tv_set_size2 = (TextView) findViewById(R.id.tv_set_size2);
		tv_address = (TextView) findViewById(R.id.tv_address);

	}


	@Override
	protected void onResume() {
		super.onResume();
		tv_name.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
		tv_set_size1.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
		tv_phone.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
		tv_contact.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
		tv_set_size2.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
		tv_address.setTextSize(Constant.TEXT_SIZE[2] * TxtManager.getInstance().getTxtSize(this));
	}
