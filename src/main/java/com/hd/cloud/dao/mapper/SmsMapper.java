package com.hd.cloud.dao.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.hd.cloud.bo.Sms;
import com.hd.cloud.dao.sql.SmsSqlProvider;

/**
 * 
 * @ClassName: SmsMapper
 * @Description: 短信mapper
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午2:17:20
 *
 */
@Mapper
public interface SmsMapper {

	/**
	 * @Title: save
	 * @Description: 保存短信发送记录
	 * @Table: sms_verify_record_br 短信发送记录表
	 */
	@InsertProvider(type = SmsSqlProvider.class, method = "createSms")
	@SelectKey(keyProperty = "id", before = false, resultType = int.class, statement = {
			"SELECT LAST_INSERT_ID() AS sms_verify_record_br" })
	int save(Sms sms);

	/**
	 * @Title: update
	 * @Description: 更改短信发送记录
	 * @Table: sms_verify_record_br 短信发送记录表
	 */
	@UpdateProvider(type = SmsSqlProvider.class, method = "update")
	int update(Sms sms);

	/**
	 * @Title: getCaptchaByCountryCodeAndRocordAccount
	 * @Description: 通过类型和 电话号码/邮箱 查询短信发送记录
	 * @Table: sms_verify_record_br 短信发送记录表
	 */
	@SelectProvider(type = SmsSqlProvider.class, method = "getCaptchaByCountryCodeAndRocordAccount")
	@Results(value = {
			@Result(property = "id", column = "sms_verify_record_br_seq", javaType = long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "recordAccount", column = "verify_rocord_account", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "code", column = "verify_record_verify_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "codeType", column = "verify_record_code_itype", javaType = int.class, jdbcType = JdbcType.TINYINT),
			@Result(property = "status", column = "verify_record_verify_itype", javaType = int.class, jdbcType = JdbcType.TINYINT),
			@Result(property = "countryCode", column = "city_dict_intl_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "createTime", column = "create_time", javaType = String.class, jdbcType = JdbcType.TIMESTAMP), })
	Sms getCaptchaByCountryCodeAndRocordAccount(@Param("param1") String recordAccount, @Param("param2") int codeType);

	/**
	 * @Title: getCountByCodeTypeAndRecordAccount
	 * @Description: 通过类型和 电话号码/邮箱 查询 发送次数
	 * @Table: sms_verify_record_br 短信发送记录表
	 */
	@SelectProvider(type = SmsSqlProvider.class, method = "getCountByCodeTypeAndRecordAccount")
	int getCountByCodeTypeAndRecordAccount(Sms sms);

}
