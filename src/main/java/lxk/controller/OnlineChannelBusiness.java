package lxk.controller;

import org.springframework.beans.factory.InitializingBean;

/**
 * 与渠道商通信的接口定义
 * 
 * 
 *implements OnlineChannelBusiness
 */
public interface OnlineChannelBusiness extends InitializingBean {
	 
	/** 绑定号 */
	String bindId = "bindId";
	/** 申请号 */
	String applyNo = "applyNo";
	/** 商户号 */
	String merchantNo = "merchantNo";
	/** 支付要素 */
	String payElements = "payElements";
	/** 订单金额 */
	String orderAmount = "orderAmount";
	/** 本系统流水号 */
	String serialNumber = "serialNumber";
	/** 渠道请求号 */
	String bankOrderId = "bankOrderId";
	/** 支付渠道流水号 */
	String consumeOrderId = "consumeOrderId";
	/** 渠道支付说明 */
	String channelDescription = "channelDescription";
	/** 渠道支付时间 */
	String channelCompleteDate = "channelCompleteDate";
	/** 渠道返回的所有值 */
	String channelResponseJson = "channelResponseJson";
	/** 渠道返回的手续费（成本） */
	String channelfee = "channelfee";
	/** t0接口打款结果码 */
	String t0PayResult = "t0PayResult";
	/** t0接口打款描述 */
	String t0PayDesc = "t0PayDesc";
	/** reqSn通道方订单号 */
	String reqSn = "reqSn";
	/** bankType交易使用卡种*/
    String bankType = "bankType";
    String retMsg="retMsg";
	/**
	 * 是否需要重定向
	 */
	public boolean needRedirect();
	
	/**
	 * 渠道是够大商户模式
	 * @return
	 */
	public boolean isBigMerchant();

	
}
