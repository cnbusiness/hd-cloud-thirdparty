package com.hd.cloud.rest;

import javax.inject.Inject;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd.cloud.bo.Sms;
import com.hd.cloud.service.SmsService;
import com.hd.cloud.vo.GetSmsVo;
import com.hd.cloud.vo.SmsVo;
import com.hlb.cloud.bo.BoUtil;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: SmsResource
 * @Description: 发送短信
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午2:53:13
 *
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("sms")
public class SmsResource {

	@Inject
	private SmsService smsService;

	/**
	 * 
	 * @Title: sendSms
	 * @param:
	 * @Description: 发送验证类短信
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "发送验证类短信", notes = "发送验证类短信")
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil sendSms(@RequestBody Sms sms) {
		return smsService.sendSms(sms);
	}

	/**
	 * 
	 * @Title: sendBusinessSms
	 * @param:
	 * @Description: 发送业务短信
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "发送业务短信", notes = "发送业务短信")
	@ResponseBody
	@RequestMapping(value = "/business", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil sendBusinessSms(@RequestBody SmsVo smsVo) {
		BoUtil boUtil = smsService.sendBusinessSms(smsVo.getMobile(), smsVo.getMessage());
		log.info("#####boUtil:{}", boUtil);
		return boUtil;
	}

	/**
	 * 
	 * @Title: checkCaptchaByPhoneNo
	 * @param:
	 * @Description: 检测验证码
	 * @return Sms
	 */
	@ApiOperation(httpMethod = "POST", value = "检测短信", notes = "检测短信")
	@ResponseBody
	@RequestMapping(value = "/verificationcode", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public Sms checkCaptchaByPhoneNo(@RequestBody GetSmsVo getSmsVo) {
		return smsService.getCaptchaByCountryCodeAndRocordAccount(getSmsVo.getMobile(), getSmsVo.getCaptchaType());
	}

	/**
	 * 
	 * @Title: updateSms
	 * @param:
	 * @Description: 修改短信验证码状态
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "PUT", value = "修改短信验证码状态", notes = "修改短信验证码状态")
	@ResponseBody
	@RequestMapping(value = "/verificationcode", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public BoUtil updateSms(final @RequestBody Sms sms) {
		int result = smsService.update(sms);
		if (result > 0) {
			return BoUtil.getDefaultTrueBo();
		} else {
			return BoUtil.getDefaultFalseBo();
		}
	}
}
