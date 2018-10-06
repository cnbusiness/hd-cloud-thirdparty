package com.hd.cloud.dao.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.hd.cloud.bo.Sms;
import com.hd.cloud.dao.SmsDao;
import com.hd.cloud.dao.mapper.SmsMapper;

/**
 * 
 * @ClassName: SmsDaoMybatisImpl
 * @Description: 短信
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午2:17:08
 *
 */
@Repository
public class SmsDaoMybatisImpl implements SmsDao {

	@Inject
	private SmsMapper smsMapper;

	@Override
	public int save(Sms sms) {
		sms.setActiveFlag("y");
		return smsMapper.save(sms);
	}

	@Override
	public int getCountByCodeTypeAndRecordAccount(Sms sms) {
		return smsMapper.getCountByCodeTypeAndRecordAccount(sms);
	}

	@Override
	public Sms getCaptchaByCountryCodeAndRocordAccount(String recordAccount, int codeType) {
		return smsMapper.getCaptchaByCountryCodeAndRocordAccount(recordAccount, codeType);
	}

	@Override
	public int update(Sms sms) {
		return smsMapper.update(sms);
	}

}
