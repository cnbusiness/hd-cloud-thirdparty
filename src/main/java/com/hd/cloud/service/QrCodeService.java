package com.hd.cloud.service;

/**
 * 
 * @ClassName: QrCodeService
 * @Description: 二维码生成服务
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年1月4日 上午11:27:03
 *
 */
public interface QrCodeService {

	/**
	 * 
	 * @Title: generateTextQrcode
	 * @param:
	 * @Description: 生成二维码
	 * @return String
	 */
	public String generateTextQrcode(long id, String savePath, String path);

	/**
	 * 
	 * @Title: reportShopQrcode
	 * @param:
	 * @Description: 二维码打包
	 * @return void
	 */
	public void reportShopQrcode(long merchantId, long companyId);

}
