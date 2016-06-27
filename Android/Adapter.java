
	private Context context;
	// TODO change the MyItem class to your data bean class
	private List<MyItem> datas;

	public MyAdapter(Context context, List<MyItem> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getItemCount() {
		return datas.size();
	}

	public static class ViewHolder{
		public ImageView iv_bg_avatar;
		public ImageView tv_avatar;
		public TextView tv_name;
		public TextView tv_age;
		public TextView tv_training_years;
		public TextView tv_cert_level;
		public TextView tv_career;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item, null);
			holder.iv_bg_avatar = (ImageView) convertView.findViewById(R.id.iv_bg_avatar);
			holder.tv_avatar = (ImageView) convertView.findViewById(R.id.tv_avatar);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
			holder.tv_training_years = (TextView) convertView.findViewById(R.id.tv_training_years);
			holder.tv_cert_level = (TextView) convertView.findViewById(R.id.tv_cert_level);
			holder.tv_career = (TextView) convertView.findViewById(R.id.tv_career);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// TODO set data

		return convertView;
	}


