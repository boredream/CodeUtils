

	private TextView tv_fenge;
	private TextView tv_desc;
	private TextView tv_complete;
	private TextView tv_modify;
	private TextView textView8;
	private TextView dian_name1;
	private TextView tv_show_company_name;
	private TextView textView01;
	private TextView textView9;
	private TextView dian_name2;
	private TextView tv_show_company_info;
	private TextView textView02;
	private TextView textView10;
	private TextView dian_name3;
	private TextView tv_show_company_city;
	private TextView textView03;
	private TextView textView14;
	private TextView textView04;
	private TextView tv_set_size1;
	private TextView textView222;
	private TextView textView;
	private TextView textView06;
	private TextView tv_51;
	private TextView tv_show_company_other_desc;
	private TextView textView08;
	private TextView tv_set_size2;
	private TextView textView09;
	private TextView tv_53;

	private void initView() {
		tv_fenge = (TextView) findViewById(R.id.tv_fenge);
		tv_desc = (TextView) findViewById(R.id.tv_desc);
		tv_complete = (TextView) findViewById(R.id.tv_complete);
		tv_modify = (TextView) findViewById(R.id.tv_modify);
		textView8 = (TextView) findViewById(R.id.textView8);
		dian_name1 = (TextView) findViewById(R.id.dian_name1);
		tv_show_company_name = (TextView) findViewById(R.id.tv_show_company_name);
		textView01 = (TextView) findViewById(R.id.textView01);
		textView9 = (TextView) findViewById(R.id.textView9);
		dian_name2 = (TextView) findViewById(R.id.dian_name2);
		tv_show_company_info = (TextView) findViewById(R.id.tv_show_company_info);
		textView02 = (TextView) findViewById(R.id.textView02);
		textView10 = (TextView) findViewById(R.id.textView10);
		dian_name3 = (TextView) findViewById(R.id.dian_name3);
		tv_show_company_city = (TextView) findViewById(R.id.tv_show_company_city);
		textView03 = (TextView) findViewById(R.id.textView03);
		textView14 = (TextView) findViewById(R.id.textView14);
		textView04 = (TextView) findViewById(R.id.textView04);
		tv_set_size1 = (TextView) findViewById(R.id.tv_set_size1);
		textView222 = (TextView) findViewById(R.id.textView222);
		textView = (TextView) findViewById(R.id.textView);
		textView06 = (TextView) findViewById(R.id.textView06);
		tv_51 = (TextView) findViewById(R.id.tv_51);
		tv_show_company_other_desc = (TextView) findViewById(R.id.tv_show_company_other_desc);
		textView08 = (TextView) findViewById(R.id.textView08);
		tv_set_size2 = (TextView) findViewById(R.id.tv_set_size2);
		textView09 = (TextView) findViewById(R.id.textView09);
		tv_53 = (TextView) findViewById(R.id.tv_53);

	}


	@Override
	protected void onResume() {
		super.onResume();
		tv_fenge.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		tv_desc.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		tv_complete.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		tv_modify.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView8.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		dian_name1.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		tv_show_company_name.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView01.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView9.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		dian_name2.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		tv_show_company_info.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView02.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView10.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		dian_name3.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		tv_show_company_city.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView03.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView14.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView04.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		tv_set_size1.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView222.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView06.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		tv_51.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		tv_show_company_other_desc.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView08.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		tv_set_size2.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		textView09.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
		tv_53.setTextSize(Constant.TEXT_SIZE[3] * TxtManager.getInstance().getTxtSize(context));
	}
