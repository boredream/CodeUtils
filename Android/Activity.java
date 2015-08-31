

	private RadioGroup tg_recharge_type;
	private RadioButton rb_credit;
	private RadioButton rb_vip;
	private LinearLayout ll_credit;
	private MyListView lv_goodlist;
	private TextView tv_pay_tip;
	private TextView tv_tip;
	private Button btn_commit;

	private void initView() {
		tg_recharge_type = (RadioGroup) findViewById(R.id.tg_recharge_type);
		rb_credit = (RadioButton) findViewById(R.id.rb_credit);
		rb_vip = (RadioButton) findViewById(R.id.rb_vip);
		ll_credit = (LinearLayout) findViewById(R.id.ll_credit);
		lv_goodlist = (MyListView) findViewById(R.id.lv_goodlist);
		tv_pay_tip = (TextView) findViewById(R.id.tv_pay_tip);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		btn_commit = (Button) findViewById(R.id.btn_commit);

		btn_commit.setOnClickListener(this);
		tg_recharge_type.setOnCheckedChangeListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit:

			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_credit:

			break;
		case R.id.rb_vip:

			break;
		}
	}
