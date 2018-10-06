package com.hd.cloud.util;

import java.io.File;

import com.hd.cloud.ThirdPartyServiceApplication.HdFileUploadConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: com.hlb.cloud.util
 * @Description: 二维码生成Util
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年1月4日 上午10:37:59
 *
 */
@Slf4j
public class AbstractQrCodeUtil {

	static int USER_QRCODE_WIDGHT = 720;

	static int USER_QRCODE_HEIGHT = 720;

	/**
	 * 
	 * @Title: generateTextQrcode
	 * @param:
	 * @Description: 生成二维码
	 * @return String
	 */
	public String generateTextQrcode(HdFileUploadConfig hdFileUploadConfig, long id, String savePath, String path) {
		String content = hdFileUploadConfig.getWechatUrl() + id;
		log.debug("content:{}", content);
		String basePath = hdFileUploadConfig.getBasepath();
		log.debug("basePath:{}", basePath);
		String destImagePath = basePath + File.separator + savePath + File.separator + path;
		log.debug("destImagePath:{}", destImagePath);
		try {
			QrcodeToolKit.textQrcode(content, USER_QRCODE_WIDGHT, USER_QRCODE_HEIGHT, destImagePath);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
		return File.separator + savePath + File.separator + path;

	}
}
