package com.boredream.herb.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boredream.boreweibo.activity.UserInfoActivity;
import com.boredream.boreweibo.adapter.UserItemAdapter;
import com.boredream.boreweibo.api.BoreWeiboApi;
import com.boredream.boreweibo.api.SimpleRequestListener;
import com.boredream.boreweibo.constants.AccessTokenKeeper;
import com.boredream.boreweibo.entity.User;
import com.boredream.boreweibo.entity.UserItem;
import com.boredream.boreweibo.utils.TitleBuilder;
import com.boredream.boreweibo.widget.WrapHeightListView;
import com.boredream.herb.BaseApplication;
import com.boredream.herb.BaseFragment;
import com.boredream.herb.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class UserFragment extends BaseFragment {

	private LinearLayout ll_userinfo;
	
	private ImageView iv_avatar;
	private TextView tv_subhead;
	private TextView tv_caption;

	private TextView tv_status_count;
	private TextView tv_follow_count;
	private TextView tv_fans_count;
	
	private WrapHeightListView lv_user_items;

	private User user;
	private View view;

	private UserItemAdapter adapter;
	private List<UserItem> userItems;
	
	private BoreWeiboApi weiboApi;
	private Oauth2AccessToken mAccessToken;
	private ImageLoader imageLoader;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = View.inflate(getActivity(), R.layout.frag_user, null);
		
		weiboApi = new BoreWeiboApi(activity);
		mAccessToken = AccessTokenKeeper.readAccessToken(activity);
		imageLoader = ImageLoader.getInstance();
		
		initView();
		
		setItem();

		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		// show/hide方法不会走生命周期
		System.out.println("user frag onStart()");
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if(!hidden) {
			weiboApi.usersShow(mAccessToken.getUid(), "", 
					new SimpleRequestListener(activity, null) {

						@Override
						public void onComplete(String response) {
							super.onComplete(response);
							
							BaseApplication application = (BaseApplication) activity.getApplication();
							application.currentUser = user = new Gson().fromJson(response, User.class);
							
							setUserInfo();
						}
				
			});
		}
	}
	
	private void initView() {
		// 标题栏
		new TitleBuilder(view)
			.setTitleText("我")
			.build();
		// 用户信息
		ll_userinfo = (LinearLayout) view.findViewById(R.id.ll_userinfo);
		ll_userinfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, UserInfoActivity.class);
				startActivity(intent);
			}
		});
		iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
		tv_subhead = (TextView) view.findViewById(R.id.tv_subhead);
		tv_caption = (TextView) view.findViewById(R.id.tv_caption);
		// 互动信息栏: 微博数、关注数、粉丝数
		tv_status_count = (TextView) view.findViewById(R.id.tv_status_count);
		tv_follow_count = (TextView) view.findViewById(R.id.tv_follow_count);
		tv_fans_count = (TextView) view.findViewById(R.id.tv_fans_count);
		// 设置栏列表
		lv_user_items = (WrapHeightListView) view.findViewById(R.id.lv_user_items);
		userItems = new ArrayList<UserItem>();
		adapter = new UserItemAdapter(activity, userItems);
		lv_user_items.setAdapter(adapter);
	}

	// 设置用户信息
	private void setUserInfo() {
		tv_subhead.setText(user.getName());
		tv_caption.setText("简介:" + user.getDescription());
		imageLoader.displayImage(user.getAvatar_large(), iv_avatar);
		tv_status_count.setText("" + user.getStatuses_count());
		tv_follow_count.setText("" + user.getFriends_count());
		tv_fans_count.setText("" + user.getFollowers_count());
	}
	
	// 设置栏列表
	private void setItem() {
		userItems.add(new UserItem(false, R.drawable.push_icon_app_small_1, "新的朋友", ""));
		userItems.add(new UserItem(false, R.drawable.push_icon_app_small_2, "微博等级", "Lv13"));
		userItems.add(new UserItem(false, R.drawable.push_icon_app_small_3, "编辑资料", ""));
		userItems.add(new UserItem(true, R.drawable.push_icon_app_small_4, "我的相册", "(18)"));
		userItems.add(new UserItem(false, R.drawable.push_icon_app_small_5, "我的点评", ""));
		userItems.add(new UserItem(false, R.drawable.push_icon_app_small_4, "我的赞", "(32)"));
		userItems.add(new UserItem(true, R.drawable.push_icon_app_small_3, "微博支付", ""));
		userItems.add(new UserItem(false, R.drawable.push_icon_app_small_2, "微博运动", "步数、卡路里、跑步轨迹"));
		userItems.add(new UserItem(true, R.drawable.push_icon_app_small_1, "更多", "收藏、名片"));
		adapter.notifyDataSetChanged();
	}

}
