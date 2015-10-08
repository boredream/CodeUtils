

	private RadioGroup rg_check_type;
	private RadioButton rb_single;
	private RadioButton rb_multi;
	private ListView lv;
	private Button btn;

	private void initView() {
		rg_check_type = (RadioGroup) findViewById(R.id.rg_check_type);
		rb_single = (RadioButton) findViewById(R.id.rb_single);
		rb_multi = (RadioButton) findViewById(R.id.rb_multi);
		lv = (ListView) findViewById(R.id.lv);
		btn = (Button) findViewById(R.id.btn);

		btn.setOnClickListener(this);
		rg_check_type.setOnCheckedChangeListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn:

			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_single:

			break;
		case R.id.rb_multi:

			break;
		}
	}
