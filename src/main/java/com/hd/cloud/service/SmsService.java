package com.hd.cloud.service;

import com.hd.cloud.bo.Sms;
import com.hlb.cloud.bo.BoUtil;

/**
 * 
 * @ClassName: SmsService
 * @Description: 短信服务
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午2:19:12
 *
 */
public interface SmsService {

	/**
	 * 
	 * @Title: update
	 * @param: Sms
	 *             model
	 * @Description: 修改状态
	 * @return int
	 */
	int update(Sms sms);

	/**
	 * 
	 * @Title: getCaptchaByCountrycodeAndMobile
	 * @param:countryCode 国家码
	 *                        mobile 电话号码 codeType 类型
	 * @Description: 通过电话号码 区号 类型 获取验证码
	 * @return Sms
	 */
	Sms getCaptchaByCountryCodeAndRocordAccount(String recordAccount, int codeType);

	/**
	 * 
	 * @Title: sendSms
	 * @param: sms
	 *             短信息实体
	 * @Description: 发送短信
	 * @return BoUtil
	 */
	BoUtil sendSms(Sms sms);

	/**
	 * 
	 * @Title: sendBusinessSms
	 * @param: 电话号码
	 *             message 短信息内容
	 * @Description: 发送业务短信
	 * @return BoUtil
	 */
	BoUtil sendBusinessSms(String mobile, String message);

}
