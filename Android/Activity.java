

	private ImageView iv_icon;
	private TextView tv_info;
	private Button btn_left;
	private Button btn_right;

	private void initView() {
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		tv_info = (TextView) findViewById(R.id.tv_info);
		btn_left = (Button) findViewById(R.id.btn_left);
		btn_right = (Button) findViewById(R.id.btn_right);

		btn_left.setOnClickListener(this);
		btn_right.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:

			break;
		case R.id.btn_right:

			break;
		}
	}
