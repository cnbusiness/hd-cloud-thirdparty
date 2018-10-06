package com.hd.cloud.service.impl;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hd.cloud.service.ThirdPartySmsService;
import com.hlb.cloud.bo.BoUtil;

/**
 * 
 * @ClassName: ThirdPartyNexmoServiceImpl
 * @Description: nexmo
 * @author yaojie yao.jie@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年7月9日 上午9:19:25
 *
 */
@Slf4j
@Service("ThirdPartyNexmoSmsServiceImpl")
public class ThirdPartyNexmoServiceImpl implements ThirdPartySmsService {

	@Override
	public BoUtil sendSms(String phone, String message) {
		// 处理马来西亚的号码格式（多出了个0）
		phone = handlePhone(phone);
		log.info("###Call nexmo发送短信Start: phone:{},message:{}", phone, message);
		HttpClient httpClient = new DefaultHttpClient();
		String loginUrl = "https://rest.nexmo.com/sms/json";
		HttpPost httpPatch = new HttpPost(loginUrl);
		// httpPatch.setHeader("Content-type", "application/json");
		httpPatch.setHeader("Charset", HTTP.UTF_8);
		httpPatch.setHeader("Accept", "application/json");
		httpPatch.setHeader("Accept-Charset", HTTP.UTF_8);
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		try {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("api_key", "3ad7b6e8"));
			nvps.add(new BasicNameValuePair("api_secret", "201b1c9735d729ba"));
			nvps.add(new BasicNameValuePair("from", "NEXMO"));
			nvps.add(new BasicNameValuePair("to", phone));
			nvps.add(new BasicNameValuePair("text", message));
			httpPatch.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpClient.execute(httpPatch);
			log.info(" nexmo response结果 : {} ", response);
			if (response != null
					&& response.getStatusLine().getStatusCode() == HttpStatus.OK
							.value()) {
				String result = EntityUtils.toString(response.getEntity());
				log.info("###nexmo发送短信成功返回结果 ： {}", result);
				boUtil = BoUtil.getDefaultTrueBo();
			} else {
				log.info("###nexmo发送短信响应失败");
				boUtil = BoUtil.getDefaultFalseBo();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("###nexmo发送短信失败，异常：{}", e);
			boUtil = BoUtil.getDefaultFalseBo();
		}
		log.info("###nexmo发送短信End");
		return boUtil;
	}

	/**
	 * 处理马来西亚号码 多出个0的格式
	 * 
	 * @param phone
	 * @return
	 */
	public String handlePhone(String phone) {
		String prefix = phone.substring(0, 3);
		if ("600".equals(prefix)) {
			phone = "60" + phone.substring(3, phone.length());
		}
		return phone;
	}
}
