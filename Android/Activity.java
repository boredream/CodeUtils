

	private ImageView iv_back;
	private EditText et_search;

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		et_search = (EditText) findViewById(R.id.et_search);

		iv_back.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:

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
