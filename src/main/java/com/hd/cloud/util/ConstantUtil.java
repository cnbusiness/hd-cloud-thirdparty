package com.hd.cloud.util;

/**
 * 
 * @ClassName: ConstantUtil
 * @Description: 常量字典
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:24:57
 *
 */
public class ConstantUtil {

	// 检查一分钟是否已经有记录 type
	public static final int ONE_MINUTE_CHECK_TYPE = 1;

	// 检查一天最多10条 type
	public static final int ONE_DAY_CHECK_TYPE = 2;

	// 短信发送限制数
	public static final int SEND_SMS_LIMIT = 10;

	// 一分钟是否已经有记录
	public static final int ONE_MINUTE_RECORD = -1;

	// 每天发送限制
	public static final int ONE_DAY_RECORD = -2;

}
