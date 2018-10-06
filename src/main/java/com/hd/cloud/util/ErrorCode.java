package com.hd.cloud.util;

/**
 * 
 * @ClassName: ErrorCode
 * @Description: 错误码
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:27:10
 *
 */
public class ErrorCode {

	// 请上传图片
	public final static String UPLOAD_IMAGE_EMPTY = "hd0070001";

	// 上传成功
	public final static String UPLOAD_SUCCESS = "hd0070002";

	// 上传图片失败
	public final static String UPLOAD_IMAGE_FAIL = "hd0070003";

	// 上传类型或文件用途不能为空
	public final static String UPLOAD_FILE_ERROR = "hd0070004";

	// 上传类型不正确
	public final static String TYPE_ERROR = "hd0070005";

	// 文件用途不正确
	public final static String USE_ERROR = "hd0070006";

	// 上传失败
	public final static String UPLOAD_FAIL = "hd0070007";

	// 请上传文件
	public final static String UPLOAD_FILE_EMPTY = "hd0070008";
}
