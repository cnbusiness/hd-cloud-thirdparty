package com.hd.cloud.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.hd.cloud.service.ThirdPartySmsService;
import com.hd.cloud.sms.mwgate.CHttpPost;
import com.hd.cloud.sms.mwgate.ConfigManager;
import com.hd.cloud.sms.mwgate.Message;
import com.hlb.cloud.bo.BoUtil;

/**
 * 
 * @ClassName: MoxianMontnetsSmsServiceImpl
 * @Description: montnets type=8
 * @author yaojie yao.jie@moxiangroup.com
 * @Company moxian
 * @date 2016年1月18日 下午5:22:31
 *
 */
@Slf4j
@Service("MoxianMontnetsSmsServiceImpl")
public class MoxianMontnetsSmsServiceImpl implements ThirdPartySmsService {

	@Override
	public BoUtil sendSms(String phone, String msg) {
		if (phone.substring(0, 2).equals("86")) {// 中国的
			phone = phone.substring(2, phone.length());
		}
		String userid = "WE105GV";
		String pwd = "8T20pvP";
		boolean isEncryptPwd = true;
		log.debug("###Call montnets发送短信Start: phone:{},msg:{}", phone, msg);
		BoUtil boUtil = null;
		// 日期格式定义
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
		// 主IP信息 必填
		String masterIpAddress = "api01.monyun.cn:7901";
		// 备IP1 选填
		String ipAddress1 = "api01.monyun.cn:7901";
		// 备IP2 选填
		String ipAddress2 = null;
		// 备IP3 选填
		String ipAddress3 = null;
		// 设置IP
		ConfigManager.setIpInfo(masterIpAddress, ipAddress1, ipAddress2,
				ipAddress3);
		try {
			// 参数类
			Message message = new Message();
			// 实例化短信处理对象
			CHttpPost cHttpPost = new CHttpPost();
			// 设置账号 将 userid转成大写,以防大小写不一致
			message.setUserid(userid.toUpperCase());
			// 判断密码是否加密。
			// 密码加密，则对密码进行加密
			if (isEncryptPwd) {
				// 设置时间戳
				String timestamp = sdf.format(Calendar.getInstance().getTime());
				message.setTimestamp(timestamp);
				// 对密码进行加密
				String encryptPwd = cHttpPost.encryptPwd(message.getUserid(),
						pwd, message.getTimestamp());
				// 设置加密后的密码
				message.setPwd(encryptPwd);
			} else {
				// 设置密码
				message.setPwd(pwd);
			}
			// 设置手机号码 此处只能设置一个手机号码
			message.setMobile(phone);
			// 设置内容
			message.setContent(msg);
			// 设置扩展号
			message.setExno("");
			// 用户自定义流水编号
			message.setCustid("");
			// 自定义扩展数据
			message.setExdata("");
			// 业务类型
			message.setSvrtype("SMS001");
			// 返回的平台流水编号等信息
			StringBuffer msgId = new StringBuffer();
			// 返回值
			int result = -310099;
			// 发送短信
			result = cHttpPost.singleSend(message, msgId);
			log.info("###montnets发送短信格式{},返回状态码:{}", message, result);
			// result为0:成功;非0:失败
			if (result == 0) {
				log.info("发送验证码提交成功 : {}", msgId.toString());
				boUtil = BoUtil.getDefaultTrueBo();
			} else {
				log.info("发送验证码提交失败错误码： {}", result);
				boUtil = BoUtil.getDefaultFalseBo();
			}
		} catch (Exception e) {
			// 异常处理
			e.printStackTrace();
			boUtil = BoUtil.getDefaultFalseBo();
		}

		log.info("###montnets发送短信End");
		return boUtil;
	}
}
