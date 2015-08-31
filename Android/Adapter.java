
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
			holder.cb_credit = (CheckBox) convertView.findViewById(R.id.cb_credit);
			holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// set data

		return convertView;
	}


	public static class ViewHolder{
		public CheckBox cb_credit;
		public TextView tv_money;
	}
