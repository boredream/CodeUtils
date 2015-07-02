package com.sfbm.convenientmobile.constants;

public class B2CConstants {
	// 登录接口
	public static final String METHOD_LOGIN = "/api/acc/login.action";
	// 注册账号
	public static final String METHOD_REGISTER = "/api/acc/register.action";
	// 验证码
	public static final String METHOD_CODE = "/api/acc/seccode.action";
	// 忘记密码
	public static final String METHOD_FORGETPEDS = "/api/acc/forget.action";
	// 忘记密码
	public static final String METHOD_FORGETPSWPEDS = "/api/acc/part/ftpd.action";
	// 修改密码
	public static final String METHOD_UPDATEPWDS = "/api/acc/change.action";
	// 修改支付密码
	public static final String METHOD_UPDATEPAYPWDS = "/api/acc/part/mtpd.action";
	// 账号激活
	public static final String METHOD_ACTIVATE = "/service/pad/activateAccounts.action";
	// 生成QQ订单接口
	public static final String METHOD_QQ_QUERYPRICE = "/api/qqc/all/queryPrice.action";
	// 生成QQ订单接口
	public static final String METHOD_QQORDER = "/api/qqc/part/applyOrder.action";
	// 生成手机充值订单接口
	public static final String METHOD_MOBILEORDER = "/service/pad/mOrder.action";
	// Q币充值支付接口
	public static final String METHOD_QQPAY = "/service/pad/qqPay.action";
	// 手机充值查询价钱
	public static final String METHOD_MOBILEPAY_PRICE = "/api/phone/all/queryMobileAttr.action";
	// 手机充值下单
	public static final String MOBILEPAY_ORDER = "/api/phone/part/placeOrder.action";
	// 修改联系人
	public static final String METHOD_UPDATE_CONTACT = "/api/phone/part/editMark.action";
	// 手机充值支付接口
	public static final String METHOD_MOBILEPAY = "/service/pad/mPay.action";
	// 查询商品售价接口
	public static final String METHOD_QUERYGOODS = "/service/pad/queryGoods.action";
	// 查询商品信息接口
	public static final String METHOD_MGOODSINFO = "/service/pad/mGoodsInfo.action";
	// 同步服务器上订单接口
	public static final String METHOD_QUERYORDERGOODS = "/service/pad/queryOrderGoods.action";
	// 检查更新接口
	public static final String METHOD_QUERYVERSION = "/service/version/pad.action";
	// 更新内容查询
	public static final String QUERY_VERSION_CONTENT = "/service/pad/queryEditionMessages.action";
	// 查询订单列表
	public static final String QUERY_ORDERLST = "/api/order/queryOrderLst.action";
	// 删除订单
	public static final String DELETE_ORDER = "/api/order/part/deleteOrder.action";
	// 添加到一键充值
	public static final String ADD_ONEKEY_RECHARGE = "/api/order/oneKeyRecharge.action";
	// 一键充值列表
	public static final String QUERY_ONEKEY_RECHARGE = "/api/order/queryOrderLst.action";

	// 一键充值下单
	public static final String ONEKEY_RECHARGE_ORDER = "/api/order/part/keyRechargeMobile.action";

	// 意见反馈
	public static final String METHOD_FEEDBACK = "/api/acc/part/receiveFeedback.action";

	// 省市地区查询
	public static final String AREA_SEARCH = "/service/pad/queryArea.action";

	// 手机充值取消订单
	public static final String MOBILE_CANCEL_ORDER = "/service/pad/mCancel.action";

	// QQ充值取消订单
	public static final String QQ_CANCEL_ORDER = "/service/pad/qqCancel.action";

	// ////////////////游戏/////////////////
	// 查询所有游戏
	public static final String QUERY_ALL_GAMES = "/api/game/all/queryBrands.action";
	// 获取游戏支持的充值方式
	public static final String QUERY_GAME_CHARGE = "/api/game/all/queryChargeType.action";
	// 根据游戏ID以及是否为卡密查询所有商品
	public static final String QUERY_GAME_GOODS = "/api/game/all/queryGoods.action";
	// 获取界面展示
	public static final String QUERY_GAME_UI = "/api/game/all/queryGameUI.action";
	// 游戏下单
	public static final String APPLY_G_ORDER = "/api/game/part/applyOrder.action";

	public static String PAY_SIGN = "/api/yp/app/reqV.action";

	public static String PAY_PSW_SETTING = "/api/acc/part/tpd.action";

	public static final String METHOD_UODATE = "/api/acc/updateVersion.action";

	// 模拟支付成功
	public static String PAY_MOCK = "/api/yp/simu/callback/unbind.action";
	// ///////////////////////////易宝////////////////////////////////////////

	// 查询绑定银行卡列表
	public static final String QUERY_BANKLIST = "/api/yp/app/qbc.action";
	// 发送绑卡支付请求绑卡支付
	public static final String MOTHOD_BINDCARD = "/api/yp/app/bpr.action";
	// 发送短信验证码
	public static final String MOTHOD_YBCODE = "/api/yp/app/rysv.action";
	// 确认支付
	public static final String MOTHOD_YBPAY = "/api/yp/app/ybcc.action";
	// 解绑银行卡
	public static final String MOTHOD_DELETECARD = "/api/yp/app/rbc.action";
	
	// ///////////////////////////other////////////////////////////////////////
	// 分享单页
	public static final String SHARE_ADDRESS = "/app/share.jsp";
}
