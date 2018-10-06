package com.hd.cloud.service.impl;

import java.io.File;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.hd.cloud.ThirdPartyServiceApplication.HdFileUploadConfig;
import com.hd.cloud.service.QrCodeService;
import com.hd.cloud.util.AbstractQrCodeUtil;
import com.hd.cloud.util.FileZipUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: QrCodeServiceImpl
 * @Description: 生成二维码
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年1月4日 上午11:28:14
 *
 */
@Slf4j
@Service
public class QrCodeServiceImpl implements QrCodeService {

	@Inject
	private HdFileUploadConfig hlbFileUploadConfig;

	@Override
	public String generateTextQrcode(long id, String savePath, String path) {
		AbstractQrCodeUtil abstractQrCodeUtil = new AbstractQrCodeUtil();
		return abstractQrCodeUtil.generateTextQrcode(hlbFileUploadConfig, id, savePath, path);
	}

	@Override
	public void reportShopQrcode(long merchantId, long companyId) {
		String folder = "";
		if (merchantId > 0) {
			folder += "/" + merchantId + "/";
		}
		if (companyId > 0) {
			folder += companyId + "/";
		}
		log.info("####folder:{}", folder);
		String savePath = hlbFileUploadConfig.getBasepath() + "/showqrcode" + folder;
		String parentZipFilePath = hlbFileUploadConfig.getBasepath() + "/showqrcode/zip/";
		File parentZipDiretory = new File(parentZipFilePath);
		if (!parentZipDiretory.exists() || !parentZipDiretory.isDirectory()) {
			parentZipDiretory.mkdir();
		}
		String zipFilePath = parentZipFilePath + merchantId + companyId + "/";
		File zipDiretory = new File(zipFilePath);
		if (!zipDiretory.exists() || !zipDiretory.isDirectory()) {
			zipDiretory.mkdir();
		}
		String fileName = "qrcode";
		FileZipUtil.fileToZip(savePath, zipFilePath, fileName);
	}

}
