

	private RelativeLayout layout_head;
	private ImageView iv_set_avator;
	private ImageView iv_avator_arrow;
	private RelativeLayout layout_name;
	private TextView tv_set_name;
	private RelativeLayout layout_description;
	private TextView tv_set_description;
	private ImageView iv_description_arrow;
	private RelativeLayout layout_gender;
	private TextView tv_set_gender;
	private ImageView iv_gender_arrow;
	private Button btn_add_friend;
	private Button btn_chat;

	private void initView() {
		layout_head = (RelativeLayout) findViewById(R.id.layout_head);
		iv_set_avator = (ImageView) findViewById(R.id.iv_set_avator);
		iv_avator_arrow = (ImageView) findViewById(R.id.iv_avator_arrow);
		layout_name = (RelativeLayout) findViewById(R.id.layout_name);
		tv_set_name = (TextView) findViewById(R.id.tv_set_name);
		layout_description = (RelativeLayout) findViewById(R.id.layout_description);
		tv_set_description = (TextView) findViewById(R.id.tv_set_description);
		iv_description_arrow = (ImageView) findViewById(R.id.iv_description_arrow);
		layout_gender = (RelativeLayout) findViewById(R.id.layout_gender);
		tv_set_gender = (TextView) findViewById(R.id.tv_set_gender);
		iv_gender_arrow = (ImageView) findViewById(R.id.iv_gender_arrow);
		btn_add_friend = (Button) findViewById(R.id.btn_add_friend);
		btn_chat = (Button) findViewById(R.id.btn_chat);

		btn_add_friend.setOnClickListener(this);
		btn_chat.setOnClickListener(this);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_friend:

			break;
		case R.id.btn_chat:

			break;
		}
	}
