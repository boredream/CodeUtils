
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
		public TextView tv_index;
		public TextView tv_name;
		public TextView tv_singer;
		public Button btn_download;
		public Button btn_play;
		public ProgressBar pb_download;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item, null);
			holder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_singer = (TextView) convertView.findViewById(R.id.tv_singer);
			holder.btn_download = (Button) convertView.findViewById(R.id.btn_download);
			holder.btn_play = (Button) convertView.findViewById(R.id.btn_play);
			holder.pb_download = (ProgressBar) convertView.findViewById(R.id.pb_download);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// TODO set data

		return convertView;
	}


