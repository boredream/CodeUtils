package com.sfbm.convenientmobile.http;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.boredream.volley.BDListener;
import com.boredream.volley.BDParseError;
import com.boredream.volley.BDVolleyHttp;
import com.boredream.volley.BDVolleyUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.sfbm.convenientmobile.BaseApplication;
import com.sfbm.convenientmobile.constants.B2CConstants;
import com.sfbm.convenientmobile.constants.ResponseConstants;
import com.sfbm.convenientmobile.entity.B2CBaseResponse;
import com.sfbm.convenientmobile.entity.B2CUser;
import com.sfbm.convenientmobile.entity.BankCardInfoEntity;
import com.sfbm.convenientmobile.entity.BankCardInfoResponse;
import com.sfbm.convenientmobile.entity.BindCardResponse;
import com.sfbm.convenientmobile.entity.CodeResponse;
import com.sfbm.convenientmobile.entity.FastRechargeResponse;
import com.sfbm.convenientmobile.entity.GameChargeType;
import com.sfbm.convenientmobile.entity.GameChargeTypeResponse;
import com.sfbm.convenientmobile.entity.GameGood;
import com.sfbm.convenientmobile.entity.GameGoodsResponse;
import com.sfbm.convenientmobile.entity.GameInfoEntitys;
import com.sfbm.convenientmobile.entity.GameUIInfoEntity;
import com.sfbm.convenientmobile.entity.GameUIResponse;
import com.sfbm.convenientmobile.entity.MobileOrderInfoResponse;
import com.sfbm.convenientmobile.entity.MonbileInfoResponse;
import com.sfbm.convenientmobile.entity.OrderListResponse;
import com.sfbm.convenientmobile.entity.OrderSubmitReturn;
import com.sfbm.convenientmobile.entity.PayInfoResponse;
import com.sfbm.convenientmobile.entity.QQOrderResponse;
import com.sfbm.convenientmobile.entity.QQPriceResponse;
import com.sfbm.convenientmobile.entity.UpdateResponse;
import com.sfbm.convenientmobile.entity.VersionContentResponse;
import com.sfbm.convenientmobile.entity.YpCodeResponse;
import com.sfbm.convenientmobile.utils.LogUtils;
import com.sfbm.convenientmobile.utils.MD5;
import com.sfbm.convenientmobile.utils.SHA1;
import com.sfbm.convenientmobile.utils.StringUtils;

/**
 * @author Administrator
 * 
 */
public class B2CHttpRequest {

	private static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	// 第1种加密方式 未登录
	private static String getSign(String time) {
		return new SHA1().getSHA1("time=" + new MD5().getMD5ofStr(String.valueOf(time)));
	}

	// 第2种加密方式 登录
	private static String getLoginSign(String time) {
		B2CUser user = BaseApplication.curUser;

		String regeist = user.getRegeist_time();
		regeist = regeist.replace(" ", "").replace("-", "").replace(":", "");

		return new SHA1().getSHA1("acc_id=" + user.getAcc_id() + "uid=" + user.getUid() + "time=" + time + "regeist_time=" + new MD5().getMD5ofStr(regeist));

	}

	/************************ 获得验证码 **********************************/
	public static void requestCode(String phoneNum, String smsType, B2CListener<CodeResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("phone", phone);
		params.put("sms_type", sms_type);
		params.put("time", time);
		params.put("sign", getSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_CODE), params, CodeResponse.class, listener);
	}

	/************************ 注册 **********************************/
	public static void requestRegister(String phoneNum, String pwds, B2CListener<B2CUser> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("phone", phoneNum);
		params.put("lkey", pwds);
		params.put("time", time);
		params.put("sign", getSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_REGISTER), params, B2CUser.class, listener);
	}

	/**
	 * 
	 * @param inputPsw
	 * @param userName
	 * @param localPsw
	 * @param listener
	 */
	public static void login(String inputPsw, String userName, String localPsw, B2CListener<B2CUser> listener) {
		// 一次加密：将lpasswd 进行MD5 加密
		String lkey = "";
		// 我们的md5返回的是32位的字串,不是32位的就是原始的密码,以后需要加密
		if (TextUtils.isEmpty(localPsw) || localPsw.length() != 32) {
			lkey = new MD5().getMD5ofStr(inputPsw);
		} else {
			// input最多20的字符,包含就可以了
			if (localPsw.startsWith(inputPsw)) {
				// 不需要加密了
				lkey = localPsw;
			} else {
				// 需要加密
				lkey = new MD5().getMD5ofStr(inputPsw);
			}
		}
		String curTime = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", userName);
		params.put("lkey", lkey);
		params.put("time", curTime);
		params.put("sign", getSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_LOGIN), params, B2CUser.class, listener);
	}

	/***************************** 忘记密码 **********************************/
	public static void forgetPwds(String phone, String incode, String newPwds, B2CListener<B2CBaseResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("phone", phone);
		params.put("incode", incode);
		params.put("nkey", new MD5().getMD5ofStr(newPwds));
		params.put("time", time);
		params.put("sign", getSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_FORGETPEDS), params, B2CBaseResponse.class, listener);
	}

	/***************************** 忘记支付密码 **********************************/
	public static void forgetPayPwds(String acc_id, String phone, String card_no, String incode, String newPwds, B2CListener<B2CBaseResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acc_id", acc_id);
		params.put("phone", phone);
		params.put("card_no", card_no);
		params.put("incode", incode);
		params.put("ntpd", StringUtils.getPayPsw(newPwds));
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_FORGETPSWPEDS), params, B2CBaseResponse.class, listener);
	}

	/****************************** 修改密码 ************************************/
	public static void updatePws(String lkey, String nkey, B2CListener<B2CBaseResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acc_id", BaseApplication.curUser.getAcc_id());
		params.put("lkey", new MD5().getMD5ofStr(lkey));// 旧密码
		params.put("nkey", new MD5().getMD5ofStr(nkey));// 新密码
		params.put("time", time);
		params.put("sign", getSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_UPDATEPWDS), params, B2CBaseResponse.class, listener);
	}

	/****************************** 修改支付密码 ************************************/
	public static void updatePayPws(String o_tpd, String n_tpd, B2CListener<B2CBaseResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acc_id", BaseApplication.curUser.getAcc_id());
		params.put("o_tpd", StringUtils.getPayPsw(o_tpd));// 旧密码
		params.put("n_tpd", StringUtils.getPayPsw(n_tpd));// 新密码
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_UPDATEPAYPWDS), params, B2CBaseResponse.class, listener);
	}

	/****************************** 修改充值联系人 ************************************/
	public static void updateContactName(String o_id, String lx_name, B2CListener<B2CBaseResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acc_id", BaseApplication.curUser.getAcc_id());
		params.put("o_id", o_id);// 订单号
		params.put("lx_name", lx_name);// 联系人姓名
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_UPDATE_CONTACT), params, B2CBaseResponse.class, listener);
	}

	/***************************** 查询售价 **********************************/
	public static void queryQQPrice(int goods_num, B2CListener<QQPriceResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cat_id", B2CConstants.CAT_ID_QQ);
		params.put("goods_num", goods_num);// 订单号
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_QQ_QUERYPRICE), params, QQPriceResponse.class, listener);
	}

	/***************************** QQ **********************************/

	public static void qqOrder(String qqno, int goods_num, String good_id, String price, B2CListener<QQOrderResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("qqno", qqno);
		params.put("acc_id", BaseApplication.curUser.getAcc_id());
		params.put("goods_num", goods_num);
		params.put("good_id", good_id);
		params.put("price", price);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_QQORDER), params, QQOrderResponse.class, listener);
	}

	/***************************** 游戏 **********************************/
	public static void gameList(B2CListener<GameInfoEntitys> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cat_id", B2CConstants.CAT_ID_GAME);
		params.put("time", time);
		params.put("sign", getSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.QUERY_ALL_GAMES), params, GameInfoEntitys.class, listener);
	}

	/***************************** 游戏查询充值方式 **********************************/
	public static void gameChargeType(String brand_id, B2CListener<GameChargeTypeResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("brand_id", brand_id);
		params.put("time", time);
		params.put("sign", getSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.QUERY_GAME_CHARGE), params, GameChargeTypeResponse.class, listener);
	}

	/***************************** 游戏查询商品信息 **********************************/
	public static void gameGoods(String brand_id, int is_cardspwd, B2CListener<GameGoodsResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("brand_id", brand_id);
		params.put("is_cardspwd", is_cardspwd);
		params.put("time", time);
		params.put("sign", getSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.QUERY_GAME_GOODS), params, GameGoodsResponse.class, listener);
	}

	/***************************** 游戏查询充值参数UI信息 **********************************/
	public static void gameUIs(String goods_sn, B2CListener<GameUIResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("goods_sn", goods_sn);
		params.put("time", time);
		params.put("sign", getSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.QUERY_GAME_UI), params, GameUIResponse.class, listener);
	}

	/***************************** 游戏下单 **********************************/
	public static void requestGameSubmitOrder(GameUIInfoEntity chooseItem, B2CListener<OrderSubmitReturn> listener) {
		final Map<String, Object> params = new HashMap<String, Object>();
		String time = String.valueOf(System.currentTimeMillis());
		String curSelectedNum = chooseItem.getCurSelectedNum();
		GameGood curGoodsInfo = chooseItem.getCurGoodsInfo();
		GameChargeType curCardpwd = chooseItem.getCurCardpwd();
		int isPws = curCardpwd.getIs_cardspwd();
		params.put("acc_id", BaseApplication.curUser.getAcc_id());
		params.put("goods_sn", chooseItem.getCurGoodsInfo().getGoods_sn());
		params.put("buynumber", curSelectedNum);
		params.put("total", chooseItem.getCurTotalPrice());
		params.put("is_cardspwd", String.valueOf(isPws));
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		if (isPws == 2 && !StringUtils.isEmpty(chooseItem.getCurAccount())) {
			params.put("tran_num", chooseItem.getCurAccount());
		}
		String companyName = chooseItem.getGameInfo().getCompanyName();
		if (!StringUtils.isEmpty(companyName)) {
			params.put("company_name", companyName);
		}
		String brand_name = curGoodsInfo.getBrand_name();
		if (!StringUtils.isEmpty(brand_name)) {
			params.put("brand_name", brand_name);
		}
		String curAccountType = chooseItem.getCurAccountType();
		if (!StringUtils.isEmpty(curAccountType)) {
			params.put("account_type", curAccountType);
		}
		if (chooseItem != null) {
			if (chooseItem.getCurRole() != null) {
				params.put("account_type_id", chooseItem.getCurRole().getAccountTypeId());
				params.put("account_type_name", chooseItem.getCurRole().getAccountTypeName());
			}
			if (chooseItem.getCurChargeType() != null) {
				params.put("charge_type_id", chooseItem.getCurChargeType().getCharge_type_id());
				params.put("charge_type_name", chooseItem.getCurChargeType().getCharge_type_name());
			}
			if (chooseItem.getCurGameServ() != null) {
				params.put("game_srv_id", chooseItem.getCurGameServ().getGameServId());
				params.put("game_srv_name", chooseItem.getCurGameServ().getGameServName());
			}
			if (chooseItem.getCurGameArea() != null) {
				params.put("game_area_id", chooseItem.getCurGameArea().getGameAreaId());
				params.put("game_area_name", chooseItem.getCurGameArea().getGameAreaName());
			}
			if (chooseItem.getCurChooseGem() != null) {
				params.put("choosegem_id", chooseItem.getCurChooseGem().getChoosegemId());
				params.put("choosegem_name", chooseItem.getCurChooseGem().getChoosegemName());
			}
		}
		doHttp(B2CConstants.getUrl(B2CConstants.APPLY_G_ORDER), params, OrderSubmitReturn.class, listener);
	}

	/************************ mobile **********************************/

	public static void requestMobileNum(String phoneNum, B2CListener<MonbileInfoResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tran_num", phoneNum);
		params.put("time", time);
		params.put("sign", getSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_MOBILEPAY_PRICE), params, MonbileInfoResponse.class, listener);
	}

	/************************ 订单查询 **********************************/
	public static void requestOrder(String accId, B2CListener<OrderListResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acc_id", accId);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.QUERY_ORDERLST), params, OrderListResponse.class, listener);
	}

	/************************ 删除订单 **********************************/
	public static void requestDeleteOrder(String acc_id, String o_id, B2CListener<B2CBaseResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acc_id", acc_id);
		params.put("o_id", o_id);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.DELETE_ORDER), params, B2CBaseResponse.class, listener);
	}

	/************************ 添加到一键充值 **********************************/
	public static void requestAddToFast(String accId, String isRepeat, String orderId, B2CListener<B2CBaseResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acc_id", accId);
		params.put("is_repeat", isRepeat);
		params.put("o_id", orderId);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.ADD_ONEKEY_RECHARGE), params, B2CBaseResponse.class, listener);
	}

	/************************ 一键充值列表查询 **********************************/
	public static void requestFastRechargeList(String accId, String isRepeat, B2CListener<FastRechargeResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acc_id", accId);
		params.put("is_repeat", isRepeat);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.QUERY_ONEKEY_RECHARGE), params, FastRechargeResponse.class, listener);
	}

	/************************ 一键充值下单 **********************************/
	public static void requestFastRechargeOrder(String oid, String acc_id, B2CListener<MobileOrderInfoResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("o_id", oid);
		params.put("acc_id", acc_id);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.ONEKEY_RECHARGE_ORDER), params, MobileOrderInfoResponse.class, listener);
	}

	/************************ 话费下单接口 **********************************/

	public static void requestMobileOrder(String accId, String phone, String goodsId, String areaPriceId, String shopPrice, String contactName, B2CListener<MobileOrderInfoResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acc_id", accId);
		params.put("tran_num", phone);
		params.put("tran_type", "M");
		params.put("tran_id", goodsId);
		params.put("lx_name", contactName);
		params.put("app_goods_prices_id", areaPriceId);
		params.put("shop_price", shopPrice);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.MOBILEPAY_ORDER), params, MobileOrderInfoResponse.class, listener);
	}

	/************************ 意见反馈 **********************************/

	public static void requestFeedback(String opinion, B2CListener<B2CBaseResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		B2CUser curUser = BaseApplication.curUser;
		params.put("acc_id", curUser.getAcc_id());
		params.put("phone", curUser.getMobile());
		params.put("opinion", opinion);
		params.put("time", time);
		params.put("resource", "ARD");
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_FEEDBACK), params, B2CBaseResponse.class, listener);
	}

	/************************ 获取网页支付加密串 **********************************/
	public static void requestPaySign(String tpd, String accId, String o_id, String total_prices, String term_id, String view_ua, String fcb_url, B2CListener<PayInfoResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tpd", StringUtils.getPayPsw(tpd));
		params.put("acc_id", accId);
		params.put("o_id", o_id);
		params.put("total_prices", total_prices);
		params.put("tran_type", "Q");
		params.put("term_id", term_id);
		params.put("view_ua", view_ua);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.PAY_SIGN), params, PayInfoResponse.class, listener);
	}

	public static void requestPayPswSetting(String accId, String card_no, String true_name, String tpd, B2CListener<B2CBaseResponse> listener) {
		String time = String.valueOf(getCurrentTime());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("acc_id", accId);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		params.put("card_no", card_no);
		params.put("true_name", true_name);
		params.put("tpd", StringUtils.getPayPsw(tpd));
		doHttp(B2CConstants.getUrl(B2CConstants.PAY_PSW_SETTING), params, B2CBaseResponse.class, listener);
	}

	/************************ 模拟支付成功 **********************************/
	public static void requestPayMock(String o_id, String total_prices, BDListener<String> listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("o_id", o_id);
		params.put("total_prices", total_prices);
		BDVolleyHttp.postString(B2CConstants.getUrl(B2CConstants.PAY_MOCK), params, listener);
	}

	/**************************** 查询更新内容 ****************************/
	public static void queryVersionContent(String versionName, B2CListener<VersionContentResponse> listener) {
		long time = getCurrentTime();
		String param = "time=" + time + "&editionId=" + versionName;
		String sign = new MD5().getMD5ofStr(param);
		String strParam = param + "&sign=" + sign;
		Map<String, Object> params = BDVolleyUtils.str2params(strParam);
		doHttp(B2CConstants.getUrl(B2CConstants.QUERY_VERSION_CONTENT), params, VersionContentResponse.class, listener);
	}

	/*************************** 获取绑卡关系 *******************************/
	public static void queryBindList(B2CListener<BankCardInfoResponse> listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		String time = String.valueOf(getCurrentTime());
		params.put("acc_id", BaseApplication.curUser.getAcc_id());
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.QUERY_BANKLIST), params, BankCardInfoResponse.class, listener);
	}

	/************************ 发送绑卡支付请求绑卡支付 *****************************/
	public static void questBindCard(Context context, BankCardInfoEntity info, String totalPrice, String o_id, String tran_type, B2CListener<BindCardResponse> listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		String time = String.valueOf(getCurrentTime());
		params.put("acc_id", BaseApplication.curUser.getAcc_id());
		params.put("bindid", info.getBindid());
		params.put("o_id", o_id);
		params.put("total_prices", totalPrice);
		params.put("tran_type", tran_type);
		params.put("fcb_url", "");
		params.put("term_id", StringUtils.getIMIE(context));
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		params.put("term_id", StringUtils.getIMIE(context));
		params.put("term_id", StringUtils.getIMIE(context));
		doHttp(B2CConstants.getUrl(B2CConstants.MOTHOD_BINDCARD), params, BindCardResponse.class, listener);
	}

	/************************ 发送易宝短信验证码 *****************************/
	public static void questYBCode(String o_id, B2CListener<YpCodeResponse> listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		String time = String.valueOf(getCurrentTime());
		params.put("acc_id", BaseApplication.curUser.getAcc_id());
		params.put("o_id", o_id);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.MOTHOD_YBCODE), params, YpCodeResponse.class, listener);
	}

	/************************ **********确认支付 *****************************/
	public static void payOrder(String o_id, String code, B2CListener<B2CBaseResponse> listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		String time = String.valueOf(getCurrentTime());
		params.put("acc_id", BaseApplication.curUser.getAcc_id());
		params.put("o_id", o_id);
		params.put("v_code", code);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.MOTHOD_YBPAY), params, B2CBaseResponse.class, listener);
	}

	public static void deleteCard(String bindId, B2CListener<B2CBaseResponse> listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		String time = String.valueOf(getCurrentTime());
		params.put("acc_id", BaseApplication.curUser.getAcc_id());
		params.put("bindid", bindId);
		params.put("time", time);
		params.put("sign", getLoginSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.MOTHOD_DELETECARD), params, B2CBaseResponse.class, listener);
	}

	public static void update(Context context, B2CListener<UpdateResponse> listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		String time = String.valueOf(getCurrentTime());
		params.put("version", StringUtils.getAppVersionName(context));
		params.put("app_type", "ARD");
		params.put("time", time);
		params.put("sign", getSign(time));
		doHttp(B2CConstants.getUrl(B2CConstants.METHOD_UODATE), params, UpdateResponse.class, listener);
	}

	/*************************************************************************/

	/***************************** Base **************************************/
	// 通用的string回调,其他方法都是gson->response bean回调
	// public static void request4String(B2CListener<String> listener) {
	// doHttp("url", new HashMap<String, Object>(), String.class, listener);
	// }

	// 方法内框架可切换,需要处理自定义通用回调接口B2CListener
	private static <T> void doHttp(final String url, final Map<String, Object> params, final Class<T> clazz, final B2CListener<T> listener) {

		BDVolleyHttp.postJsonObject(url, params, clazz,
		// 只分发回调,处理在实现回调类B2CSimpleListener中
				new BDListener<T>() {

					@Override
					public void onErrorResponse(VolleyError error) {
						if (listener != null) {
							// error转换,第三个构造函数为框架自定义错误类
							// 如果自定义错误类是可以获取ret和ret_msg的,即可以解析成B2CBaesResponse的则获取ret处理
							if (error instanceof BDParseError) {
								BDParseError parseError = (BDParseError) error;
								try {
									String json = parseError.getMessage().replace("\\\"", "\"").replace("\"{", "{").replace("}\"", "}");
									B2CBaseResponse resBean = new Gson().fromJson(json, B2CBaseResponse.class);
									// json解析错误,如果ret为0,也视为错误
									if (resBean.getRet().equals(ResponseConstants.SUCCESS)) {
										listener.onErrorResponse(new B2CError(ResponseConstants.RESPONSE_ERROR_CODE, ResponseConstants.RESPONSE_ERROR, error));
									} else {
										listener.onErrorResponse(new B2CError(resBean.getRet(), resBean.getRet_msg(), null));
									}
								} catch (JsonParseException e) {
									e.printStackTrace();
									listener.onErrorResponse(new B2CError(ResponseConstants.RESPONSE_ERROR_CODE, ResponseConstants.RESPONSE_ERROR, error));
								}
							} else if (error instanceof TimeoutError) {
								listener.onErrorResponse(new B2CError(ResponseConstants.E_TIMEOUT_ERROR_CODE, ResponseConstants.E_TIMEOUT_ERROR, error));
							} else if (error instanceof NoConnectionError) {
								listener.onErrorResponse(new B2CError(ResponseConstants.NET_ERROR_CODE, ResponseConstants.NET_ERROR, error));
							} else {
								listener.onErrorResponse(new B2CError(ResponseConstants.RESPONSE_ERROR_CODE, ResponseConstants.RESPONSE_ERROR, error));
							}
						} else {
							throw new RuntimeException("listener is null");
						}
					}

					@Override
					public void onResponse(T response) {
						if (listener != null) {
							// 业务需要,数据成功返回,但是ret为非0的,也视为error
							if (response instanceof B2CBaseResponse) {
								B2CBaseResponse resBean = (B2CBaseResponse) response;
								if (resBean.getRet().equals(ResponseConstants.SUCCESS)) {
									listener.onResponse(response);
								} else {
									listener.onErrorResponse(new B2CError(resBean.getRet(), resBean.getRet_msg(), response));
								}
							} else {
								listener.onErrorResponse(new B2CError(ResponseConstants.RESPONSE_ERROR_CODE, ResponseConstants.RESPONSE_ERROR, response));
								LogUtils.show("DD", "response bean not extends B2CBaseResponse");
							}
						} else {
							throw new RuntimeException("listener is null");
						}

					}
				});
	}
}