package com.hd.cloud.service;

import com.hlb.cloud.bo.BoUtil;

/**
 * 
 * @ClassName: ThirdPartySmsService
 * @Description: 第三方发送短信实现接口
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午2:22:59
 *
 */
public interface ThirdPartySmsService {
	/**
	 * 
	 * @Title: sendSms
	 * @param: sms 短信息实体
	 * @Description: 发送短信
	 * @return BoUtil
	 */
	BoUtil sendSms(String mobile, String message);
}
