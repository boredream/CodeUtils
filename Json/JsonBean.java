public class JsonBeans {
	private ArrayList<Statuses> statuses;
	private int previous_cursor;
	private long next_cursor;
	private int total_number;

	/*sub class*/
	public class User {
		private int id;
		private String screen_name;
		private String name;
		private String province;
		private String city;
		private String location;
		private String description;
		private String url;
		private String profile_image_url;
		private String domain;
		private String gender;
		private int followers_count;
		private int friends_count;
		private int statuses_count;
		private int favourites_count;
		private String created_at;
		private boolean following;
		private boolean allow_all_act_msg;
		private String remark;
		private boolean geo_enabled;
		private boolean verified;
		private boolean allow_all_comment;
		private String avatar_large;
		private String verified_reason;
		private boolean follow_me;
		private int online_status;
		private int bi_followers_count;
	}

	/*sub class*/
	public class Statuses {
		private String created_at;
		private long id;
		private String text;
		private String source;
		private boolean favorited;
		private boolean truncated;
		private String in_reply_to_status_id;
		private String in_reply_to_user_id;
		private String in_reply_to_screen_name;
		private Object geo;
		private String mid;
		private int reposts_count;
		private int comments_count;
		private ArrayList<Object> annotations;
		private User user;
	}
}