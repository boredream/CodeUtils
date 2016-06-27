

	private LinearLayout ll_container;
	private RadioGroup rg_accept_training;
	private RadioButton rb_yes;
	private RadioButton rb_no;
	private ImageView iv_add_avatar;
	private ImageView iv_avatar;
	private ImageView iv_add_cert;
	private ImageView iv_cert;
	private EditText et_career;
	private Button btn_submit;

	private void initView() {
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		rg_accept_training = (RadioGroup) findViewById(R.id.rg_accept_training);
		rb_yes = (RadioButton) findViewById(R.id.rb_yes);
		rb_no = (RadioButton) findViewById(R.id.rb_no);
		iv_add_avatar = (ImageView) findViewById(R.id.iv_add_avatar);
		iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
		iv_add_cert = (ImageView) findViewById(R.id.iv_add_cert);
		iv_cert = (ImageView) findViewById(R.id.iv_cert);
		et_career = (EditText) findViewById(R.id.et_career);
		btn_submit = (Button) findViewById(R.id.btn_submit);

		iv_add_avatar.setOnClickListener(this);
		iv_add_cert.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		rg_accept_training.setOnCheckedChangeListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add_avatar:

			break;
		case R.id.iv_add_cert:

			break;
		case R.id.btn_submit:

			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_yes:

			break;
		case R.id.rb_no:

			break;
		}
	}

	/**
	 * TODO 输入验证,可根据需要自行修改补充
	 */
	private void submit() {
		// 开始验证输入内容
		String career = et_career.getText().toString().trim();
		if(TextUtils.isEmpty(career)) {
			Toast.makeText(this, "career不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// TODO 验证成功,下面开始使用数据
		
		
	}
