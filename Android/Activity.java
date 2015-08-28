

	private TextView tv_title;
	private TextView tv_link;
	private Button btn_submit;
	private RadioGroup rg_tab;
	private RadioButton rb1;
	private RadioButton rb2;
	private RadioButton rb3;

	private void initView() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_link = (TextView) findViewById(R.id.tv_link);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		rb1 = (RadioButton) findViewById(R.id.rb1);
		rb2 = (RadioButton) findViewById(R.id.rb2);
		rb3 = (RadioButton) findViewById(R.id.rb3);

		tv_link.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		rg_tab.setOnCheckedChangeListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_link:

			break;
		case R.id.btn_submit:

			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb1:

			break;
		case R.id.rb2:

			break;
		case R.id.rb3:

			break;
		}
	}
