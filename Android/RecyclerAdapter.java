
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
		public ImageView iv_avatar;
		public TextView tv_name;
		public TextView tv_total_time;
		public ViewHolder(final View itemView) {
			super(itemView);
			iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
			tv_name = (TextView) itemView.findViewById(R.id.tv_name);
			tv_total_time = (TextView) itemView.findViewById(R.id.tv_total_time);
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

