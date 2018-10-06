package com.hd.cloud.service.impl;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hd.cloud.bo.Sms;
import com.hd.cloud.dao.SmsDao;
import com.hd.cloud.service.SmsService;
import com.hd.cloud.service.ThirdPartySmsService;
import com.hd.cloud.util.ConstantUtil;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.util.CommonConstantUtil;
import com.hlb.cloud.util.RandomUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: SmsServiceImpl
 * @Description: 短信
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午2:20:11
 *
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

	@Inject
	private SmsDao smsDao;

	@Inject
	@Qualifier("ThirdPartyChuangLanSmsServiceImpl")
	private ThirdPartySmsService thirdPartySmsService;
	
	@Inject
	@Qualifier("ThirdPartyNexmoSmsServiceImpl")
	private ThirdPartySmsService thirdPartyNexmoSmsService;
	
	@Inject
	@Qualifier("MoxianMontnetsSmsServiceImpl")
	private ThirdPartySmsService thirdPartyMontnetsSmsService;
	
	/**
	 * codeType 对应的数值 1 手机号码注册 2 手机找回密码 3 邮箱找回密码 4 邮箱绑定 5 设置支付密码 6 找回支付密码 7 绑定手机 8
	 * 更换手机
	 * 
	 * @Title: sendSms
	 * @param: sms
	 *             model (checkType 为查询的时间区间类型 1为查询一分钟内 2为查询一天内)
	 * @Description: 发送验证短信
	 * @return BoUtil data -1 为1分钟已经有记录 -2 每天发送限制 >0 保存成功
	 */
	@Override
	public BoUtil sendSms(Sms sms) {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		// 检测短信发送的限制 一分钟是否已经有记录
		sms.setCheckType(ConstantUtil.ONE_MINUTE_CHECK_TYPE);
		int oneminuteSendCount = smsDao.getCountByCodeTypeAndRecordAccount(sms);
		log.debug("###发送验证短信Start:" + sms);
		if (oneminuteSendCount > 0) {
			boUtil.setData(ConstantUtil.ONE_MINUTE_RECORD);
			return boUtil;
		}
		log.debug("###一分钟发送短信次数:" + oneminuteSendCount);
		// 检测短信发送的限制 一条最多10条
		sms.setCheckType(ConstantUtil.ONE_DAY_CHECK_TYPE);
		int sendCount = smsDao.getCountByCodeTypeAndRecordAccount(sms);
		if (sendCount >= ConstantUtil.SEND_SMS_LIMIT) {
			boUtil.setData(ConstantUtil.ONE_DAY_RECORD);
			return boUtil;
		}
		String code = RandomUtil.createRandCode(4);
		sms.setCode(code);
		log.debug("###一天发送短息次数:" + sendCount);
		boUtil.setData(0);
		String mobile = sms.getRecordAccount();
		String message = getSmsMessage(sms);
		if (!mobile.startsWith(sms.getCountryCode())) {// 手机号码没有国家码，就加上国家码
			mobile = sms.getCountryCode() + mobile;
		}

		// 入库
		smsDao.save(sms);
		log.debug("###保存数据库:" + sms);
		if (sms.getId() > 0) {
			boUtil = BoUtil.getDefaultTrueBo();
			boUtil.setData(sms.getId());
			try {
				if(sms.getCountryCode().equals("86")) {
					mobile=mobile.replaceFirst(sms.getCountryCode(), "");
					boUtil = thirdPartyMontnetsSmsService.sendSms(mobile, message);
				}else{
					boUtil = thirdPartyNexmoSmsService.sendSms(mobile, message);
				}
				log.debug("###发送短信结束:" + boUtil);
			} catch (Exception e) {
				// 发送失败 标记一下
				sms.setActiveFlag(CommonConstantUtil.ACTIVE_FLAG_D);
				smsDao.update(sms);
				log.debug("###调用kafka失败，回滚数据:" + sms);
			}
		}
		log.debug("###发送验证短信End:" + boUtil);
		return boUtil;
	}

	/**
	 * 
	 * @Title: getSmsMessage
	 * @param:
	 * @Description: 获取短信模板
	 * @return String
	 */
	private String getSmsMessage(Sms sms) {
		String message = "";
		if (sms.getCodeType() == 1) {
			if(sms.getCountryCode().equals("86")){
				message = "您的验证码是 " + sms.getCode() + "，在10分钟内有效。如非本人操作请忽略本短信。 ";
			}else{
				message = "The registered SMS verification code is: " + sms.getCode() + " If you have not submitted the request, please ignore it";
			}
		} else if (sms.getCodeType() == 2) {
			if(sms.getCountryCode().equals("86")){
				message = "您的验证码是 " + sms.getCode() + "，在10分钟内有效。如非本人操作请忽略本短信。 ";
			}else{
				message = "Dear users, your verification code is " + sms.getCode() + "  valid for 10 minutes";
			}
		} else if (sms.getCodeType() == 5) {
			if(sms.getCountryCode().equals("86")){
				message = "您的验证码是 " + sms.getCode() + "，在10分钟内有效。如非本人操作请忽略本短信。 ";
			}else{
				message = "Dear users, your verification code is " + sms.getCode() + "  valid for 10 minutes";
			}
		} else if (sms.getCodeType() == 6) {
			if(sms.getCountryCode().equals("86")){
				message = "您的验证码是 " + sms.getCode() + "，在10分钟内有效。如非本人操作请忽略本短信。 ";
			}else{
				message = "Dear users, your verification code is " + sms.getCode() + "  valid for 10 minutes";
			}
		} else if (sms.getCodeType() == 7) {
			if(sms.getCountryCode().equals("86")){
				message = "您的验证码是 " + sms.getCode() + "，在10分钟内有效。如非本人操作请忽略本短信。 ";
			}else{
				message = "Dear users, your verification code is " + sms.getCode() + "  valid for 10 minutes";
			}
		} else if (sms.getCodeType() == 8) {
			if(sms.getCountryCode().equals("86")){
				message = "您的验证码是 " + sms.getCode() + "，在10分钟内有效。如非本人操作请忽略本短信。 ";
			}else{
				message = "Dear users, your verification code is " + sms.getCode() + "  valid for 10 minutes";
			}
		}
		return message;
	}

	// 通过电话号码 区号 类型 获取验证码
	@Override
	public Sms getCaptchaByCountryCodeAndRocordAccount(String recordAccount, int codeType) {
		return smsDao.getCaptchaByCountryCodeAndRocordAccount(recordAccount, codeType);
	}

	@Override
	public int update(Sms sms) {
		return smsDao.update(sms);
	}

	@Override
	public BoUtil sendBusinessSms(String mobile, String message) {
		return thirdPartySmsService.sendSms(mobile, message);
	}

}
