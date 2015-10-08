
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
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// set data

		return convertView;
	}


	public static class ViewHolder{
		public TextView tv_name;
		public TextView tv_des;
	}
