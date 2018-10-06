package com.hd.cloud.service.impl;

import java.util.HashMap;

import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hd.cloud.ThirdPartyConfig.ThirdPartySmsConfig;
import com.hd.cloud.service.ThirdPartySmsService; 
import com.hlb.cloud.bo.BoUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @ClassName: ThirdPartyInfoBipSmsServiceImpl
 * @Description: infobip
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午2:25:40
 *
 */
@Slf4j
@Service("ThirdPartyInfoBipSmsServiceImpl")
public class ThirdPartyInfoBipSmsServiceImpl implements ThirdPartySmsService {

	@Inject
	private ThirdPartySmsConfig thirdPartySmsConfig;

	@Override
	public BoUtil sendSms(String phone, String message) {
		// 处理马来西亚的号码格式（多出了个0）
		phone = handlePhone(phone);
		log.info("###Call infobip发送短信Start: phone:{},message:{}", phone, message);
		String path = thirdPartySmsConfig.getInfoBipDomain();
		String username = thirdPartySmsConfig.getUsername();
		String password = thirdPartySmsConfig.getPassword();
		String userPwd = username + ":" + password;
		String encryUserPwd = new Base64().encodeAsString(userPwd.getBytes());// 加密账号密码
		String account = "Basic " + encryUserPwd;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(path);
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Authorization", account);
		message += "【CoinsDream】";
		JSONObject params = new JSONObject();
		params.put("from", "796");
		params.put("to", phone);
		params.put("text", message);
		log.info("###infobip发送短信格式{}", params);
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		try {
			StringEntity stringEntity = new StringEntity(params.toString(), "utf-8");
			httpPost.setEntity(stringEntity);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();// 设置请求连接超时
																														// 6秒
																														// 和传输超时时间6秒
			httpPost.setConfig(requestConfig);
			HttpResponse res = httpclient.execute(httpPost);
			if (res.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
				String result = EntityUtils.toString(res.getEntity());
				JSONObject responseJson = JSONObject.fromObject(result);
				log.info("###infobip发送短信返回结果{}", responseJson);
				HashMap<String, String> hashMap = new HashMap<String, String>();
				JSONArray jsonArray = responseJson.getJSONArray("messages");
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject row = jsonArray.getJSONObject(i);
					hashMap.put("messageId", row.get("messageId") == null ? "" : row.get("messageId").toString());
					Object groupId = row.getJSONObject("status").get("groupId");
					hashMap.put("groupId", groupId == null ? "-1" : groupId.toString());
				}
				if (!hashMap.isEmpty()) {
					// 0 == 消息已接收 ，1== 消息处于待接收状态 ，2 == 消息无法发送 ，3 == 消息已发送 ，4 == 消息已过期， 5== 消息被拒绝
					if ("0".equalsIgnoreCase(hashMap.get("groupId")) || "1".equalsIgnoreCase(hashMap.get("groupId"))
							|| "3".equalsIgnoreCase(hashMap.get("groupId"))) {
						log.info("###infobip发送短信成功");
						boUtil = BoUtil.getDefaultTrueBo();
					} else {
						log.info("###infobip发送短信返回结果提示失败，并无报错异常");
						boUtil = BoUtil.getDefaultFalseBo();
					}
				} else {
					log.info("###infobip发送短信并无返回结果");
					boUtil = BoUtil.getDefaultFalseBo();
				}
			} else {
				log.info("###infobip发送短信响应失败");
				boUtil = BoUtil.getDefaultFalseBo();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("###infobip发送短信失败，异常：{}", e);
			boUtil = BoUtil.getDefaultFalseBo();
		}
		log.info("###infobip发送短信End");
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
