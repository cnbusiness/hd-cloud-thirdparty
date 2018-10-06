package com.hd.cloud.dao.sql;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

import com.hd.cloud.bo.Sms;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: SmsSqlProvider
 * @Description: 短信sql
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午2:18:05
 *
 */
@Slf4j
public class SmsSqlProvider {

	/**
	 * 
	 * @Title: createUserProfile
	 * @param: Sms
	 *             model
	 * @Description: 新增短信记录
	 * @return String
	 */
	public String createSms(final Sms sms) {
		return new SQL() {
			{
				INSERT_INTO("sms_verify_record_br");
				VALUES("verify_rocord_account", "#{recordAccount}");
				VALUES("verify_record_verify_code", "#{code}");
				VALUES("verify_record_code_itype", "#{codeType}");
				VALUES("verify_record_verify_itype", "#{status}");
				VALUES("city_dict_intl_code", "#{countryCode}");
				VALUES("active_flag", "#{activeFlag}");
			}
		}.toString();
	}

	/**
	 * 
	 * @Title: update
	 * @param: Sms
	 *             model
	 * @Description: 修改信息
	 * @return String
	 */
	public String update(Sms sms) {
		return new SQL() {
			{
				UPDATE("sms_verify_record_br");
				if (sms.getActiveFlag() != null) {
					SET("active_flag = #{activeFlag}");
				}
				if (sms.getStatus() != 0) {
					SET("verify_record_verify_itype = #{status}");
				}
				WHERE("sms_verify_record_br_seq = #{id}");
			}
		}.toString();
	}

	/**
	 * 
	 * @Title: getCaptchaByCountryCodeAndRocordAccount
	 * @param:
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return String
	 */
	public String getCaptchaByCountryCodeAndRocordAccount(Map<String, Object> map) {
		String sql = new SQL() {
			{
				SELECT(" sms_verify_record_br_seq,verify_rocord_account,verify_record_verify_code,verify_record_code_itype,verify_record_verify_itype,city_dict_intl_code,create_time  ");
				FROM("sms_verify_record_br ");
				WHERE(" verify_record_verify_itype=0  and active_flag='y' and create_time>=DATE_SUB(NOW(),INTERVAL 10 MINUTE) and verify_rocord_account="
						+ map.get("param1") + " and verify_record_code_itype=" + map.get("param2"));
				ORDER_BY("  sms_verify_record_br_seq desc  limit 0,1");
			}
		}.toString();
		log.info("###sql:{}", sql);
		return sql;
	}

	/**
	 * 
	 * @Title: getCountByCodeTypeAndRecordAccount
	 * @param: Sms
	 *             model
	 * @Description: 时间查询
	 * @return String
	 */
	public String getCountByCodeTypeAndRecordAccount(final Sms sms) {
		// 一分钟以内
		if (sms.getCheckType() == 1) {
			return new SQL() {
				{
					SELECT("count(1)");
					FROM("sms_verify_record_br");
					WHERE("  active_flag='y' AND verify_record_code_itype=#{codeType} AND verify_rocord_account=#{recordAccount} AND create_time>=DATE_SUB(NOW(), INTERVAL 1 MINUTE) AND create_time<=NOW()");
				}
			}.toString();
		} else {
			return new SQL() {
				{
					SELECT("count(1)");
					FROM("sms_verify_record_br");
					WHERE("  active_flag='y' AND verify_record_code_itype=#{codeType} AND verify_rocord_account=#{recordAccount} AND  to_days(create_time) = to_days(NOW())");
				}
			}.toString();
		}

	}
}
