package com.hd.cloud.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @ClassName: SequenceUtil
 * @Description: 生成订单号
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年12月18日 下午5:28:44
 *
 */
final public class SequenceUtil {

	private static SequenceUtil sequenceUtil = null;
	private static String dateOrderString = "", dateRebateString = "", dateMsgString = "", dateRefundString = "";
	private static AtomicInteger orderAi = new AtomicInteger();
	private static AtomicInteger rebateOrderAi = new AtomicInteger();
	private static AtomicInteger msgAi = new AtomicInteger();
	private final static String PREFIX = "QR";
	private final static String REBATE_PREFIX = "MF";
	private final static String MSG_PREFIX = "SM";
	private final static String REFUND_PREFIX = "IO";
	private final static int fixLen = 6;
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");

	/**
	 * 构造函数
	 */
	private SequenceUtil() {
	}

	/**
	 * 
	 * getInstance
	 * 
	 * @param
	 * @return SequenceUtil
	 */
	public synchronized static SequenceUtil getInstance() {
		if (null == sequenceUtil) {
			sequenceUtil = new SequenceUtil();
		}
		return sequenceUtil;
	}

	/**
	 * 新版兑换劵和物流商品产品定义订单生成规则为：OA + 150515100013(yyMMddHHmmss) + 1(服务器节点编号) + 1(自增流水号)
	 * + 3151（随机生成数，位数根据自增流水的长度来定） 注：自增流水号、服务器节点编号和随机生成数的位数之和是固定6位 generateOrderId
	 * 
	 * @param :
	 *            serverNum
	 *            服务器编号（每台节点服务器在配置文件中定义一个编号,编号是数字字符串,如"1"：服务器节点一，"2":服务器节点二，"3":服务器节点三）
	 *            String
	 * @return
	 */
	public String generateOrderId(String serverNum) {
		String strOrder = sdf.format(new Date());
		String seq = "";
		if (dateOrderString.equals(strOrder)) {
			seq = String.valueOf(orderAi.incrementAndGet());
			if (seq.length() <= 0 || seq.length() > 4) {
				return PREFIX + strOrder + "0" + SequenceUtil.getFixLenthString(fixLen - 1);
			}
			return PREFIX + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		} else {
			dateOrderString = strOrder;
			orderAi = new AtomicInteger();
			seq = String.valueOf(orderAi.incrementAndGet());
			return PREFIX + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		}
	}

	/**
	 * 新版现场返利产品定义订单生成规则为：MF + 150515100013(yyMMddHHmmss)+ 1(服务器节点编号) + 1(自增流水号) +
	 * 3151（随机生成数，位数根据自增流水的长度来定） 注：自增流水号、服务器节点编号和随机生成数的位数之和是固定6位
	 * generateRebateOrderId
	 * 
	 * @param :
	 *            serverNum
	 *            服务器编号（每台节点服务器在配置文件中定义一个编号,编号是数字字符串,如"1"：服务器节点一，"2":服务器节点二，"3":服务器节点三）
	 *            String
	 * @return
	 */
	public String generateRebateOrderId(String serverNum) {
		String strOrder = sdf.format(new Date());
		String seq = "";
		if (dateRebateString.equals(strOrder)) {
			seq = String.valueOf(rebateOrderAi.incrementAndGet());
			if (seq.length() <= 0 || seq.length() > 4) {
				return REBATE_PREFIX + strOrder + "0" + SequenceUtil.getFixLenthString(fixLen - 1);
			}
			return REBATE_PREFIX + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		} else {
			dateRebateString = strOrder;
			rebateOrderAi = new AtomicInteger();
			seq = String.valueOf(rebateOrderAi.incrementAndGet());
			return REBATE_PREFIX + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		}
	}

	/**
	 * 
	 * @Title: generateMsgId
	 * @param:
	 * @Description: 生成消息ID
	 * @return String
	 */
	public String generateMsgId(String serverNum) {
		String strOrder = sdf.format(new Date());
		String seq = "";
		if (dateMsgString.equals(strOrder)) {
			seq = String.valueOf(msgAi.incrementAndGet());
			if (seq.length() <= 0 || seq.length() > 4) {
				return MSG_PREFIX + strOrder + "0" + SequenceUtil.getFixLenthString(fixLen - 1);
			}
			return MSG_PREFIX + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		} else {
			dateMsgString = strOrder;
			msgAi = new AtomicInteger();
			seq = String.valueOf(msgAi.incrementAndGet());
			return MSG_PREFIX + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		}
	}

	/**
	 * 
	 * @Title: generateOrderRefundNo
	 * @param: prefix[根据两位交易类型]+时间戳(精确到秒)+流水号(四位递增),如：IO01201508111000130001
	 * @Description: 退款订单号
	 * @return String
	 */
	public String generateOrderRefundNo(String serverNum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strOrder = sdf.format(new Date());
		String prefix = REFUND_PREFIX;
		String seq = "";
		if (dateRefundString.equals(strOrder)) {
			seq = String.valueOf(rebateOrderAi.incrementAndGet());
			if (seq.length() <= 0 || seq.length() > 4) {
				return prefix + strOrder + "0" + SequenceUtil.getFixLenthString(fixLen - 1);
			}
			return prefix + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		} else {
			dateRefundString = strOrder;
			rebateOrderAi = new AtomicInteger();
			seq = String.valueOf(rebateOrderAi.incrementAndGet());
			return prefix + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		}
	}

	/**
	 * strLength必须至少要有一位,不能超过5位
	 * 
	 * @param strLength
	 * @return
	 */
	private static String getFixLenthString(int strLength) {
		if (strLength <= 0 || strLength > 5) {
			return "";
		}
		int pross = 0;
		if (strLength == 5) {
			pross = ThreadLocalRandom.current().nextInt(10000, 99999);
		} else if (strLength == 4) {
			pross = ThreadLocalRandom.current().nextInt(1000, 9999);
		} else if (strLength == 3) {
			pross = ThreadLocalRandom.current().nextInt(100, 999);
		} else if (strLength == 2) {
			pross = ThreadLocalRandom.current().nextInt(10, 99);
		} else if (strLength == 1) {
			pross = ThreadLocalRandom.current().nextInt(1, 9);
		} else {
			return "";
		}
		return String.valueOf(pross);
	}

}