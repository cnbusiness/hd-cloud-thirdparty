package com.hd.cloud.dao;

import com.hd.cloud.bo.Sms;

/**
 * 
 * @ClassName: SmsDao
 * @Description: 短信管理
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午2:16:08
 *
 */
public interface SmsDao {
	/**
	 * 
	 * @Title: save
	 * @param: sms
	 *             短信息实体
	 * @Description: 保存
	 * @return int
	 */
	int save(Sms sms);

	/**
	 * 
	 * @Title: update
	 * @param:
	 * @Description: 更新发送信息(状态、active_flag)
	 * @return int
	 */
	int update(Sms sms);

	/**
	 * 
	 * @Title: getCaptchaByCountryCodeAndRocordAccount
	 * @param: recordAccount
	 *             电话号码/邮箱 codeType 类型 1 手机号码注册 2 手机找回密码 3 邮箱找回密码
	 * @Description: 通过类型和 电话号码/邮箱 查询
	 * @return Sms
	 */
	Sms getCaptchaByCountryCodeAndRocordAccount(String recordAccount, int codeType);

	/**
	 * 
	 * @Title: getCountByCodeTypeAndRecordAccount
	 * @param: sms
	 *             短信model
	 * @Description:通过类型和 电话号码/邮箱 查询 发送次数
	 * @return int
	 */
	int getCountByCodeTypeAndRecordAccount(Sms sms);
}
