package com.hd.cloud.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hd.cloud.ThirdPartyConfig.ThirdPartySmsConfig;
import com.hd.cloud.service.ThirdPartySmsService;
import com.hd.cloud.sms.request.SmsSendRequest;
import com.hd.cloud.sms.response.SmsSendResponse;
import com.hd.cloud.sms.util.ChuangLanSmsUtil;
import com.hlb.cloud.bo.BoUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("ThirdPartyChuangLanSmsServiceImpl")
public class ThirdPartyChuangLanSmsServiceImpl implements ThirdPartySmsService {

	@Inject
	private ThirdPartySmsConfig thirdPartySmsConfig;

	@Override
	public BoUtil sendSms(String mobile, String message) {
		// 请求地址请登录253云通讯自助通平台查看或者询问您的商务负责人获取
		String smsSingleRequestServerUrl = thirdPartySmsConfig.getInfoBipDomain();
		// 状态报告
		String report = "true";
		SmsSendRequest smsSingleRequest = new SmsSendRequest(thirdPartySmsConfig.getUsername(),
				thirdPartySmsConfig.getPassword(), message, mobile, report);

		String requestJson = JSON.toJSONString(smsSingleRequest);
		log.info("##############before request string is:{}", requestJson);
		String response = ChuangLanSmsUtil.sendSmsByPost(smsSingleRequestServerUrl, requestJson);
		log.info("##############response after request result is :{}", response);
		SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
		log.info("##############response  toString is :{}", smsSingleResponse);
		if (smsSingleResponse != null) {
			if (smsSingleResponse.getCode().equals("0")) {
				return BoUtil.getDefaultTrueBo();
			} else {
				BoUtil boUtil = BoUtil.getDefaultFalseBo();
				log.info("##############smsSingleResponse :{}", smsSingleResponse);
				return boUtil;
			}
		} else {
			return BoUtil.getDefaultFalseBo();
		}
	}

}
