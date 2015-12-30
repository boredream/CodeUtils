

	private EditText et_username;
	private EditText et_password;
	private Button btn_login;

	private void initView() {
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_login = (Button) findViewById(R.id.btn_login);

		btn_login.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:

			break;
		}
	}

	/**
	 * TODO 输入验证,可根据需要自行修改补充
	 */
	private void submit() {
		// 开始验证输入内容
		String username = et_username.getText().toString().trim();
		if(TextUtils.isEmpty(username)) {
			Toast.makeText(this, "username不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String password = et_password.getText().toString().trim();
		if(TextUtils.isEmpty(password)) {
			Toast.makeText(this, "password不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// TODO 验证成功,下面开始使用数据
		
		
	}
