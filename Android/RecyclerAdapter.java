
	private Context context;
	private List<Object> datas;

	public MyAdapter(Context context, List<Object> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getItemCount() {
		return datas.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView iv_index;
		public TextView tv_index;
		public ImageView iv_avatar;
		public ImageView iv_top_logo;
		public ImageView iv_sex;
		public LinearLayout ll_level;
		public TextView tv_level;
		public TextView tv_name;
		public ImageView iv_coin;
		public TextView tv_coin_count;
		public Button btn_follow;
		public Button btn_followed;
		public ViewHolder(final View itemView) {
			super(itemView);
			iv_index = (ImageView) itemView.findViewById(R.id.iv_index);
			tv_index = (TextView) itemView.findViewById(R.id.tv_index);
			iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
			iv_top_logo = (ImageView) itemView.findViewById(R.id.iv_top_logo);
			iv_sex = (ImageView) itemView.findViewById(R.id.iv_sex);
			ll_level = (LinearLayout) itemView.findViewById(R.id.ll_level);
			tv_level = (TextView) itemView.findViewById(R.id.tv_level);
			tv_name = (TextView) itemView.findViewById(R.id.tv_name);
			iv_coin = (ImageView) itemView.findViewById(R.id.iv_coin);
			tv_coin_count = (TextView) itemView.findViewById(R.id.tv_coin_count);
			btn_follow = (Button) itemView.findViewById(R.id.btn_follow);
			btn_followed = (Button) itemView.findViewById(R.id.btn_followed);
		}
	}

	@Override
	public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(context).inflate(R.layout.item_xx, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final Object data = datas.get(position);


	}

