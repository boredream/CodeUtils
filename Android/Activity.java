	private EditText et_search;
	private TextView tv_name;
	private TextView tv_link;
	private Button btn_confirm;
	private RadioGroup rg_tab;
	private RadioButton rb_home;
	private RadioButton rb_category;

	private void initView() {
		et_search = (EditText) findViewById(R.id.et_search);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_link = (TextView) findViewById(R.id.tv_link);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
		rb_home = (RadioButton) findViewById(R.id.rb_home);
		rb_category = (RadioButton) findViewById(R.id.rb_category);

		tv_link.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		rg_tab.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_link:

			break;
		case R.id.btn_confirm:

			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_home:

			break;
		case R.id.rb_category:

			break;
		}
	}

	/**
	 * TODO 输入验证,可根据需要自行修改补充
	 */
	private void submit() {
		// 开始验证输入内容
		String search = et_search.getText().toString().trim();
		if(TextUtils.isEmpty(search)) {
			Toast.makeText(this, "search不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// TODO 验证成功,下面开始使用数据
		
		
	}
