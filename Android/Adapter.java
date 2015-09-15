
	private Context context;
	// TODO change the MyItem class to your data bean class
	private List<MyItem> datas;

	public MyAdapter(Context context, List<MyItem> datas) {
		this.context = context;
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public MyItem getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item, null);
			holder.iv_vip_logo = (ImageView) convertView.findViewById(R.id.iv_vip_logo);
			holder.tv_vip_name = (TextView) convertView.findViewById(R.id.tv_vip_name);
			holder.tv_is_recommend = (TextView) convertView.findViewById(R.id.tv_is_recommend);
			holder.cb_vip = (CheckBox) convertView.findViewById(R.id.cb_vip);
			holder.tv_vip_info = (TextView) convertView.findViewById(R.id.tv_vip_info);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// set data

		return convertView;
	}


	public static class ViewHolder{
		public ImageView iv_vip_logo;
		public TextView tv_vip_name;
		public TextView tv_is_recommend;
		public CheckBox cb_vip;
		public TextView tv_vip_info;
	}
