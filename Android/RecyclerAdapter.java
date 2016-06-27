
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
		public ImageView iv_image;
		public TextView tv_title;
		public TextView tv_name;
		public TextView tv_size;
		public ViewHolder(final View itemView) {
			super(itemView);
			iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
			tv_title = (TextView) itemView.findViewById(R.id.tv_title);
			tv_name = (TextView) itemView.findViewById(R.id.tv_name);
			tv_size = (TextView) itemView.findViewById(R.id.tv_size);
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

